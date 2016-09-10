package p455w0rd.p455w0rdsthings.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.P455w0rdsThings;
import p455w0rd.p455w0rdsthings.client.gui.GuiDankNull;
import p455w0rd.p455w0rdsthings.client.gui.GuiNewChatP;
import p455w0rd.p455w0rdsthings.container.ContainerDankNull;

public class GuiHandler implements IGuiHandler {
	
	public Object getServerGuiElement(int ID, EntityPlayer playerIn, World worldIn, int x, int y, int z) {
		if (ID == Globals.GUINUM_DANKNULL) {
			return new ContainerDankNull(playerIn);
		}
		return null;
	}

	public Object getClientGuiElement(int ID, EntityPlayer playerIn, World worldIn, int x, int y, int z) {
		if (ID == Globals.GUINUM_DANKNULL) {
			return new GuiDankNull(new ContainerDankNull(playerIn), playerIn.inventory);
		}
		if (ID == Globals.GUINUM_CHATWINDOW) {
			return new GuiNewChatP(Minecraft.getMinecraft());
		}
		return null;
	}

	public static void launchGui(int ID, EntityPlayer playerIn, World worldIn, int x, int y, int z) {
		playerIn.openGui(P455w0rdsThings.INSTANCE, ID, worldIn, x, y, z);
	}
}
