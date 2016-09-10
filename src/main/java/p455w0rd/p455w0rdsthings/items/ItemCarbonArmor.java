package p455w0rd.p455w0rdsthings.items;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals.Upgrades;
import p455w0rd.p455w0rdsthings.client.model.ModelCarbonArmor;
import p455w0rd.p455w0rdsthings.proxy.ClientProxy;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class ItemCarbonArmor extends ItemArmor {
	
	protected Map<EntityEquipmentSlot, ModelBiped> models = null;
	public final EntityEquipmentSlot type;
	private boolean UpgradedIcon = false;

	public ItemCarbonArmor(ItemArmor.ArmorMaterial materialIn, EntityEquipmentSlot equipmentSlotIn, String itemName) {
		super(materialIn, 0, equipmentSlotIn);
		this.type = equipmentSlotIn;
		setRegistryName(itemName);
		setUnlocalizedName(itemName);
		GameRegistry.register(this);
		setCreativeTab(ClientProxy.creativeTab);
	}

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return (ItemUtils.isItemUpgradeActive(stack, this.armorType)) || (isUpgradedIcon());
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		ItemStack upgradedItem = new ItemStack(itemIn, 1);
		subItems.add(new ItemStack(itemIn, 1));
		Upgrades upgradeType = null;
		switch (this.armorType) {
		case HEAD:
			upgradeType = Upgrades.NIGHTVISION;
			break;
		case CHEST:
			upgradeType = Upgrades.FLIGHT;
			break;
		case LEGS:
			upgradeType = Upgrades.SPEED;
			break;
		case FEET:
			upgradeType = Upgrades.STEPASSIST;
			break;
		case MAINHAND:
			break;
		case OFFHAND:
			break;
		}
		ItemUtils.enableUpgrade(upgradedItem, upgradeType);
		subItems.add(upgradedItem);
	}

	public boolean getHasSubtypes() {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		if (hasEffect(stack)) {
			tooltip.add("");
			tooltip.add(TextFormatting.WHITE + "Active Upgrade: " + TextFormatting.BLUE + TextFormatting.ITALIC
					+ getUpgradeDesc(this.armorType));
			if (playerIn.capabilities.isFlying) {
				if (this.type == EntityEquipmentSlot.CHEST) {
					tooltip.add("");
					tooltip.add(TextFormatting.ITALIC + "Cannot remove chestplate while flying!");
				}
			}
		}
	}

	private String getUpgradeDesc(EntityEquipmentSlot type) {
		switch (type) {
		case HEAD:
			return "Night Vision";
		case CHEST:
			return "Flight";
		case FEET:
			return "Step Assist";
		case LEGS:
			return "Speed";
		case MAINHAND:
			break;
		case OFFHAND:
			break;
		}
		return "";
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot,
			ModelBiped original) {
		if (hasEffect(itemStack)) {
			ModelBiped model = getArmorModelForSlot(entityLiving, itemStack, armorSlot);
			if (model == null) {
				model = provideArmorModelForSlot(itemStack, armorSlot);
			}
			if (model != null) {
				model.setModelAttributes(original);
				return model;
			}
		}
		return super.getArmorModel(entityLiving, itemStack, armorSlot, original);
	}

	@SuppressWarnings({
			"unchecked", "rawtypes"
	})
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModelForSlot(EntityLivingBase entity, ItemStack stack, EntityEquipmentSlot slot) {
		if (this.models == null) {
			this.models = new EnumMap(EntityEquipmentSlot.class);
		}
		return (ModelBiped) this.models.get(slot);
	}

	@SideOnly(Side.CLIENT)
	public ModelBiped provideArmorModelForSlot(ItemStack stack, EntityEquipmentSlot slot) {
		this.models.put(slot, new ModelCarbonArmor(slot));
		return (ModelBiped) this.models.get(slot);
	}

	@SideOnly(Side.CLIENT)
	public String getArmorTexture(ItemStack itemStack, Entity entity, EntityEquipmentSlot slot, String layer) {
		if (hasEffect(itemStack)) {
			return "p455w0rdsthings:textures/models/armor/ModelCarbonArmor.png";
		}
		if (slot == EntityEquipmentSlot.LEGS) {
			return "p455w0rdsthings:textures/models/armor/carbon_layer_2.png";
		}
		return "p455w0rdsthings:textures/models/armor/carbon_layer_1.png";
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}

	public boolean isUpgradedIcon() {
		return this.UpgradedIcon;
	}

	public Item setUpgradedIcon(boolean upgradedIcon) {
		this.UpgradedIcon = upgradedIcon;
		return this;
	}
}
