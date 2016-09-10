package p455w0rd.p455w0rdsthings.lib.config;

import java.io.File;

public class DefaultingConfigFile
  extends ConfigFile
{
  public DefaultingConfigFile(File file)
  {
    if (file.exists()) {
      load(file);
    }
  }
  
  public void saveConfig()
  {
    if (this.file != null) {
      super.saveConfig();
    }
  }
}


