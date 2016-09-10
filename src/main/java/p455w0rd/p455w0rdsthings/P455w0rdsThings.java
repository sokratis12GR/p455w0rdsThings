package p455w0rd.p455w0rdsthings;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import p455w0rd.p455w0rdsthings.handlers.GuiHandler;
import p455w0rd.p455w0rdsthings.handlers.PacketHandler;
import p455w0rd.p455w0rdsthings.proxy.CommonProxy;

@Mod(modid=Globals.MODID, name=Globals.NAME, dependencies = "required-after:Forge@[12.18.1.2017,);", version=Globals.VERSION, acceptedMinecraftVersions = "["+Globals.MCVERSION+"]")
public class P455w0rdsThings {
	
	@SidedProxy(clientSide = "p455w0rd.p455w0rdsthings.proxy.ClientProxy", serverSide = "p455w0rd.p455w0rdsthings.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@Instance("p455w0rdsthings")
	public static P455w0rdsThings INSTANCE;

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		INSTANCE = this;
		PacketHandler.registerMessages("p455w0rdsthings");
		proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler("p455w0rdsthings", new GuiHandler());
	}
}
