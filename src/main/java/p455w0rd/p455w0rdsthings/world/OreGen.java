package p455w0rd.p455w0rdsthings.world;

import java.util.Random;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import p455w0rd.p455w0rdsthings.ModBlocks;

public class OreGen implements IWorldGenerator
{
  private WorldGenerator genNetherCarbonOre;
  
  public OreGen()
  {
    this.genNetherCarbonOre = new WorldGenMinable(ModBlocks.netherCarbonOreBlock.getDefaultState(), 5, BlockMatcher.forBlock(Blocks.NETHERRACK));
  }
  
  public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
  {
    if (world.provider.getDimension() == -1) {
      for (int i = 0; i < 5; i++)
      {
        int x = chunkX * 16 + random.nextInt(16);
        int y = 0 + random.nextInt(255);
        int z = chunkZ * 16 + random.nextInt(16);
        this.genNetherCarbonOre.generate(world, random, new BlockPos(x, y, z));
      }
    }
  }
}


