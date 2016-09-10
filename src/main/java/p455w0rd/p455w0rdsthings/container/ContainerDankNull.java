package p455w0rd.p455w0rdsthings.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import p455w0rd.p455w0rdsthings.inventory.InventoryDankNull;
import p455w0rd.p455w0rdsthings.inventory.slot.DankNullSlot;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

public class ContainerDankNull extends Container {
	
	InventoryPlayer inventoryPlayer;
	InventoryDankNull inventoryDankNull;
	ItemStack dankNullStack;
	int numRows = 0;
	int totalSlots = 0;

	public ContainerDankNull(EntityPlayer playerIn) {
		this.inventoryPlayer = playerIn.inventory;
		this.dankNullStack = ItemUtils.getDankNull(this.inventoryPlayer);
		this.inventoryDankNull = new InventoryDankNull(this.dankNullStack);
		this.numRows = (this.dankNullStack.getItemDamage() + 1);
		if (this.numRows > 6) {
			this.numRows -= 6;
		}
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(this.inventoryPlayer, i, i * 20 + (9 + i),
					90 + (this.numRows - 1) + (this.numRows * 20 + 6)));
			this.totalSlots += 1;
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(this.inventoryPlayer, j + i * 9 + 9, j * 20 + (9 + j),
						149 + (this.numRows - 1) + i - (6 - this.numRows) * 20 + i * 20));
				this.totalSlots += 1;
			}
		}
		for (int i = 0; i < this.numRows; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(
						new DankNullSlot(this.inventoryDankNull, j + i * 9, j * 20 + (9 + j), 19 + i + i * 20));
				this.totalSlots += 1;
			}
		}
		this.totalSlots -= 1;
	}

	public boolean canInteractWith(EntityPlayer playerIn) {
		return true;
	}

	public void onContainerClosed(EntityPlayer playerIn) {
		InventoryPlayer inventoryplayer = playerIn.inventory;
		if (inventoryplayer.getItemStack() != null) {
			playerIn.dropItem(inventoryplayer.getItemStack(), false);
			inventoryplayer.setItemStack((ItemStack) null);
		}
		ItemUtils.reArrangeStacks(this.dankNullStack);
	}

	private void arrangeSlots() {
		int invSize = this.inventoryDankNull.getSizeInventory();
		if (invSize <= 0) {
			return;
		}
		for (int i = 0; i < invSize; i++) {
			if (this.inventoryDankNull.getStackInSlot(i) == null) {
				for (int j = 0; j < invSize; j++) {
					if (j > i) {
						if (this.inventoryDankNull.getStackInSlot(j) != null) {
							this.inventoryDankNull.setInventorySlotContents(i,
									this.inventoryDankNull.getStackInSlot(j));
							this.inventoryDankNull.decrStackSize(j, 1);
							break;
						}
					}
				}
			}
		}
		this.inventoryDankNull.markDirty();
	}

	public static boolean canAddItemToSlot(Slot slotIn, ItemStack stack, boolean stackSizeMatters) {
		return true;
	}

	public boolean canDragIntoSlot(Slot slotIn) {
		if ((slotIn.inventory instanceof InventoryDankNull)) {
			return false;
		}
		return true;
	}

	public Slot getSlotFromInventory(IInventory inv, int slotIn) {
		for (int i = 0; i < this.inventorySlots.size(); i++) {
			Slot slot = (Slot) this.inventorySlots.get(i);
			if (slot.isHere(inv, slotIn)) {
				return slot;
			}
		}
		return null;
	}

	private int getNextAvailableSlot() {
		for (int i = 36; i <= this.totalSlots; i++) {
			Slot s = (Slot) this.inventorySlots.get(i);
			if (s != null) {
				if (s.getStack() == null) {
					return i;
				}
			}
		}
		return -1;
	}

	private boolean isStackAlreadyAdded(ItemStack itemStackIn) {
		for (int i = 36; i <= this.totalSlots; i++) {
			ItemStack dankNullStack = ((Slot) this.inventorySlots.get(i)).getStack();
			if (dankNullStack != null) {
				if (ItemUtils.areItemsEqual(dankNullStack, itemStackIn)) {
					return true;
				}
			}
		}
		return false;
	}

	private int getStackSlot(ItemStack itemStackIn) {
		for (int i = 36; i <= this.totalSlots; i++) {
			Slot s = (Slot) this.inventorySlots.get(i);
			if ((s != null) && (s.getStack() != null)) {
				if (ItemUtils.areItemsEqual(s.getStack(), itemStackIn)) {
					return i;
				}
			}
		}
		return -1;
	}

	private ItemStack addStack(ItemStack heldStack, boolean isSlotEmpty, EntityPlayer player) {
		InventoryPlayer inventoryplayer = player.inventory;
		long maxStackSize = ItemUtils.getDankNullMaxStackSize(this.dankNullStack);
		if (isSlotEmpty) {
			ItemStack newStack = heldStack.copy();
			newStack.setItemDamage(heldStack.getItemDamage());
			if (!isStackAlreadyAdded(heldStack)) {
				if (!newStack.hasTagCompound()) {
					newStack.setTagCompound(new NBTTagCompound());
				}
				NBTTagCompound nbtTC = newStack.getTagCompound();
				nbtTC.setLong("p455w0rd.StackSize", heldStack.stackSize);
				newStack.setTagCompound(nbtTC);
				if (getNextAvailableSlot() != -1) {
					newStack.stackSize = 1;
					((Slot) this.inventorySlots.get(getNextAvailableSlot())).putStack(newStack);
					inventoryplayer.setItemStack(null);
				}
			}
			else if (getStackSlot(heldStack) != -1) {
				ItemStack slotItem = ((Slot) this.inventorySlots.get(getStackSlot(heldStack))).getStack();
				long currentStackSize = slotItem.getTagCompound().getLong("p455w0rd.StackSize");
				long additionalStackSize = heldStack.stackSize;
				long finalStackSize = currentStackSize + additionalStackSize;
				if (finalStackSize <= maxStackSize) {
					slotItem.getTagCompound().setLong("p455w0rd.StackSize", finalStackSize);
				}
				else {
					slotItem.getTagCompound().setLong("p455w0rd.StackSize", maxStackSize);
				}
				inventoryplayer.setItemStack(null);
			}
			return heldStack;
		}
		ItemStack newStack = heldStack.copy();
		if (!isStackAlreadyAdded(heldStack)) {
			if (!newStack.hasTagCompound()) {
				newStack.setTagCompound(new NBTTagCompound());
			}
			NBTTagCompound nbtTC = newStack.getTagCompound();
			nbtTC.setLong("p455w0rd.StackSize", heldStack.stackSize);
			newStack.setTagCompound(nbtTC);
			if (getNextAvailableSlot() != -1) {
				newStack.stackSize = 1;
				((Slot) this.inventorySlots.get(getNextAvailableSlot())).putStack(newStack);
				inventoryplayer.setItemStack(null);
			}
			else {
				return heldStack;
			}
		}
		else if (getStackSlot(heldStack) != -1) {
			ItemStack slotItem = ((Slot) this.inventorySlots.get(getStackSlot(heldStack))).getStack();
			long currentStackSize = slotItem.getTagCompound().getLong("p455w0rd.StackSize");
			long additionalStackSize = heldStack.stackSize;
			long finalStackSize = currentStackSize + additionalStackSize;
			if (finalStackSize <= maxStackSize) {
				slotItem.getTagCompound().setLong("p455w0rd.StackSize", finalStackSize);
			}
			else {
				slotItem.getTagCompound().setLong("p455w0rd.StackSize", maxStackSize);
			}
			inventoryplayer.setItemStack(null);
		}
		return heldStack;
	}

	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		Slot clickSlot = (Slot) this.inventorySlots.get(index);
		if (clickSlot.getHasStack()) {
			if (!isDankNullSlot(index)) {
				if ((getNextAvailableSlot() == -1) && (!isStackAlreadyAdded(clickSlot.getStack()))) {
					if (!moveStackWithinInventory(clickSlot.getStack(), index)) {
						return null;
					}
					this.inventoryDankNull.markDirty();
					return clickSlot.getStack();
				}
				addStack(clickSlot.getStack(), true, playerIn);
				clickSlot.putStack((ItemStack) null);
				playerIn.inventory.markDirty();
				ItemUtils.setSelectedStackIndex(this.dankNullStack, 0);
				this.inventoryDankNull.markDirty();
			}
			else {
				ItemStack newStack = clickSlot.getStack().copy();
				int realMaxStackSize = newStack.getMaxStackSize();
				long currentStackSize = ItemUtils.getDankNullStackSize(newStack);
				newStack.getTagCompound().removeTag("p455w0rd.StackSize");
				if (newStack.getTagCompound().hasNoTags()) {
					newStack.setTagCompound(null);
				}
				if (currentStackSize > realMaxStackSize) {
					newStack.stackSize = realMaxStackSize;
					if (moveStackToInventory(newStack)) {
						ItemUtils.decrDankNullStackSize(clickSlot.getStack(), this.dankNullStack, realMaxStackSize);
					}
				}
				else {
					newStack.stackSize = ((int) currentStackSize);
					if (moveStackToInventory(newStack)) {
						ItemUtils.decrDankNullStackSize(clickSlot.getStack(), this.dankNullStack, currentStackSize);
						clickSlot.putStack(null);
					}
					arrangeSlots();
					ItemUtils.setSelectedStackIndex(this.dankNullStack, 0);
				}
			}
		}
		return null;
	}

	private boolean isInHotbar(int index) {
		return (index >= 0) && (index <= 8);
	}

	private boolean isInInventory(int index) {
		return (index >= 9) && (index <= 36);
	}

	private boolean moveStackWithinInventory(ItemStack itemStackIn, int index) {
		if (isInHotbar(index)) {
			for (int i = 9; i <= 36; i++) {
				Slot possiblyOpenSlot = (Slot) this.inventorySlots.get(i);
				if (!possiblyOpenSlot.getHasStack()) {
					possiblyOpenSlot.putStack(itemStackIn);
					((Slot) this.inventorySlots.get(index)).putStack(null);
					this.inventoryPlayer.markDirty();
					return true;
				}
			}
		}
		else if (isInInventory(index)) {
			for (int i = 0; i <= 8; i++) {
				Slot possiblyOpenSlot = (Slot) this.inventorySlots.get(i);
				if (!possiblyOpenSlot.getHasStack()) {
					possiblyOpenSlot.putStack(itemStackIn);
					((Slot) this.inventorySlots.get(index)).putStack(null);
					this.inventoryPlayer.markDirty();
					return true;
				}
			}
		}
		return false;
	}

	protected boolean moveStackToInventory(ItemStack itemStackIn) {
		for (int i = 0; i <= 36; i++) {
			Slot possiblyOpenSlot = (Slot) this.inventorySlots.get(i);
			if (!possiblyOpenSlot.getHasStack()) {
				possiblyOpenSlot.putStack(itemStackIn);
				return true;
			}
		}
		return false;
	}

	private boolean isDankNullSlot(int index) {
		return index >= 36;
	}

	private int getDankNullIndex(int index) {
		return index - 36;
	}

	public ItemStack slotClick(int index, int dragType, ClickType clickTypeIn, EntityPlayer player) {
		InventoryPlayer inventoryplayer = player.inventory;

		ItemStack heldStack = inventoryplayer.getItemStack();
		if (index == 64537 || index == -999) {
			if (inventoryplayer.getItemStack() != null) {
				if (dragType == 0) {
					player.dropItem(inventoryplayer.getItemStack(), true);
					inventoryplayer.setItemStack((ItemStack) null);
				}
				if (dragType == 1) {
					player.dropItem(inventoryplayer.getItemStack().splitStack(1), true);
					if (inventoryplayer.getItemStack().stackSize == 0) {
						inventoryplayer.setItemStack((ItemStack) null);
					}
				}
			}
			return heldStack;
		}
		if (isDankNullSlot(index)) {
			Slot s = (Slot) this.inventorySlots.get(index);

			ItemStack thisStack = s.getStack();
			if ((thisStack != null) && ((thisStack.getItem() instanceof ItemDankNull))) {
				return null;
			}
			if (index == -1) {
				return heldStack;
			}
			if (heldStack != null) {
				if (thisStack == null) {
					addStack(heldStack, true, player);
				}
				else {
					addStack(heldStack, false, player);
				}
				return heldStack;
			}
			if ((heldStack == null) && (thisStack != null)) {
				if (dragType == 0) {
					if (((clickTypeIn == ClickType.PICKUP) || (clickTypeIn == ClickType.QUICK_MOVE)) && (dragType == 0)
							&& (clickTypeIn == ClickType.QUICK_MOVE)) {
						if (index < 0) {
							return null;
						}
						return transferStackInSlot(player, index);
					}
					long currentStackSize = thisStack.getTagCompound().getLong("p455w0rd.StackSize");
					int thisStackMaxStackSize = thisStack.getMaxStackSize();
					if ((thisStack.stackSize == 0) || (currentStackSize == 0L)) {
						this.inventoryDankNull.setInventorySlotContents(getDankNullIndex(index), null);
						inventoryplayer.setItemStack(null);
						this.inventoryDankNull.markDirty();
						return null;
					}
					if (currentStackSize > thisStackMaxStackSize) {
						ItemUtils.decrDankNullStackSize(thisStack, this.dankNullStack, thisStackMaxStackSize);
						ItemStack newStack = thisStack.copy();
						newStack.getTagCompound().removeTag("p455w0rd.StackSize");
						if (newStack.getTagCompound().hasNoTags()) {
							newStack.setTagCompound(null);
						}
						newStack.stackSize = thisStackMaxStackSize;
						inventoryplayer.setItemStack(newStack);
						this.inventoryDankNull.markDirty();
					}
					else {
						this.inventoryDankNull.setInventorySlotContents(getDankNullIndex(index), null);
						this.inventoryDankNull.markDirty();
						ItemStack newStack = thisStack.copy();
						newStack.getTagCompound().removeTag("p455w0rd.StackSize");
						if (newStack.getTagCompound().hasNoTags()) {
							newStack.setTagCompound(null);
						}
						newStack.stackSize = ((int) currentStackSize);
						inventoryplayer.setItemStack(newStack);
					}
				}
				arrangeSlots();
				if (ItemUtils.getSelectedStack(this.dankNullStack) == null) {
					ItemUtils.setSelectedStackIndex(this.dankNullStack, 0);
				}
				return thisStack;
			}
		}
		else if ((index != -1) && (index != 64537)) {
			Slot s = (Slot) this.inventorySlots.get(index);
			if (s.getStack() != null) {
				if (!(s.getStack().getItem() instanceof ItemDankNull)) {
					return super.slotClick(index, dragType, clickTypeIn, player);
				}
			}
			else {
				return super.slotClick(index, dragType, clickTypeIn, player);
			}
		}
		else {
			return super.slotClick(index, dragType, clickTypeIn, player);
		}
		return null;
	}
}
