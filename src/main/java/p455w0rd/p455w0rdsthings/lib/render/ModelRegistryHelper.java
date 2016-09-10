package p455w0rd.p455w0rdsthings.lib.render;

import com.google.common.base.Function;
import com.google.common.collect.Maps;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.BuiltInModel;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ModelRegistryHelper
{
  private static List<Pair<ModelResourceLocation, IBakedModel>> registerModels = new LinkedList();
  
  static
  {
    MinecraftForge.EVENT_BUS.register(new ModelRegistryHelper());
  }
  
  public static void register(ModelResourceLocation location, IBakedModel model)
  {
    registerModels.add(new ImmutablePair(location, model));
  }
  
  public static void registerItemRenderer(Item item, IItemRenderer renderer)
  {
    ModelResourceLocation modelLoc = new ModelResourceLocation(item.getRegistryName(), "inventory");
    register(modelLoc, renderer);
    ModelLoader.setCustomMeshDefinition(item, new ItemMeshDefinition()
    {
      public ModelResourceLocation getModelLocation(ItemStack stack)
      {
        return modelLoc;
      }
    });
  }
  
  @SubscribeEvent
  public void onModelBake(ModelBakeEvent event)
  {
    for (Pair<ModelResourceLocation, IBakedModel> pair : registerModels) {
      event.getModelRegistry().putObject(pair.getKey(), pair.getValue());
    }
  }
  
  public static void setParticleTexture(Block block, final ResourceLocation tex) {
      final ModelResourceLocation modelLoc = new ModelResourceLocation(block.getRegistryName(), "particle");
      ModelRegistryHelper.register(modelLoc, (IBakedModel)new BuiltInModel(BlockRenderer.blockCameraTransform, ItemOverrideList.NONE){

          public TextureAtlasSprite getParticleTexture() {
              return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(tex.toString());
          }
      });
      ModelLoader.setCustomStateMapper((Block)block, (IStateMapper)new IStateMapper(){

          
		public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
              return Maps.toMap((Iterable)blockIn.getBlockState().getValidStates(), (Function)new Function<IBlockState, ModelResourceLocation>(){

                  public ModelResourceLocation apply(IBlockState input) {
                      return modelLoc;
                  }
              });
          }

      });
  }
}


