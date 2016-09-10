package p455w0rd.p455w0rdsthings.client.render;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import p455w0rd.p455w0rdsthings.lib.render.IItemRenderer;
import p455w0rd.p455w0rdsthings.lib.render.ModelRegistryHelper;

public class PModelRegistryHelper extends ModelRegistryHelper {
	
	public static void registerDankNullRenderer(Item item, IItemRenderer renderer, int damage) {
		int modelDamage = damage;
		if (damage > 5) {
			modelDamage -= 5;
		}
		ModelResourceLocation modelLoc = new ModelResourceLocation(
				Item.REGISTRY.getNameForObject(item) + "" + modelDamage + modelDamage, "inventory");
		ModelLoader.setCustomModelResourceLocation(item, damage, modelLoc);
		register(modelLoc, renderer);
	}

	public static void registerRenderer(Item item, IItemRenderer renderer) {
		ModelResourceLocation modelLoc = new ModelResourceLocation(
				(ResourceLocation) Item.REGISTRY.getNameForObject(item), "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, modelLoc);
		register(modelLoc, renderer);
	}
}
