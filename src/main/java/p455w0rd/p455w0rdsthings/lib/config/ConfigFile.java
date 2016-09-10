package p455w0rd.p455w0rdsthings.lib.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ConfigFile
  extends ConfigTagParent
{
  public static final byte[] crlf = { 13, 10 };
  public File file;
  private boolean loading;
  
  public ConfigFile(File file)
  {
    this.newlinemode = 2;
    load(file);
  }
  
  protected ConfigFile() {}
  
  protected void load(File file)
  {
    try
    {
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      if (!file.exists()) {
        file.createNewFile();
      }
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    this.file = file;
    loadConfig();
  }
  
  protected void loadConfig()
  {
    this.loading = true;
    try
    {
      BufferedReader reader = new BufferedReader(new FileReader(this.file));
      for (;;)
      {
        reader.mark(2000);
        String line = reader.readLine();
        if ((line != null) && (line.startsWith("#")))
        {
          if ((this.comment == null) || (this.comment.equals(""))) {
            this.comment = line.substring(1);
          } else {
            this.comment = (this.comment + "\n" + line.substring(1));
          }
        }
        else
        {
          reader.reset();
          break;
        }
      }
      loadChildren(reader);
      reader.close();
    }
    catch (IOException e)
    {
      throw new RuntimeException(e);
    }
    this.loading = false;
  }
  
  public ConfigFile setComment(String header)
  {
    super.setComment(header);
    return this;
  }
  
  public ConfigFile setSortMode(int mode)
  {
    super.setSortMode(mode);
    return this;
  }
  
  public String getNameQualifier()
  {
    return "";
  }
  
  public static String readLine(BufferedReader reader)
    throws IOException
  {
    String line = reader.readLine();
    return line == null ? null : line.replace("\t", "");
  }
  
  public static void writeLine(PrintWriter writer, String line, int tabs)
  {
    for (int i = 0; i < tabs; i++) {
      writer.print('\t');
    }
    writer.println(line);
  }
  
  public void saveConfig() {
      PrintWriter writer;
      if (this.loading) {
          return;
      }
      try {
          writer = new PrintWriter(this.file);
      }
      catch (FileNotFoundException e) {
          throw new RuntimeException(e);
      }
      this.writeComment(writer, 0);
      ConfigFile.writeLine(writer, "", 0);
      this.saveTagTree(writer, 0, "");
      writer.flush();
      writer.close();
  }
  
  public boolean isLoading()
  {
    return this.loading;
  }
}


