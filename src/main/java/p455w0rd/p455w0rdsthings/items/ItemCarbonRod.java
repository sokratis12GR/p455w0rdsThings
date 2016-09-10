package p455w0rd.p455w0rdsthings.items;

import java.util.List;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class ItemCarbonRod extends ItemBase {
	
	public ItemCarbonRod() {
		super("carbonRod");
	}

	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < 7; i++) {
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}

	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.translateToLocal(getUnlocalizedNameInefficiently(stack) + "" + getDamage(stack) + ".name").trim();
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		for (int i = 0; i < 7; i++) {
			ModelLoader.setCustomModelResourceLocation(this, i,
					new ModelResourceLocation(getRegistryName() + "" + i, "inventory"));
		}
	}
}
