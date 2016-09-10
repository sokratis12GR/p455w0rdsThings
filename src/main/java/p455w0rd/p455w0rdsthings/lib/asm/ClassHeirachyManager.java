package p455w0rd.p455w0rdsthings.lib.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.tree.ClassNode;

public class ClassHeirachyManager
  implements IClassTransformer
{
  public static class SuperCache
  {
    String superclass;
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public HashSet<String> parents = new HashSet();
    private boolean flattened;
    
    public void add(String parent)
    {
      this.parents.add(parent);
    }
    
    public void flatten()
    {
      if (this.flattened) {
        return;
      }
      for (String s : new ArrayList<String>(this.parents))
      {
        SuperCache c = ClassHeirachyManager.declareClass(s);
        if (c != null)
        {
          c.flatten();
          this.parents.addAll(c.parents);
        }
      }
      this.flattened = true;
    }
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static HashMap<String, SuperCache> superclasses = new HashMap();
  private static LaunchClassLoader cl = Launch.classLoader;
  
  public static String toKey(String name)
  {
    if (ObfMapping.obfuscated) {
      name = FMLDeobfuscatingRemapper.INSTANCE.map(name.replace('.', '/')).replace('/', '.');
    }
    return name;
  }
  
  public static String unKey(String name)
  {
    if (ObfMapping.obfuscated) {
      name = FMLDeobfuscatingRemapper.INSTANCE.unmap(name.replace('.', '/')).replace('/', '.');
    }
    return name;
  }
  
  public static boolean classExtends(String name, String superclass)
  {
    name = toKey(name);
    superclass = toKey(superclass);
    if (name.equals(superclass)) {
      return true;
    }
    SuperCache cache = declareClass(name);
    if (cache == null) {
      return false;
    }
    cache.flatten();
    return cache.parents.contains(superclass);
  }
  
  private static SuperCache declareClass(String name)
  {
    name = toKey(name);
    SuperCache cache = (SuperCache)superclasses.get(name);
    if (cache != null) {
      return cache;
    }
    try
    {
      byte[] bytes = cl.getClassBytes(unKey(name));
      if (bytes != null) {
        cache = declareASM(bytes);
      }
    }
    catch (Exception localException) {}
    if (cache != null) {
      return cache;
    }
    try
    {
      cache = declareReflection(name);
    }
    catch (ClassNotFoundException localClassNotFoundException) {}
    return cache;
  }
  
  private static SuperCache declareReflection(String name)
    throws ClassNotFoundException
  {
    Class<?> aclass = Class.forName(name);
    
    SuperCache cache = getOrCreateCache(name);
    if (aclass.isInterface())
    {
      cache.superclass = "java.lang.Object";
    }
    else
    {
      if (name.equals("java.lang.Object")) {
        return cache;
      }
      cache.superclass = toKey(aclass.getSuperclass().getName());
    }
    cache.add(cache.superclass);
    for (Class<?> iclass : aclass.getInterfaces()) {
      cache.add(toKey(iclass.getName()));
    }
    return cache;
  }
  
  private static SuperCache declareASM(byte[] bytes)
  {
    ClassNode node = ASMHelper.createClassNode(bytes);
    String name = toKey(node.name);
    
    SuperCache cache = getOrCreateCache(name);
    cache.superclass = toKey(node.superName.replace('/', '.'));
    cache.add(cache.superclass);
    for (String iclass : node.interfaces) {
      cache.add(toKey(iclass.replace('/', '.')));
    }
    return cache;
  }
  
  public byte[] transform(String name, String tname, byte[] bytes)
  {
    if (bytes == null) {
      return null;
    }
    if (!superclasses.containsKey(tname)) {
      declareASM(bytes);
    }
    return bytes;
  }
  
  public static SuperCache getOrCreateCache(String name)
  {
    SuperCache cache = (SuperCache)superclasses.get(name);
    if (cache == null) {
      superclasses.put(name, cache = new SuperCache());
    }
    return cache;
  }
  
  public static String getSuperClass(String name, boolean runtime)
  {
    name = toKey(name);
    SuperCache cache = declareClass(name);
    if (cache == null) {
      return "java.lang.Object";
    }
    cache.flatten();
    String s = cache.superclass;
    if (!runtime) {
      s = FMLDeobfuscatingRemapper.INSTANCE.unmap(s);
    }
    return s;
  }
}


