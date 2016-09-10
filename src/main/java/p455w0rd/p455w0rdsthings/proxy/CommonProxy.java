package p455w0rd.p455w0rdsthings.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import p455w0rd.p455w0rdsthings.ModAchievements;
import p455w0rd.p455w0rdsthings.ModBlocks;
import p455w0rd.p455w0rdsthings.ModEntities;
import p455w0rd.p455w0rdsthings.ModFluids;
import p455w0rd.p455w0rdsthings.ModItems;
import p455w0rd.p455w0rdsthings.handlers.EventsHandler;
import p455w0rd.p455w0rdsthings.handlers.OreDictionaryHandler;
import p455w0rd.p455w0rdsthings.handlers.RecipeHandler;
import p455w0rd.p455w0rdsthings.world.OreGen;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent e) {
		ModFluids.init();
		ModBlocks.init();
		ModItems.init();
	}

	public void init(FMLInitializationEvent e) {
		MinecraftForge.EVENT_BUS.register(new EventsHandler());
		OreDictionaryHandler.addOres();
		RecipeHandler.addRecipes();
		ModEntities.init();
		GameRegistry.registerWorldGenerator(new OreGen(), 0);
		ModAchievements.init();
	}

	public void postInit(FMLPostInitializationEvent e) {
	}
}
