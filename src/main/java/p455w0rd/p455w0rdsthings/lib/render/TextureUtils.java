package p455w0rd.p455w0rdsthings.lib.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import p455w0rd.p455w0rdsthings.lib.color.Colour;
import p455w0rd.p455w0rdsthings.lib.color.ColourARGB;

@SuppressWarnings("deprecation")
public class TextureUtils {
	
	static {
		MinecraftForge.EVENT_BUS.register(new TextureUtils());
	}

	private static ArrayList<IIconRegister> iconRegisters = new ArrayList<IIconRegister>();

	public static void addIconRegister(IIconRegister registrar) {
		iconRegisters.add(registrar);
	}

	@SubscribeEvent
	public void textureLoad(TextureStitchEvent.Pre event) {
		for (IIconRegister reg : iconRegisters) {
			reg.registerIcons(event.getMap());
		}
	}

	public static int[] loadTextureData(ResourceLocation resource) {
		return loadTexture(resource).data;
	}

	public static Colour[] loadTextureColours(ResourceLocation resource) {
		int[] idata = loadTextureData(resource);
		Colour[] data = new Colour[idata.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = new ColourARGB(idata[i]);
		}
		return data;
	}

	public static InputStream getTextureResource(ResourceLocation textureFile) throws IOException {
		return Minecraft.getMinecraft().getResourceManager().getResource(textureFile).getInputStream();
	}

	public static BufferedImage loadBufferedImage(ResourceLocation textureFile) {
		try {
			return loadBufferedImage(getTextureResource(textureFile));
		}
		catch (Exception e) {
			System.err.println("Failed to load texture file: " + textureFile);
			e.printStackTrace();
		}
		return null;
	}

	public static BufferedImage loadBufferedImage(InputStream in) throws IOException {
		BufferedImage img = ImageIO.read(in);
		in.close();
		return img;
	}

	public static void copySubImg(int[] fromTex, int fromWidth, int fromX, int fromY, int width, int height,
			int[] toTex, int toWidth, int toX, int toY) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int fp = (y + fromY) * fromWidth + x + fromX;
				int tp = (y + toX) * toWidth + x + toX;

				toTex[tp] = fromTex[fp];
			}
		}
	}

	public static TextureAtlasSprite getBlankIcon(int size, TextureMap textureMap) {
		String s = "blank_" + size;
		TextureAtlasSprite icon = textureMap.getTextureExtry(s);
		if (icon == null) {
			textureMap.setTextureEntry(s, icon = new TextureSpecial(s).blank(size));
		}
		return icon;
	}

	public static TextureSpecial getTextureSpecial(TextureMap textureMap, String name) {
		if (textureMap.getTextureExtry(name) != null) {
			throw new IllegalStateException("Texture: " + name + " is already registered");
		}
		TextureSpecial icon = new TextureSpecial(name);
		textureMap.setTextureEntry(name, icon);
		return icon;
	}

	public static void prepareTexture(int target, int texture, int min_mag_filter, int wrap) {
		GlStateManager.glTexParameteri(target, 10241, min_mag_filter);
		GlStateManager.glTexParameteri(target, 10240, min_mag_filter);
		if (target == 3553) {
			GlStateManager.bindTexture(target);
		}
		else {
			GL11.glBindTexture(target, texture);
		}
		switch (target) {
		case 32879:
			GlStateManager.glTexParameteri(target, 32882, wrap);
		case 3553:
			GlStateManager.glTexParameteri(target, 10243, wrap);
		case 3552:
			GlStateManager.glTexParameteri(target, 10242, wrap);
		}
	}

	public static TextureDataHolder loadTexture(ResourceLocation resource) {
		BufferedImage img = loadBufferedImage(resource);
		if (img == null) {
			throw new RuntimeException("Texture not found: " + resource);
		}
		return new TextureDataHolder(img);
	}

	public static boolean refreshTexture(TextureMap map, String name) {
		if (map.getTextureExtry(name) == null) {
			map.setTextureEntry(name, new PlaceholderTexture(name));
			return true;
		}
		return false;
	}

	public static TextureManager getTextureManager() {
		return Minecraft.getMinecraft().renderEngine;
	}

	public static TextureAtlasSprite getTexture(String location) {
		return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location);
	}

	public static TextureAtlasSprite getTexture(ResourceLocation location) {
		return getTexture(location.toString());
	}

	public static TextureAtlasSprite getBlockTexture(String string) {
		return getBlockTexture(new ResourceLocation(string));
	}

	public static TextureAtlasSprite getBlockTexture(ResourceLocation location) {
		return getTexture(new ResourceLocation(location.getResourceDomain(), "blocks/" + location.getResourcePath()));
	}

	public static TextureAtlasSprite getItemTexture(String string) {
		return getItemTexture(new ResourceLocation(string));
	}

	public static TextureAtlasSprite getItemTexture(ResourceLocation location) {
		return getTexture(new ResourceLocation(location.getResourceDomain(), "items/" + location.getResourcePath()));
	}

	public static void changeTexture(String texture) {
		changeTexture(new ResourceLocation(texture));
	}

	public static void changeTexture(ResourceLocation texture) {
		getTextureManager().bindTexture(texture);
	}

	public static void disableMipmap(String texture) {
		disableMipmap(new ResourceLocation(texture));
	}

	public static void disableMipmap(ResourceLocation texture) {
		getTextureManager().getTexture(texture).setBlurMipmap(false, false);
	}

	public static void restoreLastMipmap(String texture) {
		restoreLastMipmap(new ResourceLocation(texture));
	}

	public static void restoreLastMipmap(ResourceLocation location) {
		getTextureManager().getTexture(location).restoreLastBlurMipmap();
	}

	public static void bindBlockTexture() {
		changeTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	public static void dissableBlockMipmap() {
		disableMipmap(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	public static void restoreBlockMipmap() {
		restoreLastMipmap(TextureMap.LOCATION_BLOCKS_TEXTURE);
	}

	public static abstract interface IIconRegister {
		public abstract void registerIcons(TextureMap paramTextureMap);
	}
}
