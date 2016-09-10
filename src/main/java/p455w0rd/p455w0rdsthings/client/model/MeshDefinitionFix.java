package p455w0rd.p455w0rdsthings.client.model;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract interface MeshDefinitionFix extends ItemMeshDefinition {
	
	public abstract ModelResourceLocation getLocation(ItemStack paramItemStack);

	public static ItemMeshDefinition create(MeshDefinitionFix lambda) {
		return lambda;
	}

	public default ModelResourceLocation getModelLocation(ItemStack stack) {
		return getLocation(stack);
	}
}
