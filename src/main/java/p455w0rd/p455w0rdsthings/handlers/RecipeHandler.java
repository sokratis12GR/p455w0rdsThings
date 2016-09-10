package p455w0rd.p455w0rdsthings.handlers;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import p455w0rd.p455w0rdsthings.Globals.Upgrades;
import p455w0rd.p455w0rdsthings.ModBlocks;
import p455w0rd.p455w0rdsthings.ModItems;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class RecipeHandler {
	
	public static void addRecipes() {
		
		//Items
		ItemStack coalBlock = new ItemStack(Blocks.COAL_BLOCK, 1);
		ItemStack rawCarbon = new ItemStack(ModItems.rawCarbon, 1);
		ItemStack carbonRod = new ItemStack(ModItems.carbonRod, 1);
		ItemStack redStoneCarbonRod = new ItemStack(ModItems.carbonRod, 1, 1);
		ItemStack lapisCarbonRod = new ItemStack(ModItems.carbonRod, 1, 2);
		ItemStack ironCarbonRod = new ItemStack(ModItems.carbonRod, 1, 3);
		ItemStack goldCarbonRod = new ItemStack(ModItems.carbonRod, 1, 4);
		ItemStack diamondCarbonRod = new ItemStack(ModItems.carbonRod, 1, 5);
		ItemStack emeraldCarbonRod = new ItemStack(ModItems.carbonRod, 1, 6);
		ItemStack redPane = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 14);
		ItemStack bluePane = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 3);
		ItemStack whitePane = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 0);
		ItemStack yellowPane = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 4);
		ItemStack cyanPane = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 9);
		ItemStack limePane = new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 5);
		ItemStack redStonePanel = new ItemStack(ModItems.dankNullPanel, 1, 0);
		ItemStack lapisPanel = new ItemStack(ModItems.dankNullPanel, 1, 1);
		ItemStack ironPanel = new ItemStack(ModItems.dankNullPanel, 1, 2);
		ItemStack goldPanel = new ItemStack(ModItems.dankNullPanel, 1, 3);
		ItemStack diamondPanel = new ItemStack(ModItems.dankNullPanel, 1, 4);
		ItemStack emeraldPanel = new ItemStack(ModItems.dankNullPanel, 1, 5);
		ItemStack dankNull0 = new ItemStack(ModItems.dankNullItem, 1, 0);
		ItemStack dankNull1 = new ItemStack(ModItems.dankNullItem, 1, 1);
		ItemStack dankNull2 = new ItemStack(ModItems.dankNullItem, 1, 2);
		ItemStack dankNull3 = new ItemStack(ModItems.dankNullItem, 1, 3);
		ItemStack dankNull4 = new ItemStack(ModItems.dankNullItem, 1, 4);
		ItemStack dankNull5 = new ItemStack(ModItems.dankNullItem, 1, 5);
		ItemStack carbonBlock = new ItemStack(ModBlocks.carbonBlock, 1);
		ItemStack diamond = new ItemStack(Items.DIAMOND, 1);
		ItemStack string = new ItemStack(Items.STRING, 1);
		ItemStack paper = new ItemStack(Items.PAPER, 1);
		ItemStack nameTag = new ItemStack(Items.NAME_TAG, 1);
		ItemStack leather = new ItemStack(Items.LEATHER, 1);
		ItemStack saddle = new ItemStack(Items.SADDLE, 1);
		ItemStack goldBlock = new ItemStack(Blocks.GOLD_BLOCK, 1);
		ItemStack apple = new ItemStack(Items.APPLE, 1);
		ItemStack notchApple = new ItemStack(Items.GOLDEN_APPLE, 1, 1);
		ItemStack carbonHelmet = new ItemStack(ModItems.carbonHelmet, 1);
		ItemStack carbonChestplate = new ItemStack(ModItems.carbonChestplate, 1);
		ItemStack carbonLeggings = new ItemStack(ModItems.carbonLeggings, 1);
		ItemStack carbonBoots = new ItemStack(ModItems.carbonBoots, 1);
		ItemStack diamondBlock = new ItemStack(Blocks.DIAMOND_BLOCK);
		ItemStack carbonHelmetUpgraded = carbonHelmet.copy();
		ItemUtils.enableUpgrade(carbonHelmetUpgraded, Upgrades.NIGHTVISION);
		ItemStack carbonChestplateUpgraded = carbonChestplate.copy();
		ItemUtils.enableUpgrade(carbonChestplateUpgraded, Upgrades.FLIGHT);
		ItemStack carbonLeggingsUpgraded = carbonLeggings.copy();
		ItemUtils.enableUpgrade(carbonLeggingsUpgraded, Upgrades.SPEED);
		ItemStack carbonBootsUpgraded = carbonBoots.copy();
		ItemUtils.enableUpgrade(carbonBootsUpgraded, Upgrades.STEPASSIST);
		ItemStack carbonHelmet2 = carbonHelmet.copy();
		ItemStack carbonChestplate2 = carbonChestplate.copy();
		ItemStack carbonLeggings2 = carbonLeggings.copy();
		ItemStack carbonBoots2 = carbonBoots.copy();

		//Smelting
		GameRegistry.addSmelting(coalBlock, rawCarbon, 0.7F);
		GameRegistry.addSmelting(rawCarbon, carbonRod, 0.85F);		
		GameRegistry.addSmelting(diamondBlock, carbonBlock, 0.85F);

		//Shaped
		GameRegistry.addRecipe(new ShapedOreRecipe(redStoneCarbonRod, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('b'), carbonRod, Character.valueOf('a'), "dustRedstone" }));

		GameRegistry.addRecipe(new ShapedOreRecipe(lapisCarbonRod, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), "gemLapis", Character.valueOf('b'), redStoneCarbonRod }));

		GameRegistry.addRecipe(new ShapedOreRecipe(ironCarbonRod, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), "ingotIron", Character.valueOf('b'), lapisCarbonRod }));

		GameRegistry.addRecipe(new ShapedOreRecipe(goldCarbonRod, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), "ingotGold", Character.valueOf('b'), ironCarbonRod }));

		GameRegistry.addRecipe(new ShapedOreRecipe(diamondCarbonRod, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), "gemDiamond", Character.valueOf('b'), goldCarbonRod }));

		GameRegistry.addRecipe(new ShapedOreRecipe(emeraldCarbonRod, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), "gemEmerald", Character.valueOf('b'), diamondCarbonRod }));

		GameRegistry.addRecipe(new ShapedOreRecipe(redStonePanel, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), redStoneCarbonRod, Character.valueOf('b'), redPane }));

		GameRegistry.addRecipe(new ShapedOreRecipe(lapisPanel, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), lapisCarbonRod, Character.valueOf('b'), bluePane }));

		GameRegistry.addRecipe(new ShapedOreRecipe(ironPanel, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), ironCarbonRod, Character.valueOf('b'), whitePane }));

		GameRegistry.addRecipe(new ShapedOreRecipe(goldPanel, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), goldCarbonRod, Character.valueOf('b'), yellowPane }));

		GameRegistry.addRecipe(new ShapedOreRecipe(diamondPanel, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), diamondCarbonRod, Character.valueOf('b'), cyanPane }));

		GameRegistry.addRecipe(new ShapedOreRecipe(emeraldPanel, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), emeraldCarbonRod, Character.valueOf('b'), limePane }));

		GameRegistry.addRecipe(new ShapedOreRecipe(dankNull0, new Object[] { "   ", "a a", "aaa",
				Character.valueOf('a'), redStonePanel }));

		GameRegistry.addRecipe(new ShapedOreRecipe(dankNull1, new Object[] { "   ", "a a", "aaa",
				Character.valueOf('a'), lapisPanel }));

		GameRegistry.addRecipe(new ShapedOreRecipe(dankNull2, new Object[] { "   ", "a a", "aaa",
				Character.valueOf('a'), ironPanel }));

		GameRegistry.addRecipe(new ShapedOreRecipe(dankNull3, new Object[] { "   ", "a a", "aaa",
				Character.valueOf('a'), goldPanel }));

		GameRegistry.addRecipe(new ShapedOreRecipe(dankNull4, new Object[] { "   ", "a a", "aaa",
				Character.valueOf('a'), diamondPanel }));

		GameRegistry.addRecipe(new ShapedOreRecipe(dankNull5, new Object[] { "   ", "a a", "aaa",
				Character.valueOf('a'), emeraldPanel }));

		GameRegistry.addRecipe(new ShapedOreRecipe(carbonBlock, new Object[] { "aaa", "aaa", "aaa",
				Character.valueOf('a'), rawCarbon }));

		GameRegistry.addRecipe(new ShapedOreRecipe(diamond, new Object[] { "aaa", "aaa", "aaa",
				Character.valueOf('a'), carbonBlock }));

		GameRegistry.addRecipe(new ShapedOreRecipe(saddle, new Object[] { " a ", "aaa", "a a",
				Character.valueOf('a'), leather }));

		GameRegistry.addRecipe(new ShapedOreRecipe(notchApple, new Object[] { "aaa", "aba", "aaa",
				Character.valueOf('a'), goldBlock, Character.valueOf('b'), apple }));

		GameRegistry.addRecipe(new ShapedOreRecipe(carbonHelmet, new Object[] { "aaa", "a a",
				Character.valueOf('a'), carbonBlock }));

		GameRegistry.addRecipe(new ShapedOreRecipe(carbonChestplate, new Object[] { "a a", "aaa", "aaa",
				Character.valueOf('a'), carbonBlock }));

		GameRegistry.addRecipe(new ShapedOreRecipe(carbonLeggings, new Object[] { "aaa", "a a", "a a",
				Character.valueOf('a'), carbonBlock }));

		GameRegistry.addRecipe(new ShapedOreRecipe(carbonBoots, new Object[] { "a a", "a a",
				Character.valueOf('a'), carbonBlock }));

		carbonHelmet2.setItemDamage(32767);
		GameRegistry.addRecipe(new ShapedOreRecipe(carbonHelmetUpgraded, new Object[] { "aba", "a a",
				Character.valueOf('a'), emeraldCarbonRod, Character.valueOf('b'), carbonHelmet2 }));

		carbonChestplate2.setItemDamage(32767);
		GameRegistry.addRecipe(new ShapedOreRecipe(carbonChestplateUpgraded, new Object[] { "a a", "aba", "aaa",
				Character.valueOf('a'), emeraldCarbonRod, Character.valueOf('b'), carbonChestplate2 }));

		carbonLeggings2.setItemDamage(32767);
		GameRegistry.addRecipe(new ShapedOreRecipe(carbonLeggingsUpgraded, new Object[] { "aba", "a a", "a a",
				Character.valueOf('a'), emeraldCarbonRod, Character.valueOf('b'), carbonLeggings2 }));

		carbonBoots2.setItemDamage(32767);
		GameRegistry.addRecipe(new ShapedOreRecipe(carbonBootsUpgraded, new Object[] { "aba", "a a",
				Character.valueOf('a'), emeraldCarbonRod, Character.valueOf('b'), carbonBoots2 }));
		
		//Shapeless
		GameRegistry.addRecipe(new ShapelessOreRecipe(nameTag, new Object[] { string, paper }));
	}
}
