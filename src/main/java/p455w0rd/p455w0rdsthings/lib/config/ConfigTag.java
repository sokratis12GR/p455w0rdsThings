package p455w0rd.p455w0rdsthings.lib.config;

import java.io.PrintWriter;

public class ConfigTag
  extends ConfigTagParent
{
  public ConfigTagParent parent;
  public String name;
  public String qualifiedname;
  public String value;
  public boolean brace;
  public boolean newline;
  
  public ConfigTag(ConfigTagParent parent, String name)
  {
    this.parent = parent;
    this.name = name;
    this.qualifiedname = (parent.getNameQualifier() + name);
    this.newline = (parent.newlinemode == 2);
    parent.addChild(this);
  }
  
  public String getNameQualifier()
  {
    return this.qualifiedname + ".";
  }
  
  public void saveConfig()
  {
    this.parent.saveConfig();
  }
  
  public ConfigTag onLoaded()
  {
    return this;
  }
  
  public void setValue(String value)
  {
    this.value = value;
    saveConfig();
  }
  
  public void setDefaultValue(String defaultValue)
  {
    if (this.value == null)
    {
      this.value = defaultValue;
      saveConfig();
    }
  }
  
  public void setIntValue(int i)
  {
    setValue(Integer.toString(i));
  }
  
  public void setBooleanValue(boolean b)
  {
    setValue(Boolean.toString(b));
  }
  
  public void setHexValue(int i)
  {
    setValue("0x" + Long.toString(i << 32 >>> 32, 16));
  }
  
  public <T> void set(IConfigType<T> type, T entry)
  {
    setValue(type.configValue(entry));
  }
  
  public String getValue()
  {
    return this.value;
  }
  
  public String getValue(String defaultValue)
  {
    setDefaultValue(defaultValue);
    return this.value;
  }
  
  public int getIntValue()
  {
    return Integer.parseInt(getValue());
  }
  
  public int getIntValue(int defaultValue)
  {
    try
    {
      if (this.value != null) {
        return getIntValue();
      }
    }
    catch (NumberFormatException localNumberFormatException) {}
    setIntValue(defaultValue);
    return defaultValue;
  }
  
  public boolean getBooleanValue()
  {
    String value = getValue();
    if ((value != null) && ((value.equalsIgnoreCase("true")) || (value.equalsIgnoreCase("yes")))) {
      return true;
    }
    if ((value != null) && ((value.equalsIgnoreCase("false")) || (value.equalsIgnoreCase("no")))) {
      return false;
    }
    throw new NumberFormatException(this.qualifiedname + ".value=" + value);
  }
  
  public boolean getBooleanValue(boolean defaultValue)
  {
    try
    {
      if (this.value != null) {
        return getBooleanValue();
      }
    }
    catch (NumberFormatException localNumberFormatException) {}
    setBooleanValue(defaultValue);
    return defaultValue;
  }
  
  public int getHexValue()
  {
    return (int)Long.parseLong(getValue().replace("0x", ""), 16);
  }
  
  public int getHexValue(int defaultValue)
  {
    try
    {
      if (this.value != null) {
        return getHexValue();
      }
    }
    catch (NumberFormatException localNumberFormatException) {}
    setHexValue(defaultValue);
    return defaultValue;
  }
  
  public <T> T get(IConfigType<T> type)
  {
    try
    {
      return (T)type.valueOf(getValue());
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public <T> T get(IConfigType<T> type, T defaultValue)
  {
    try
    {
      if (this.value != null) {
        return (T)get(type);
      }
    }
    catch (Exception localException) {}
    set(type, defaultValue);
    return defaultValue;
  }
  
  public void save(PrintWriter writer, int tabs, String bracequalifier, boolean first)
  {
    String vname;
    if ((this.qualifiedname.contains(".")) && (bracequalifier.length() > 0)) {
      vname = this.qualifiedname.substring(bracequalifier.length() + 1);
    } else {
      vname = this.qualifiedname;
    }
    if ((this.newline) && (!first)) {
      ConfigFile.writeLine(writer, "", tabs);
    }
    writeComment(writer, tabs);
    if (this.value != null) {
      ConfigFile.writeLine(writer, vname + "=" + this.value, tabs);
    }
    if (!hasChildTags()) {
      return;
    }
    if (this.brace)
    {
      if (this.value == null) {
        ConfigFile.writeLine(writer, vname, tabs);
      }
      ConfigFile.writeLine(writer, "{", tabs);
      saveTagTree(writer, tabs + 1, this.qualifiedname);
      ConfigFile.writeLine(writer, "}", tabs);
    }
    else
    {
      saveTagTree(writer, tabs, bracequalifier);
    }
  }
  
  public ConfigTag setComment(String comment)
  {
    super.setComment(comment);
    return this;
  }
  
  public ConfigTag setSortMode(int mode)
  {
    super.setSortMode(mode);
    return this;
  }
  
  public ConfigTag setNewLine(boolean b)
  {
    this.newline = b;
    saveConfig();
    return this;
  }
  
  public ConfigTag useBraces()
  {
    this.brace = true;
    if (this.parent.newlinemode == 1) {
      this.newline = true;
    }
    saveConfig();
    return this;
  }
  
  public ConfigTag setPosition(int pos)
  {
    this.position = pos;
    saveConfig();
    return this;
  }
  
  public boolean containsTag(String tagname)
  {
    return getTag(tagname, false) != null;
  }
  
  public int getId(String name, int defaultValue)
  {
    return getTag(name).getIntValue(defaultValue);
  }
  
  public int getId(String name)
  {
    int ret = getId(name, this.IDBase);
    this.IDBase = (ret + 1);
    return ret;
  }
  
  public int getAcheivementId(String name, int defaultValue)
  {
    return getTag(name).getIntValue(defaultValue);
  }
  
  public ConfigTag setBaseID(int i)
  {
    this.IDBase = i;
    return this;
  }
  
  public int position = Integer.MAX_VALUE;
  private int IDBase;
  
  public static abstract interface IConfigType<T>
  {
    public abstract String configValue(T paramT);
    
    public abstract T valueOf(String paramString)
      throws Exception;
  }
}


