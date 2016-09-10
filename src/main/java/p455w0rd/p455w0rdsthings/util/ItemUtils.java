package p455w0rd.p455w0rdsthings.util;

import com.google.common.collect.Lists;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.handlers.PacketHandler;
import p455w0rd.p455w0rdsthings.items.ItemCarbonArmor;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.network.PacketSetSelectedItem;

public class ItemUtils {
	
	public static ItemStack getDankNull(InventoryPlayer playerInv) {
		EntityPlayer player = playerInv.player;
		ItemStack dankNullItem = null;
		if (player.getHeldItemMainhand() != null) {
			if ((player.getHeldItemMainhand().getItem() instanceof ItemDankNull)) {
				dankNullItem = player.getHeldItemMainhand();
			} else if ((player.getHeldItemOffhand() != null)
					&& ((player.getHeldItemOffhand().getItem() instanceof ItemDankNull))) {
				dankNullItem = player.getHeldItemOffhand();
			}
		} else if (player.getHeldItemOffhand() != null) {
			if ((player.getHeldItemOffhand().getItem() instanceof ItemDankNull)) {
				dankNullItem = player.getHeldItem(EnumHand.OFF_HAND);
			} else if ((player.getHeldItemMainhand() != null)
					&& ((player.getHeldItemMainhand().getItem() instanceof ItemDankNull))) {
				dankNullItem = player.getHeldItemMainhand();
			}
		}
		if (dankNullItem == null) {
			int invSize = playerInv.getSizeInventory();
			if (invSize <= 0) {
				return null;
			}
			for (int i = 0; i < invSize; i++) {
				ItemStack itemStack = playerInv.getStackInSlot(i);
				if (itemStack != null) {
					if ((itemStack.getItem() instanceof ItemDankNull)) {
						dankNullItem = itemStack;
						break;
					}
				}
			}
		}
		return dankNullItem;
	}

	public static void reArrangeStacks(ItemStack itemStackIn) {
		NBTTagList itemList = getInventoryTagList(itemStackIn);
		if (itemList != null) {
			for (int i = 0; i < itemList.tagCount(); i++) {
				itemList.getCompoundTagAt(i).setInteger("Slot", i);
			}
		}
	}

	@SuppressWarnings("unused")
	private List<ItemStack> getInventoryList(ItemStack itemStackIn) {
		if ((itemStackIn.hasTagCompound()) && (itemStackIn.getTagCompound().hasKey("danknull-inventory"))) {
			List<ItemStack> inventoryItemStacks = Lists.newArrayList();
			NBTTagCompound nbtTC = itemStackIn.getTagCompound();
			NBTTagList itemList = nbtTC.getTagList("danknull-inventory", 10);
			if (itemList != null) {
				for (int i = 0; i < itemList.tagCount(); i++) {
					inventoryItemStacks.add(ItemStack.loadItemStackFromNBT(itemList.getCompoundTagAt(i)));
				}
				return inventoryItemStacks;
			}
		}
		return null;
	}

	public static int getSelectedStackIndex(ItemStack itemStackIn) {
		if (!itemStackIn.hasTagCompound()) {
			itemStackIn.setTagCompound(new NBTTagCompound());
		}
		if (!itemStackIn.getTagCompound().hasKey("selectedIndex")) {
			itemStackIn.getTagCompound().setInteger("selectedIndex", 0);
		}
		return itemStackIn.getTagCompound().getInteger("selectedIndex");
	}

	public static ItemStack getItemByIndex(ItemStack itemStackIn, int index) {
		if ((itemStackIn.hasTagCompound()) && (itemStackIn.getTagCompound().hasKey("danknull-inventory"))) {
			NBTTagCompound nbtTC = itemStackIn.getTagCompound();
			NBTTagList itemList = nbtTC.getTagList("danknull-inventory", 10);
			if (itemList.getCompoundTagAt(index) != null) {
				return ItemStack.loadItemStackFromNBT(itemList.getCompoundTagAt(index));
			}
		}
		return null;
	}

	public static void setSelectedStackIndex(ItemStack itemStackIn, int index) {
		if (!itemStackIn.hasTagCompound()) {
			itemStackIn.setTagCompound(new NBTTagCompound());
		}
		itemStackIn.getTagCompound().setInteger("selectedIndex", index);
	}

	public static void setNextSelectedStack(ItemStack itemStackIn) {
		setNextSelectedStack(itemStackIn, null);
	}

