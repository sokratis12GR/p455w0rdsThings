package p455w0rd.p455w0rdsthings.lib.render;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.lib.color.ColourRGBA;
import p455w0rd.p455w0rdsthings.lib.lighting.LC;
import p455w0rd.p455w0rdsthings.lib.lighting.LightMatrix;
import p455w0rd.p455w0rdsthings.lib.util.Copyable;
import p455w0rd.p455w0rdsthings.lib.vec.Transformation;
import p455w0rd.p455w0rdsthings.lib.vec.Vector3;
import p455w0rd.p455w0rdsthings.lib.vec.Rotation;

public class CCRenderState
{
  private static int nextOperationIndex;
  
  public static int registerOperation()
  {
    return nextOperationIndex++;
  }
  
  public static int operationCount()
  {
    return nextOperationIndex;
  }
  
  private static ArrayList<VertexAttribute<?>> vertexAttributes = new ArrayList<VertexAttribute<?>>();
  
  private static int registerVertexAttribute(VertexAttribute<?> attr)
  {
    vertexAttributes.add(attr);
    return vertexAttributes.size() - 1;
  }
  
  public static VertexAttribute<?> getAttribute(int index)
  {
    return vertexAttributes.get(index);
  }
  
  public static List<VertexAttribute<?>> getRegisteredVertexAttributes()
  {
    return ImmutableList.copyOf(vertexAttributes);
  }
  
  public static abstract interface IVertexSource
  {
    public abstract Vertex5[] getVertices();
    
    public abstract <T> T getAttributes(CCRenderState.VertexAttribute<T> paramVertexAttribute);
    
    public abstract boolean hasAttribute(CCRenderState.VertexAttribute<?> paramVertexAttribute);
    
    public abstract void prepareVertex();
  }
  
  public static abstract class VertexAttribute<T>
    implements CCRenderState.IVertexOperation
  {
    public final int attributeIndex = CCRenderState.registerVertexAttribute(this);
    private final int operationIndex = CCRenderState.registerOperation();
    public boolean active = false;
    
    public abstract T newArray(int paramInt);
    
    public int operationID()
    {
      return this.operationIndex;
    }
  }
  
  @SuppressWarnings("unchecked")
public static void arrayCopy(Object src, int srcPos, Object dst, int destPos, int length)
  {
    System.arraycopy(src, srcPos, dst, destPos, length);
    if ((dst instanceof Copyable[]))
    {
      Object[] oa = (Object[])dst;
      Copyable<Object>[] c = (Copyable[])dst;
      for (int i = destPos; i < destPos + length; i++) {
        if (c[i] != null) {
          oa[i] = c[i].copy();
        }
      }
    }
  }
  
  @SuppressWarnings("unchecked")
public static <T> T copyOf(VertexAttribute<?> vertexAttribute, Object object, int length)
  {
    T dst = (T) vertexAttribute.newArray(length);
    arrayCopy(object, 0, dst, 0, ((Object[])object).length);
    return dst;
  }
  
