package p455w0rd.p455w0rdsthings.client.render;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.model.pipeline.LightUtil;

public class RenderModel
{
  public static void render(IBakedModel model, ItemStack stack)
  {
    render(model, -1, stack);
  }
  
  public static void render(IBakedModel model, int color)
  {
    render(model, color, (ItemStack)null);
  }
  
  public static void render(IBakedModel model, int color, ItemStack stack)
  {
    Tessellator tessellator = Tessellator.getInstance();
    VertexBuffer vertexbuffer = tessellator.getBuffer();
    vertexbuffer.begin(7, DefaultVertexFormats.ITEM);
    for (EnumFacing enumfacing : EnumFacing.values()) {
      renderQuads(vertexbuffer, model.getQuads((IBlockState)null, enumfacing, 0L), color, stack);
    }
    renderQuads(vertexbuffer, model.getQuads((IBlockState)null, (EnumFacing)null, 0L), color, stack);
    tessellator.draw();
  }
  
  public static void renderQuads(VertexBuffer renderer, List<BakedQuad> quads, int color, ItemStack stack)
  {
    boolean flag = (color == -1) && (stack != null);
    int i = 0;
    for (int j = quads.size(); i < j; i++)
    {
      BakedQuad bakedquad = (BakedQuad)quads.get(i);
      int k = color;
      if ((flag) && (bakedquad.hasTintIndex()))
      {
        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        k = itemColors.getColorFromItemstack(stack, bakedquad.getTintIndex());
        if (EntityRenderer.anaglyphEnable) {
          k = TextureUtil.anaglyphColor(k);
        }
        k |= 0xFF000000;
      }
      LightUtil.renderQuadColor(renderer, bakedquad, k);
    }
  }
}


