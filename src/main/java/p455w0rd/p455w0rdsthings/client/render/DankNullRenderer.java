package p455w0rd.p455w0rdsthings.client.render;

import java.util.ArrayList;
import java.util.List;
import javax.vecmath.Matrix4f;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import p455w0rd.p455w0rdsthings.Globals;
import p455w0rd.p455w0rdsthings.ModItems;
import p455w0rd.p455w0rdsthings.items.ItemDankNull;
import p455w0rd.p455w0rdsthings.lib.render.IItemRenderer;
import p455w0rd.p455w0rdsthings.lib.render.TransformUtils;
import p455w0rd.p455w0rdsthings.util.ItemUtils;

@SideOnly(Side.CLIENT)
public class DankNullRenderer implements IItemRenderer {

	public void renderItem(ItemStack item) {
		if (!(item.getItem() instanceof ItemDankNull)) {
			return;
		}
		
		RenderManager rm = Minecraft.getMinecraft().getRenderManager();
		if (rm == null) {
			return;
		}
		GameSettings options = rm.options;
		if (options == null) {
			return;
		}
		int view = options.thirdPersonView;
		int index = ItemUtils.getSelectedStackIndex(item);
		ItemStack containedStack = ItemUtils.getItemByIndex(item, index);
		int modelDamage = item.getItemDamage();
		if (modelDamage > 5) {
			modelDamage -= 6;
		}
		IBakedModel holderModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher()
				.getItemModel(new ItemStack(ModItems.dankNullHolder, 1, modelDamage));

		TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
		if (containedStack != null) {
			IBakedModel containedItemModel = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(containedStack);

			GlStateManager.pushMatrix();
			if (((containedStack.getItem() instanceof ItemBlock))
					&& (!(Block.getBlockFromItem(containedStack.getItem()) instanceof BlockTorch))) {
				GlStateManager.scale(0.4D, 0.4D, 0.4D);
				if (containedItemModel.isBuiltInRenderer()) {
					if ((view > 0) || (!isStackInHand(item))) {
						GlStateManager.scale(1.1D, 1.1D, 1.1D);
						GlStateManager.translate(1.25D, 1.4D, 1.25D);
					} else {
						GlStateManager.translate(1.25D, 2.0D, 1.25D);
					}
				} else if ((view > 0) || (!isStackInHand(item))) {
					GlStateManager.translate(0.75D, 0.9D, 0.75D);
				} else {
					GlStateManager.translate(0.75D, 1.5D, 0.75D);
				}
			} else {
				GlStateManager.scale(0.5D, 0.5D, 0.5D);
				if (containedItemModel.isBuiltInRenderer()) {
					if ((view > 0) || (!isStackInHand(item))) {
						if ((containedStack.getItem() instanceof ItemSkull)) {
							if (containedStack.getItemDamage() == 5) {
								GlStateManager.scale(0.65D, 0.65D, 0.65D);
								GlStateManager.translate(1.5D, 3.0D, 1.5D);
							} else {
								GlStateManager.translate(0.75D, 2.25D, 1.1D);
							}
						} else {
							GlStateManager.scale(1.1D, 1.1D, 1.1D);
							GlStateManager.translate(1.25D, 1.4D, 1.25D);
						}
					} else if ((containedStack.getItem() instanceof ItemSkull)) {
						if (containedStack.getItemDamage() == 5) {
							GlStateManager.scale(0.65D, 0.65D, 0.65D);
							GlStateManager.translate(1.5D, 3.0D, 1.5D);
						} else {
							GlStateManager.translate(0.75D, 2.25D, 1.1D);
						}
					} else {
						GlStateManager.translate(0.75D, 2.0D, 1.0D);
					}
				} else if ((view > 0) || (!isStackInHand(item))) {
					GlStateManager.translate(0.5D, 0.9D, 0.5D);
				} else {
					GlStateManager.translate(0.5D, 1.5D, 0.5D);
				}
			}
			if (item.isOnItemFrame()) {
				GlStateManager.scale(1.25D, 1.25D, 1.25D);
				GlStateManager.translate(-0.2D, -0.2D, -0.5D);
			}
			if (containedItemModel.isBuiltInRenderer()) {
				GlStateManager.translate(0.0D, 0.0D, 0.0D);

				GlStateManager.rotate(Globals.TIME, 1.0F, Globals.TIME, 1.0F);
				GlStateManager.translate(-0.5D, 0.0D, -0.5D);
			} else {
				GlStateManager.rotate(Globals.TIME, 1.0F, 1.0F, 1.0F);
			}
			containedItemModel = ForgeHooksClient.handleCameraTransforms(containedItemModel, ItemCameraTransforms.TransformType.NONE, false);
			String[] registryName = containedStack.getItem().getRegistryName().toString().split(":");
			String modID = registryName[0];
			//System.out.println("Name: "+modID);
			if (modID.equalsIgnoreCase("p455w0rdsthings") || modID.equalsIgnoreCase("minecraft")) {
				renderItem(containedStack, containedItemModel);
			}
			else {
				GlStateManager.translate(0.5D, 0.5D, 0.5D);
				Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().thePlayer, containedStack,
						ItemCameraTransforms.TransformType.NONE);
				//GlStateManager.translate(0.0D, 0.0D, 0.0D);
			}

			GlStateManager.popMatrix();
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableLighting();
		GlStateManager.enableBlend();
		GlStateManager.enableRescaleNormal();
		GlStateManager.pushMatrix();
		if (item.isOnItemFrame()) {
			GlStateManager.scale(1.25D, 1.25D, 1.25D);
			GlStateManager.translate(-0.1D, -0.1D, -0.25D);
		}
		holderModel = ForgeHooksClient.handleCameraTransforms(holderModel, ItemCameraTransforms.TransformType.NONE,
				false);
		renderItem(item, holderModel);

		GlStateManager.popMatrix();

		GlStateManager.disableRescaleNormal();
		//GlStateManager.disableLighting();

		textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		textureManager.getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
	}