  public static VertexAttribute<Vector3[]> normalAttrib = new VertexAttribute<Vector3[]>()
  {
    private Vector3[] normalRef;
    
    public Vector3[] newArray(int length)
    {
      return new Vector3[length];
    }
    
    public boolean load()
    {
      this.normalRef = ((Vector3[])CCRenderState.model.getAttributes(this));
      if (CCRenderState.model.hasAttribute(this)) {
        return this.normalRef != null;
      }
      if (CCRenderState.model.hasAttribute(CCRenderState.sideAttrib))
      {
        CCRenderState.pipeline.addDependency(CCRenderState.sideAttrib);
        return true;
      }
      throw new IllegalStateException("Normals requested but neither normal or side attrutes are provided by the model");
    }
    
    public void operate()
    {
      if (this.normalRef != null) {
        CCRenderState.normal.set(this.normalRef[CCRenderState.vertexIndex]);
      } else {
        CCRenderState.normal.set(Rotation.axes[CCRenderState.side]);
      }
    }
  };
  public static VertexAttribute<int[]> colourAttrib = new VertexAttribute<int[]>()
  {
    private int[] colourRef;
    
    public int[] newArray(int length)
    {
      return new int[length];
    }
    
    public boolean load()
    {
      this.colourRef = ((int[])CCRenderState.model.getAttributes(this));
      return (this.colourRef != null) || (!CCRenderState.model.hasAttribute(this));
    }
    
    public void operate()
    {
      if (this.colourRef != null) {
        CCRenderState.colour = ColourRGBA.multiply(CCRenderState.baseColour, this.colourRef[CCRenderState.vertexIndex]);
      } else {
        CCRenderState.colour = CCRenderState.baseColour;
      }
    }
  };
  public static VertexAttribute<int[]> lightingAttrib = new VertexAttribute<int[]>()
  {
    private int[] colourRef;
    
    public int[] newArray(int length)
    {
      return new int[length];
    }
    
    public boolean load()
    {
      if ((!CCRenderState.computeLighting) || (!CCRenderState.fmt.hasColor()) || (!CCRenderState.model.hasAttribute(this))) {
        return false;
      }
      this.colourRef = ((int[])CCRenderState.model.getAttributes(this));
      if (this.colourRef != null)
      {
        CCRenderState.pipeline.addDependency(CCRenderState.colourAttrib);
        return true;
      }
      return false;
    }
    
    public void operate()
    {
      CCRenderState.colour = ColourRGBA.multiply(CCRenderState.colour, this.colourRef[CCRenderState.vertexIndex]);
    }
  };
  public static VertexAttribute<int[]> sideAttrib = new VertexAttribute<int[]>()
  {
    private int[] sideRef;
    
    public int[] newArray(int length)
    {
      return new int[length];
    }
    
    public boolean load()
    {
      this.sideRef = ((int[])CCRenderState.model.getAttributes(this));
      if (CCRenderState.model.hasAttribute(this)) {
        return this.sideRef != null;
      }
      CCRenderState.pipeline.addDependency(CCRenderState.normalAttrib);
      return true;
    }
    
    public void operate()
    {
      if (this.sideRef != null) {
        CCRenderState.side = this.sideRef[CCRenderState.vertexIndex];
      } else {
        CCRenderState.side = CCModel.findSide(CCRenderState.normal);
      }
    }
  };
  public static VertexAttribute<LC[]> lightCoordAttrib = new VertexAttribute<LC[]>()
  {
    private LC[] lcRef;
    private Vector3 vec = new Vector3();
    private Vector3 pos = new Vector3();
    
    public LC[] newArray(int length)
    {
      return new LC[length];
    }
    
    public boolean load()
    {
      this.lcRef = ((LC[])CCRenderState.model.getAttributes(this));
      if (CCRenderState.model.hasAttribute(this)) {
        return this.lcRef != null;
      }
      this.pos.set(CCRenderState.lightMatrix.pos.x, CCRenderState.lightMatrix.pos.y, CCRenderState.lightMatrix.pos.z);
      CCRenderState.pipeline.addDependency(CCRenderState.sideAttrib);
      CCRenderState.pipeline.addRequirement(Transformation.operationIndex);
      return true;
    }
    
    public void operate()
    {
      if (this.lcRef != null) {
        CCRenderState.lc.set(this.lcRef[CCRenderState.vertexIndex]);
      } else {
        CCRenderState.lc.compute(this.vec.set(CCRenderState.vert.vec).sub(this.pos), CCRenderState.side);
      }
    }
  };
  public static IVertexSource model;
  public static int firstVertexIndex;
  public static int lastVertexIndex;
  public static int vertexIndex;
  public static CCRenderPipeline pipeline = new CCRenderPipeline();
  @SideOnly(Side.CLIENT)
  public static VertexBuffer r;
  @SideOnly(Side.CLIENT)
  public static VertexFormat fmt;
  public static int baseColour;
  public static int alphaOverride;
  public static boolean computeLighting;
  public static LightMatrix lightMatrix = new LightMatrix();
  public static final Vertex5 vert = new Vertex5();
  public static final Vector3 normal = new Vector3();
  public static int colour;
  public static int brightness;
  public static int side;
  public static LC lc = new LC();
  
  public static void reset()
  {
    model = null;
    pipeline.reset();
    computeLighting = true;
    baseColour = alphaOverride = -1;
  }
  
  public static void setPipeline(IVertexOperation... ops)
  {
    pipeline.setPipeline(ops);
  }
  
  public static void setPipeline(IVertexSource model, int start, int end, IVertexOperation... ops)
  {
    pipeline.reset();
    setModel(model, start, end);
    pipeline.setPipeline(ops);
  }
  
