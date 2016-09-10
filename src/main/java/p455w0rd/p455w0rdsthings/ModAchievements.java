package p455w0rd.p455w0rdsthings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;

public class ModAchievements {
	public static final Achievement carbonAch = new Achievement("achievement.carbonAchievement", "carbonAchievement", 0,
			0, new ItemStack(ModItems.rawCarbon, 1), (Achievement) null);
	
	public static final Achievement nanoAch = new Achievement("achievement.nanoTubeAchievement", "nanoTubeAchievement",
			0, 1, new ItemStack(ModItems.carbonRod, 1, 0), carbonAch);
	
	public static final Achievement redStoneRodAch = new Achievement("achievement.redStoneRodAchievement",
			"redStoneRodAchievement", 0, 3, new ItemStack(ModItems.carbonRod, 1, 1), nanoAch);
	
	public static final Achievement lapisRodAch = new Achievement("achievement.lapisRodAchievement",
			"lapisRodAchievement", 0, 5, new ItemStack(ModItems.carbonRod, 1, 2), redStoneRodAch);
	
	public static final Achievement ironRodAch = new Achievement("achievement.ironRodAchievement", "ironRodAchievement",
			0, 7, new ItemStack(ModItems.carbonRod, 1, 3), lapisRodAch);
	
	public static final Achievement goldRodAch = new Achievement("achievement.goldRodAchievement", "goldRodAchievement",
			0, 9, new ItemStack(ModItems.carbonRod, 1, 4), ironRodAch);
	
	public static final Achievement diamondRodAch = new Achievement("achievement.diamondRodAchievement",
			"diamondRodAchievement", 0, 11, new ItemStack(ModItems.carbonRod, 1, 5), goldRodAch);
	
	public static final Achievement emeraldRodAch = new Achievement("achievement.emeraldRodAchievement",
			"emeraldRodAchievement", 0, 13, new ItemStack(ModItems.carbonRod, 1, 6), diamondRodAch);
	
	public static final Achievement netherCarbonBlockAch = new Achievement("achievement.netherCarbonBlockAchievement",
			"netherCarbonBlockAchievement", 0, -2, new ItemStack(ModBlocks.netherCarbonOreBlock, 1),
			(Achievement) null);
	
	public static final Achievement redStonePaneAch = new Achievement("achievement.redStonePaneAchievement",
			"redStonePaneAchievement", 2, 3, new ItemStack(ModItems.dankNullPanel, 1, 0), redStoneRodAch);
	
	public static final Achievement lapisPaneAch = new Achievement("achievement.lapisPaneAchievement",
			"lapisPaneAchievement", 2, 5, new ItemStack(ModItems.dankNullPanel, 1, 1), lapisRodAch);
	
	public static final Achievement ironPaneAch = new Achievement("achievement.ironPaneAchievement",
			"ironPaneAchievement", 2, 7, new ItemStack(ModItems.dankNullPanel, 1, 2), ironRodAch);
	
	public static final Achievement goldPaneAch = new Achievement("achievement.goldPaneAchievement",
			"goldPaneAchievement", 2, 9, new ItemStack(ModItems.dankNullPanel, 1, 3), goldRodAch);
	
	public static final Achievement diamondPaneAch = new Achievement("achievement.diamondPaneAchievement",
			"diamondPaneAchievement", 2, 11, new ItemStack(ModItems.dankNullPanel, 1, 4), diamondRodAch);
	
	public static final Achievement emeraldPaneAch = new Achievement("achievement.emeraldPaneAchievement",
			"emeraldPaneAchievement", 2, 13, new ItemStack(ModItems.dankNullPanel, 1, 5), emeraldRodAch);
	
	public static final Achievement redStoneDankNullAch = new Achievement("achievement.redStoneDankNullAchievement",
			"redStoneDankNullAchievement", 4, 3, new ItemStack(ModItems.dankNullItem, 1, 0), redStonePaneAch);
	
	public static final Achievement lapisDankNullAch = new Achievement("achievement.lapisDankNullAchievement",
			"lapisDankNullAchievement", 4, 5, new ItemStack(ModItems.dankNullItem, 1, 1), lapisPaneAch);
	
