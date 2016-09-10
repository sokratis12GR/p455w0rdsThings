package p455w0rd.p455w0rdsthings.lib.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.PngSizeInfo;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
@SideOnly(Side.CLIENT)
public class TextureSpecial extends TextureAtlasSprite implements TextureUtils.IIconRegister {
	
	private int spriteIndex;
	private SpriteSheetManager.SpriteSheet spriteSheet;
	private TextureFX textureFX;
	private int mipmapLevels;
	private int rawWidth;
	private int rawHeight;
	private int blankSize = -1;
	private ArrayList<TextureDataHolder> baseTextures;
	public int atlasIndex;

	protected TextureSpecial(String par1) {
		super(par1);
	}

	public TextureSpecial addTexture(TextureDataHolder t) {
		if (this.baseTextures == null) {
			this.baseTextures = new ArrayList<TextureDataHolder>();
		}
		this.baseTextures.add(t);
		return this;
	}

	public TextureSpecial baseFromSheet(SpriteSheetManager.SpriteSheet spriteSheet, int spriteIndex) {
		this.spriteSheet = spriteSheet;
		this.spriteIndex = spriteIndex;
		return this;
	}

	public TextureSpecial addTextureFX(TextureFX fx) {
		this.textureFX = fx;
		return this;
	}

	public void initSprite(int sheetWidth, int sheetHeight, int originX, int originY, boolean rotated) {
		super.initSprite(sheetWidth, sheetHeight, originX, originY, rotated);
		if (this.textureFX != null) {
			this.textureFX.onTextureDimensionsUpdate(this.rawWidth, this.rawHeight);
		}
	}

	public void updateAnimation() {
		if (this.textureFX != null) {
			this.textureFX.update();
			if (this.textureFX.changed()) {
				int[][] mipmaps = new int[this.mipmapLevels + 1][];
				mipmaps[0] = this.textureFX.imageData;

				mipmaps = TextureUtil.generateMipmapData(this.mipmapLevels, this.width, mipmaps);
				TextureUtil.uploadTextureMipmap(mipmaps, this.width, this.height, this.originX, this.originY, false,
						false);
			}
		}
	}

	public void loadSprite(PngSizeInfo sizeInfo, boolean animationMeta) {
		this.rawWidth = sizeInfo.pngWidth;
		this.rawHeight = sizeInfo.pngHeight;
		try {
			super.loadSprite(sizeInfo, false);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void generateMipmaps(int level) {
		super.generateMipmaps(level);
		this.mipmapLevels = level;
	}

	public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
		return true;
	}

	public void addFrame(int[] data, int width, int height) {
		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		BufferedImage[] images = new BufferedImage[settings.mipmapLevels + 1];
		images[0] = new BufferedImage(width, height, 2);
		images[0].setRGB(0, 0, width, height, data, 0, width);
	}

	public boolean load(IResourceManager manager, ResourceLocation location) {
		if (this.baseTextures != null) {
			for (TextureDataHolder tex : this.baseTextures) {
				addFrame(tex.data, tex.width, tex.height);
			}
		}
		else if (this.spriteSheet != null) {
			TextureDataHolder tex = this.spriteSheet.createSprite(this.spriteIndex);
			addFrame(tex.data, tex.width, tex.height);
		}
		else if (this.blankSize > 0) {
			addFrame(new int[this.blankSize * this.blankSize], this.blankSize, this.blankSize);
		}
		if (this.framesTextureData.isEmpty()) {
			throw new RuntimeException("No base frame for texture: " + getIconName());
		}
		return false;
	}

	public boolean hasAnimationMetadata() {
		return (this.textureFX != null) || (super.hasAnimationMetadata());
	}

	public int getFrameCount() {
		if (this.textureFX != null) {
			return 1;
		}
		return super.getFrameCount();
	}

	public TextureSpecial blank(int size) {
		this.blankSize = size;
		return this;
	}

	public TextureSpecial selfRegister() {
		TextureUtils.addIconRegister(this);
		return this;
	}

	public void registerIcons(TextureMap textureMap) {
		textureMap.setTextureEntry(getIconName(), this);
	}
}
