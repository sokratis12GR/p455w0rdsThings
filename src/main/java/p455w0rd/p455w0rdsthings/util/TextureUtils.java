package p455w0rd.p455w0rdsthings.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TextureUtils
{
  public Minecraft mc;
  private static BlendState blendState;
  
  public void drawModLogo(ResourceLocation logoPath, double x, double y, int width, int height)
  {
    float zLevel = 300.0F;
    blendState.blend.setEnabled();
    this.mc.renderEngine.bindTexture(logoPath);
    Tessellator tess = Tessellator.getInstance();
    VertexBuffer wr = tess.getBuffer();
    GL11.glPushMatrix();
    wr.begin(7, DefaultVertexFormats.POSITION_TEX);
    wr.pos(x, y + height, zLevel).tex(0.0D, 1.0D).endVertex();
    wr.pos(x + width, y + height, zLevel).tex(1.0D, 1.0D).endVertex();
    wr.pos(x + width, y, zLevel).tex(1.0D, 0.0D).endVertex();
    wr.pos(x, y, zLevel).tex(0.0D, 0.0D).endVertex();
    tess.draw();
    GL11.glPopMatrix();
    blendState.blend.setDisabled();
  }
  
  static class BlendState
  {
    public TextureUtils.BooleanState blend;
    public int srcFactor;
    public int dstFactor;
    public int srcFactorAlpha;
    public int dstFactorAlpha;
    
    private BlendState()
    {
      this.blend = new TextureUtils.BooleanState(3042);
      this.srcFactor = 1;
      this.dstFactor = 0;
      this.srcFactorAlpha = 1;
      this.dstFactorAlpha = 0;
    }
  }
  
  static class BooleanState
  {
    private final int capability;
    private boolean currentState = false;
    
    public BooleanState(int capabilityIn)
    {
      this.capability = capabilityIn;
    }
    
    public void setDisabled()
    {
      setState(false);
    }
    
    public void setEnabled()
    {
      setState(true);
    }
    
    public void setState(boolean state)
    {
      if (state != this.currentState)
      {
        this.currentState = state;
        if (state) {
          GL11.glEnable(this.capability);
        } else {
          GL11.glDisable(this.capability);
        }
      }
    }
  }
}


