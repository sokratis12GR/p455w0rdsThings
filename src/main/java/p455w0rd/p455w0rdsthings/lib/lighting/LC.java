package p455w0rd.p455w0rdsthings.lib.lighting;

import p455w0rd.p455w0rdsthings.lib.render.CCModel;
import p455w0rd.p455w0rdsthings.lib.util.Copyable;
import p455w0rd.p455w0rdsthings.lib.vec.Vector3;

public class LC
  implements Copyable<LC>
{
  public int side;
  public float fa;
  public float fb;
  public float fc;
  public float fd;
  
  public LC()
  {
    this(0, 0.0F, 0.0F, 0.0F, 0.0F);
  }
  
  public LC(int s, float a, float b, float c, float d)
  {
    this.side = s;
    this.fa = a;
    this.fb = b;
    this.fc = c;
    this.fd = d;
  }
  
  public LC set(int s, float a, float b, float c, float d)
  {
    this.side = s;
    this.fa = a;
    this.fb = b;
    this.fc = c;
    this.fd = d;
    return this;
  }
  
  public LC set(LC lc)
  {
    return set(lc.side, lc.fa, lc.fb, lc.fc, lc.fd);
  }
  
  public LC compute(Vector3 vec, Vector3 normal)
  {
    int side = CCModel.findSide(normal);
    if (side < 0) {
      return set(12, 1.0F, 0.0F, 0.0F, 0.0F);
    }
    return compute(vec, side);
  }
  
  public LC compute(Vector3 vec, int side)
  {
    boolean offset = false;
    switch (side)
    {
    case 0: 
      offset = vec.y <= 0.0D;
      break;
    case 1: 
      offset = vec.y >= 1.0D;
      break;
    case 2: 
      offset = vec.z <= 0.0D;
      break;
    case 3: 
      offset = vec.z >= 1.0D;
      break;
    case 4: 
      offset = vec.x <= 0.0D;
      break;
    case 5: 
      offset = vec.x >= 1.0D;
    }
    if (!offset) {
      side += 6;
    }
    return computeO(vec, side);
  }
  
  public LC computeO(Vector3 vec, int side)
  {
    Vector3 v1 = p455w0rd.p455w0rdsthings.lib.vec.Rotation.axes[(((side & 0xE) + 3) % 6)];
    Vector3 v2 = p455w0rd.p455w0rdsthings.lib.vec.Rotation.axes[(((side & 0xE) + 5) % 6)];
    float d1 = (float)vec.scalarProject(v1);
    float d2 = 1.0F - d1;
    float d3 = (float)vec.scalarProject(v2);
    float d4 = 1.0F - d3;
    return set(side, d2 * d4, d2 * d3, d1 * d4, d1 * d3);
  }
  
  public LC copy()
  {
    return new LC(this.side, this.fa, this.fb, this.fc, this.fd);
  }
}


