package p455w0rd.p455w0rdsthings.lib.asm;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

public class ModularASMTransformer
{
  public static class ClassNodeTransformerList
  {
    @SuppressWarnings({ "unchecked", "rawtypes" })
	List<ModularASMTransformer.ClassNodeTransformer> transformers = new LinkedList();
    @SuppressWarnings({ "unchecked", "rawtypes" })
	HashSet<ObfMapping> methodsToSort = new HashSet();
    
    public void add(ModularASMTransformer.ClassNodeTransformer t)
    {
      this.transformers.add(t);
      t.addMethodsToSort(this.methodsToSort);
    }
    
    public byte[] transform(byte[] bytes)
    {
      ClassNode cnode = new ClassNode();
      ClassReader reader = new ClassReader(bytes);
      ClassVisitor cv = cnode;
      if (!this.methodsToSort.isEmpty()) {
        cv = new LocalVariablesSorterVisitor(this.methodsToSort, cv);
      }
      reader.accept(cv, 8);
      try
      {
        int writeFlags = 0;
        for (ModularASMTransformer.ClassNodeTransformer t : this.transformers)
        {
          t.transform(cnode);
          writeFlags |= t.writeFlags;
        }
        bytes = ASMHelper.createBytes(cnode, writeFlags);
        if (ASMHelper.config.getTag("dump_asm").getBooleanValue(true)) {
          ASMHelper.dump(bytes, new File("asm/ccl_modular/" + cnode.name.replace('/', '#') + ".txt"), false, false);
        }
        return bytes;
      }
      catch (RuntimeException e)
      {
        ASMHelper.dump(bytes, new File("asm/ccl_modular/" + cnode.name.replace('/', '#') + ".txt"), false, false);
        throw e;
      }
    }
  }
  
  public static abstract class ClassNodeTransformer
  {
    public int writeFlags;
    
    public ClassNodeTransformer(int writeFlags)
    {
      this.writeFlags = writeFlags;
    }
    
    public ClassNodeTransformer()
    {
      this(3);
    }
    
    public abstract String className();
    
    public abstract void transform(ClassNode paramClassNode);
    
    public void addMethodsToSort(Set<ObfMapping> set) {}
  }
  
  public static abstract class MethodTransformer
    extends ModularASMTransformer.ClassNodeTransformer
  {
    public final ObfMapping method;
    
    public MethodTransformer(ObfMapping method)
    {
      this.method = method.toClassloading();
    }
    
    public String className()
    {
      return this.method.javaClass();
    }
    
    public void transform(ClassNode cnode)
    {
      MethodNode mv = ASMHelper.findMethod(this.method, cnode);
      if (mv == null) {
        throw new RuntimeException("Method not found: " + this.method);
      }
      try
      {
        transform(mv);
      }
      catch (Exception e)
      {
        throw new RuntimeException("Error transforming method: " + this.method, e);
      }
    }
    
    public abstract void transform(MethodNode paramMethodNode);
  }
  
  public static class MethodWriter
    extends ModularASMTransformer.ClassNodeTransformer
  {
    public final int access;
    public final ObfMapping method;
    public final String[] exceptions;
    public InsnList list;
    
    public MethodWriter(int access, ObfMapping method)
    {
      this(access, method, null, (InsnList)null);
    }
    
    public MethodWriter(int access, ObfMapping method, InsnList list)
    {
      this(access, method, null, list);
    }
    
    public MethodWriter(int access, ObfMapping method, ASMBlock block)
    {
      this(access, method, null, block);
    }
    
    public MethodWriter(int access, ObfMapping method, String[] exceptions)
    {
      this(access, method, exceptions, (InsnList)null);
    }
    
    public MethodWriter(int access, ObfMapping method, String[] exceptions, InsnList list)
    {
      this.access = access;
      this.method = method.toClassloading();
      this.exceptions = exceptions;
      this.list = list;
    }
    
    public MethodWriter(int access, ObfMapping method, String[] exceptions, ASMBlock block)
    {
      this(access, method, exceptions, block.rawListCopy());
    }
    
    public String className()
    {
      return this.method.javaClass();
    }
    
    public void transform(ClassNode cnode)
    {
      MethodNode mv = ASMHelper.findMethod(this.method, cnode);
      if (mv == null)
      {
        mv = (MethodNode)this.method.visitMethod(cnode, this.access, this.exceptions);
      }
      else
      {
        mv.access = this.access;
        mv.instructions.clear();
        if (mv.localVariables != null) {
          mv.localVariables.clear();
        }
        if (mv.tryCatchBlocks != null) {
          mv.tryCatchBlocks.clear();
        }
      }
      write(mv);
    }
    
    public void write(MethodNode mv)
    {
      ASMHelper.logger.debug("Writing method " + this.method);
      this.list.accept(mv);
    }
  }
  
