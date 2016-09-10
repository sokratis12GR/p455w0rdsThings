package p455w0rd.p455w0rdsthings.lib.asm;

import java.util.Set;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.LocalVariablesSorter;

public class LocalVariablesSorterVisitor
  extends ClassVisitor
{
  public Set<ObfMapping> methods;
  public String owner;
  
  public LocalVariablesSorterVisitor(Set<ObfMapping> methods, ClassVisitor cv)
  {
    super(262144, cv);
    this.methods = methods;
  }
  
  public LocalVariablesSorterVisitor(ClassVisitor cv)
  {
    this(null, cv);
  }
  
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces)
  {
    super.visit(version, access, name, signature, superName, interfaces);
    this.owner = name;
  }
  
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions)
  {
    MethodVisitor mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
    return (this.methods == null) || (this.methods.contains(new ObfMapping(this.owner, name, desc))) ? new LocalVariablesSorter(access, desc, mv) : mv;
  }
}