	public static final Achievement ironDankNullAch = new Achievement("achievement.ironDankNullAchievement",
			"ironDankNullAchievement", 4, 7, new ItemStack(ModItems.dankNullItem, 1, 2), ironPaneAch);
	
	public static final Achievement goldDankNullAch = new Achievement("achievement.goldDankNullAchievement",
			"goldDankNullAchievement", 4, 9, new ItemStack(ModItems.dankNullItem, 1, 3), goldPaneAch);
	
	public static final Achievement diamondDankNullAch = new Achievement("achievement.diamondDankNullAchievement",
			"diamondDankNullAchievement", 4, 11, new ItemStack(ModItems.dankNullItem, 1, 4), diamondPaneAch);
	
	public static final Achievement emeraldDankNullAch = new Achievement("achievement.emeraldDankNullAchievement",
			"emeraldDankNullAchievement", 4, 13, new ItemStack(ModItems.dankNullItem, 1, 5), emeraldPaneAch);
	
	public static final Achievement carbonBlockAch = new Achievement("achievement.carbonBlockAchievement",
			"carbonBlockAchievement", -2, 0, new ItemStack(ModBlocks.carbonBlock, 1), carbonAch);
	
	public static final Achievement carbonHelmAch = new Achievement("achievement.carbonHelmAchievement",
			"carbonHelmAchievement", -2, 1, new ItemStack(ModItems.carbonHelmet, 1), carbonBlockAch);
	
	public static final Achievement carbonChestplateAch = new Achievement("achievement.carbonChestplateAchievement",
			"carbonChestplateAchievement", -2, 3, new ItemStack(ModItems.carbonChestplate, 1), carbonBlockAch);
	
	public static final Achievement carbonLeggingsAch = new Achievement("achievement.carbonLeggingsAchievement",
			"carbonLeggingsAchievement", -2, 5, new ItemStack(ModItems.carbonLeggings, 1), carbonBlockAch);
	
	public static final Achievement carbonBootsAch = new Achievement("achievement.carbonBootsAchievement",
			"carbonBootsAchievement", -2, 7, new ItemStack(ModItems.carbonBoots, 1), carbonBlockAch);
	
	public static final Achievement carbon3DHelmAch = new Achievement("achievement.carbon3DHelmAchievement",
			"carbon3DHelmAchievement", -4, 1, new ItemStack(ModItems.carbonHelmet), carbonBlockAch);
	
	private static final AchievementPage achPage = new AchievementPage("p455w0rd's Things",
			new Achievement[] { carbonAch, nanoAch, netherCarbonBlockAch, redStoneRodAch, lapisRodAch, ironRodAch,
					goldRodAch, diamondRodAch, emeraldRodAch, redStonePaneAch, lapisPaneAch, ironPaneAch, goldPaneAch,
					diamondPaneAch, emeraldPaneAch, redStoneDankNullAch, lapisDankNullAch, ironDankNullAch,
					goldDankNullAch, diamondDankNullAch, emeraldDankNullAch, carbonBlockAch, carbonHelmAch,
					carbonChestplateAch, carbonLeggingsAch, carbonBootsAch, carbon3DHelmAch });

	public static void init() {
		registerAchievements();
		registerPage();
	}

	public static void registerAchievements() {
		int i = achPage.getAchievements().size();
		for (int j = 0; j < i; j++) {
			Achievement ach = (Achievement) achPage.getAchievements().get(j);
			if (!AchievementList.ACHIEVEMENTS.contains(ach)) {
				ach.registerStat();
			}
		}
	}

	public static void registerPage() {
		if (AchievementPage.getAchievementPage("Wireless Crafting Term") == null) {
			AchievementPage.registerAchievementPage(achPage);
		}
	}

	public static void addAchievementToPage(Achievement a) {
		addAchievementToPage(a, false, null);
	}

	public static void addAchievementToPage(Achievement a, boolean hidden, EntityPlayer player) {
		achPage.getAchievements().add(a);
	}

	public static void triggerAch(Achievement a, EntityPlayer p) {
		p.addStat(a, 1);
	}
}