	public static void setNextSelectedStack(ItemStack itemStackIn, EntityPlayer player) {
		int currentIndex = getSelectedStackIndex(itemStackIn);
		int totalSize = getItemCount(itemStackIn);
		int maxIndex = totalSize - 1;
		int newIndex = 0;
		if (totalSize > 1) {
			if (currentIndex == maxIndex) {
				PacketHandler.INSTANCE.sendToServer(new PacketSetSelectedItem(newIndex));
			} else {
				newIndex = currentIndex + 1;
				PacketHandler.INSTANCE.sendToServer(new PacketSetSelectedItem(newIndex));
			}
			if (player != null) {
				displaySelectedMessage(itemStackIn, player, newIndex);
			}
		}
	}

	public static void displaySelectedMessage(ItemStack itemStackIn, EntityPlayer player, int index) {
		player.addChatComponentMessage(new TextComponentString(TextFormatting.BLUE + "" + TextFormatting.ITALIC + ""
				+ getItemByIndex(itemStackIn, index).getDisplayName() + " Selected"));
	}

	public static void setPreviousSelectedStack(ItemStack itemStackIn, EntityPlayer player) {
		int currentIndex = getSelectedStackIndex(itemStackIn);
		int totalSize = getItemCount(itemStackIn);
		int maxIndex = totalSize - 1;
		int newIndex = 0;
		if (totalSize > 1) {
			if (currentIndex == 0) {
				newIndex = maxIndex;
				PacketHandler.INSTANCE.sendToServer(new PacketSetSelectedItem(newIndex));
			} else {
				newIndex = currentIndex - 1;
				PacketHandler.INSTANCE.sendToServer(new PacketSetSelectedItem(newIndex));
			}
			if (player != null) {
				displaySelectedMessage(itemStackIn, player, newIndex);
			}
		}
	}

	public static int getItemCount(ItemStack itemStackIn) {
		if (getInventoryTagList(itemStackIn) == null) {
			return 0;
		}
		return getInventoryTagList(itemStackIn).tagCount();
	}

	public static NBTTagList getInventoryTagList(ItemStack itemStackIn) {
		if ((itemStackIn.hasTagCompound()) && (itemStackIn.getTagCompound().hasKey("danknull-inventory"))) {
			return itemStackIn.getTagCompound().getTagList("danknull-inventory", 10);
		}
		return null;
	}

	public static void decrSelectedStackSize(ItemStack itemStackIn, long amount) {
		long newStackSize = getSelectedStackSize(itemStackIn) - amount;
		NBTTagCompound nbtTC = getSelectedStack(itemStackIn).getTagCompound();
		if (newStackSize >= 1L) {
			nbtTC.setLong("p455w0rd.StackSize", newStackSize);
		} else {
			NBTTagList tagList = itemStackIn.getTagCompound().getTagList("danknull-inventory", 10);
			int index = getSelectedStackIndex(itemStackIn);
			tagList.removeTag(index);
			reArrangeStacks(itemStackIn);
		}
	}

	public static long getSelectedStackSize(ItemStack itemStackIn) {
		ItemStack selectedStack = getSelectedStack(itemStackIn);
		if (selectedStack != null) {
			long selectedStackSize = selectedStack.getTagCompound().getLong("p455w0rd.StackSize");
			return selectedStackSize;
		}
		return 0L;
	}

	public static ItemStack getSelectedStack(ItemStack itemStackIn) {
		if ((itemStackIn.hasTagCompound()) && (itemStackIn.getTagCompound().hasKey("danknull-inventory"))) {
			NBTTagCompound nbtTC = itemStackIn.getTagCompound();
			if (!nbtTC.hasKey("selectedIndex")) {
				nbtTC.setInteger("selectedIndex", 0);
			}
			int selectedIndex = nbtTC.getInteger("selectedIndex");
			NBTTagList itemList = nbtTC.getTagList("danknull-inventory", 10);
			if (itemList != null) {
				ItemStack selectedStack = ItemStack.loadItemStackFromNBT(itemList.getCompoundTagAt(selectedIndex));
				if (selectedStack != null) {
					return selectedStack;
				}
			}
		}
		return null;
	}

	public static ItemStack isFiltered(ItemStack itemStackIn, ItemStack filteredStack) {
		if ((!itemStackIn.hasTagCompound()) || (!itemStackIn.getTagCompound().hasKey("danknull-inventory"))) {
			return null;
		}
		NBTTagCompound nbtTC = itemStackIn.getTagCompound();
		NBTTagList itemList = nbtTC.getTagList("danknull-inventory", 10);
		if (itemList != null) {
			for (int i = 0; i < itemList.tagCount(); i++) {
				if (itemList.getCompoundTagAt(i) != null) {
					ItemStack currentItem = ItemStack.loadItemStackFromNBT(itemList.getCompoundTagAt(i));
					if ((currentItem != null) && (areItemsEqual(currentItem, filteredStack))) {
						return currentItem;
					}
				}
			}
		}
		return null;
	}

