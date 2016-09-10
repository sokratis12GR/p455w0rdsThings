package p455w0rd.p455w0rdsthings.materials;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import p455w0rd.p455w0rdsthings.util.ReflectionUtils;

public class MaterialLiquidXp extends MaterialLiquid {
	
	public MaterialLiquidXp(MapColor color) {
		super(color);
		setReplaceable();
		setNoPushMobility();
		try {
			ReflectionUtils.findMethod(Material.class, new String[] {
					"setTranslucent", "func_76223_p"
			}, new Class[0]).invoke(true);
		}
		catch (Throwable e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean isLiquid() {
		return true;
	}

	public boolean blocksMovement() {
		return false;
	}

	public boolean isSolid() {
		return false;
	}
}
