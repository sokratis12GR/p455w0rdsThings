package p455w0rd.p455w0rdsthings.inventory;

import java.util.Iterator;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class InvIterator implements Iterator<ItemStack> {
	
	private final IInventory inventory;
	private final int size;
	private int counter = 0;

	public InvIterator(IInventory inventory) {
		this.inventory = inventory;
		this.size = this.inventory.getSizeInventory();
	}

	public boolean hasNext() {
		return this.counter < this.size;
	}

	public ItemStack next() {
		ItemStack result = this.inventory.getStackInSlot(this.counter);
		this.counter += 1;

		return result;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}
