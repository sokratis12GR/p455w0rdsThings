package p455w0rd.p455w0rdsthings.lib.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map.Entry;

public class SimpleProperties
{
  public HashMap<String, String> propertyMap = new HashMap<String, String>();
  public File propertyFile;
  public boolean saveOnChange = false;
  public String encoding;
  private boolean loading = false;
  
  public SimpleProperties(File file, boolean saveOnChange, String encoding)
  {
    this.propertyFile = file;
    this.saveOnChange = saveOnChange;
    this.encoding = encoding;
  }
  
  public SimpleProperties(File file, boolean saveOnChange)
  {
    this(file, saveOnChange, Charset.defaultCharset().name());
  }
  
  public SimpleProperties(File file)
  {
    this(file, true);
  }
  
  public void load()
  {
    clear();
    this.loading = true;
    try
    {
      BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.propertyFile), this.encoding));
      for (;;)
      {
        String read = reader.readLine();
        if (read == null) {
          break;
        }
        int equalIndex = read.indexOf('=');
        if (equalIndex != -1) {
          setProperty(read.substring(0, equalIndex), read.substring(equalIndex + 1));
        }
      }
      reader.close();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    this.loading = false;
  }
  
  public void save()
  {
    try
    {
      PrintStream writer = new PrintStream(this.propertyFile);
      for (Entry<String, String> entry : this.propertyMap.entrySet()) {
        writer.println((String)entry.getKey() + "=" + (String)entry.getValue());
      }
      writer.close();
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void clear()
  {
    this.propertyMap.clear();
  }
  
  public boolean hasProperty(String key)
  {
    return this.propertyMap.containsKey(key);
  }
  
  public void removeProperty(String key)
  {
    if ((this.propertyMap.remove(key) != null) && (this.saveOnChange) && (!this.loading)) {
      save();
    }
  }
  
  public void setProperty(String key, int value)
  {
    setProperty(key, Integer.toString(value));
  }
  
  public void setProperty(String key, boolean value)
  {
    setProperty(key, Boolean.toString(value));
  }
  
  public void setProperty(String key, String value)
  {
    this.propertyMap.put(key, value);
    if ((this.saveOnChange) && (!this.loading)) {
      save();
    }
  }
  
  public int getProperty(String property, int defaultvalue)
  {
    try
    {
      return Integer.parseInt(getProperty(property, Integer.toString(defaultvalue)));
    }
    catch (NumberFormatException nfe) {}
    return defaultvalue;
  }
  
  public boolean getProperty(String property, boolean defaultvalue)
  {
    try
    {
      return Boolean.parseBoolean(getProperty(property, Boolean.toString(defaultvalue)));
    }
    catch (NumberFormatException nfe) {}
    return defaultvalue;
  }
  
  public String getProperty(String property, String defaultvalue)
  {
    String value = (String)this.propertyMap.get(property);
    if (value == null)
    {
      setProperty(property, defaultvalue);
      return defaultvalue;
    }
    return value;
  }
  
  public String getProperty(String property)
  {
    return (String)this.propertyMap.get(property);
  }
}


