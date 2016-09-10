package p455w0rd.p455w0rdsthings.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.proxy.ClientProxy;

public class ItemWirelessReceiver extends Item {
	
	private final String name = "wireless_receiver";

	public ItemWirelessReceiver() {
		getClass();
		setRegistryName(name);
		getClass();
		setUnlocalizedName(name);
		GameRegistry.register(this);
		setMaxStackSize(1);
		setCreativeTab(ClientProxy.creativeTab);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
