package p455w0rd.p455w0rdsthings.lib.color;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.lib.config.ConfigTag.IConfigType;
import p455w0rd.p455w0rdsthings.lib.math.MathHelper;
import p455w0rd.p455w0rdsthings.lib.util.Copyable;

public abstract class Colour
  implements Copyable<Colour>
{
  public static IConfigType<Colour> configRGB = new IConfigType<Colour>()
  {
	  private final Pattern patternRGB = Pattern.compile("(\\d+),(\\d+),(\\d+)");

      public String configValue(Colour entry) {
          String s = Long.toString((long)entry.rgb() << 32 >>> 32, 16);
          while (s.length() < 6) {
              s = "0" + s;
          }
          return "0x" + s.toUpperCase();
      }

      @Override
      public Colour valueOf(String text) throws Exception {
          Matcher matcherRGB = this.patternRGB.matcher(text.replaceAll("\\s", ""));
          if (matcherRGB.matches()) {
              return new ColourRGBA(Integer.parseInt(matcherRGB.group(1)), Integer.parseInt(matcherRGB.group(2)), Integer.parseInt(matcherRGB.group(3)), 255);
          }
          int hex = (int)Long.parseLong(text.replace("0x", ""), 16);
          return new ColourRGBA(hex << 8 | 255);
      }
  };
  public byte r;
  public byte g;
  public byte b;
  public byte a;
  
  public Colour(int r, int g, int b, int a)
  {
    this.r = ((byte)r);
    this.g = ((byte)g);
    this.b = ((byte)b);
    this.a = ((byte)a);
  }
  
  public Colour(Colour colour)
  {
    this.r = colour.r;
    this.g = colour.g;
    this.b = colour.b;
    this.a = colour.a;
  }
  
  @SideOnly(Side.CLIENT)
  public void glColour()
  {
    GlStateManager.color((this.r & 0xFF) / 255.0F, (this.g & 0xFF) / 255.0F, (this.b & 0xFF) / 255.0F, (this.a & 0xFF) / 255.0F);
  }
  
  @SideOnly(Side.CLIENT)
  public void glColour(int a)
  {
    GlStateManager.color((this.r & 0xFF) / 255.0F, (this.g & 0xFF) / 255.0F, (this.b & 0xFF) / 255.0F, a / 255.0F);
  }
  
  public abstract int pack();
  
  public String toString()
  {
    return getClass().getSimpleName() + "[0x" + Integer.toHexString(pack()).toUpperCase() + "]";
  }
  
  public Colour add(Colour colour2)
  {
    this.a = ((byte)(this.a + colour2.a));
    this.r = ((byte)(this.r + colour2.r));
    this.g = ((byte)(this.g + colour2.g));
    this.b = ((byte)(this.b + colour2.b));
    return this;
  }
  
  public Colour sub(Colour colour2)
  {
    int ia = (this.a & 0xFF) - (colour2.a & 0xFF);
    int ir = (this.r & 0xFF) - (colour2.r & 0xFF);
    int ig = (this.g & 0xFF) - (colour2.g & 0xFF);
    int ib = (this.b & 0xFF) - (colour2.b & 0xFF);
    this.a = ((byte)(ia < 0 ? 0 : ia));
    this.r = ((byte)(ir < 0 ? 0 : ir));
    this.g = ((byte)(ig < 0 ? 0 : ig));
    this.b = ((byte)(ib < 0 ? 0 : ib));
    return this;
  }
  
  public Colour invert()
  {
    this.a = ((byte)(255 - (this.a & 0xFF)));
    this.r = ((byte)(255 - (this.r & 0xFF)));
    this.g = ((byte)(255 - (this.g & 0xFF)));
    this.b = ((byte)(255 - (this.b & 0xFF)));
    return this;
  }
  
  public Colour multiply(Colour colour2)
  {
    this.a = ((byte)(int)((this.a & 0xFF) * ((colour2.a & 0xFF) / 255.0D)));
    this.r = ((byte)(int)((this.r & 0xFF) * ((colour2.r & 0xFF) / 255.0D)));
    this.g = ((byte)(int)((this.g & 0xFF) * ((colour2.g & 0xFF) / 255.0D)));
    this.b = ((byte)(int)((this.b & 0xFF) * ((colour2.b & 0xFF) / 255.0D)));
    return this;
  }
  
  public Colour scale(double d)
  {
    this.a = ((byte)(int)((this.a & 0xFF) * d));
    this.r = ((byte)(int)((this.r & 0xFF) * d));
    this.g = ((byte)(int)((this.g & 0xFF) * d));
    this.b = ((byte)(int)((this.b & 0xFF) * d));
    return this;
  }
  
  public Colour interpolate(Colour colour2, double d)
  {
    return add(colour2.copy().sub(this).scale(d));
  }
  
  public Colour multiplyC(double d)
  {
    this.r = ((byte)(int)MathHelper.clip((this.r & 0xFF) * d, 0.0D, 255.0D));
    this.g = ((byte)(int)MathHelper.clip((this.g & 0xFF) * d, 0.0D, 255.0D));
    this.b = ((byte)(int)MathHelper.clip((this.b & 0xFF) * d, 0.0D, 255.0D));
    
    return this;
  }
  
  public abstract Colour copy();
  
  public int rgb()
  {
    return (this.r & 0xFF) << 16 | (this.g & 0xFF) << 8 | this.b & 0xFF;
  }
  
  public int argb()
  {
    return (this.a & 0xFF) << 24 | (this.r & 0xFF) << 16 | (this.g & 0xFF) << 8 | this.b & 0xFF;
  }
  
  public int rgba()
  {
    return (this.r & 0xFF) << 24 | (this.g & 0xFF) << 16 | (this.b & 0xFF) << 8 | this.a & 0xFF;
  }
  
  public Colour set(Colour colour)
  {
    this.r = colour.r;
    this.g = colour.g;
    this.b = colour.b;
    this.a = colour.a;
    return this;
  }
  
  public boolean equals(Colour colour)
  {
    return (colour != null) && (rgba() == colour.rgba());
  }
}


