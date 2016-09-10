package p455w0rd.p455w0rdsthings.lib.asm;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFileChooser;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import p455w0rd.p455w0rdsthings.lib.config.ConfigTag;

@SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
public class ObfMapping
{
  public static class ObfRemapper
    extends Remapper
  {
    
	private HashMap<String, String> fields = new HashMap();
    private HashMap<String, String> funcs = new HashMap();
    
    public ObfRemapper()
    {
      try
      {
        Field rawFieldMapsField = FMLDeobfuscatingRemapper.class.getDeclaredField("rawFieldMaps");
        Field rawMethodMapsField = FMLDeobfuscatingRemapper.class.getDeclaredField("rawMethodMaps");
        rawFieldMapsField.setAccessible(true);
        rawMethodMapsField.setAccessible(true);
        Map<String, Map<String, String>> rawFieldMaps = (Map)rawFieldMapsField.get(FMLDeobfuscatingRemapper.INSTANCE);
        Map<String, Map<String, String>> rawMethodMaps = (Map)rawMethodMapsField.get(FMLDeobfuscatingRemapper.INSTANCE);
        if (rawFieldMaps == null) {
          throw new IllegalStateException("p455w0rd.p455w0rdsthings.lib.asm.ObfMapping loaded too early. Make sure all references are in or after the asm transformer load stage");
        }
        for (Map<String, String> map : rawFieldMaps.values()) {
          for (Map.Entry<String, String> entry : map.entrySet()) {
            if (((String)entry.getValue()).startsWith("field")) {
              this.fields.put(entry.getValue(), ((String)entry.getKey()).substring(0, ((String)entry.getKey()).indexOf(':')));
            }
          }
        }
        for (Map<String, String> map : rawMethodMaps.values()) {
          for (Map.Entry<String, String> entry : map.entrySet()) {
            if (((String)entry.getValue()).startsWith("func")) {
              this.funcs.put(entry.getValue(), ((String)entry.getKey()).substring(0, ((String)entry.getKey()).indexOf('(')));
            }
          }
        }
      }
      catch (Exception e)
      {
        throw new RuntimeException(e);
      }
    }
    
    public String mapMethodName(String owner, String name, String desc)
    {
      String s = (String)this.funcs.get(name);
      return s == null ? name : s;
    }
    
    public String mapFieldName(String owner, String name, String desc)
    {
      String s = (String)this.fields.get(name);
      return s == null ? name : s;
    }
    
    public String map(String typeName)
    {
      return FMLDeobfuscatingRemapper.INSTANCE.unmap(typeName);
    }
    
    public String unmap(String typeName)
    {
      return FMLDeobfuscatingRemapper.INSTANCE.map(typeName);
    }
    
    public boolean isObf(String typeName)
    {
      return (!map(typeName).equals(typeName)) || (!unmap(typeName).equals(typeName));
    }
  }
  
