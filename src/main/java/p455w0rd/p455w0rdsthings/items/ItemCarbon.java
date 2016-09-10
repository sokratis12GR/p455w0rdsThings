package p455w0rd.p455w0rdsthings.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCarbon extends ItemBase implements IFuelHandler {
	
	public ItemCarbon() {
		super("rawCarbon");
		GameRegistry.registerFuelHandler(this);
	}

	public int getBurnTime(ItemStack fuel) {
		if (fuel.getItem() == this) {
			return 10000;
		}
		return 0;
	}
}
