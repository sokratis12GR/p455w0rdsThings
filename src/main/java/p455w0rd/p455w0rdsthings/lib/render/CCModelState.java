package p455w0rd.p455w0rdsthings.lib.render;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraftforge.client.model.IPerspectiveAwareModel.MapWrapper;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class CCModelState
  implements IModelState
{
  private final ImmutableMap<? extends IModelPart, TRSRTransformation> map;
  private final Optional<TRSRTransformation> def;
  
  public CCModelState(ImmutableMap<? extends IModelPart, TRSRTransformation> map)
  {
    this(map, Optional.absent());
  }
  
  public CCModelState(ImmutableMap<? extends IModelPart, TRSRTransformation> map, Optional<TRSRTransformation> def)
  {
    this.map = map;
    this.def = def;
  }
  
  public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part)
  {
    if ((!part.isPresent()) || (!this.map.containsKey(part.get()))) {
      return this.def;
    }
    return Optional.fromNullable(this.map.get(part.get()));
  }
  
  public ImmutableMap<? extends IModelPart, TRSRTransformation> getMap()
  {
    return this.map;
  }
  
  public ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> getTransforms()
  {
    return MapWrapper.getTransforms(this);
  }
}


