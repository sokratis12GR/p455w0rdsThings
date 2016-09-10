package p455w0rd.p455w0rdsthings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import p455w0rd.p455w0rdsthings.entity.EntityEnderman2;

public class ModEntities {

	public static void init() {

		EntityRegistry.registerModEntity(EntityEnderman2.class, "enderman2", 0, P455w0rdsThings.INSTANCE, 80, 3, false,
				9725844, 0);

		EntitySpawnPlacementRegistry.setPlacementType(EntityEnderman2.class, SpawnPlacementType.ON_GROUND);
		EntityRegistry.addSpawn(EntityEnderman2.class, 5, 1, 2, EnumCreatureType.MONSTER, getBiomeList());
	}

	private static Biome[] getBiomeList() {

		List<Biome> biomes = new ArrayList<Biome>();
		Iterator<Biome> biomeList = Biome.REGISTRY.iterator();

		while (biomeList.hasNext()) {
			try {
				if (!((Biome) biomeList.next()).getBiomeName().equals("sky")) {
					biomes.add(biomeList.next());
				}
			} catch (NoSuchElementException localNoSuchElementException) {
			}
		}
		return (Biome[]) biomes.toArray(new Biome[biomes.size()]);
	}
}
