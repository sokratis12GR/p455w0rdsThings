package p455w0rd.p455w0rdsthings.lib.render;

import java.util.ArrayList;
import java.util.HashMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class SpriteSheetManager {
	
	@SideOnly(Side.CLIENT)
	public static class SpriteSheet implements TextureUtils.IIconRegister {
		private int tilesX;
		private int tilesY;
		private ArrayList<Integer> newSprites = new ArrayList<Integer>();
		private TextureSpecial[] sprites;
		private ResourceLocation resource;
		private TextureDataHolder texture;
		private int spriteWidth;
		private int spriteHeight;
		public int atlasIndex;

		private SpriteSheet(int tilesX, int tilesY, ResourceLocation textureFile) {
			this.tilesX = tilesX;
			this.tilesY = tilesY;
			this.resource = textureFile;
			this.sprites = new TextureSpecial[tilesX * tilesY];
		}

		public void requestIndicies(int... indicies) {
			for (int i : indicies) {
				setupSprite(i);
			}
		}

		public void registerIcons(TextureMap textureMap) {
			if (TextureUtils.refreshTexture(textureMap, this.resource.getResourcePath())) {
				reloadTexture();
				for (TextureSpecial sprite : this.sprites) {
					if (sprite != null) {
						textureMap.setTextureEntry(sprite.getIconName(), sprite);
					}
				}
			}
			else {
				for (int i : newSprites) {
					textureMap.setTextureEntry(sprites[i].getIconName(), sprites[i]);
				}
			}
			this.newSprites.clear();
		}

		public TextureSpecial setupSprite(int i) {
			if (this.sprites[i] == null) {
				String name = this.resource + "_" + i;
				this.sprites[i] = new TextureSpecial(name).baseFromSheet(this, i);
				this.newSprites.add(Integer.valueOf(i));
			}
			return this.sprites[i];
		}

		private void reloadTexture() {
			this.texture = TextureUtils.loadTexture(this.resource);
			this.spriteWidth = (this.texture.width / this.tilesX);
			this.spriteHeight = (this.texture.height / this.tilesY);
		}

		public TextureAtlasSprite getSprite(int index) {
			TextureAtlasSprite i = this.sprites[index];
			if (i == null) {
				throw new IllegalArgumentException(
						"Sprite at index: " + index + " from texture file " + this.resource + " was not preloaded.");
			}
			return i;
		}

		public TextureDataHolder createSprite(int spriteIndex) {
			int sx = spriteIndex % this.tilesX;
			int sy = spriteIndex / this.tilesX;
			TextureDataHolder sprite = new TextureDataHolder(this.spriteWidth, this.spriteHeight);
			TextureUtils.copySubImg(this.texture.data, this.texture.width, sx * this.spriteWidth,
					sy * this.spriteHeight, this.spriteWidth, this.spriteHeight, sprite.data, this.spriteWidth, 0, 0);
			return sprite;
		}

		public int spriteWidth() {
			return this.spriteWidth;
		}

		public int spriteHeight() {
			return this.spriteHeight;
		}

		public TextureSpecial bindTextureFX(int i, TextureFX textureFX) {
			return setupSprite(i).addTextureFX(textureFX);
		}

		public SpriteSheet selfRegister(int atlas) {
			TextureUtils.addIconRegister(this);
			return this;
		}
	}

	private static HashMap<String, SpriteSheet> spriteSheets = new HashMap<String, SpriteSheet>();

	public static SpriteSheet getSheet(ResourceLocation resource) {
		return getSheet(16, 16, resource);
	}

	public static SpriteSheet getSheet(int tilesX, int tilesY, ResourceLocation resource) {
		SpriteSheet sheet = (SpriteSheet) spriteSheets.get(resource.toString());
		if (sheet == null) {
			spriteSheets.put(resource.toString(), sheet = new SpriteSheet(tilesX, tilesY, resource));
		}
		return sheet;
	}
}
