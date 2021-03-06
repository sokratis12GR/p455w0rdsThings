package p455w0rd.p455w0rdsthings.client.gui;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ITabCompleter;
import net.minecraft.util.TabCompleter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class GuiChatP extends GuiScreen implements ITabCompleter {
	
	private String historyBuffer = "";
	private int sentHistoryCursor = -1;
	private TabCompleter tabCompleter;
	protected GuiTextField inputField;
	private String defaultInputFieldText = "";

	public GuiChatP() {
	}

	public GuiChatP(String defaultText) {
		this.defaultInputFieldText = defaultText;
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.sentHistoryCursor = new GuiNewChatP(Minecraft.getMinecraft()).getSentMessages().size();
		this.inputField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 12, this.width - 4, 12);
		this.inputField.setMaxStringLength(100);
		this.inputField.setEnableBackgroundDrawing(false);
		this.inputField.setFocused(true);
		this.inputField.setText(this.defaultInputFieldText);
		this.inputField.setCanLoseFocus(false);
		this.tabCompleter = new ChatTabCompleter(this.inputField);
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		new GuiNewChatP(Minecraft.getMinecraft()).resetScroll();
	}

	public void updateScreen() {
		this.inputField.updateCursorCounter();
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		this.tabCompleter.resetRequested();
		if (keyCode == 15) {
			this.tabCompleter.complete();
		}
		else {
			this.tabCompleter.resetDidComplete();
		}
		if (keyCode == 1) {
			this.mc.displayGuiScreen((GuiScreen) null);
		}
		else if ((keyCode != 28) && (keyCode != 156)) {
			if (keyCode == 200) {
				getSentHistory(-1);
			}
			else if (keyCode == 208) {
				getSentHistory(1);
			}
			else if (keyCode == 201) {
				new GuiNewChatP(Minecraft.getMinecraft())
						.scroll(new GuiNewChatP(Minecraft.getMinecraft()).getLineCount() - 1);
			}
			else if (keyCode == 209) {
				new GuiNewChatP(Minecraft.getMinecraft())
						.scroll(-new GuiNewChatP(Minecraft.getMinecraft()).getLineCount() + 1);
			}
			else {
				this.inputField.textboxKeyTyped(typedChar, keyCode);
			}
		}
		else {
			String s = this.inputField.getText().trim();
			if (!s.isEmpty()) {
				sendChatMessage(s);
			}
			this.mc.displayGuiScreen((GuiScreen) null);
		}
	}

	public void handleMouseInput() throws IOException {
		super.handleMouseInput();
		int i = Mouse.getEventDWheel();
		if (i != 0) {
			if (i > 1) {
				i = 1;
			}
			if (i < -1) {
				i = -1;
			}
			if (!isShiftKeyDown()) {
				i *= 7;
			}
			new GuiNewChatP(Minecraft.getMinecraft()).scroll(i);
		}
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (mouseButton == 0) {
			ITextComponent itextcomponent = new GuiNewChatP(Minecraft.getMinecraft()).getChatComponent(Mouse.getX(),
					Mouse.getY());
			if (handleComponentClick(itextcomponent)) {
				return;
			}
		}
		this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	protected void setText(String newChatText, boolean shouldOverwrite) {
		if (shouldOverwrite) {
			this.inputField.setText(newChatText);
		}
		else {
			this.inputField.writeText(newChatText);
		}
	}

	public void getSentHistory(int msgPos) {
		int i = this.sentHistoryCursor + msgPos;
		int j = new GuiNewChatP(Minecraft.getMinecraft()).getSentMessages().size();
		i = MathHelper.clamp_int(i, 0, j);
		if (i != this.sentHistoryCursor) {
			if (i == j) {
				this.sentHistoryCursor = j;
				this.inputField.setText(this.historyBuffer);
			}
			else {
				if (this.sentHistoryCursor == j) {
					this.historyBuffer = this.inputField.getText();
				}
				this.inputField.setText((String) new GuiNewChatP(Minecraft.getMinecraft()).getSentMessages().get(i));
				this.sentHistoryCursor = i;
			}
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		this.inputField.drawTextBox();
		ITextComponent itextcomponent = new GuiNewChatP(Minecraft.getMinecraft()).getChatComponent(Mouse.getX(),
				Mouse.getY());
		if ((itextcomponent != null) && (itextcomponent.getStyle().getHoverEvent() != null)) {
			handleComponentHover(itextcomponent, mouseX, mouseY);
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	public boolean doesGuiPauseGame() {
		return false;
	}

	public void setCompletions(String... newCompletions) {
		this.tabCompleter.setCompletions(newCompletions);
	}

	@SideOnly(Side.CLIENT)
	public static class ChatTabCompleter extends TabCompleter {
		private Minecraft clientInstance = Minecraft.getMinecraft();

		public ChatTabCompleter(GuiTextField p_i46749_1_) {
			super(p_i46749_1_, false);
		}

		public void complete() {
			super.complete();
			if (this.completions.size() > 1) {
				StringBuilder stringbuilder = new StringBuilder();
				for (String s : this.completions) {
					if (stringbuilder.length() > 0) {
						stringbuilder.append(", ");
					}
					stringbuilder.append(s);
				}
				new GuiNewChatP(Minecraft.getMinecraft())
						.printChatMessageWithOptionalDeletion(new TextComponentString(stringbuilder.toString()), 1);
			}
		}

		@Nullable
		public BlockPos getTargetBlockPos() {
			BlockPos blockpos = null;
			if ((this.clientInstance.objectMouseOver != null)
					&& (this.clientInstance.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK)) {
				blockpos = this.clientInstance.objectMouseOver.getBlockPos();
			}
			return blockpos;
		}
	}
}
