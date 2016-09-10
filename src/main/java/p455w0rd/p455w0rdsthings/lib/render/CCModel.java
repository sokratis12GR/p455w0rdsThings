package p455w0rd.p455w0rdsthings.lib.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import p455w0rd.p455w0rdsthings.lib.lighting.LC;
import p455w0rd.p455w0rdsthings.lib.lighting.LightModel;
import p455w0rd.p455w0rdsthings.lib.render.CCModel;
import p455w0rd.p455w0rdsthings.lib.render.uv.UV;
import p455w0rd.p455w0rdsthings.lib.render.uv.UVTransformation;
import p455w0rd.p455w0rdsthings.lib.render.uv.UVTranslation;
import p455w0rd.p455w0rdsthings.lib.util.Copyable;
import p455w0rd.p455w0rdsthings.lib.vec.Cuboid6;
import p455w0rd.p455w0rdsthings.lib.vec.RedundantTransformation;
import p455w0rd.p455w0rdsthings.lib.vec.Transformation;
import p455w0rd.p455w0rdsthings.lib.vec.TransformationList;
import p455w0rd.p455w0rdsthings.lib.vec.Vector3;

public class CCModel
  implements CCRenderState.IVertexSource, Copyable<CCModel>
{
  public final int vertexMode;
  public final int vp;
  public Vertex5[] verts;
  
  private static class PositionNormalEntry
  {
    public Vector3 pos;
    public LinkedList<Vector3> normals = new LinkedList<Vector3>();
    
    public PositionNormalEntry(Vector3 position)
    {
      this.pos = position;
    }
    
    public boolean positionEqual(Vector3 v)
    {
      return (this.pos.x == v.x) && (this.pos.y == v.y) && (this.pos.z == v.z);
    }
    
    public PositionNormalEntry addNormal(Vector3 normal)
    {
      this.normals.add(normal);
      return this;
    }
  }
  
  public ArrayList<Object> attributes = new ArrayList<Object>();
  
  protected CCModel(int vertexMode)
  {
    if ((vertexMode != 7) && (vertexMode != 4)) {
      throw new IllegalArgumentException("Models must be GL_QUADS or GL_TRIANGLES");
    }
    this.vertexMode = vertexMode;
    this.vp = (vertexMode == 7 ? 4 : 3);
  }
  
  public Vector3[] normals()
  {
    return (Vector3[])getAttributes(CCRenderState.normalAttrib);
  }
  
  public Vertex5[] getVertices()
  {
    return this.verts;
  }
  
  @SuppressWarnings("unchecked")
public <T> T getAttributes(CCRenderState.VertexAttribute<T> attr)
  {
    if (attr.attributeIndex < this.attributes.size()) {
      return (T)this.attributes.get(attr.attributeIndex);
    }
    return null;
  }
  
  public boolean hasAttribute(CCRenderState.VertexAttribute<?> attrib)
  {
    return (attrib.attributeIndex < this.attributes.size()) && (this.attributes.get(attrib.attributeIndex) != null);
  }
  
  public void prepareVertex() {}
  
  public <T> T getOrAllocate(CCRenderState.VertexAttribute<T> attrib)
  {
    T array = getAttributes(attrib);
    if (array == null)
    {
      while (this.attributes.size() <= attrib.attributeIndex) {
        this.attributes.add(null);
      }
      this.attributes.set(attrib.attributeIndex, array = attrib.newArray(this.verts.length));
    }
    return array;
  }
  
  public CCModel generateBox(int i, double x1, double y1, double z1, double w, double h, double d, double tx, double ty, double tw, double th, double f)
  {
    double x2 = x1 + w;
    double y2 = y1 + h;
    double z2 = z1 + d;
    x1 /= f;
    x2 /= f;
    y1 /= f;
    y2 /= f;
    z1 /= f;
    z2 /= f;
    
    double u1 = (tx + d + w) / tw;
    double v1 = (ty + d) / th;
    double u2 = (tx + d * 2.0D + w) / tw;
    double v2 = ty / th;
    this.verts[(i++)] = new Vertex5(x1, y1, z2, u1, v2);
    this.verts[(i++)] = new Vertex5(x1, y1, z1, u1, v1);
    this.verts[(i++)] = new Vertex5(x2, y1, z1, u2, v1);
    this.verts[(i++)] = new Vertex5(x2, y1, z2, u2, v2);
    
    u1 = (tx + d) / tw;
    v1 = (ty + d) / th;
    u2 = (tx + d + w) / tw;
    v2 = ty / th;
    this.verts[(i++)] = new Vertex5(x2, y2, z2, u2, v2);
    this.verts[(i++)] = new Vertex5(x2, y2, z1, u2, v1);
    this.verts[(i++)] = new Vertex5(x1, y2, z1, u1, v1);
    this.verts[(i++)] = new Vertex5(x1, y2, z2, u1, v2);
    
    u1 = (tx + d + w) / tw;
    v1 = (ty + d) / th;
    u2 = (tx + d) / tw;
    v2 = (ty + d + h) / th;
    this.verts[(i++)] = new Vertex5(x1, y2, z1, u2, v1);
    this.verts[(i++)] = new Vertex5(x2, y2, z1, u1, v1);
    this.verts[(i++)] = new Vertex5(x2, y1, z1, u1, v2);
    this.verts[(i++)] = new Vertex5(x1, y1, z1, u2, v2);
    
    u1 = (tx + d * 2.0D + w * 2.0D) / tw;
    v1 = (ty + d) / th;
    u2 = (tx + d * 2.0D + w) / tw;
    v2 = (ty + d + h) / th;
    this.verts[(i++)] = new Vertex5(x1, y2, z2, u1, v1);
    this.verts[(i++)] = new Vertex5(x1, y1, z2, u1, v2);
    this.verts[(i++)] = new Vertex5(x2, y1, z2, u2, v2);
    this.verts[(i++)] = new Vertex5(x2, y2, z2, u2, v1);
    
    u1 = (tx + d) / tw;
    v1 = (ty + d) / th;
    u2 = tx / tw;
    v2 = (ty + d + h) / th;
    this.verts[(i++)] = new Vertex5(x1, y2, z2, u2, v1);
    this.verts[(i++)] = new Vertex5(x1, y2, z1, u1, v1);
    this.verts[(i++)] = new Vertex5(x1, y1, z1, u1, v2);
    this.verts[(i++)] = new Vertex5(x1, y1, z2, u2, v2);
    
    u1 = (tx + d * 2.0D + w) / tw;
    v1 = (ty + d) / th;
    u2 = (tx + d + w) / tw;
    v2 = (ty + d + h) / th;
    this.verts[(i++)] = new Vertex5(x2, y1, z2, u1, v2);
    this.verts[(i++)] = new Vertex5(x2, y1, z1, u2, v2);
    this.verts[(i++)] = new Vertex5(x2, y2, z1, u2, v1);
    this.verts[(i++)] = new Vertex5(x2, y2, z2, u1, v1);
    
    return this;
  }
  
  public CCModel generateBlock(int i, Cuboid6 bounds)
  {
    return generateBlock(i, bounds, 0);
  }
  
  public CCModel generateBlock(int i, Cuboid6 bounds, int mask)
  {
    return generateBlock(i, bounds.min.x, bounds.min.y, bounds.min.z, bounds.max.x, bounds.max.y, bounds.max.z, mask);
  }
  
  public CCModel generateBlock(int i, double x1, double y1, double z1, double x2, double y2, double z2)
  {
    return generateBlock(i, x1, y1, z1, x2, y2, z2, 0);
  }
  
  public CCModel generateBlock(int i, double x1, double y1, double z1, double x2, double y2, double z2, int mask)
  {
    if ((mask & 0x1) == 0)
    {
      double u1 = x1;
      double v1 = z1;
      double u2 = x2;
      double v2 = z2;
      this.verts[(i++)] = new Vertex5(x1, y1, z2, u1, v2, 0);
      this.verts[(i++)] = new Vertex5(x1, y1, z1, u1, v1, 0);
      this.verts[(i++)] = new Vertex5(x2, y1, z1, u2, v1, 0);
      this.verts[(i++)] = new Vertex5(x2, y1, z2, u2, v2, 0);
    }
    if ((mask & 0x2) == 0)
    {
      double u1 = x1;
      double v1 = z1;
      double u2 = x2;
      double v2 = z2;
      this.verts[(i++)] = new Vertex5(x2, y2, z2, u2, v2, 1);
      this.verts[(i++)] = new Vertex5(x2, y2, z1, u2, v1, 1);
      this.verts[(i++)] = new Vertex5(x1, y2, z1, u1, v1, 1);
      this.verts[(i++)] = new Vertex5(x1, y2, z2, u1, v2, 1);
    }
    if ((mask & 0x4) == 0)
    {
      double u1 = 1.0D - x1;
      double v1 = 1.0D - y2;
      double u2 = 1.0D - x2;
      double v2 = 1.0D - y1;
      this.verts[(i++)] = new Vertex5(x1, y1, z1, u1, v2, 2);
      this.verts[(i++)] = new Vertex5(x1, y2, z1, u1, v1, 2);
      this.verts[(i++)] = new Vertex5(x2, y2, z1, u2, v1, 2);
      this.verts[(i++)] = new Vertex5(x2, y1, z1, u2, v2, 2);
    }
    if ((mask & 0x8) == 0)
    {
      double u1 = x1;
      double v1 = 1.0D - y2;
      double u2 = x2;
      double v2 = 1.0D - y1;
      this.verts[(i++)] = new Vertex5(x2, y1, z2, u2, v2, 3);
      this.verts[(i++)] = new Vertex5(x2, y2, z2, u2, v1, 3);
      this.verts[(i++)] = new Vertex5(x1, y2, z2, u1, v1, 3);
      this.verts[(i++)] = new Vertex5(x1, y1, z2, u1, v2, 3);
    }
    if ((mask & 0x10) == 0)
    {
      double u1 = z1;
      double v1 = 1.0D - y2;
      double u2 = z2;
      double v2 = 1.0D - y1;
      this.verts[(i++)] = new Vertex5(x1, y1, z2, u2, v2, 4);
      this.verts[(i++)] = new Vertex5(x1, y2, z2, u2, v1, 4);
      this.verts[(i++)] = new Vertex5(x1, y2, z1, u1, v1, 4);
      this.verts[(i++)] = new Vertex5(x1, y1, z1, u1, v2, 4);
    }
    if ((mask & 0x20) == 0)
    {
      double u1 = 1.0D - z1;
      double v1 = 1.0D - y2;
      double u2 = 1.0D - z2;
      double v2 = 1.0D - y1;
      this.verts[(i++)] = new Vertex5(x2, y1, z1, u1, v2, 5);
      this.verts[(i++)] = new Vertex5(x2, y2, z1, u1, v1, 5);
      this.verts[(i++)] = new Vertex5(x2, y2, z2, u2, v1, 5);
      this.verts[(i++)] = new Vertex5(x2, y1, z2, u2, v2, 5);
    }
    return this;
  }
  
  public CCModel computeNormals()
  {
    return computeNormals(0, this.verts.length);
  }
  
  public CCModel computeNormals(int start, int length)
  {
    if ((length % this.vp != 0) || (start % this.vp != 0)) {
      throw new IllegalArgumentException("Cannot generate normals across polygons");
    }
    Vector3[] normals = (Vector3[])getOrAllocate(CCRenderState.normalAttrib);
    for (int k = 0; k < length; k += this.vp)
    {
      int i = k + start;
      Vector3 diff1 = this.verts[(i + 1)].vec.copy().subtract(this.verts[i].vec);
      Vector3 diff2 = this.verts[(i + this.vp - 1)].vec.copy().subtract(this.verts[i].vec);
      normals[i] = diff1.crossProduct(diff2).normalize();
      for (int d = 1; d < this.vp; d++) {
        normals[(i + d)] = normals[i].copy();
      }
    }
    return this;
  }
  
  public CCModel computeLighting(LightModel light)
  {
    Vector3[] normals = normals();
    int[] colours = (int[])getAttributes(CCRenderState.lightingAttrib);
    if (colours == null)
    {
      colours = (int[])getOrAllocate(CCRenderState.lightingAttrib);
      Arrays.fill(colours, -1);
    }
    for (int k = 0; k < this.verts.length; k++) {
      colours[k] = light.apply(colours[k], normals[k]);
    }
    return this;
  }
  
  public CCModel setColour(int c)
  {
    int[] colours = (int[])getOrAllocate(CCRenderState.colourAttrib);
    Arrays.fill(colours, c);
    return this;
  }
  
  public CCModel computeLightCoords()
  {
    LC[] lcs = (LC[])getOrAllocate(CCRenderState.lightCoordAttrib);
    Vector3[] normals = normals();
    for (int i = 0; i < this.verts.length; i++) {
      lcs[i] = new LC().compute(this.verts[i].vec, normals[i]);
    }
    return this;
  }
  
  public CCModel smoothNormals() {
      ArrayList<PositionNormalEntry> map = new ArrayList<PositionNormalEntry>();
      Vector3[] normals = this.normals();
      block0 : for (int k = 0; k < this.verts.length; ++k) {
          Vector3 vec = this.verts[k].vec;
          for (PositionNormalEntry e : map) {
              if (!e.positionEqual(vec)) continue;
              e.addNormal(normals[k]);
              continue block0;
          }
          map.add(new PositionNormalEntry(vec).addNormal(normals[k]));
      }
      for (PositionNormalEntry e : map) {
          if (e.normals.size() <= 1) continue;
          Vector3 new_n = new Vector3();
          for (Vector3 n2 : e.normals) {
              new_n.add(n2);
          }
          new_n.normalize();
          for (Vector3 n : e.normals) {
              n.set(new_n);
          }
      }
      return this;
  }
  
  public CCModel apply(Transformation t)
  {
    for (int k = 0; k < this.verts.length; k++) {
      this.verts[k].apply(t);
    }
    Vector3[] normals = normals();
    if (normals != null) {
      for (int k = 0; k < normals.length; k++) {
        t.applyN(normals[k]);
      }
    }
    return this;
  }
  
  public CCModel apply(UVTransformation uvt)
  {
    for (int k = 0; k < this.verts.length; k++) {
      this.verts[k].apply(uvt);
    }
    return this;
  }
  
  public CCModel expand(int extraVerts) {
      int newLen = this.verts.length + extraVerts;
      this.verts = Arrays.copyOf(this.verts, newLen);
      for (int i = 0; i < this.attributes.size(); ++i) {
          if (this.attributes.get(i) == null) continue;
          this.attributes.set(i, CCRenderState.copyOf(CCRenderState.getAttribute(i), this.attributes.get(i), newLen));
      }
      return this;
  }
  
  public void render(double x, double y, double z, double u, double v)
  {
    render(new CCRenderState.IVertexOperation[] { new Vector3(x, y, z).translation(), new UVTranslation(u, v) });
  }
  
  public void render(double x, double y, double z, UVTransformation u)
  {
    render(new CCRenderState.IVertexOperation[] { new Vector3(x, y, z).translation(), u });
  }
  
  public void render(Transformation t, double u, double v)
  {
    render(new CCRenderState.IVertexOperation[] { t, new UVTranslation(u, v) });
  }
  
  public void render(CCRenderState.IVertexOperation... ops)
  {
    render(0, this.verts.length, ops);
  }
  
  public void render(int start, int end, CCRenderState.IVertexOperation... ops)
  {
    CCRenderState.setPipeline(this, start, end, ops);
    CCRenderState.render();
  }
  
  public static CCModel quadModel(int numVerts)
  {
    return newModel(7, numVerts);
  }
  
  public static CCModel triModel(int numVerts)
  {
    return newModel(4, numVerts);
  }
  
  public static CCModel newModel(int vertexMode, int numVerts)
  {
    CCModel model = newModel(vertexMode);
    model.verts = new Vertex5[numVerts];
    return model;
  }
  
  public static CCModel newModel(int vertexMode)
  {
    return new CCModel(vertexMode);
  }
  
  public static double[] parseDoubles(String s, String token)
  {
    String[] as = s.split(token);
    double[] values = new double[as.length];
    for (int i = 0; i < as.length; i++) {
      values[i] = Double.parseDouble(as[i]);
    }
    return values;
  }
  
  public static void illegalAssert(boolean b, String err)
  {
    if (!b) {
      throw new IllegalArgumentException(err);
    }
  }
  
  public static void assertMatch(Matcher m, String s)
  {
    m.reset(s);
    illegalAssert(m.matches(), "Malformed line: " + s);
  }
  
  private static final Pattern vertPattern = Pattern.compile("v(?: ([\\d\\.+-]+))+");
  private static final Pattern uvwPattern = Pattern.compile("vt(?: ([\\d\\.+-]+))+");
  private static final Pattern normalPattern = Pattern.compile("vn(?: ([\\d\\.+-]+))+");
  private static final Pattern polyPattern = Pattern.compile("f(?: ((?:\\d*)(?:/\\d*)?(?:/\\d*)?))+");
  public static final Matcher vertMatcher = vertPattern.matcher("");
  public static final Matcher uvwMatcher = uvwPattern.matcher("");
  public static final Matcher normalMatcher = normalPattern.matcher("");
  public static final Matcher polyMatcher = polyPattern.matcher("");
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
public static Map<String, CCModel> parseObjModels(InputStream input, int vertexMode, Transformation coordSystem)
    throws IOException
  {
    if (coordSystem == null) {
      coordSystem = new RedundantTransformation();
    }
    int vp = vertexMode == 7 ? 4 : 3;
    
    HashMap<String, CCModel> modelMap = new HashMap();
    ArrayList<Vector3> verts = new ArrayList();
    ArrayList<Vector3> uvs = new ArrayList();
    ArrayList<Vector3> normals = new ArrayList();
    ArrayList<int[]> polys = new ArrayList();
    String modelName = "unnamed";
    
    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
    String line;
    while ((line = reader.readLine()) != null)
    {
      line = line.replaceAll("\\s+", " ").trim();
      if ((!line.startsWith("#")) && (line.length() != 0)) {
        if (line.startsWith("v "))
        {
          assertMatch(vertMatcher, line);
          double[] values = parseDoubles(line.substring(2), " ");
          illegalAssert(values.length >= 3, "Vertices must have x, y and z components");
          Vector3 vert = new Vector3(values[0], values[1], values[2]);
          coordSystem.apply(vert);
          verts.add(vert);
        }
        else if (line.startsWith("vt "))
        {
          assertMatch(uvwMatcher, line);
          double[] values = parseDoubles(line.substring(3), " ");
          illegalAssert(values.length >= 2, "Tex Coords must have u, and v components");
          uvs.add(new Vector3(values[0], 1.0D - values[1], 0.0D));
        }
        else if (line.startsWith("vn "))
        {
          assertMatch(normalMatcher, line);
          double[] values = parseDoubles(line.substring(3), " ");
          illegalAssert(values.length >= 3, "Normals must have x, y and z components");
          Vector3 norm = new Vector3(values[0], values[1], values[2]).normalize();
          coordSystem.applyN(norm);
          normals.add(norm);
        }
        else
        {
          if (line.startsWith("f "))
          {
            assertMatch(polyMatcher, line);
            String[] av = line.substring(2).split(" ");
            illegalAssert(av.length >= 3, "Polygons must have at least 3 vertices");
            int[][] polyVerts = new int[av.length][3];
            for (int i = 0; i < av.length; i++)
            {
              String[] as = av[i].split("/");
              for (int p = 0; p < as.length; p++) {
                if (as[p].length() > 0) {
                  polyVerts[i][p] = Integer.parseInt(as[p]);
                }
              }
            }
            if (vp == 3) {
              triangulate(polys, polyVerts);
            } else {
              quadulate(polys, polyVerts);
            }
          }
          if (line.startsWith("g "))
          {
            if (!polys.isEmpty())
            {
              modelMap.put(modelName, createModel(verts, uvs, normals, vertexMode, polys));
              polys.clear();
            }
            modelName = line.substring(2);
          }
        }
      }
    }
    if (!polys.isEmpty()) {
      modelMap.put(modelName, createModel(verts, uvs, normals, vertexMode, polys));
    }
    return modelMap;
  }
  
  public static void triangulate(List<int[]> polys, int[][] polyVerts)
  {
    for (int i = 2; i < polyVerts.length; i++)
    {
      polys.add(polyVerts[0]);
      polys.add(polyVerts[i]);
      polys.add(polyVerts[(i - 1)]);
    }
  }
  
  public static void quadulate(List<int[]> polys, int[][] polyVerts)
  {
    if (polyVerts.length == 4)
    {
      polys.add(polyVerts[0]);
      polys.add(polyVerts[3]);
      polys.add(polyVerts[2]);
      polys.add(polyVerts[1]);
    }
    else
    {
      for (int i = 2; i < polyVerts.length; i++)
      {
        polys.add(polyVerts[0]);
        polys.add(polyVerts[i]);
        polys.add(polyVerts[(i - 1)]);
        polys.add(polyVerts[(i - 1)]);
      }
    }
  }
  
  public static Map<String, CCModel> parseObjModels(ResourceLocation res)
  {
    return parseObjModels(res, 4, null);
  }
  
  public static Map<String, CCModel> parseObjModels(ResourceLocation res, Transformation coordSystem)
  {
    try
    {
      return parseObjModels(Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream(), 4, coordSystem);
    }
    catch (IOException e)
    {
      throw new RuntimeException("failed to load model: " + res, e);
    }
  }
  
  public static Map<String, CCModel> parseObjModels(ResourceLocation res, int vertexMode, Transformation coordSystem)
  {
    try
    {
      return parseObjModels(Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream(), vertexMode, coordSystem);
    }
    catch (Exception e)
    {
      throw new RuntimeException("failed to load model: " + res, e);
    }
  }
  
  public static CCModel createModel(List<Vector3> verts, List<Vector3> uvs, List<Vector3> normals, int vertexMode, List<int[]> polys)
  {
    int vp = vertexMode == 7 ? 4 : 3;
    if ((polys.size() < vp) || (polys.size() % vp != 0)) {
      throw new IllegalArgumentException("Invalid number of vertices for model: " + polys.size());
    }
    boolean hasNormals = ((int[])polys.get(0))[2] > 0;
    CCModel model = newModel(vertexMode, polys.size());
    if (hasNormals) {
      model.getOrAllocate(CCRenderState.normalAttrib);
    }
    for (int i = 0; i < polys.size(); i++)
    {
      int[] ai = (int[])polys.get(i);
      Vector3 vert = ((Vector3)verts.get(ai[0] - 1)).copy();
      Vector3 uv = ai[1] <= 0 ? new Vector3() : ((Vector3)uvs.get(ai[1] - 1)).copy();
      if (ai[2] > 0 != hasNormals) {
        throw new IllegalArgumentException("Normals are an all or nothing deal here.");
      }
      model.verts[i] = new Vertex5(vert, uv.x, uv.y);
      if (hasNormals) {
        model.normals()[i] = ((Vector3)normals.get(ai[2] - 1)).copy();
      }
    }
    return model;
  }
  
  private static <T> int addIndex(List<T> list, T elem)
  {
    int i = list.indexOf(elem) + 1;
    if (i == 0)
    {
      list.add(elem);
      i = list.size();
    }
    return i;
  }
  
  private static String clean(double d)
  {
    return d == (int)d ? Integer.toString((int)d) : Double.toString(d);
  }
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
public static void exportObj(Map<String, CCModel> models, PrintWriter p)
  {
    List<Vector3> verts = new ArrayList();
    List<UV> uvs = new ArrayList();
    List<Vector3> normals = new ArrayList();
    List<int[]> polys = new ArrayList();
    for (Map.Entry<String, CCModel> e : models.entrySet())
    {
      p.println("g " + (String)e.getKey());
      CCModel m = (CCModel)e.getValue();
      
      int vStart = verts.size();
      int uStart = uvs.size();
      int nStart = normals.size();
      boolean hasNormals = m.normals() != null;
      polys.clear();
      for (int i = 0; i < m.verts.length; i++)
      {
        int[] ia = new int[hasNormals ? 3 : 2];
        ia[0] = addIndex(verts, m.verts[i].vec);
        ia[1] = addIndex(uvs, m.verts[i].uv);
        if (hasNormals) {
          ia[2] = addIndex(normals, m.normals()[i]);
        }
        polys.add(ia);
      }
      if (vStart < verts.size())
      {
        p.println();
        for (int i = vStart; i < verts.size(); i++)
        {
          Vector3 v = (Vector3)verts.get(i);
          p.format("v %s %s %s\n", new Object[] { clean(v.x), clean(v.y), clean(v.z) });
        }
      }
      if (uStart < uvs.size())
      {
        p.println();
        for (int i = uStart; i < uvs.size(); i++)
        {
          UV uv = (UV)uvs.get(i);
          p.format("vt %s %s\n", new Object[] { clean(uv.u), clean(uv.v) });
        }
      }
      if (nStart < normals.size())
      {
        p.println();
        for (int i = nStart; i < normals.size(); i++)
        {
          Vector3 n = (Vector3)normals.get(i);
          p.format("vn %s %s %s\n", new Object[] { clean(n.x), clean(n.y), clean(n.z) });
        }
      }
      p.println();
      for (int i = 0; i < polys.size(); i++)
      {
        if (i % m.vp == 0) {
          p.format("f", new Object[0]);
        }
        int[] ia = (int[])polys.get(i);
        if (hasNormals) {
          p.format(" %d/%d/%d", new Object[] { Integer.valueOf(ia[0]), Integer.valueOf(ia[1]), Integer.valueOf(ia[2]) });
        } else {
          p.format(" %d/%d", new Object[] { Integer.valueOf(ia[0]), Integer.valueOf(ia[1]) });
        }
        if (i % m.vp == m.vp - 1) {
          p.println();
        }
      }
    }
  }
  
  public CCModel shrinkUVs(double d)
  {
    for (int k = 0; k < this.verts.length; k += this.vp)
    {
      UV uv = new UV();
      for (int i = 0; i < this.vp; i++) {
        uv.add(this.verts[(k + i)].uv);
      }
      uv.multiply(1.0D / this.vp);
      for (int i = 0; i < this.vp; i++)
      {
        Vertex5 vert = this.verts[(k + i)];
        vert.uv.u += (vert.uv.u < uv.u ? d : -d);
        vert.uv.v += (vert.uv.v < uv.v ? d : -d);
      }
    }
    return this;
  }
  
  public CCModel sidedCopy(int side1, int side2, Vector3 point)
  {
    return copy().apply(new TransformationList(new Transformation[] { (Transformation)p455w0rd.p455w0rdsthings.lib.vec.Rotation.sideRotations[side1].inverse(), p455w0rd.p455w0rdsthings.lib.vec.Rotation.sideRotations[side2] }).at(point));
  }
  
  public static void copy(CCModel src, int srcpos, CCModel dst, int destpos, int length)
  {
    for (int k = 0; k < length; k++) {
      dst.verts[(destpos + k)] = src.verts[(srcpos + k)].copy();
    }
    for (int i = 0; i < src.attributes.size(); i++) {
      if (src.attributes.get(i) != null) {
        CCRenderState.arrayCopy(src.attributes.get(i), srcpos, dst.getOrAllocate(CCRenderState.getAttribute(i)), destpos, length);
      }
    }
  }
  
  public static void generateSidedModels(CCModel[] models, int side, Vector3 point)
  {
    for (int s = 0; s < 6; s++) {
      if (s != side) {
        models[s] = models[side].sidedCopy(side, s, point);
      }
    }
  }
  
  public static void generateSidedModelsH(CCModel[] models, int side, Vector3 point)
  {
    for (int s = 2; s < 6; s++) {
      if (s != side) {
        models[s] = models[side].sidedCopy(side, s, point);
      }
    }
  }
  
  public CCModel backfacedCopy()
  {
    return generateBackface(this, 0, copy(), 0, this.verts.length);
  }
  
  public static CCModel generateBackface(CCModel src, int srcpos, CCModel dst, int destpos, int length)
  {
    int vp = src.vp;
    if ((srcpos % vp != 0) || (destpos % vp != 0) || (length % vp != 0)) {
      throw new IllegalArgumentException("Vertices do not align with polygons");
    }
    int[][] o = { { 0, 0 }, { 1, vp - 1 }, { 2, vp - 2 }, { 3, vp - 3 } };
    for (int i = 0; i < length; i++)
    {
      int b = i / vp * vp;
      int d = i % vp;
      int di = destpos + b + o[d][1];
      int si = srcpos + b + o[d][0];
      dst.verts[di] = src.verts[si].copy();
      for (int a = 0; a < src.attributes.size(); a++) {
        if (src.attributes.get(a) != null) {
          CCRenderState.arrayCopy(src.attributes.get(a), si, dst.getOrAllocate(CCRenderState.getAttribute(a)), di, 1);
        }
      }
      if ((dst.normals() != null) && (dst.normals()[di] != null)) {
        dst.normals()[di].negate();
      }
    }
    return dst;
  }
  
  public CCModel generateSidedParts(int side, Vector3 point)
  {
    if (this.verts.length % (6 * this.vp) != 0) {
      throw new IllegalArgumentException("Invalid number of vertices for sided part generation");
    }
    int length = this.verts.length / 6;
    for (int s = 0; s < 6; s++) {
      if (s != side) {
        generateSidedPart(side, s, point, length * side, length * s, length);
      }
    }
    return this;
  }
  
  public CCModel generateSidedPartsH(int side, Vector3 point)
  {
    if (this.verts.length % (4 * this.vp) != 0) {
      throw new IllegalArgumentException("Invalid number of vertices for sided part generation");
    }
    int length = this.verts.length / 4;
    for (int s = 2; s < 6; s++) {
      if (s != side) {
        generateSidedPart(side, s, point, length * (side - 2), length * (s - 2), length);
      }
    }
    return this;
  }
  
  public CCModel generateSidedPart(int side1, int side2, Vector3 point, int srcpos, int destpos, int length)
  {
    return apply(new TransformationList(new Transformation[] { (Transformation)p455w0rd.p455w0rdsthings.lib.vec.Rotation.sideRotations[side1].inverse(), p455w0rd.p455w0rdsthings.lib.vec.Rotation.sideRotations[side2] }).at(point), srcpos, destpos, length);
  }
  
  public CCModel apply(Transformation t, int srcpos, int destpos, int length)
  {
    for (int k = 0; k < length; k++)
    {
      this.verts[(destpos + k)] = this.verts[(srcpos + k)].copy();
      this.verts[(destpos + k)].vec.apply(t);
    }
    Vector3[] normals = normals();
    if (normals != null) {
      for (int k = 0; k < length; k++)
      {
        normals[(destpos + k)] = normals[(srcpos + k)].copy();
        t.applyN(normals[(destpos + k)]);
      }
    }
    return this;
  }
  
  public static CCModel combine(Collection<CCModel> models)
  {
    if (models.isEmpty()) {
      return null;
    }
    int numVerts = 0;
    int vertexMode = -1;
    for (CCModel model : models)
    {
      if (vertexMode == -1) {
        vertexMode = model.vertexMode;
      }
      if (vertexMode != model.vertexMode) {
        throw new IllegalArgumentException("Cannot combine models with different vertex modes");
      }
      numVerts += model.verts.length;
    }
    CCModel c_model = newModel(vertexMode, numVerts);
    int i = 0;
    for (CCModel model : models)
    {
      copy(model, 0, c_model, i, model.verts.length);
      i += model.verts.length;
    }
    return c_model;
  }
  
  public CCModel twoFacedCopy()
  {
    CCModel model = newModel(this.vertexMode, this.verts.length * 2);
    copy(this, 0, model, 0, this.verts.length);
    return generateBackface(model, 0, model, this.verts.length, this.verts.length);
  }
  
  public CCModel copy()
  {
    CCModel model = newModel(this.vertexMode, this.verts.length);
    copy(this, 0, model, 0, this.verts.length);
    return model;
  }
  
  public Vector3 collapse()
  {
    Vector3 v = new Vector3();
    for (Vertex5 vert : this.verts) {
      v.add(vert.vec);
    }
    v.multiply(1.0D / this.verts.length);
    return v;
  }
  
  public CCModel zOffset(Cuboid6 offsets)
  {
    for (int k = 0; k < this.verts.length; k++)
    {
      Vertex5 vert = this.verts[k];
      Vector3 normal = normals()[k];
      switch (findSide(normal))
      {
      case 0: 
        vert.vec.y += offsets.min.y;
        break;
      case 1: 
        vert.vec.y += offsets.max.y;
        break;
      case 2: 
        vert.vec.z += offsets.min.z;
        break;
      case 3: 
        vert.vec.z += offsets.max.z;
        break;
      case 4: 
        vert.vec.x += offsets.min.x;
        break;
      case 5: 
        vert.vec.x += offsets.max.x;
      }
    }
    return this;
  }
  
  public static int findSide(Vector3 normal)
  {
    if (normal.y <= -0.99D) {
      return 0;
    }
    if (normal.y >= 0.99D) {
      return 1;
    }
    if (normal.z <= -0.99D) {
      return 2;
    }
    if (normal.z >= 0.99D) {
      return 3;
    }
    if (normal.x <= -0.99D) {
      return 4;
    }
    if (normal.x >= 0.99D) {
      return 5;
    }
    return -1;
  }
  
  public Cuboid6 bounds()
  {
    Vector3 vec1 = this.verts[0].vec;
    Cuboid6 c = new Cuboid6(vec1.copy(), vec1.copy());
    for (int i = 1; i < this.verts.length; i++) {
      c.enclose(this.verts[i].vec);
    }
    return c;
  }
}


