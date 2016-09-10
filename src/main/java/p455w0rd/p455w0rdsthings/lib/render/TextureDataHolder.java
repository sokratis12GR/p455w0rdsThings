package p455w0rd.p455w0rdsthings.lib.render;

import java.awt.image.BufferedImage;

public class TextureDataHolder
{
  public int width;
  public int height;
  public int[] data;
  
  public TextureDataHolder(int width, int height)
  {
    this.width = width;
    this.height = height;
    this.data = new int[width * height];
  }
  
  public TextureDataHolder(int[] data, int width)
  {
    this.data = data;
    this.width = width;
    this.height = (data.length / width);
  }
  
  public TextureDataHolder(BufferedImage img)
  {
    this(img.getWidth(), img.getHeight());
    img.getRGB(0, 0, this.width, this.height, this.data, 0, this.width);
  }
  
  public TextureDataHolder copyData()
  {
    int[] copy = new int[this.data.length];
    System.arraycopy(this.data, 0, copy, 0, this.data.length);
    this.data = copy;
    return this;
  }
}


