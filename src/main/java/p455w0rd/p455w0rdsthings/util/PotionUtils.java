package p455w0rd.p455w0rdsthings.util;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PotionUtils
{
  public static void effectPlayer(EntityPlayer player, Potion potion, int amplifier)
  {
    if ((player.getActivePotionEffect(potion) == null) || (player.getActivePotionEffect(potion).getDuration() >= 0)) {
      player.addPotionEffect(new PotionEffect(potion, 318, amplifier, false, false));
    }
  }
  
  public static boolean isPotionActive(EntityPlayer player, Potion potion)
  {
    return player.getActivePotionEffect(potion) != null;
  }
  
  public static void clearPotionEffect(EntityPlayer player, Potion potion, int amplifier)
  {
    Iterator<PotionEffect> iterator = player.getActivePotionEffects().iterator();
    while (iterator.hasNext())
    {
      PotionEffect currEffect = (PotionEffect)iterator.next();
      Potion currPotion = currEffect.getPotion();
      if ((currPotion == potion) && 
        (player.getActivePotionEffect(potion) != null))
      {
        currPotion.removeAttributesModifiersFromEntity(player, player.getAttributeMap(), currEffect.getAmplifier());
        iterator.remove();
      }
    }
  }
}


