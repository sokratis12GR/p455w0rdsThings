package p455w0rd.p455w0rdsthings.lib.asm;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({"p455w0rd.p455w0rdsthings.lib.asm", "p455w0rd.p455w0rdsthings.lib.config"})
public class CCLCorePlugin
  implements IFMLLoadingPlugin
{
  public String[] getASMTransformerClass()
  {
    return new String[] { "p455w0rd.p455w0rdsthings.lib.asm.ClassHeirachyManager", "p455w0rd.p455w0rdsthings.lib.asm.RenderHookTransformer" };
  }
  
  public String getModContainerClass()
  {
    return null;
  }
  
  public String getSetupClass()
  {
    return null;
  }
  
  public void injectData(Map<String, Object> data) {}
  
  public String getAccessTransformerClass()
  {
    return null;
  }
}


