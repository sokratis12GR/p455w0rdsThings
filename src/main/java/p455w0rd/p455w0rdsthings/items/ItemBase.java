package p455w0rd.p455w0rdsthings.items;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBase extends Item {
	
	private String name = "";

	public ItemBase(String name) {
		this.name = name;
		setRegistryName(this.name);
		setUnlocalizedName(this.name);
		GameRegistry.register(this);
		setMaxStackSize(64);
		setMaxDamage(0);
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
