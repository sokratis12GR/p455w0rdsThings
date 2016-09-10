package p455w0rd.p455w0rdsthings;

import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.items.ItemCarbon;
import p455w0rd.p455w0rdsthings.items.ItemCarbonArmor;
import p455w0rd.p455w0rdsthings.items.ItemCarbonRod;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.items.ItemDankNullHolder;
import p455w0rd.p455w0rdsthings.items.ItemDankNullPanel;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class ModItems {
	
	public static ItemDankNull dankNullItem;
	public static ItemDankNullHolder dankNullHolder;
	public static ItemCarbon rawCarbon;
	public static ItemCarbonRod carbonRod;
	public static ItemDankNullPanel dankNullPanel;
	public static ItemCarbonArmor carbonHelmet;
	public static ItemCarbonArmor carbonChestplate;
	public static ItemCarbonArmor carbonLeggings;
	public static ItemCarbonArmor carbonBoots;
	public static final ArmorMaterial CARBON_MATERIAL = ItemUtils.addArmorMaterial("p455w0rdsthings:carbon", "p455w0rdsthings:carbon", 45, new int[] { 2, 6, 9, 3 }, 25, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 2.0F);

	public static void init() {
		dankNullHolder = new ItemDankNullHolder();
		dankNullItem = new ItemDankNull();
		rawCarbon = new ItemCarbon();
		carbonRod = new ItemCarbonRod();
		dankNullPanel = new ItemDankNullPanel();
		CARBON_MATERIAL.customCraftingMaterial = rawCarbon;
		carbonHelmet = new ItemCarbonArmor(CARBON_MATERIAL, EntityEquipmentSlot.HEAD, "carbon_helmet");
		carbonChestplate = new ItemCarbonArmor(CARBON_MATERIAL, EntityEquipmentSlot.CHEST, "carbon_chestplate");
		carbonLeggings = new ItemCarbonArmor(CARBON_MATERIAL, EntityEquipmentSlot.LEGS, "carbon_leggings");
		carbonBoots = new ItemCarbonArmor(CARBON_MATERIAL, EntityEquipmentSlot.FEET, "carbon_boots");

	}

	@SideOnly(Side.CLIENT)
	public static void preInitModels() {
		dankNullHolder.initModel();
		dankNullItem.initModel();

		rawCarbon.initModel();
		carbonRod.initModel();
		dankNullPanel.initModel();

		carbonHelmet.initModel();
		carbonChestplate.initModel();
		carbonLeggings.initModel();
		carbonBoots.initModel();
	}
}