  public static void bindModel(IVertexSource model)
  {
    if (CCRenderState.model != model)
    {
    	CCRenderState.model = model;
      pipeline.rebuild();
    }
  }
  
  public static void setModel(IVertexSource source)
  {
    setModel(source, 0, source.getVertices().length);
  }
  
  public static void setModel(IVertexSource source, int start, int end)
  {
    bindModel(source);
    setVertexRange(start, end);
  }
  
  public static void setVertexRange(int start, int end)
  {
    firstVertexIndex = start;
    lastVertexIndex = end;
  }
  
  public static CCDynamicModel dynamicModel(VertexAttribute<?>... attrs)
  {
    CCDynamicModel m = new CCDynamicModel(attrs);
    bindModel(m);
    return m;
  }
  
  public static void render(IVertexOperation... ops)
  {
    setPipeline(ops);
    render();
  }
  
  public static void render()
  {
    Vertex5[] verts = model.getVertices();
    for (vertexIndex = firstVertexIndex; vertexIndex < lastVertexIndex; vertexIndex += 1)
    {
      model.prepareVertex();
      vert.set(verts[vertexIndex]);
      runPipeline();
      writeVert();
    }
  }
  
  public static void runPipeline()
  {
    pipeline.operate();
  }
  
  public static void writeVert()
  {
    for (int e = 0; e < fmt.getElementCount(); e++)
    {
      VertexFormatElement fmte = fmt.getElement(e);
      switch (fmte.getUsage())
      {
      case POSITION: 
        r.pos(vert.vec.x, vert.vec.y, vert.vec.z);
        break;
      case UV: 
        if (fmte.getIndex() == 0) {
          r.tex(vert.uv.u, vert.uv.v);
        } else {
          r.lightmap(brightness >> 16 & 0xFFFF, brightness & 0xFFFF);
        }
        break;
      case COLOR: 
        r.color(colour >>> 24, colour >> 16 & 0xFF, colour >> 8 & 0xFF, alphaOverride >= 0 ? alphaOverride : colour & 0xFF);
        break;
      case NORMAL: 
        r.normal((float)normal.x, (float)normal.y, (float)normal.z);
        break;
      case PADDING: 
        break;
      default: 
        throw new UnsupportedOperationException("Generic vertex format element");
      }
    }
    r.endVertex();
  }
  
  public static void pushColour()
  {
    GlStateManager.color((colour >>> 24) / 255.0F, (colour >> 16 & 0xFF) / 255.0F, (colour >> 8 & 0xFF) / 255.0F, (alphaOverride >= 0 ? alphaOverride : colour & 0xFF) / 255.0F);
  }
  
  @SuppressWarnings("deprecation")
public static void setBrightness(IBlockAccess world, BlockPos pos)
  {
    brightness = world.getBlockState(pos).getBlock().getPackedLightmapCoords(world.getBlockState(pos), world, pos);
  }
  
  public static void pullLightmap()
  {
    brightness = (int)OpenGlHelper.lastBrightnessY << 16 | (int)OpenGlHelper.lastBrightnessX;
  }
  
  public static void pushLightmap()
  {
    OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, brightness & 0xFFFF, brightness >>> 16);
  }
  
  @Deprecated
  public static void changeTexture(String texture)
  {
    TextureUtils.changeTexture(texture);
  }
  
  @Deprecated
  public static void changeTexture(ResourceLocation texture)
  {
    TextureUtils.changeTexture(texture);
  }
  
  @SideOnly(Side.CLIENT)
  public static VertexBuffer startDrawing(int mode, VertexFormat format)
  {
    VertexBuffer r = Tessellator.getInstance().getBuffer();
    r.begin(mode, format);
    return r;
  }
  
  @SideOnly(Side.CLIENT)
  public static void bind(VertexBuffer r)
  {
	  CCRenderState.r = r;
    fmt = r.getVertexFormat();
  }
  
  @SideOnly(Side.CLIENT)
  public static VertexBuffer pullBuffer()
  {
    bind(Tessellator.getInstance().getBuffer());
    return r;
  }
  
  public static void draw()
  {
    Tessellator.getInstance().draw();
  }
  
  public static abstract interface IVertexOperation
  {
    public abstract boolean load();
    
    public abstract void operate();
    
    public abstract int operationID();
  }
}


