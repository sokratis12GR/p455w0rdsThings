package p455w0rd.p455w0rdsthings.lib.lighting;

import p455w0rd.p455w0rdsthings.lib.render.CCRenderState;
import p455w0rd.p455w0rdsthings.lib.vec.Vector3;

public class LightModel
  implements CCRenderState.IVertexOperation
{
  public static final int operationIndex = CCRenderState.registerOperation();
  
  public static class Light
  {
    public Vector3 ambient = new Vector3();
    public Vector3 diffuse = new Vector3();
    public Vector3 position;
    
    public Light(Vector3 pos)
    {
      this.position = pos.copy().normalize();
    }
    
    public Light setDiffuse(Vector3 vec)
    {
      this.diffuse.set(vec);
      return this;
    }
    
    public Light setAmbient(Vector3 vec)
    {
      this.ambient.set(vec);
      return this;
    }
  }
  
  public static LightModel standardLightModel = new LightModel().setAmbient(new Vector3(0.4D, 0.4D, 0.4D)).addLight(new Light(new Vector3(0.2D, 1.0D, -0.7D)).setDiffuse(new Vector3(0.6D, 0.6D, 0.6D))).addLight(new Light(new Vector3(-0.2D, 1.0D, 0.7D)).setDiffuse(new Vector3(0.6D, 0.6D, 0.6D)));
  private Vector3 ambient = new Vector3();
  private Light[] lights = new Light[8];
  private int lightCount;
  
  public LightModel addLight(Light light)
  {
    this.lights[(this.lightCount++)] = light;
    return this;
  }
  
  public LightModel setAmbient(Vector3 vec)
  {
    this.ambient.set(vec);
    return this;
  }
  
  public int apply(int colour, Vector3 normal)
  {
    Vector3 n_colour = this.ambient.copy();
    for (int l = 0; l < this.lightCount; l++)
    {
      Light light = this.lights[l];
      double n_l = light.position.dotProduct(normal);
      double f = n_l > 0.0D ? 1.0D : 0.0D;
      n_colour.x += light.ambient.x + f * light.diffuse.x * n_l;
      n_colour.y += light.ambient.y + f * light.diffuse.y * n_l;
      n_colour.z += light.ambient.z + f * light.diffuse.z * n_l;
    }
    if (n_colour.x > 1.0D) {
      n_colour.x = 1.0D;
    }
    if (n_colour.y > 1.0D) {
      n_colour.y = 1.0D;
    }
    if (n_colour.z > 1.0D) {
      n_colour.z = 1.0D;
    }
    n_colour.multiply((colour >>> 24) / 255.0D, (colour >> 16 & 0xFF) / 255.0D, (colour >> 8 & 0xFF) / 255.0D);
    return (int)(n_colour.x * 255.0D) << 24 | (int)(n_colour.y * 255.0D) << 16 | (int)(n_colour.z * 255.0D) << 8 | colour & 0xFF;
  }
  
  public boolean load()
  {
    if (!CCRenderState.computeLighting) {
      return false;
    }
    CCRenderState.pipeline.addDependency(CCRenderState.normalAttrib);
    CCRenderState.pipeline.addDependency(CCRenderState.colourAttrib);
    return true;
  }
  
  public void operate()
  {
    CCRenderState.colour = apply(CCRenderState.colour, CCRenderState.normal);
  }
  
  public int operationID()
  {
    return operationIndex;
  }
  
  public PlanarLightModel reducePlanar()
  {
    int[] colours = new int[6];
    for (int i = 0; i < 6; i++) {
      colours[i] = apply(-1, p455w0rd.p455w0rdsthings.lib.vec.Rotation.axes[i]);
    }
    return new PlanarLightModel(colours);
  }
}


