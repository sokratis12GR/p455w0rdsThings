package p455w0rd.p455w0rdsthings;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import p455w0rd.p455w0rdsthings.Globals.Upgrades;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class CreativeTabP extends CreativeTabs {
	
	public CreativeTabP() {
		super("p455w0rdsthings");
	}

	public Item getTabIconItem() {
		return ModItems.dankNullItem;
	}

	public void displayAllRelevantItems(List<ItemStack> items) {
		for (int i = 0; i < 6; i++) {
			items.add(new ItemStack(ModItems.dankNullItem, 1, i));
		}
		for (int i = 0; i < 7; i++) {
			items.add(new ItemStack(ModItems.carbonRod, 1, i));
		}
		for (int i = 0; i < 6; i++) {
			items.add(new ItemStack(ModItems.dankNullPanel, 1, i));
		}
		
		items.add(new ItemStack(ModItems.rawCarbon, 1));
		items.add(new ItemStack(ModBlocks.carbonBlock, 1));
		items.add(new ItemStack(ModBlocks.netherCarbonOreBlock, 1));

		items.add(new ItemStack(ModItems.carbonHelmet, 1));
		items.add(new ItemStack(ModItems.carbonChestplate, 1));
		items.add(new ItemStack(ModItems.carbonLeggings, 1));
		items.add(new ItemStack(ModItems.carbonBoots, 1));

		ItemStack upgradedCarbonHelm = new ItemStack(ModItems.carbonHelmet);
		ItemUtils.enableUpgrade(upgradedCarbonHelm, Upgrades.NIGHTVISION);
		items.add(upgradedCarbonHelm);

		ItemStack upgradedCarbonChest = new ItemStack(ModItems.carbonChestplate);
		ItemUtils.enableUpgrade(upgradedCarbonChest, Upgrades.FLIGHT);
		items.add(upgradedCarbonChest);

		ItemStack upgradedCarbonLegs = new ItemStack(ModItems.carbonLeggings);
		ItemUtils.enableUpgrade(upgradedCarbonLegs, Upgrades.SPEED);
		items.add(upgradedCarbonLegs);

		ItemStack upgradedCarbonBoots = new ItemStack(ModItems.carbonBoots);
		ItemUtils.enableUpgrade(upgradedCarbonBoots, Upgrades.STEPASSIST);
		items.add(upgradedCarbonBoots);

		super.displayAllRelevantItems(items);
	}
}
