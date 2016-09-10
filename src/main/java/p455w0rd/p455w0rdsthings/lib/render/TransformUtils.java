package p455w0rd.p455w0rdsthings.lib.render;

import com.google.common.collect.ImmutableMap;
import javax.vecmath.Vector3f;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraftforge.common.model.TRSRTransformation;

public class TransformUtils
{
  private static final TRSRTransformation flipX = new TRSRTransformation(null, null, new Vector3f(-1.0F, 1.0F, 1.0F), null);
  public static final CCModelState DEFAULT_BLOCK;
  public static final CCModelState DEFAULT_ITEM;
  public static final CCModelState DEFAULT_TOOL;
  
  static
  {
    TRSRTransformation thirdPerson = get(0.0F, 2.5F, 0.0F, 75.0F, 45.0F, 0.0F, 0.375F);
    
    ImmutableMap.Builder<TransformType, TRSRTransformation> defaultBlockBuilder = ImmutableMap.builder();
    defaultBlockBuilder.put(TransformType.GUI, get(0.0F, 0.0F, 0.0F, 30.0F, 225.0F, 0.0F, 0.625F));
    defaultBlockBuilder.put(TransformType.GROUND, get(0.0F, 3.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.25F));
    defaultBlockBuilder.put(TransformType.FIXED, get(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
    defaultBlockBuilder.put(TransformType.THIRD_PERSON_RIGHT_HAND, thirdPerson);
    defaultBlockBuilder.put(TransformType.THIRD_PERSON_LEFT_HAND, leftify(thirdPerson));
    defaultBlockBuilder.put(TransformType.FIRST_PERSON_RIGHT_HAND, get(0.0F, 0.0F, 0.0F, 0.0F, 45.0F, 0.0F, 0.4F));
    defaultBlockBuilder.put(TransformType.FIRST_PERSON_LEFT_HAND, get(0.0F, 0.0F, 0.0F, 0.0F, 225.0F, 0.0F, 0.4F));
    DEFAULT_BLOCK = new CCModelState(defaultBlockBuilder.build());
    
    thirdPerson = get(0.0F, 3.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.55F);
    TRSRTransformation firstPerson = get(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F);
    ImmutableMap.Builder<TransformType, TRSRTransformation> defaultItemBuilder = ImmutableMap.builder();
    defaultItemBuilder.put(TransformType.GROUND, get(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.5F));
    defaultItemBuilder.put(TransformType.HEAD, get(0.0F, 13.0F, 7.0F, 0.0F, 180.0F, 0.0F, 1.0F));
    defaultItemBuilder.put(TransformType.THIRD_PERSON_RIGHT_HAND, thirdPerson);
    defaultItemBuilder.put(TransformType.THIRD_PERSON_LEFT_HAND, leftify(thirdPerson));
    defaultItemBuilder.put(TransformType.FIRST_PERSON_RIGHT_HAND, firstPerson);
    defaultItemBuilder.put(TransformType.FIRST_PERSON_LEFT_HAND, leftify(firstPerson));
    DEFAULT_ITEM = new CCModelState(defaultItemBuilder.build());
    
    ImmutableMap.Builder<TransformType, TRSRTransformation> defaultToolBuilder = ImmutableMap.builder();
    defaultToolBuilder.put(TransformType.THIRD_PERSON_RIGHT_HAND, get(0.0F, 4.0F, 0.5F, 0.0F, -90.0F, 55.0F, 0.85F));
    defaultToolBuilder.put(TransformType.THIRD_PERSON_LEFT_HAND, get(0.0F, 4.0F, 0.5F, 0.0F, 90.0F, -55.0F, 0.85F));
    defaultToolBuilder.put(TransformType.FIRST_PERSON_RIGHT_HAND, get(1.13F, 3.2F, 1.13F, 0.0F, -90.0F, 25.0F, 0.68F));
    defaultToolBuilder.put(TransformType.FIRST_PERSON_LEFT_HAND, get(1.13F, 3.2F, 1.13F, 0.0F, 90.0F, -25.0F, 0.68F));
    DEFAULT_TOOL = new CCModelState(defaultToolBuilder.build());
  }
  
  private static TRSRTransformation get(float tx, float ty, float tz, float ax, float ay, float az, float s)
  {
    return TRSRTransformation.blockCenterToCorner(new TRSRTransformation(new Vector3f(tx / 16.0F, ty / 16.0F, tz / 16.0F), TRSRTransformation.quatFromXYZDegrees(new Vector3f(ax, ay, az)), new Vector3f(s, s, s), null));
  }
  
  private static TRSRTransformation leftify(TRSRTransformation transform)
  {
    return TRSRTransformation.blockCenterToCorner(flipX.compose(TRSRTransformation.blockCornerToCenter(transform)).compose(flipX));
  }
}


