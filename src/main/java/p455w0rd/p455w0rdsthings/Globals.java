package p455w0rd.p455w0rdsthings;

public class Globals {
	
	public static final String MODID = "p455w0rdsthings";
	public static final String MCVERSION = "1.10.2";
	public static final String VERSION = MCVERSION+"@version@";
	public static final String NAME = "p455w0rd's Things";
	public static final String SERVER_PROXY_CLASS = "p455w0rd.p455w0rdsthings.proxy.CommonProxy";
	public static final String CLIENT_PROXY_CLASS = "p455w0rd.p455w0rdsthings.proxy.ClientProxy";
	public static final String GUI_FACTORY = "p455w0rd.p455w0rdsthings.client.gui.GuiFactory";
	private static int guiIndex = 0;
	public static int GUINUM_DANKNULL = ++guiIndex;
	public static int GUINUM_CHATWINDOW = ++guiIndex;
	public static int GUINUM_COMPRESSOR = ++guiIndex;
	public static int GUINUM_FURNACE = ++guiIndex;
	public static int GUINUM_BATTERY = ++guiIndex;
	public static int GUINUM_SOLARPANEL = ++guiIndex;
	public static boolean GUI_DANKNULL_ISOPEN = false;
	public static boolean CARBONCHESTPLATE_ISEQUIPPED = false;
	public static float TIME = 0.0F;

	public static enum Upgrades {
		
		STEPASSIST, FLIGHT, NIGHTVISION, SPEED, ENDERVISION, LAVAWALKER;

	}
}
