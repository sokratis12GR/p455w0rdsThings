package p455w0rd.p455w0rdsthings.client.render;

import javax.annotation.Nonnull;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import p455w0rd.p455w0rdsthings.util.ItemUtils;
import p455w0rd.p455w0rdsthings.util.ReadableNumberConverter;

public class PRenderItem extends RenderItem {
	
	private boolean useLg = false;
	ItemStack itemStack;

	public PRenderItem(TextureManager p_i46552_1_, ModelManager p_i46552_2_, ItemColors p_i46552_3_, boolean useLg) {
		super(p_i46552_1_, p_i46552_2_, p_i46552_3_);
		this.useLg = useLg;
	}

	public void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack is, int par4, int par5, String par6Str) {
		if (is != null) {
			float scaleFactor = this.useLg ? 1.0F : 0.5F;
			float inverseScaleFactor = 1.0F / scaleFactor;
			int offset = this.useLg ? 0 : -1;
			String stackSize = "";

			boolean unicodeFlag = fontRenderer.getUnicodeFlag();
			fontRenderer.setUnicodeFlag(false);
			if (is.getItem().showDurabilityBar(is)) {
				double health = is.getItem().getDurabilityForDisplay(is);
				int j = (int) Math.round(13.0D - health * 13.0D);
				int i = (int) Math.round(255.0D - health * 255.0D);

				GlStateManager.disableDepth();
				GlStateManager.disableTexture2D();

				Tessellator tessellator = Tessellator.getInstance();
				VertexBuffer vertexbuffer = tessellator.getBuffer();
				draw(vertexbuffer, par4 + 2, par5 + 13, 13, 2, 0, 0, 0, 255);
				draw(vertexbuffer, par4 + 2, par5 + 13, 12, 1, (255 - i) / 4, 64, 0, 255);
				draw(vertexbuffer, par4 + 2, par5 + 13, j, 1, 255 - i, i, 0, 255);

				GlStateManager.enableTexture2D();

				GlStateManager.enableDepth();
			}
			long amount = 0L;
			if (ItemUtils.isDankNullStack(is)) {
				amount = ItemUtils.getDankNullStackSize(is);
				if (amount != 0L) {
					scaleFactor = 0.5F;
					inverseScaleFactor = 1.0F / scaleFactor;
					offset = -1;
					stackSize = getToBeRenderedStackSize(amount);
				}
			}
			else {
				amount = is.stackSize;
				if (amount != 0L) {
					scaleFactor = 1.0F;
					inverseScaleFactor = 1.0F / scaleFactor;
					offset = 0;
					stackSize = getToBeRenderedStackSize(amount);
				}
			}
			GlStateManager.disableLighting();
			GlStateManager.disableAlpha();
			GlStateManager.disableBlend();
			GlStateManager.disableDepth();
			GlStateManager.pushMatrix();
			GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);

			int X = (int) ((par4 + offset + 16.0F - fontRenderer.getStringWidth(stackSize) * scaleFactor)
					* inverseScaleFactor);
			int Y = (int) ((par5 + offset + 16.0F - 7.0F * scaleFactor) * inverseScaleFactor);
			if (amount > 1L) {
				fontRenderer.drawStringWithShadow(stackSize, X, Y, 16777215);
			}
			GlStateManager.popMatrix();
			GlStateManager.enableDepth();
			GlStateManager.enableBlend();
			GlStateManager.enableAlpha();
			GlStateManager.enableLighting();

			fontRenderer.setUnicodeFlag(unicodeFlag);
		}
	}

	private void draw(VertexBuffer renderer, int x, int y, int width, int height, int red, int green, int blue,
			int alpha) {
		renderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
		renderer.pos(x + 0, y + 0, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + 0, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + width, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
		renderer.pos(x + width, y + 0, 0.0D).color(red, green, blue, alpha).endVertex();
		Tessellator.getInstance().draw();
	}

	private String getToBeRenderedStackSize(long originalSize) {
		if (this.useLg) {
			return ReadableNumberConverter.INSTANCE.toSlimReadableForm(originalSize);
		}
		return ReadableNumberConverter.INSTANCE.toWideReadableForm(originalSize);
	}

	public ItemStack getStack() {
		return this.itemStack;
	}

	public void setStack(@Nonnull ItemStack stack, boolean regularSlotStack) {
		this.itemStack = stack;
		this.useLg = regularSlotStack;
	}
}