	public static boolean addFilteredStackToDankNull(ItemStack itemStackIn, ItemStack newStack) {
		ItemStack filteredStack = isFiltered(itemStackIn, newStack);
		if (filteredStack != null) {
			long maxStackSize = getDankNullMaxStackSize(itemStackIn);
			long currentFilteredStackSize = getDankNullStackSize(filteredStack);
			long itemToAddStackSize = newStack.stackSize;
			if (currentFilteredStackSize + itemToAddStackSize > maxStackSize) {
				setDankNullStackSize(filteredStack, maxStackSize);
			} else {
				setDankNullStackSize(filteredStack, currentFilteredStackSize + itemToAddStackSize);
			}
			return true;
		}
		return false;
	}

	public static void setDankNullStackSize(ItemStack dankNullStack, long amount) {
		if (isDankNullStack(dankNullStack)) {
			dankNullStack.getTagCompound().setLong("p455w0rd.StackSize", amount);
		}
	}

	public static ItemStack getDankNullStack(ItemStack itemStackIn, ItemStack filteredStack) {
		return null;
	}

	public static void decrDankNullStackSize(ItemStack dankNullStack, ItemStack itemStackIn, long amount) {
		long newStackSize = getDankNullStackSize(dankNullStack) - amount;
		NBTTagCompound nbtTC = dankNullStack.getTagCompound();
		if (newStackSize >= 1L) {
			nbtTC.setLong("p455w0rd.StackSize", newStackSize);
		} else {
			NBTTagList tagList = itemStackIn.getTagCompound().getTagList("danknull-inventory", 10);
			int index = getSelectedStackIndex(itemStackIn);
			tagList.removeTag(index);
			reArrangeStacks(itemStackIn);
		}
	}

	public static long getDankNullStackSize(ItemStack itemStackIn) {
		if (isDankNullStack(itemStackIn)) {
			return itemStackIn.getTagCompound().getLong("p455w0rd.StackSize");
		}
		return 0L;
	}

	public static boolean isDankNullStack(ItemStack itemStackIn) {
		return (itemStackIn.hasTagCompound()) && (itemStackIn.getTagCompound().hasKey("p455w0rd.StackSize"));
	}

	public static long getDankNullMaxStackSize(ItemStack itemStackIn) {
		int level = itemStackIn.getItemDamage() + 1;
		if (level == 6) {
			return Long.MAX_VALUE;
		}
		return level * (128 * level);
	}

	public static boolean hasUpgrade(ItemStack is, Globals.Upgrades upgrade) {
		if (is.hasTagCompound()) {
			NBTTagCompound nbt = is.getTagCompound();
			switch (upgrade) {
			case FLIGHT:
				return nbt.hasKey("PFlight");
			case STEPASSIST:
				return nbt.hasKey("PStep");
			case SPEED:
				return nbt.hasKey("PSpeed");
			case NIGHTVISION:
				return nbt.hasKey("PVision");
			default:
				break;
			}
		}
		return false;
	}

