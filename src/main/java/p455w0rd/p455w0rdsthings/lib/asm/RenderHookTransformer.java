package p455w0rd.p455w0rdsthings.lib.asm;

import java.util.Map;
import net.minecraft.launchwrapper.IClassTransformer;

public class RenderHookTransformer
  implements IClassTransformer
{
  private ModularASMTransformer transformer = new ModularASMTransformer();
  
  public RenderHookTransformer()
  {
    Map<String, ASMBlock> blocks = ASMReader.loadResource("/assets/p455w0rdsthings/asm/hooks.asm");
    this.transformer.add(new ModularASMTransformer.MethodInjector(new ObfMapping("net/minecraft/client/renderer/RenderItem", "func_180454_a", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/renderer/block/model/IBakedModel;)V"), (ASMBlock)blocks.get("n_IItemRenderer"), (ASMBlock)blocks.get("IItemRenderer"), true));
  }
  
  public byte[] transform(String name, String tname, byte[] bytes)
  {
    return this.transformer.transform(name, bytes);
  }
}


