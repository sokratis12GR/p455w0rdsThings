package p455w0rd.p455w0rdsthings.proxy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import p455w0rd.p455w0rdsthings.CreativeTabP;
import p455w0rd.p455w0rdsthings.ModBlocks;
import p455w0rd.p455w0rdsthings.ModItems;
import p455w0rd.p455w0rdsthings.client.render.DankNullRenderer;

public class ClientProxy extends CommonProxy {
	
	public static CreativeTabs creativeTab;

	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		creativeTab = new CreativeTabP().setNoScrollbar();
		ModBlocks.initModels();
		ModItems.preInitModels();
	}

	public void init(FMLInitializationEvent e) {
		super.init(e);
		MinecraftForge.EVENT_BUS.register(new DankNullRenderer());
	}
}
