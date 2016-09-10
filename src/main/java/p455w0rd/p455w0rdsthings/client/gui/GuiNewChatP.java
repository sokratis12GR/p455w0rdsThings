package p455w0rd.p455w0rdsthings.client.gui;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiNewChatP extends Gui {
	
	private static final Splitter NEWLINE_SPLITTER = Splitter.on('\n');
	private static final Joiner NEWLINE_STRING_JOINER = Joiner.on("\\n");
	private static final Logger LOGGER = LogManager.getLogger();
	private final Minecraft mc;
	private final List<String> sentMessages = Lists.newArrayList();
	private final List<ChatLineP> chatLines = Lists.newArrayList();
	private final List<ChatLineP> drawnChatLines = Lists.newArrayList();
	private int scrollPos;
	private boolean isScrolled;
	private final AbstractClientPlayer player;

	public GuiNewChatP(Minecraft mcIn) {
		this.mc = mcIn;
		this.player = this.mc.thePlayer;
	}

	public void drawChat(int updateCounter) {
		if (this.mc.gameSettings.chatVisibility != EnumChatVisibility.HIDDEN) {
			int i = getLineCount();
			boolean flag = false;
			int j = 0;
			int k = this.drawnChatLines.size();
			float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
			if (k > 0) {
				if (getChatOpen()) {
					flag = true;
				}
				float f1 = getChatScale();
				int l = MathHelper.ceiling_float_int(getChatWidth() / f1);
				GlStateManager.pushMatrix();
				GlStateManager.translate(2.0F, 8.0F, 0.0F);
				GlStateManager.scale(f1, f1, 1.0F);
				for (int i1 = 0; (i1 + this.scrollPos < this.drawnChatLines.size()) && (i1 < i); i1++) {
					ChatLineP chatline = (ChatLineP) this.drawnChatLines.get(i1 + this.scrollPos);
					if (chatline != null) {
						int j1 = updateCounter - chatline.getUpdatedCounter();
						if ((j1 < 200) || (flag)) {
							double d0 = j1 / 200.0D;
							d0 = 1.0D - d0;
							d0 *= 10.0D;
							d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
							d0 *= d0;
							int l1 = (int) (255.0D * d0);
							if (flag) {
								l1 = 255;
							}
							l1 = (int) (l1 * f);
							j++;
							if (l1 > 3) {
								int i2 = 0;
								int j2 = -i1 * 9;
								drawRect(i2 - 2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
								this.mc.getTextureManager()
										.bindTexture(DefaultPlayerSkin.getDefaultSkin(this.player.getUniqueID()));
								String s = chatline.getChatComponent().getFormattedText();
								GlStateManager.enableBlend();
								drawTexturedModalRect(i2, j2 - 8, 0, 0, 16, 16);
								this.mc.fontRendererObj.drawStringWithShadow(s, i2, j2 - 8, 16777215 + (l1 << 24));
								GlStateManager.disableAlpha();
								GlStateManager.disableBlend();
							}
						}
					}
				}
				if (flag) {
					int k2 = this.mc.fontRendererObj.FONT_HEIGHT;
					GlStateManager.translate(-3.0F, 0.0F, 0.0F);
					int l2 = k * k2 + k;
					int i3 = j * k2 + j;
					int j3 = this.scrollPos * i3 / k;
					int k1 = i3 * i3 / l2;
					if (l2 != i3) {
						int k3 = j3 > 0 ? 170 : 96;
						int l3 = this.isScrolled ? 13382451 : 3355562;
						drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
						drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
					}
				}
				GlStateManager.popMatrix();
			}
		}
	}

	public void clearChatMessages() {
		this.drawnChatLines.clear();
		this.chatLines.clear();
		this.sentMessages.clear();
	}

	public void printChatMessage(ITextComponent chatComponent) {
		printChatMessageWithOptionalDeletion(chatComponent, 0);
	}

	public void printChatMessageWithOptionalDeletion(ITextComponent chatComponent, int chatLineId) {
		setChatLine(chatComponent, chatLineId, this.mc.ingameGUI.getUpdateCounter(), false);
		LOGGER.info("[CHAT] " + NEWLINE_STRING_JOINER.join(NEWLINE_SPLITTER.split(chatComponent.getUnformattedText())));
	}

	private void setChatLine(ITextComponent chatComponent, int chatLineId, int updateCounter, boolean displayOnly) {
		if (chatLineId != 0) {
			deleteChatLine(chatLineId);
		}
		int i = MathHelper.floor_float(getChatWidth() / getChatScale());
		List<ITextComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRendererObj, false,
				false);
		boolean flag = getChatOpen();
		for (ITextComponent itextcomponent : list) {
			if ((flag) && (this.scrollPos > 0)) {
				this.isScrolled = true;
				scroll(1);
			}
			this.drawnChatLines.add(0, new ChatLineP(updateCounter, itextcomponent, chatLineId));
		}
		while (this.drawnChatLines.size() > 100) {
			this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
		}
		if (!displayOnly) {
			this.chatLines.add(0, new ChatLineP(updateCounter, chatComponent, chatLineId));
			while (this.chatLines.size() > 100) {
				this.chatLines.remove(this.chatLines.size() - 1);
			}
		}
	}

	public void refreshChat() {
		this.drawnChatLines.clear();
		resetScroll();
		for (int i = this.chatLines.size() - 1; i >= 0; i--) {
			ChatLineP chatline = (ChatLineP) this.chatLines.get(i);
			setChatLine(chatline.getChatComponent(), chatline.getChatLineID(), chatline.getUpdatedCounter(), true);
		}
	}

	public List<String> getSentMessages() {
		return this.sentMessages;
	}

	public void addToSentMessages(String message) {
		if ((this.sentMessages.isEmpty())
				|| (!((String) this.sentMessages.get(this.sentMessages.size() - 1)).equals(message))) {
			this.sentMessages.add(message);
		}
	}

	public void resetScroll() {
		this.scrollPos = 0;
		this.isScrolled = false;
	}

	public void scroll(int amount) {
		this.scrollPos += amount;
		int i = this.drawnChatLines.size();
		if (this.scrollPos > i - getLineCount()) {
			this.scrollPos = (i - getLineCount());
		}
		if (this.scrollPos <= 0) {
			this.scrollPos = 0;
			this.isScrolled = false;
		}
	}

	@Nullable
	public ITextComponent getChatComponent(int mouseX, int mouseY) {
		if (!getChatOpen()) {
			return null;
		}
		ScaledResolution scaledresolution = new ScaledResolution(this.mc);
		int i = scaledresolution.getScaleFactor();
		float f = getChatScale();
		int j = mouseX / i - 2;
		int k = mouseY / i - 40;
		j = MathHelper.floor_float(j / f);
		k = MathHelper.floor_float(k / f);
		if ((j >= 0) && (k >= 0)) {
			int l = Math.min(getLineCount(), this.drawnChatLines.size());
			if ((j <= MathHelper.floor_float(getChatWidth() / getChatScale()))
					&& (k < this.mc.fontRendererObj.FONT_HEIGHT * l + l)) {
				int i1 = k / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
				int j1;
				if ((i1 >= 0) && (i1 < this.drawnChatLines.size())) {
					ChatLineP chatline = (ChatLineP) this.drawnChatLines.get(i1);
					j1 = 0;
					for (ITextComponent itextcomponent : chatline.getChatComponent()) {
						if ((itextcomponent instanceof TextComponentString)) {
							j1 += this.mc.fontRendererObj
									.getStringWidth(GuiUtilRenderComponents.removeTextColorsIfConfigured(
											((TextComponentString) itextcomponent).getText(), false));
							if (j1 > j) {
								return itextcomponent;
							}
						}
					}
				}
				return null;
			}
			return null;
		}
		return null;
	}

	public boolean getChatOpen() {
		return this.mc.currentScreen instanceof GuiChat;
	}

	public void deleteChatLine(int id) {
		Iterator<ChatLineP> iterator = this.drawnChatLines.iterator();
		while (iterator.hasNext()) {
			ChatLineP chatline = (ChatLineP) iterator.next();
			if (chatline.getChatLineID() == id) {
				iterator.remove();
			}
		}
		iterator = this.chatLines.iterator();
		while (iterator.hasNext()) {
			ChatLineP chatline1 = (ChatLineP) iterator.next();
			if (chatline1.getChatLineID() == id) {
				iterator.remove();
				break;
			}
		}
	}

	public int getChatWidth() {
		return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
	}

	public int getChatHeight() {
		return calculateChatboxHeight(
				getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
	}

	public float getChatScale() {
		return this.mc.gameSettings.chatScale;
	}

	public static int calculateChatboxWidth(float scale) {
		int i = 320;
		int j = 40;
		return MathHelper.floor_float(scale * (i - j) + j);
	}

	public static int calculateChatboxHeight(float scale) {
		int i = 180;
		int j = 20;
		return MathHelper.floor_float(scale * (i - j) + j);
	}

	public int getLineCount() {
		return getChatHeight() / 9;
	}
}
