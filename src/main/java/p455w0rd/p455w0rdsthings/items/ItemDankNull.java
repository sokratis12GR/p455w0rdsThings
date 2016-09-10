package p455w0rd.p455w0rdsthings.items;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.client.render.DankNullRenderer;
import p455w0rd.p455w0rdsthings.client.render.PModelRegistryHelper;
import p455w0rd.p455w0rdsthings.handlers.GuiHandler;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

@SuppressWarnings("deprecation")
public class ItemDankNull extends Item {

	public ItemDankNull() {
		setRegistryName("dankNull");
		setUnlocalizedName("dankNull");
		GameRegistry.register(this);
		setMaxStackSize(1);
		setMaxDamage(0);
	}

	@SideOnly(Side.CLIENT)
	public boolean hasEffect(ItemStack stack) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	public void initModel() {
		for (int i = 0; i < 12; i++) {
			PModelRegistryHelper.registerDankNullRenderer(this, new DankNullRenderer(), i);
		}
	}

	
	public String getItemStackDisplayName(ItemStack stack) {
		return I18n.translateToLocal(getUnlocalizedNameInefficiently(stack) + getDamage(stack) + ".name").trim();
	}

	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn,
			EnumHand hand) {
		if (playerIn.isSneaking()) {
			GuiHandler.launchGui(Globals.GUINUM_DANKNULL, playerIn, worldIn, (int) playerIn.posX, (int) playerIn.posY,
					(int) playerIn.posZ);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemStackIn);
	}

	@SideOnly(Side.CLIENT)
	public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
		for (int i = 0; i < 12; i++) {
			subItems.add(new ItemStack(itemIn, 1, i));
		}
	}

	public boolean getHasSubtypes() {
		return true;
	}

	public boolean isDamaged(ItemStack stack) {
		return false;
	}

	public boolean isRepairable() {
		return false;
	}

	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	public boolean isDamageable() {
		return false;
	}

	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote) {
			return EnumActionResult.FAIL;
		}
		ItemStack selectedStack = ItemUtils.getSelectedStack(stack);
		if (playerIn.isSneaking()) {
			if ((selectedStack == null) && (ItemUtils.getItemCount(stack) > 0)) {
				ItemUtils.setSelectedStackIndex(stack, 0);
			}
			GuiHandler.launchGui(Globals.GUINUM_DANKNULL, playerIn, worldIn, (int) playerIn.posX, (int) playerIn.posY,
					(int) playerIn.posZ);
		} else {
			if (selectedStack == null) {
				return EnumActionResult.FAIL;
			}
			if (!(selectedStack.getItem() instanceof ItemBlock)) {
				return EnumActionResult.FAIL;
			}
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			if ((block.isReplaceable(worldIn, pos)) && (block == Blocks.SNOW_LAYER)) {
				facing = EnumFacing.UP;
			} else if (!block.isReplaceable(worldIn, pos)) {
				pos = pos.offset(facing);
			}
			if ((ItemUtils.getSelectedStackSize(stack) > 0L) && (playerIn.canPlayerEdit(pos, facing, stack))) {
				pos = pos.offset(facing.getOpposite());
				EnumActionResult result = selectedStack.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY,
						hitZ);
				if (!playerIn.capabilities.isCreativeMode) {
					ItemUtils.decrSelectedStackSize(stack, 1L);
				}
				return result;
			}
		}
		return EnumActionResult.FAIL;
	}

	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
	}
}
