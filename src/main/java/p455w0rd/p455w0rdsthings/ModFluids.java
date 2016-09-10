package p455w0rd.p455w0rdsthings;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.IFluidBlock;

public class ModFluids {
	
	public static Fluid xpJuice;
	public static final Set<Fluid> fluids = new HashSet<Fluid>();
	public static final Set<IFluidBlock> modFluidBlocks = new HashSet<IFluidBlock>();

	public static void init() {
		xpJuice = createFluid("xpjuice", true);
	}

	private static Fluid createFluid(String name, boolean hasFlowIcon) {
		String texturePrefix = "p455w0rdsthings:blocks/";

		ResourceLocation still = new ResourceLocation(texturePrefix + name + "_still");
		ResourceLocation flowing = hasFlowIcon ? new ResourceLocation(texturePrefix + name + "_flow") : still;

		Fluid fluid = new Fluid(name, still, flowing);
		boolean useOwnFluid = FluidRegistry.registerFluid(fluid);
		if (!useOwnFluid) {
			fluid = FluidRegistry.getFluid(name);
		}
		fluids.add(fluid);

		return fluid;
	}
}
