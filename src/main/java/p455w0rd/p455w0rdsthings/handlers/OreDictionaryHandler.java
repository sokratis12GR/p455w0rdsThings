package p455w0rd.p455w0rdsthings.handlers;

import net.minecraftforge.oredict.OreDictionary;
import p455w0rd.p455w0rdsthings.ModItems;

public class OreDictionaryHandler {
	
	public static void addOres() {
		OreDictionary.registerOre("rawCarbon", ModItems.rawCarbon);
		OreDictionary.registerOre("carbonRaw", ModItems.rawCarbon);
	}
}
