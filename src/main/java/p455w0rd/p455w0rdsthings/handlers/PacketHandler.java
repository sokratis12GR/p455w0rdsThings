package p455w0rd.p455w0rdsthings.handlers;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import p455w0rd.p455w0rdsthings.network.PacketSetSelectedItem;

public class PacketHandler {
	
	private static int packetId = 0;
	public static SimpleNetworkWrapper INSTANCE = null;

	public static int nextID() {
		return packetId++;
	}

	public static void registerMessages(String channelName) {
		INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
		registerMessages();
	}

	public static void registerMessages() {
		INSTANCE.registerMessage(PacketSetSelectedItem.Handler.class, PacketSetSelectedItem.class, nextID(),
				Side.SERVER);
	}
}