	private boolean isStackInHand(ItemStack itemStackIn) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if ((player.getHeldItemMainhand() == itemStackIn) || (player.getHeldItemOffhand() == itemStackIn)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
		return new ArrayList();
	}

	public void renderItem(ItemStack stack, IBakedModel model) {
		if (stack != null) {
			GlStateManager.pushMatrix();
			if (model.isBuiltInRenderer()) {
				Minecraft.getMinecraft().getItemRenderer().renderItem(Minecraft.getMinecraft().thePlayer, stack,
						ItemCameraTransforms.TransformType.NONE);
			} else {
				RenderModel.render(model, stack);
				if (stack.hasEffect()) {
					if ((stack.getItem() instanceof ItemDankNull)) {
						GlintEffectRenderer.apply(model, stack.getItemDamage());
					} else {
						GlintEffectRenderer.apply(model, -1);
					}
				}
			}
			GlStateManager.popMatrix();
		}
	}

	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(
			ItemCameraTransforms.TransformType cameraTransformType) {
		return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, TransformUtils.DEFAULT_BLOCK.getTransforms(),
				cameraTransformType);
	}

	public boolean isAmbientOcclusion() {
		return false;
	}

	public boolean isGui3d() {
		return false;
	}

	public boolean isBuiltInRenderer() {
		return true;
	}

	public TextureAtlasSprite getParticleTexture() {
		return null;
	}

	public ItemCameraTransforms getItemCameraTransforms() {
		return ItemCameraTransforms.DEFAULT;
	}

	public ItemOverrideList getOverrides() {
		return ItemOverrideList.NONE;
	}

	@SubscribeEvent
	public void tickEvent(TickEvent.PlayerTickEvent e) {
		if (e.side == Side.CLIENT) {
			if (Globals.TIME >= 360.1F) {
				Globals.TIME = 0.0F;
			}
			Globals.TIME += 0.75F;
		}
	}
}