  public static class MethodInjector
    extends MethodTransformer
  {
    public ASMBlock needle;
    public ASMBlock injection;
    public boolean before;
    
    public MethodInjector(ObfMapping method, ASMBlock needle, ASMBlock injection, boolean before)
    {
      super(method);
      this.needle = needle;
      this.injection = injection;
      this.before = before;
    }
    
    public MethodInjector(ObfMapping method, ASMBlock injection, boolean before)
    {
      this(method, null, injection, before);
    }
    
    public MethodInjector(ObfMapping method, InsnList needle, InsnList injection, boolean before)
    {
      this(method, new ASMBlock(needle), new ASMBlock(injection), before);
    }
    
    public MethodInjector(ObfMapping method, InsnList injection, boolean before)
    {
      this(method, null, new ASMBlock(injection), before);
    }
    
    public void addMethodsToSort(Set<ObfMapping> set)
    {
      set.add(this.method);
    }
    
    public void transform(MethodNode mv)
    {
      if (this.needle == null)
      {
        ASMHelper.logger.debug("Injecting " + (this.before ? "before" : "after") + " method " + this.method);
        if (this.before) {
          mv.instructions.insert(this.injection.rawListCopy());
        } else {
          mv.instructions.add(this.injection.rawListCopy());
        }
      }
      else
      {
        for (InsnListSection key : InsnComparator.findN(mv.instructions, this.needle.list))
        {
          ASMHelper.logger.debug("Injecting " + (this.before ? "before" : "after") + " method " + this.method + " @ " + key.start + " - " + key.end);
          ASMBlock injectBlock = this.injection.copy().mergeLabels(this.needle.applyLabels(key));
          if (this.before) {
            key.insertBefore(injectBlock.list.list);
          } else {
            key.insert(injectBlock.list.list);
          }
        }
      }
    }
  }
  
  public static class MethodReplacer
    extends ModularASMTransformer.MethodTransformer
  {
    public ASMBlock needle;
    public ASMBlock replacement;
    
    public MethodReplacer(ObfMapping method, ASMBlock needle, ASMBlock replacement)
    {
      super(method);
      this.needle = needle;
      this.replacement = replacement;
    }
    
    public MethodReplacer(ObfMapping method, InsnList needle, InsnList replacement)
    {
      this(method, new ASMBlock(needle), new ASMBlock(replacement));
    }
    
    public void addMethodsToSort(Set<ObfMapping> set)
    {
      set.add(this.method);
    }
    
    public void transform(MethodNode mv)
    {
      for (InsnListSection key : InsnComparator.findN(mv.instructions, this.needle.list))
      {
        ASMHelper.logger.debug("Replacing method " + this.method + " @ " + key.start + " - " + key.end);
        ASMBlock replaceBlock = this.replacement.copy().pullLabels(this.needle.applyLabels(key));
        key.insert(replaceBlock.list.list);
      }
    }
  }
  
  public static class FieldWriter
    extends ModularASMTransformer.ClassNodeTransformer
  {
    public final ObfMapping field;
    public final int access;
    public final Object value;
    
    public FieldWriter(int access, ObfMapping field, Object value)
    {
      this.field = field.toClassloading();
      this.access = access;
      this.value = value;
    }
    
    public FieldWriter(int access, ObfMapping field)
    {
      this(access, field, null);
    }
    
    public String className()
    {
      return this.field.javaClass();
    }
    
    public void transform(ClassNode cnode)
    {
      this.field.visitField(cnode, this.access, this.value);
    }
  }
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
public HashMap<String, ClassNodeTransformerList> transformers = new HashMap();
  
  public void add(ClassNodeTransformer t)
  {
    ClassNodeTransformerList list = (ClassNodeTransformerList)this.transformers.get(t.className());
    if (list == null) {
      this.transformers.put(t.className(), list = new ClassNodeTransformerList());
    }
    list.add(t);
  }
  
  public byte[] transform(String name, byte[] bytes)
  {
    if (bytes == null) {
      return null;
    }
    ClassNodeTransformerList list = (ClassNodeTransformerList)this.transformers.get(name);
    return list == null ? bytes : list.transform(bytes);
  }
}


