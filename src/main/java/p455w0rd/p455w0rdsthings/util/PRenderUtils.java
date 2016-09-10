package p455w0rd.p455w0rdsthings.util;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import p455w0rd.p455w0rdsthings.lib.render.CCRenderState;
import p455w0rd.p455w0rdsthings.lib.render.TextureUtils;
import p455w0rd.p455w0rdsthings.lib.vec.Cuboid6;
import p455w0rd.p455w0rdsthings.lib.vec.Vector3;

public class PRenderUtils
{
  private static Vector3[] vectors = new Vector3[8];
  private static EntityItem entityItem;
  
  static
  {
    for (int i = 0; i < vectors.length; i++) {
      vectors[i] = new Vector3();
    }
    entityItem = new EntityItem(null);
    entityItem.hoverStart = 0.0F;
  }
  
  public static TextureAtlasSprite prepareFluidRender(FluidStack stack, int alpha)
  {
    Fluid fluid = stack.getFluid();
    CCRenderState.colour = fluid.getColor(stack) << 8 | alpha;
    return TextureUtils.getTexture(fluid.getStill());
  }
  
  public static double fluidDensityToAlpha(double density)
  {
    return Math.pow(density, 0.4D);
  }
  
  public static void renderFluidCuboid(FluidStack stack, Cuboid6 bound, double density, double res)
  {
    if (!shouldRenderFluid(stack)) {
      return;
    }
    int alpha = 255;
    if (stack.getFluid().isGaseous()) {
      alpha = (int)(fluidDensityToAlpha(density) * 255.0D);
    } else {
      bound.max.y = (bound.min.y + (bound.max.y - bound.min.y) * density);
    }
    renderFluidCuboid(bound, prepareFluidRender(stack, alpha), res);
  }
  
  public static void renderFluidCuboidGL(FluidStack stack, Cuboid6 bound, double density, double res)
  {
    if (!shouldRenderFluid(stack)) {
      return;
    }
    preFluidRender();
    CCRenderState.startDrawing(7, DefaultVertexFormats.POSITION_TEX);
    renderFluidCuboid(stack, bound, density, res);
    CCRenderState.pushColour();
    CCRenderState.draw();
    postFluidRender();
  }
  
  @SuppressWarnings("deprecation")
public static void preFluidRender()
  {
    GlStateManager.disableLighting();
    GlStateManager.enableBlend();
    GlStateManager.blendFunc(770, 771);
    CCRenderState.changeTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
  }
  
  public static void postFluidRender()
  {
    GlStateManager.enableLighting();
    GlStateManager.disableBlend();
  }
  
  public static boolean shouldRenderFluid(FluidStack stack)
  {
    return (stack.amount > 0) && (stack.getFluid() != null);
  }
  
  public static void renderFluidCuboid(Cuboid6 bound, TextureAtlasSprite tex, double res)
  {
    renderFluidQuad(new Vector3(bound.min.x, bound.min.y, bound.min.z), new Vector3(bound.max.x, bound.min.y, bound.min.z), new Vector3(bound.max.x, bound.min.y, bound.max.z), new Vector3(bound.min.x, bound.min.y, bound.max.z), tex, res);
    
    renderFluidQuad(new Vector3(bound.min.x, bound.max.y, bound.min.z), new Vector3(bound.min.x, bound.max.y, bound.max.z), new Vector3(bound.max.x, bound.max.y, bound.max.z), new Vector3(bound.max.x, bound.max.y, bound.min.z), tex, res);
    
    renderFluidQuad(new Vector3(bound.min.x, bound.max.y, bound.min.z), new Vector3(bound.min.x, bound.min.y, bound.min.z), new Vector3(bound.min.x, bound.min.y, bound.max.z), new Vector3(bound.min.x, bound.max.y, bound.max.z), tex, res);
    
    renderFluidQuad(new Vector3(bound.max.x, bound.max.y, bound.max.z), new Vector3(bound.max.x, bound.min.y, bound.max.z), new Vector3(bound.max.x, bound.min.y, bound.min.z), new Vector3(bound.max.x, bound.max.y, bound.min.z), tex, res);
    
    renderFluidQuad(new Vector3(bound.max.x, bound.max.y, bound.min.z), new Vector3(bound.max.x, bound.min.y, bound.min.z), new Vector3(bound.min.x, bound.min.y, bound.min.z), new Vector3(bound.min.x, bound.max.y, bound.min.z), tex, res);
    
    renderFluidQuad(new Vector3(bound.min.x, bound.max.y, bound.max.z), new Vector3(bound.min.x, bound.min.y, bound.max.z), new Vector3(bound.max.x, bound.min.y, bound.max.z), new Vector3(bound.max.x, bound.max.y, bound.max.z), tex, res);
  }
  
  public static void renderFluidQuad(Vector3 point1, Vector3 point2, Vector3 point3, Vector3 point4, TextureAtlasSprite icon, double res)
  {
    renderFluidQuad(point2, vectors[0].set(point4).subtract(point1), vectors[1].set(point1).subtract(point2), icon, res);
  }
  
  public static void renderFluidQuad(Vector3 base, Vector3 wide, Vector3 high, TextureAtlasSprite icon, double res)
  {
    VertexBuffer r = CCRenderState.pullBuffer();
    double u1 = icon.getMinU();
    double du = icon.getMaxU() - icon.getMinU();
    double v2 = icon.getMaxV();
    double dv = icon.getMaxV() - icon.getMinV();
    
    double wlen = wide.mag();
    double hlen = high.mag();
    
    double x = 0.0D;
    while (x < wlen)
    {
      double rx = wlen - x;
      if (rx > res) {
        rx = res;
      }
      double y = 0.0D;
      while (y < hlen)
      {
        double ry = hlen - y;
        if (ry > res) {
          ry = res;
        }
        Vector3 dx1 = vectors[2].set(wide).multiply(x / wlen);
        Vector3 dx2 = vectors[3].set(wide).multiply((x + rx) / wlen);
        Vector3 dy1 = vectors[4].set(high).multiply(y / hlen);
        Vector3 dy2 = vectors[5].set(high).multiply((y + ry) / hlen);
        
        r.pos(base.x + dx1.x + dy2.x, base.y + dx1.y + dy2.y, base.z + dx1.z + dy2.z).tex(u1, v2 - ry / res * dv).endVertex();
        r.pos(base.x + dx1.x + dy1.x, base.y + dx1.y + dy1.y, base.z + dx1.z + dy1.z).tex(u1, v2).endVertex();
        r.pos(base.x + dx2.x + dy1.x, base.y + dx2.y + dy1.y, base.z + dx2.z + dy1.z).tex(u1 + rx / res * du, v2).endVertex();
        r.pos(base.x + dx2.x + dy2.x, base.y + dx2.y + dy2.y, base.z + dx2.z + dy2.z).tex(u1 + rx / res * du, v2 - ry / res * dv).endVertex();
        
        y += ry;
      }
      x += rx;
    }
  }
}