	public static void enableUpgrade(ItemStack is, Globals.Upgrades upgrade) {
		if (!hasUpgrade(is, upgrade)) {
			is.setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound nbt = is.getTagCompound();
		switch (upgrade) {
		case FLIGHT:
			nbt.setBoolean("PFlight", true);
			break;
		case STEPASSIST:
			nbt.setBoolean("PStep", true);
			break;
		case SPEED:
			nbt.setBoolean("PSpeed", true);
			break;
		case NIGHTVISION:
			nbt.setBoolean("PVision", true);
			break;
		default:
			break;
		}
	}

	public static void removeUpgrade(ItemStack is, Globals.Upgrades upgrade) {
		if (is.hasTagCompound()) {
			NBTTagCompound nbt = is.getTagCompound();
			switch (upgrade) {
			case FLIGHT:
				if (nbt.hasKey("PFlight")) {
					nbt.removeTag("PFlight");
				}
				break;
			case STEPASSIST:
				if (nbt.hasKey("PStep")) {
					nbt.removeTag("PStep");
				}
				break;
			case SPEED:
				if (nbt.hasKey("PSpeed")) {
					nbt.removeTag("PSpeed");
				}
				break;
			case NIGHTVISION:
				if (nbt.hasKey("PVision")) {
					nbt.removeTag("PVision");
				}
				break;
			default:
				break;
			}
		}
	}

	public static void disableUpgrade(ItemStack is, Globals.Upgrades upgrade) {
		if (isUpgradeEnabled(is, upgrade)) {
			NBTTagCompound nbt = is.getTagCompound();
			switch (upgrade) {
			case FLIGHT:
				nbt.setBoolean("PFlight", false);
				break;
			case STEPASSIST:
				nbt.setBoolean("PStep", false);
				break;
			case SPEED:
				nbt.setBoolean("PSpeed", false);
				break;
			case NIGHTVISION:
				nbt.setBoolean("PVision", false);
				break;
			default:
				break;
			}
		}
	}

	public static boolean isUpgradeEnabled(ItemStack is, Globals.Upgrades upgrade) {
		if (is.hasTagCompound()) {
			NBTTagCompound nbt = is.getTagCompound();
			switch (upgrade) {
			case FLIGHT:
				return (nbt.hasKey("PFlight")) && (nbt.getBoolean("PFlight"));
			case STEPASSIST:
				return (nbt.hasKey("PStep")) && (nbt.getBoolean("PStep"));
			case SPEED:
				return (nbt.hasKey("PSpeed")) && (nbt.getBoolean("PSpeed"));
			case NIGHTVISION:
				return (nbt.hasKey("PVision")) && (nbt.getBoolean("PVision"));
			default:
				break;
			}
		}
		return false;
	}

	public static boolean isItemUpgradeActive(ItemStack is, EntityEquipmentSlot type) {
		switch (type) {
		case HEAD:
			return (hasUpgrade(is, Globals.Upgrades.NIGHTVISION))
					&& (isUpgradeEnabled(is, Globals.Upgrades.NIGHTVISION));
		case CHEST:
			return (hasUpgrade(is, Globals.Upgrades.FLIGHT)) && (isUpgradeEnabled(is, Globals.Upgrades.FLIGHT));
		case LEGS:
			return (hasUpgrade(is, Globals.Upgrades.SPEED)) && (isUpgradeEnabled(is, Globals.Upgrades.SPEED));
		case FEET:
			return (hasUpgrade(is, Globals.Upgrades.STEPASSIST)) && (isUpgradeEnabled(is, Globals.Upgrades.STEPASSIST));
		case MAINHAND:
			break;
		case OFFHAND:
			break;
		}
		return false;
	}

	public static boolean areItemTagsEqual(ItemStack is1, ItemStack itemStackIn) {
		ItemStack newStack = is1.copy();
		if (newStack.hasTagCompound()) {
			if (newStack.getTagCompound().hasKey("p455w0rd.StackSize")) {
				newStack.getTagCompound().removeTag("p455w0rd.StackSize");
			}
			if (newStack.getTagCompound().hasNoTags()) {
				newStack.setTagCompound(null);
			}
		}
		return ItemStack.areItemStackTagsEqual(newStack, itemStackIn);
	}

	public static boolean areItemsEqual(ItemStack is1, ItemStack itemStackIn) {
		return (is1.getItem() == itemStackIn.getItem()) && (is1.getItemDamage() == itemStackIn.getItemDamage())
				&& (areItemTagsEqual(is1, itemStackIn));
	}

	public static void dropItemStackInWorld(World worldObj, double x, double y, double z, ItemStack stack) {
		float f = 0.7F;
		float d0 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		float d1 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		float d2 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		EntityItem entityitem = new EntityItem(worldObj, x + d0, y + d1, z + d2, stack);
		entityitem.setPickupDelay(10);
		if (stack.hasTagCompound()) {
			entityitem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
		}
		worldObj.spawnEntityInWorld(entityitem);
	}

	public static ItemArmor.ArmorMaterial addArmorMaterial(String enumName, String textureName, int durability,
			int[] reductionAmounts, int enchantability, SoundEvent soundOnEquip, float toughness) {
		return (ItemArmor.ArmorMaterial) EnumHelper.addEnum(ItemArmor.ArmorMaterial.class, enumName,
				new Class[] { String.class, Integer.TYPE, int[].class, Integer.TYPE, SoundEvent.class, Float.TYPE },
				new Object[] { textureName, Integer.valueOf(durability), reductionAmounts,
						Integer.valueOf(enchantability), soundOnEquip, Float.valueOf(toughness) });
	}
	
	public static boolean isCarbonChestplateEquipped(ItemStack chestPlate) {
		if (chestPlate != null) {
			if (chestPlate.getItem() instanceof ItemCarbonArmor) {
				ItemCarbonArmor carbonChestPlate = (ItemCarbonArmor) chestPlate.getItem();
				if (carbonChestPlate.type == EntityEquipmentSlot.CHEST) {
					return true;
				}
			}
		}
		return false;
	}
}
