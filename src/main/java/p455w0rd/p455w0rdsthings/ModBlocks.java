package p455w0rd.p455w0rdsthings;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.blocks.BlockCarbon;
import p455w0rd.p455w0rdsthings.blocks.BlockNetherCarbonOre;

public class ModBlocks {
	
	public static BlockCarbon carbonBlock;
	public static BlockNetherCarbonOre netherCarbonOreBlock;

	public static void init() {
		carbonBlock = new BlockCarbon();
		netherCarbonOreBlock = new BlockNetherCarbonOre();
	}

	@SideOnly(Side.CLIENT)
	public static void initModels() {
		carbonBlock.initModel();
		netherCarbonOreBlock.initModel();
	}
}
