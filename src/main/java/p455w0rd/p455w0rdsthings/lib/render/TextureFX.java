package p455w0rd.p455w0rdsthings.lib.render;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TextureFX {
	
	public int[] imageData;
	public int tileSizeBase = 16;
	public int tileSizeSquare = 256;
	public int tileSizeMask = 15;
	public int tileSizeSquareMask = 255;
	public boolean anaglyphEnabled;
	public TextureSpecial texture;

	public TextureFX(int spriteIndex, SpriteSheetManager.SpriteSheet sheet) {
		this.texture = sheet.bindTextureFX(spriteIndex, this);
	}

	public TextureFX(int size, String name) {
		this.texture = new TextureSpecial(name).blank(size).selfRegister().addTextureFX(this);
	}

	public TextureFX setAtlas(int index) {
		this.texture.atlasIndex = index;
		return this;
	}

	public void setup() {
		this.imageData = new int[this.tileSizeSquare];
	}

	public void onTextureDimensionsUpdate(int width, int height) {
		if (width != height) {
			throw new IllegalArgumentException("Non-Square textureFX not supported (" + width + ":" + height + ")");
		}
		this.tileSizeBase = width;
		this.tileSizeSquare = (this.tileSizeBase * this.tileSizeBase);
		this.tileSizeMask = (this.tileSizeBase - 1);
		this.tileSizeSquareMask = (this.tileSizeSquare - 1);
		setup();
	}

	public void update() {
		this.anaglyphEnabled = Minecraft.getMinecraft().gameSettings.anaglyph;
		onTick();
	}

	public void onTick() {
	}

	public boolean changed() {
		return true;
	}
}
