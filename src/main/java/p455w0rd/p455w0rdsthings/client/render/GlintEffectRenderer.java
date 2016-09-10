package p455w0rd.p455w0rdsthings.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class GlintEffectRenderer
{
  public static void apply(IBakedModel model, int damage)
  {
    GlStateManager.depthMask(false);
    GlStateManager.depthFunc(514);
    GlStateManager.disableLighting();
    GlStateManager.blendFunc(SourceFactor.SRC_COLOR, DestFactor.ONE);
    Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("minecraft", "textures/misc/enchanted_item_glint.png"));
    GlStateManager.matrixMode(5890);
    GlStateManager.pushMatrix();
    GlStateManager.scale(8.0F, 8.0F, 8.0F);
    float f = (float)(Minecraft.getSystemTime() % 3000L) / 3000.0F / 8.0F;
    GlStateManager.translate(f, 0.0F, 0.0F);
    GlStateManager.rotate(-50.0F, 0.0F, 0.0F, 1.0F);
    switch (damage)
    {
    case 0: 
      RenderModel.render(model, -10092544);
      break;
    case 1: 
      RenderModel.render(model, -16777114);
      break;
    case 2: 
      RenderModel.render(model, -10066330);
      break;
    case 3: 
      RenderModel.render(model, -10066432);
      break;
    case 4: 
      RenderModel.render(model, -12097946);
      break;
    case 5: 
      RenderModel.render(model, -16751104);
      break;
    case 6: 
      RenderModel.render(model, -65536);
      break;
    case 7: 
      RenderModel.render(model, -10704897);
      break;
    case 8: 
      RenderModel.render(model, -1);
      break;
    case 9: 
      RenderModel.render(model, 65280);
      break;
    case 10: 
      RenderModel.render(model, -12058625);
      break;
    case 11: 
      RenderModel.render(model, -16711936);
      break;
    case -1: 
    default: 
      RenderModel.render(model, -8372020);
    }
    GlStateManager.popMatrix();
    GlStateManager.matrixMode(5888);
    GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
    GlStateManager.enableLighting();
    GlStateManager.depthFunc(515);
    GlStateManager.depthMask(true);
    Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
  }
}


