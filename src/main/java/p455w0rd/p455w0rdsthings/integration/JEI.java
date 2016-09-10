package p455w0rd.p455w0rdsthings.integration;

import javax.annotation.Nonnull;
import mezz.jei.api.IItemBlacklist;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import p455w0rd.p455w0rdsthings.ModItems;

@Interface(iface = "mezz.jei.api.IItemBlacklist", modid = "JEI", striprefs = true)
@JEIPlugin
public class JEI implements IModPlugin {
	
	@Method(modid = "JEI")
	public void register(@Nonnull IModRegistry registry) {
		IJeiHelpers helpers = registry.getJeiHelpers();
		IItemBlacklist blacklist = helpers.getItemBlacklist();
		blacklist.addItemToBlacklist(new ItemStack(ModItems.dankNullHolder, 1, 32767));
		for (int i = 6; i < 12; i++) {
			blacklist.addItemToBlacklist(new ItemStack(ModItems.dankNullItem, 1, i));
		}
	}

	@Method(modid = "JEI")
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}
}
