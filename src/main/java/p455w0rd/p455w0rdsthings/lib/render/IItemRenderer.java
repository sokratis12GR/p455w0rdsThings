package p455w0rd.p455w0rdsthings.lib.render;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.IPerspectiveAwareModel;

public abstract interface IItemRenderer
  extends IPerspectiveAwareModel
{
  public abstract void renderItem(ItemStack paramItemStack);
}


