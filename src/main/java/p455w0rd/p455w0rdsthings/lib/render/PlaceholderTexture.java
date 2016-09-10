package p455w0rd.p455w0rdsthings.lib.render;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class PlaceholderTexture
  extends TextureAtlasSprite
{
  protected PlaceholderTexture(String par1)
  {
    super(par1);
  }
  
  public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location)
  {
    return true;
  }
  
  public boolean load(IResourceManager manager, ResourceLocation location)
  {
    return true;
  }
}


