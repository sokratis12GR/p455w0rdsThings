package p455w0rd.p455w0rdsthings.inventory;

import java.util.Iterator;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class InventoryDankNull implements IInventory, Iterable<ItemStack> {
	
	private int size = 54;
	private final ItemStack[] inv;
	private boolean enableClientEvents = false;
	private ItemStack dankNullStack;
	private int numRows = 0;

	public InventoryDankNull(ItemStack itemStackIn) {
		this.dankNullStack = itemStackIn;
		this.numRows = (itemStackIn.getItemDamage() + 1);
		this.size = (this.numRows * 9);
		this.inv = new ItemStack[this.size];
		readFromNBT();
	}

	public boolean isEmpty() {
		for (int x = 0; x < this.size; x++) {
			if (getStackInSlot(x) != null) {
				return false;
			}
		}
		return true;
	}

	public int getSizeInventory() {
		return this.size;
	}

	public ItemStack getStackInSlot(int var1) {
		return this.inv[var1];
	}

	public ItemStack decrStackSize(int slot, int qty) {
		if (this.inv[slot] != null) {
			ItemStack split = getStackInSlot(slot);
			ItemStack ns = null;
			if (qty >= split.stackSize) {
				ns = this.inv[slot];
				this.inv[slot] = null;
			}
			else {
				ns = split.splitStack(qty);
			}
			markDirty();
			return ns;
		}
		return null;
	}

	public ItemStack getStack() {
		return this.dankNullStack != null ? this.dankNullStack : null;
	}

	protected boolean eventsEnabled() {
		return (FMLCommonHandler.instance().getSide() == Side.SERVER) || (isEnableClientEvents());
	}

	public static boolean isSameItem(@Nullable ItemStack left, @Nullable ItemStack right) {
		return (left != null) && (right != null) && (left.isItemEqual(right));
	}

	public void setInventorySlotContents(int slot, ItemStack newItemStack) {
		ItemStack oldStack = this.inv[slot];
		this.inv[slot] = newItemStack;
		if (eventsEnabled()) {
			ItemStack removed = oldStack;
			ItemStack added = newItemStack;
			if ((oldStack != null) && (newItemStack != null) && (isSameItem(oldStack, newItemStack))) {
				if (oldStack.stackSize > newItemStack.stackSize) {
					removed = removed.copy();
					removed.stackSize -= newItemStack.stackSize;
					added = null;
				}
				else if (oldStack.stackSize < newItemStack.stackSize) {
					added = added.copy();
					added.stackSize -= oldStack.stackSize;
					removed = null;
				}
				else {
					removed = added = null;
				}
			}
			markDirty();
		}
	}

	public int getInventoryStackLimit() {
		return Integer.MAX_VALUE;
	}

	public void markDirty() {
		writeToNBT();
	}

	public boolean isUseableByPlayer(EntityPlayer var1) {
		return true;
	}

	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	public void setMaxStackSize(int s) {
	}

	public Iterator<ItemStack> iterator() {
		return new InvIterator(this);
	}

	private boolean isEnableClientEvents() {
		return this.enableClientEvents;
	}

	public void setEnableClientEvents(boolean enableClientEvents) {
		this.enableClientEvents = enableClientEvents;
	}

	public String getName() {
		return "danknull-inventory";
	}

	public boolean hasCustomName() {
		return true;
	}

	public ITextComponent getDisplayName() {
		return new TextComponentString(getName());
	}

	public ItemStack removeStackFromSlot(int index) {
		ItemStack itemStack = getStackInSlot(index);
		if (itemStack != null) {
			setInventorySlotContents(index, null);
		}
		return itemStack;
	}

	public void openInventory(EntityPlayer player) {
	}

	public void closeInventory(EntityPlayer player) {
	}

	public int getField(int id) {
		return 0;
	}

	public void setField(int id, int value) {
	}

	public int getFieldCount() {
		return 0;
	}

	public void clear() {
	}

	private void writeToNBT() {
		if (getStack() == null) {
			return;
		}
		if (!getStack().hasTagCompound()) {
			getStack().setTagCompound(new NBTTagCompound());
		}
		NBTTagCompound itemTC = getStack().getTagCompound();
		NBTTagList nbtTL = new NBTTagList();
		for (int i = 0; i < this.inv.length; i++) {
			if (this.inv[i] != null) {
				NBTTagCompound nbtTC = new NBTTagCompound();
				nbtTC.setInteger("Slot", i);
				this.inv[i].writeToNBT(nbtTC);
				nbtTL.appendTag(nbtTC);
			}
		}
		itemTC.setTag(getName(), nbtTL);
	}

	private void readFromNBT() {
		if ((getStack() == null) || (getStack().getTagCompound() == null)) {
			return;
		}
		NBTTagCompound itemTC = getStack().getTagCompound();
		NBTTagList nbtTL = itemTC.getTagList(getName(), 10);
		for (int i = 0; i < nbtTL.tagCount(); i++) {
			NBTTagCompound nbtTC = nbtTL.getCompoundTagAt(i);
			if (nbtTC != null) {
				int slot = nbtTC.getInteger("Slot");
				this.inv[slot] = ItemStack.loadItemStackFromNBT(nbtTC);
			}
		}
	}
}
