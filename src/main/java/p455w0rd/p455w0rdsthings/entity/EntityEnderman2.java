package p455w0rd.p455w0rdsthings.entity;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityEnderman2 extends EntityEnderman {
	
	private int targetChangeTime = 0;

	public EntityEnderman2(World worldIn) {
		super(worldIn);
	}

	private boolean shouldAttackPlayer(EntityPlayer player) {
		ItemStack itemstack = player.inventory.armorInventory[3];
		if ((itemstack != null) && (itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN))) {
			return false;
		}
		Vec3d vec3d = player.getLook(1.0F).normalize();
		Vec3d vec3d1 = new Vec3d(this.posX - player.posX,
				getEntityBoundingBox().minY + getEyeHeight() - (player.posY + player.getEyeHeight()),
				this.posZ - player.posZ);
		double d0 = vec3d1.lengthVector();
		vec3d1 = vec3d1.normalize();
		double d1 = vec3d.dotProduct(vec3d1);
		return d1 > 1.0D - 0.025D / d0 ? player.canEntityBeSeen(this) : false;
	}

	protected void updateAITasks() {
		//if (isWet()) {
		//	attackEntityFrom(DamageSource.drown, 1.0F);
		//}
		if ((this.worldObj.isDaytime()) && (this.ticksExisted >= this.targetChangeTime + 600)) {
			float f = getBrightness(1.0F);
			if ((f <= 0.5F) || (!this.worldObj.canSeeSky(new BlockPos(this)))
					|| (this.rand.nextFloat() * 30.0F >= (f - 0.4F) * 2.0F)) {
			}
		}
	}

	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (isEntityInvulnerable(source)) {
			return false;
		}
		if ((source instanceof EntityDamageSourceIndirect)) {
			for (int i = 0; i < 64; i++) {
			}
			return false;
		}
		boolean flag = super.attackEntityFrom(source, amount);
		if ((source.isUnblockable()) && (this.rand.nextInt(10) != 0)) {
			teleportRandomly();
		}
		return flag;
	}

	protected boolean isValidLightLevel() {
		return true;
	}

	public boolean getCanSpawnHere() {
		return true;
	}

	static class AIFindPlayer extends EntityAINearestAttackableTarget<EntityPlayer> {
		private final EntityEnderman2 enderman;
		private EntityPlayer player;
		private int aggroTime;
		private int teleportTime;

		public AIFindPlayer(EntityEnderman2 p_i45842_1_) {
			super(p_i45842_1_, EntityPlayer.class, false);
			this.enderman = p_i45842_1_;
		}

		@SuppressWarnings({
				"unchecked", "rawtypes"
		})
		public boolean shouldExecute() {
			double d0 = getTargetDistance();
			this.player = this.enderman.worldObj.getNearestAttackablePlayer(this.enderman.posX, this.enderman.posY,
					this.enderman.posZ, d0, d0, (Function) null, new Predicate<EntityPlayer>() {
						@Override
						public boolean apply(@Nullable EntityPlayer player) {
							return (player != null) && (AIFindPlayer.this.enderman.shouldAttackPlayer(player));
						}

					});
			return this.player != null;
		}

		public void startExecuting() {
			this.aggroTime = 5;
			this.teleportTime = 0;
		}

		public void resetTask() {
			this.player = null;
			super.resetTask();
		}

		public boolean continueExecuting() {
			if (this.player != null) {
				if (!this.enderman.shouldAttackPlayer(this.player)) {
					return false;
				}
				this.enderman.faceEntity(this.player, 10.0F, 10.0F);
				return true;
			}
			return (this.targetEntity != null) && (((EntityPlayer) this.targetEntity).isEntityAlive()) ? true
					: super.continueExecuting();
		}

		public void updateTask() {
			if (this.player != null) {
				if (--this.aggroTime <= 0) {
					this.targetEntity = this.player;
					this.player = null;
					super.startExecuting();
				}
			}
			else {
				if (this.targetEntity != null) {
					if (this.enderman.shouldAttackPlayer((EntityPlayer) this.targetEntity)) {
						if (((EntityPlayer) this.targetEntity).getDistanceSqToEntity(this.enderman) < 16.0D) {
						}
						this.teleportTime = 0;
					}
					else if ((((EntityPlayer) this.targetEntity).getDistanceSqToEntity(this.enderman) > 256.0D)
							&& (this.teleportTime++ >= 30) && (this.enderman.teleportToEntity(this.targetEntity))) {
						this.teleportTime = 0;
					}
				}
				super.updateTask();
			}
		}
	}

	static class AIPlaceBlock extends EntityAIBase {
		private final EntityEnderman2 enderman;

		public AIPlaceBlock(EntityEnderman2 p_i45843_1_) {
			this.enderman = p_i45843_1_;
		}

		public boolean shouldExecute() {
			return this.enderman.getHeldBlockState() != null;
		}

		public void updateTask() {
			Random random = this.enderman.getRNG();
			World world = this.enderman.worldObj;
			int i = MathHelper.floor_double(this.enderman.posX - 1.0D + random.nextDouble() * 2.0D);
			int j = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 2.0D);
			int k = MathHelper.floor_double(this.enderman.posZ - 1.0D + random.nextDouble() * 2.0D);
			BlockPos blockpos = new BlockPos(i, j, k);
			IBlockState iblockstate = world.getBlockState(blockpos);
			IBlockState iblockstate1 = world.getBlockState(blockpos.down());
			IBlockState iblockstate2 = this.enderman.getHeldBlockState();
			if ((iblockstate2 != null)
					&& (canPlaceBlock(world, blockpos, iblockstate2.getBlock(), iblockstate, iblockstate1))) {
				world.setBlockState(blockpos, iblockstate2, 3);
				this.enderman.setHeldBlockState((IBlockState) null);
			}
		}

		private boolean canPlaceBlock(World p_188518_1_, BlockPos p_188518_2_, Block p_188518_3_,
				IBlockState p_188518_4_, IBlockState p_188518_5_) {
			return p_188518_5_.getMaterial() == Material.AIR ? false
					: p_188518_4_.getMaterial() != Material.AIR ? false
							: !p_188518_3_.canPlaceBlockAt(p_188518_1_, p_188518_2_) ? false : p_188518_5_.isFullCube();
		}
	}
}
