package p455w0rd.p455w0rdsthings.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import p455w0rd.p455w0rdsthings.proxy.ClientProxy;

public class BlockCarbon extends BlockBase implements IFuelHandler {
	
	public BlockCarbon() {
		super(Material.ROCK, MapColor.BLACK, "carbonBlock", 50.0F, 6000000.0F);
		setCreativeTab(ClientProxy.creativeTab);
		GameRegistry.registerFuelHandler(this);
	}

	public int getBurnTime(ItemStack fuel) {
		if (fuel.getItem() == ItemBlock.getItemFromBlock(this)) {
			return 16000;
		}
		return 0;
	}
}