  public static class MCPRemapper
    extends Remapper
    implements LineProcessor<Void>
  {
    public static File[] getConfFiles() {
        if (!Strings.isNullOrEmpty((String)System.getProperty("net.minecraftforge.gradle.GradleStart.srgDir"))) {
            File srgDir = new File(System.getProperty("net.minecraftforge.gradle.GradleStart.srgDir"));
            File csvDir = new File(System.getProperty("net.minecraftforge.gradle.GradleStart.csvDir"));
            if (srgDir.exists() && csvDir.exists()) {
                File srg = new File(srgDir, "notch-srg.srg");
                File fieldCsv = new File(csvDir, "fields.csv");
                File methodCsv = new File(csvDir, "methods.csv");
                if (srg.exists() && fieldCsv.exists() && methodCsv.exists()) {
                    return new File[]{srg, fieldCsv, methodCsv};
                }
            }
        }
        ConfigTag tag = ASMHelper.config.getTag("mappingDir").setComment("Path to directory holding packaged.srg, fields.csv and methods.csv for mcp remapping");
        for (int i = 0; i < 7; ++i) {
            File[] mappings;
            File dir = MCPRemapper.confDirectoryGuess(i, tag);
            if (dir == null || dir.isFile()) continue;
            try {
                mappings = MCPRemapper.parseConfDir(dir);
            }
            catch (Exception e) {
                if (i < 4) continue;
                e.printStackTrace();
                continue;
            }
            tag.setValue(dir.getPath());
            return mappings;
        }
        throw new RuntimeException("Failed to select mappings directory, set it manually in the config");
    }
    
    public static File confDirectoryGuess(int i, ConfigTag tag)
    {
      File mcDir = (File)net.minecraftforge.fml.relauncher.FMLInjectionData.data()[6];
      switch (i)
      {
      case 0: 
        return tag.value != null ? new File(tag.getValue()) : null;
      case 1: 
        return new File(mcDir, "../conf");
      case 2: 
        return new File(mcDir, "../build/unpacked/conf");
      case 3: 
        return new File(System.getProperty("user.home"), ".gradle/caches/minecraft/net/minecraftforge/forge/" + net.minecraftforge.fml.relauncher.FMLInjectionData.data()[4] + "-" + ForgeVersion.getVersion() + "/unpacked/conf");
      }
      JFileChooser fc = new JFileChooser(mcDir);
      fc.setFileSelectionMode(1);
      fc.setDialogTitle("Select an mcp conf dir for the deobfuscator.");
      int ret = fc.showDialog(null, "Select");
      return ret == 0 ? fc.getSelectedFile() : null;
    }
    
    public static File[] parseConfDir(File confDir)
    {
      File srgDir = new File(confDir, "conf");
      if (!srgDir.exists()) {
        srgDir = confDir;
      }
      File srgs = new File(srgDir, "packaged.srg");
      if (!srgs.exists()) {
        srgs = new File(srgDir, "joined.srg");
      }
      if (!srgs.exists()) {
        throw new RuntimeException("Could not find packaged.srg or joined.srg");
      }
      File mapDir = new File(confDir, "mappings");
      if (!mapDir.exists()) {
        mapDir = confDir;
      }
      File methods = new File(mapDir, "methods.csv");
      if (!methods.exists()) {
        throw new RuntimeException("Could not find methods.csv");
      }
      File fields = new File(mapDir, "fields.csv");
      if (!fields.exists()) {
        throw new RuntimeException("Could not find fields.csv");
      }
      return new File[] { srgs, methods, fields };
    }
    
    private HashMap<String, String> fields = new HashMap();
    private HashMap<String, String> funcs = new HashMap();
    
    public MCPRemapper()
    {
      File[] mappings = getConfFiles();
      try
      {
        Resources.readLines(mappings[1].toURI().toURL(), Charsets.UTF_8, this);
        Resources.readLines(mappings[2].toURI().toURL(), Charsets.UTF_8, this);
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    
    public String mapMethodName(String owner, String name, String desc)
    {
      String s = (String)this.funcs.get(name);
      return s == null ? name : s;
    }
    
    public String mapFieldName(String owner, String name, String desc)
    {
      String s = (String)this.fields.get(name);
      return s == null ? name : s;
    }
    
    public boolean processLine(String line)
      throws IOException
    {
      int i = line.indexOf(',');
      String srg = line.substring(0, i);
      int i2 = i + 1;
      i = line.indexOf(',', i2);
      String mcp = line.substring(i2, i);
      (srg.startsWith("func") ? this.funcs : this.fields).put(srg, mcp);
      return true;
    }
    
    public Void getResult()
    {
      return null;
    }
  }
  
  public static ObfRemapper obfMapper = new ObfRemapper();
  public static Remapper mcpMapper = null;
  public static final boolean obfuscated;
  public String s_owner;
  public String s_name;
  public String s_desc;
  
  public static void loadMCPRemapper()
  {
    if (mcpMapper == null) {
      mcpMapper = new MCPRemapper();
    }
  }
  
  static
  {
    boolean obf = true;
    try
    {
      obf = Launch.classLoader.getClassBytes("net.minecraft.world.World") == null;
    }
    catch (IOException localIOException) {}
    obfuscated = obf;
    if (!obf) {
      loadMCPRemapper();
    }
  }
  
  public ObfMapping(String owner)
  {
    this(owner, "", "");
  }
  
  public ObfMapping(String owner, String name, String desc)
  {
    this.s_owner = owner;
    this.s_name = name;
    this.s_desc = desc;
    if (this.s_owner.contains(".")) {
      throw new IllegalArgumentException(this.s_owner);
    }
  }
  
  public ObfMapping(ObfMapping descmap, String subclass)
  {
    this(subclass, descmap.s_name, descmap.s_desc);
  }
  
  public static ObfMapping fromDesc(String s)
  {
    int lastDot = s.lastIndexOf('.');
    if (lastDot < 0) {
      return new ObfMapping(s, "", "");
    }
    int sep = s.indexOf('(');
    int sep_end = sep;
    if (sep < 0)
    {
      sep = s.indexOf(' ');
      sep_end = sep + 1;
    }
    if (sep < 0)
    {
      sep = s.indexOf(':');
      sep_end = sep + 1;
    }
    if (sep < 0) {
      return new ObfMapping(s.substring(0, lastDot), s.substring(lastDot + 1), "");
    }
    return new ObfMapping(s.substring(0, lastDot), s.substring(lastDot + 1, sep), s.substring(sep_end));
  }
  
  public ObfMapping subclass(String subclass)
  {
    return new ObfMapping(this, subclass);
  }
  
  public boolean matches(MethodNode node)
  {
    return (this.s_name.equals(node.name)) && (this.s_desc.equals(node.desc));
  }
  
  public boolean matches(MethodInsnNode node)
  {
    return (this.s_owner.equals(node.owner)) && (this.s_name.equals(node.name)) && (this.s_desc.equals(node.desc));
  }
  
  public AbstractInsnNode toInsn(int opcode)
  {
    if (isClass()) {
      return new TypeInsnNode(opcode, this.s_owner);
    }
    if (isMethod()) {
      return new MethodInsnNode(opcode, this.s_owner, this.s_name, this.s_desc);
    }
    return new FieldInsnNode(opcode, this.s_owner, this.s_name, this.s_desc);
  }
  
  public void visitTypeInsn(MethodVisitor mv, int opcode)
  {
    mv.visitTypeInsn(opcode, this.s_owner);
  }
  
  public void visitMethodInsn(MethodVisitor mv, int opcode)
  {
    mv.visitMethodInsn(opcode, this.s_owner, this.s_name, this.s_desc);
  }
  
  public void visitFieldInsn(MethodVisitor mv, int opcode)
  {
    mv.visitFieldInsn(opcode, this.s_owner, this.s_name, this.s_desc);
  }
  
  public MethodVisitor visitMethod(ClassVisitor visitor, int access, String[] exceptions)
  {
    return visitor.visitMethod(access, this.s_name, this.s_desc, null, exceptions);
  }
  
  public FieldVisitor visitField(ClassVisitor visitor, int access, Object value)
  {
    return visitor.visitField(access, this.s_name, this.s_desc, null, value);
  }
  
  public boolean isClass(String name)
  {
    return name.replace('.', '/').equals(this.s_owner);
  }
  
  public boolean matches(String name, String desc)
  {
    return (this.s_name.equals(name)) && (this.s_desc.equals(desc));
  }
  
  public boolean matches(FieldNode node)
  {
    return (this.s_name.equals(node.name)) && (this.s_desc.equals(node.desc));
  }
  
  public boolean matches(FieldInsnNode node)
  {
    return (this.s_owner.equals(node.owner)) && (this.s_name.equals(node.name)) && (this.s_desc.equals(node.desc));
  }
  
  public String javaClass()
  {
    return this.s_owner.replace('/', '.');
  }
  
  public boolean equals(Object obj)
  {
    if (!(obj instanceof ObfMapping)) {
      return false;
    }
    ObfMapping desc = (ObfMapping)obj;
    return (this.s_owner.equals(desc.s_owner)) && (this.s_name.equals(desc.s_name)) && (this.s_desc.equals(desc.s_desc));
  }
  
  public int hashCode()
  {
    return Objects.hashCode(new Object[] { this.s_desc, this.s_name, this.s_owner });
  }
  
  public String toString()
  {
    if (this.s_name.length() == 0) {
      return "[" + this.s_owner + "]";
    }
    if (this.s_desc.length() == 0) {
      return "[" + this.s_owner + "." + this.s_name + "]";
    }
    return "[" + (isMethod() ? methodDesc() : fieldDesc()) + "]";
  }
  
  public String methodDesc()
  {
    return this.s_owner + "." + this.s_name + this.s_desc;
  }
  
  public String fieldDesc()
  {
    return this.s_owner + "." + this.s_name + ":" + this.s_desc;
  }
  
  public boolean isClass()
  {
    return this.s_name.length() == 0;
  }
  
  public boolean isMethod()
  {
    return this.s_desc.contains("(");
  }
  
  public boolean isField()
  {
    return (!isClass()) && (!isMethod());
  }
  
  public ObfMapping map(Remapper mapper)
  {
    if (mapper == null) {
      return this;
    }
    if (isMethod()) {
      this.s_name = mapper.mapMethodName(this.s_owner, this.s_name, this.s_desc);
    } else if (isField()) {
      this.s_name = mapper.mapFieldName(this.s_owner, this.s_name, this.s_desc);
    }
    this.s_owner = mapper.mapType(this.s_owner);
    if (isMethod()) {
      this.s_desc = mapper.mapMethodDesc(this.s_desc);
    } else if (this.s_desc.length() > 0) {
      this.s_desc = mapper.mapDesc(this.s_desc);
    }
    return this;
  }
  
  public ObfMapping toRuntime()
  {
    map(mcpMapper);
    return this;
  }
  
  public ObfMapping toClassloading()
  {
    if (!obfuscated) {
      map(mcpMapper);
    } else if (obfMapper.isObf(this.s_owner)) {
      map(obfMapper);
    }
    return this;
  }
  
  public ObfMapping copy()
  {
    return new ObfMapping(this.s_owner, this.s_name, this.s_desc);
  }
}


