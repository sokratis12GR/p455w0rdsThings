package p455w0rd.p455w0rdsthings.lib.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public abstract class ConfigTagParent
{
  public abstract void saveConfig();
  
  public abstract String getNameQualifier();
  
  public static class TagOrderComparator
    implements Comparator<ConfigTag>
  {
    int sortMode;
    
    public TagOrderComparator(int sortMode)
    {
      this.sortMode = sortMode;
    }
    
    public int compare(ConfigTag o1, ConfigTag o2)
    {
      if (o1.position != o2.position) {
        return compareInt(o1.position, o2.position);
      }
      if (o1.brace != o2.brace) {
        return o1.brace ? 1 : -1;
      }
      switch (this.sortMode)
      {
      case 1: 
        if (o1.value.equals(o2.value)) {
          return 0;
        }
        if (o1.value == null) {
          return 1;
        }
        if (o2.value == null) {
          return -1;
        }
        return o1.value.compareTo(o2.value);
      }
      return o1.name.compareTo(o2.name);
    }
    
    private int compareInt(int a, int b)
    {
      return a < b ? -1 : a == b ? 0 : 1;
    }
  }
  
  private TreeMap<String, ConfigTag> childtags = new TreeMap<String, ConfigTag>();
  public String comment;
  public int sortMode = 0;
  public int newlinemode = 1;
  
  public ConfigTagParent setComment(String comment)
  {
    this.comment = comment;
    saveConfig();
    return this;
  }
  
  public ConfigTagParent setSortMode(int mode)
  {
    this.sortMode = mode;
    saveConfig();
    return this;
  }
  
  public ConfigTagParent setNewLineMode(int mode)
  {
    this.newlinemode = mode;
    for (Map.Entry<String, ConfigTag> entry : this.childtags.entrySet())
    {
      ConfigTag tag = (ConfigTag)entry.getValue();
      if (this.newlinemode == 0) {
        tag.newline = false;
      } else if (this.newlinemode == 1) {
        tag.newline = tag.brace;
      } else if (this.newlinemode == 2) {
        tag.newline = true;
      }
    }
    saveConfig();
    return this;
  }
  
  public Map<String, ConfigTag> childTagMap()
  {
    return this.childtags;
  }
  
  public boolean hasChildTags()
  {
    return !this.childtags.isEmpty();
  }
  
  public boolean containsTag(String tagname)
  {
    return getTag(tagname, false) != null;
  }
  
  public ConfigTag getNewTag(String tagname)
  {
    return new ConfigTag(this, tagname);
  }
  
  public ConfigTag getTag(String tagname, boolean create)
  {
    int dotpos = tagname.indexOf(".");
    String basetagname = dotpos == -1 ? tagname : tagname.substring(0, dotpos);
    ConfigTag basetag = (ConfigTag)this.childtags.get(basetagname);
    if (basetag == null)
    {
      if (!create) {
        return null;
      }
      basetag = getNewTag(basetagname);
      saveConfig();
    }
    if (dotpos == -1) {
      return basetag;
    }
    return basetag.getTag(tagname.substring(dotpos + 1), create);
  }
  
  public ConfigTag getTag(String tagname)
  {
    return getTag(tagname, true);
  }
  
  public boolean removeTag(String tagname)
  {
    ConfigTag tag = getTag(tagname, false);
    if (tag == null) {
      return false;
    }
    int dotpos = tagname.lastIndexOf(".");
    String lastpart = dotpos == -1 ? tagname : tagname.substring(dotpos + 1, tagname.length());
    if (tag.parent != null)
    {
      boolean ret = tag.parent.childtags.remove(lastpart) != null;
      if (ret) {
        saveConfig();
      }
      return ret;
    }
    return false;
  }
  
  public void addChild(ConfigTag tag)
  {
    this.childtags.put(tag.name, tag);
  }
  
  public <T extends ConfigTag> ArrayList<ConfigTag> getSortedTagList()
  {
    ArrayList<ConfigTag> taglist = new ArrayList<ConfigTag>(this.childtags.size());
    for (Map.Entry<String, ConfigTag> tag : this.childtags.entrySet()) {
      taglist.add(tag.getValue());
    }
    Collections.sort(taglist, new TagOrderComparator(this.sortMode));
    return taglist;
  }
  
  public void loadChildren(BufferedReader reader)
  {
    String comment = "";
    String bracequalifier = "";
    try
    {
      for (;;)
      {
        String line = ConfigFile.readLine(reader);
        if (line == null) {
          break;
        }
        if (line.startsWith("#"))
        {
          if (comment.equals("")) {
            comment = line.substring(1);
          } else {
            comment = comment + "\n" + line.substring(1);
          }
        }
        else if (line.contains("="))
        {
          String qualifiedname = line.substring(0, line.indexOf("="));
          getTag(qualifiedname).onLoaded().setComment(comment).setValue(line.substring(line.indexOf("=") + 1));
          comment = "";
          bracequalifier = qualifiedname;
        }
        else if (line.equals("{"))
        {
          getTag(bracequalifier).setComment(comment).useBraces().loadChildren(reader);
          comment = "";
          bracequalifier = "";
        }
        else
        {
          if (line.equals("}")) {
            break;
          }
          bracequalifier = line;
        }
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
  }
  
  public void saveTagTree(PrintWriter writer, int tabs, String bracequalifier)
  {
    boolean first = true;
    for (ConfigTag tag : getSortedTagList())
    {
      tag.save(writer, tabs, bracequalifier, first);
      first = false;
    }
  }
  
  public void writeComment(PrintWriter writer, int tabs)
  {
    if ((this.comment != null) && (!this.comment.equals("")))
    {
      String[] comments = this.comment.split("\n");
      for (int i = 0; i < comments.length; i++) {
        ConfigFile.writeLine(writer, "#" + comments[i], tabs);
      }
    }
  }
}


