package p455w0rd.p455w0rdsthings.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.client.event.GuiScreenEvent.MouseInputEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.ModAchievements;
import p455w0rd.p455w0rdsthings.ModItems;
import p455w0rd.p455w0rdsthings.items.ItemCarbonArmor;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.util.CapeUtils;
import p455w0rd.p455w0rdsthings.util.ItemUtils;
import p455w0rd.p455w0rdsthings.util.PotionUtils;

public class EventsHandler {

	@SubscribeEvent
	public void attackEvent(LivingAttackEvent e) {
		float damage = e.getAmount();
		if (!(e.getEntityLiving() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) e.getEntityLiving();
		if (player.getActiveItemStack() == null) {
			return;
		}
		ItemStack activeItemStack = player.getActiveItemStack();
		if ((damage > 0.0F) && (activeItemStack != null) && ((activeItemStack.getItem() instanceof ItemShield))) {
			int i = 1 + MathHelper.floor_float(damage);
			activeItemStack.damageItem(i, player);
			if (activeItemStack.stackSize <= 0) {
				EnumHand enumhand = player.getActiveHand();
				ForgeEventFactory.onPlayerDestroyItem(player, activeItemStack, enumhand);
				if (enumhand == EnumHand.MAIN_HAND) {
					player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, (ItemStack) null);
				}
				else {
					player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, (ItemStack) null);
				}
				activeItemStack = null;
				if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
					player.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + player.worldObj.rand.nextFloat() * 0.4F);
				}
			}
		}
	}

	@SubscribeEvent
	public void onItemPickUp(EntityItemPickupEvent e) {
		EntityPlayer player = e.getEntityPlayer();
		ItemStack entityStack = e.getItem().getEntityItem();
		if ((entityStack == null) || (player == null)) {
			return;
		}
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			if ((stack != null) && ((stack.getItem() instanceof ItemDankNull)) && (ItemUtils.isFiltered(stack, entityStack) != null) && (ItemUtils.addFilteredStackToDankNull(stack, entityStack))) {
				entityStack.stackSize = 0;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if ((event.getEntity() instanceof AbstractClientPlayer)) {
			CapeUtils.queuePlayerCapeReplacement((AbstractClientPlayer) event.getEntity());
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onMouseEvent(MouseEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		ItemStack dankNullItem = null;
		if (player.getHeldItem(EnumHand.MAIN_HAND) != null) {
			if ((player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemDankNull)) {
				dankNullItem = player.getHeldItem(EnumHand.MAIN_HAND);
			}
			else if ((player.getHeldItem(EnumHand.OFF_HAND) != null) && ((player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemDankNull))) {
				dankNullItem = player.getHeldItem(EnumHand.OFF_HAND);
			}
		}
		else if (player.getHeldItem(EnumHand.OFF_HAND) != null) {
			if ((player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemDankNull)) {
				dankNullItem = player.getHeldItem(EnumHand.OFF_HAND);
			}
			else if ((player.getHeldItem(EnumHand.MAIN_HAND) != null) && ((player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemDankNull))) {
				dankNullItem = player.getHeldItem(EnumHand.MAIN_HAND);
			}
		}
		if (dankNullItem == null) {
			return;
		}
		if ((event.getDwheel() == 0) && (event.isButtonstate())) {
			int currentIndex = ItemUtils.getSelectedStackIndex(dankNullItem);
			int totalSize = ItemUtils.getItemCount(dankNullItem);
			if ((currentIndex == -1) || (totalSize <= 1)) {
				return;
			}
			if (event.getButton() == 3) {
				ItemUtils.setNextSelectedStack(dankNullItem, player);
				event.setCanceled(true);
			}
			else if (event.getButton() == 4) {
				ItemUtils.setPreviousSelectedStack(dankNullItem, player);
				event.setCanceled(true);
			}
		}
		else if (player.isSneaking()) {
			int currentIndex = ItemUtils.getSelectedStackIndex(dankNullItem);
			int totalSize = ItemUtils.getItemCount(dankNullItem);
			if ((currentIndex == -1) || (totalSize <= 1)) {
				return;
			}
			int scrollForward = event.getDwheel();
			if (scrollForward < 0) {
				ItemUtils.setNextSelectedStack(dankNullItem, player);
				event.setCanceled(true);
			}
			else if (scrollForward > 0) {
				ItemUtils.setPreviousSelectedStack(dankNullItem, player);
				event.setCanceled(true);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void renderOverlayEvent(RenderGameOverlayEvent e) {
		if ((Globals.GUI_DANKNULL_ISOPEN) && ((e.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) || (e.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) || (e.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE) || (e.getType() == RenderGameOverlayEvent.ElementType.FOOD) || (e.getType() == RenderGameOverlayEvent.ElementType.HEALTH) || (e.getType() == RenderGameOverlayEvent.ElementType.ARMOR))) {
			e.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (FMLCommonHandler.instance().getSide() == Side.SERVER) {
			event.setResult(Result.ALLOW);
			return;
		}
		if (event.player != null) {
			EntityPlayer player = (EntityPlayer) event.player;
			ItemStack helmet = player.inventory.armorItemInSlot(3);
			ItemStack chestPlate = player.inventory.armorItemInSlot(2);
			ItemStack leggings = player.inventory.armorItemInSlot(1);
			ItemStack boots = player.inventory.armorItemInSlot(0);

			if ((helmet == null) || (helmet.getItem() != ModItems.carbonHelmet) || (!ItemUtils.isItemUpgradeActive(helmet, EntityEquipmentSlot.HEAD))) {
				if (PotionUtils.isPotionActive(player, MobEffects.NIGHT_VISION)) {
					PotionUtils.clearPotionEffect(player, MobEffects.NIGHT_VISION, 1);
				}
			}
			else if (ItemUtils.isItemUpgradeActive(helmet, EntityEquipmentSlot.HEAD)) {
				PotionUtils.effectPlayer(player, MobEffects.NIGHT_VISION, 1);
			}

			if ((chestPlate == null) || (!ItemUtils.isCarbonChestplateEquipped(chestPlate)) || (!ItemUtils.isItemUpgradeActive(chestPlate, EntityEquipmentSlot.CHEST))) {
				// if (Globals.CARBONCHESTPLATE_ISEQUIPPED) {
				// Globals.CARBONCHESTPLATE_ISEQUIPPED = false;
				if ((player.capabilities.allowFlying) && (!player.isCreative())) {
					if (player.capabilities.isFlying) {
						player.fallDistance = 0.0F;
					}
					player.capabilities.allowFlying = false;
				}
				// }
			}
			else if ((chestPlate.getItem() == ModItems.carbonChestplate) && (!player.capabilities.allowFlying) && (ItemUtils.isItemUpgradeActive(chestPlate, EntityEquipmentSlot.CHEST))) {
				// if (!Globals.CARBONCHESTPLATE_ISEQUIPPED) {
				// Globals.CARBONCHESTPLATE_ISEQUIPPED = true;
				player.capabilities.allowFlying = true;
				player.capabilities.setFlySpeed(0.1F);
				// }
			}

			if ((leggings == null) || (leggings.getItem() != ModItems.carbonLeggings) || (!ItemUtils.isItemUpgradeActive(leggings, EntityEquipmentSlot.LEGS))) {
				if (PotionUtils.isPotionActive(player, MobEffects.SPEED)) {
					PotionUtils.clearPotionEffect(player, MobEffects.SPEED, 0);
				}
			}
			else if (ItemUtils.isItemUpgradeActive(leggings, EntityEquipmentSlot.LEGS)) {
				PotionUtils.effectPlayer(player, MobEffects.SPEED, 0);
			}

			if (player.worldObj.isRemote) {
				if ((boots == null) || (boots.getItem() != ModItems.carbonBoots) || (!ItemUtils.isItemUpgradeActive(boots, EntityEquipmentSlot.FEET))) {
					if (player.stepHeight != 0.6F) {
						player.stepHeight = 0.6F;
					}
				}
				else if ((player.stepHeight != 1.0F) && (ItemUtils.isItemUpgradeActive(boots, EntityEquipmentSlot.FEET))) {
					player.stepHeight = 1.0F;
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void guiMouse(MouseInputEvent.Pre event) {
		if (event.getGui() instanceof GuiInventory) {
			EntityPlayer player = Minecraft.getMinecraft().thePlayer;
			if (player.capabilities.isFlying) {
				GuiInventory gui = (GuiInventory) event.getGui();
				if (gui.getSlotUnderMouse() != null) {
					Slot invSlot = gui.getSlotUnderMouse();
					if (invSlot.slotNumber == 6) {
						if (invSlot.getHasStack()) {
							ItemStack chestPlate = invSlot.getStack();
							if (chestPlate.getItem() instanceof ItemCarbonArmor) {
								if (ItemUtils.isCarbonChestplateEquipped(chestPlate)) {
									if (ItemUtils.isItemUpgradeActive(chestPlate, EntityEquipmentSlot.CHEST)) {
										event.setCanceled(true);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingFallEvent(LivingFallEvent event) {
		if (event.getEntity() != null && event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			if (player.capabilities.allowFlying || player.capabilities.isFlying) {
				ItemStack chestPlate = player.inventory.armorItemInSlot(2);
				if (ItemUtils.isCarbonChestplateEquipped(chestPlate)) {
					ItemStack currentChestPlate = player.inventory.armorItemInSlot(2);
					if (ItemUtils.isItemUpgradeActive(currentChestPlate, EntityEquipmentSlot.CHEST)) {
						event.setCanceled(true);
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onSmelting(ItemSmeltedEvent e) {
		if (e.smelting.getItem() == ModItems.rawCarbon) {
			ModAchievements.triggerAch(ModAchievements.carbonAch, e.player);
		}
		else if (e.smelting.getItem() == ModItems.carbonRod) {
			ModAchievements.triggerAch(ModAchievements.carbonBlockAch, e.player);
		}
		else {
			return;
		}
	}

	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent e) {
		if (e.crafting.getItem() == ModItems.carbonRod) {
			int dmg = e.crafting.getItemDamage();
			switch (dmg) {
			case 0:
				ModAchievements.triggerAch(ModAchievements.nanoAch, e.player);
				break;
			case 1:
				ModAchievements.triggerAch(ModAchievements.redStoneRodAch, e.player);
				break;
			case 2:
				ModAchievements.triggerAch(ModAchievements.lapisRodAch, e.player);
				break;
			case 3:
				ModAchievements.triggerAch(ModAchievements.ironRodAch, e.player);
				break;
			case 4:
				ModAchievements.triggerAch(ModAchievements.goldRodAch, e.player);
				break;
			case 5:
				ModAchievements.triggerAch(ModAchievements.diamondRodAch, e.player);
				break;
			case 6:
				ModAchievements.triggerAch(ModAchievements.emeraldRodAch, e.player);
			}
		}
	}
	
	@SubscribeEvent
	public void onMobDrop(LivingDropsEvent event) {
		if (event.getEntity() instanceof EntityDragon) {
			ItemStack emeraldPane = new ItemStack(ModItems.dankNullPanel, 5, 5);
			EntityItem drop = new EntityItem(event.getEntity().worldObj, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, emeraldPane);
			event.getDrops().add(drop);
		}
	}

	@SubscribeEvent
	public void onLootTablesLoaded(LootTableLoadEvent event) {
		final LootPool mainPool = event.getTable().getPool("main");
		if (mainPool != null) {
			if (event.getName().equals(LootTableList.ENTITIES_ENDERMAN) || event.getName().equals(LootTableList.ENTITIES_SKELETON)) {
				mainPool.addEntry(new LootEntryItem(ModItems.rawCarbon, 1, 0, new LootFunction[] {
						new SetCount(new LootCondition[0], new RandomValueRange(0, 1))
				}, new LootCondition[0], Globals.MODID + "rawCarbon"));
			}
			else if (event.getName().equals(LootTableList.ENTITIES_WITHER_SKELETON)) {
				mainPool.addEntry(new LootEntryItem(ModItems.rawCarbon, 1, 0, new LootFunction[] {
						new SetCount(new LootCondition[0], new RandomValueRange(0, 3))
				}, new LootCondition[0], Globals.MODID + "rawCarbon"));
			}
			else {
				return;
			}
		}
	}
}
