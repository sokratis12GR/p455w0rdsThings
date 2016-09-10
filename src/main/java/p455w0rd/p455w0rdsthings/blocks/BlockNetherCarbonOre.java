package p455w0rd.p455w0rdsthings.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import p455w0rd.p455w0rdsthings.ModItems;
import p455w0rd.p455w0rdsthings.proxy.ClientProxy;

public class BlockNetherCarbonOre extends BlockBase {
	EntityPlayer player;

	public BlockNetherCarbonOre() {
		super(Material.ROCK, MapColor.BLACK, "netherCarbonOreBlock", 5.0F, 6.0F);
		setCreativeTab(ClientProxy.creativeTab);
		setHarvestLevel("pickaxe", 3);
	}

	public int quantityDropped(IBlockState blockstate, int fortune, Random random) {
		return 1 + random.nextInt(3 + fortune);
	}

	public void harvestBlock(World worldIn, EntityPlayer p, BlockPos pos, IBlockState state, @Nullable TileEntity te,
			@Nullable ItemStack stack) {
		this.player = p;
		if ((!canSilkHarvest(worldIn, pos, state, this.player))
				|| (EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) <= 0)) {
			dropXpOnBlockBreak(worldIn, pos, 3 + worldIn.rand.nextInt(5));
		}
		super.harvestBlock(worldIn, p, pos, state, te, stack);
	}

	public void dropXpOnBlockBreak(World worldIn, BlockPos pos, int amt) {
		if (!worldIn.isRemote) {
			while (amt > 0) {
				int i = EntityXPOrb.getXPSplit(amt);
				amt -= i;
				worldIn.spawnEntityInWorld(
						new EntityXPOrb(worldIn, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, i));
			}
		}
	}

	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> ret = new ArrayList<ItemStack>();
		Item item = ModItems.rawCarbon;
		Random rand = (world instanceof World) ? ((World) world).rand : RANDOM;
		int count = quantityDropped(state, fortune, rand);
		for (int i = 0; i < count; i++) {
			ret.add(new ItemStack(item, 1, damageDropped(state)));
		}
		return ret;
	}
}
