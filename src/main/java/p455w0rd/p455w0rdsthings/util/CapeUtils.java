
package p455w0rd.p455w0rdsthings.util;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import p455w0rd.p455w0rdsthings.util.ReflectionUtils;

@SideOnly(value=Side.CLIENT)
public class CapeUtils {
    private static final ResourceLocation CAPE_LOCATION = new ResourceLocation("p455w0rdsthings", "textures/capes/p455cape4.png");
    private static final ResourceLocation MMD_CAPE_LOCATION = new ResourceLocation("p455w0rdsthings", "textures/capes/p455cape7.png");
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<Runnable>());
    private static final MethodHandle GET_PLAYER_INFO = ReflectionUtils.findMethod(AbstractClientPlayer.class, new String[]{"getPlayerInfo", "func_175155_b"}, new Class[0]);
    private static final MethodHandle GET_PLAYER_TEXTURES = ReflectionUtils.findFieldGetter(NetworkPlayerInfo.class, "playerTextures", "field_187107_a");
    private static List<String> PATRON_LIST = new ArrayList<String>();

    public static void queuePlayerCapeReplacement(AbstractClientPlayer player) {
        PATRON_LIST = CapeUtils.getPatronList();
        if (PATRON_LIST.size() <= 0) {
            return;
        }
        THREAD_POOL.submit(() -> {
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                return;
            }
            Minecraft.getMinecraft().addScheduledTask(() -> {
                CapeUtils.replacePlayerCape(player);
            }
            );
        }
        );
    }

    private static void replacePlayerCape(AbstractClientPlayer player) {
        NetworkPlayerInfo playerInfo;
        Map<Type, ResourceLocation> playerTextures;
        try {
            playerInfo = (NetworkPlayerInfo) GET_PLAYER_INFO.invoke(player);
        }
        catch (Throwable throwable) {
            return;
        }
        if (playerInfo == null) {
            return;
        }
        try {
            playerTextures = (Map<Type, ResourceLocation>) GET_PLAYER_TEXTURES.invoke(playerInfo);
        }
        catch (Throwable throwable) {
            return;
        }
        if (CapeUtils.doesPlayerHaveCape(player)) {
            playerTextures.put(MinecraftProfileTexture.Type.CAPE, CAPE_LOCATION);
            playerTextures.put(MinecraftProfileTexture.Type.ELYTRA, CAPE_LOCATION);
            return;
        }
        if (CapeUtils.doesPlayerHaveMMDCape(player)) {
            playerTextures.put(MinecraftProfileTexture.Type.CAPE, MMD_CAPE_LOCATION);
            playerTextures.put(MinecraftProfileTexture.Type.ELYTRA, MMD_CAPE_LOCATION);
            return;
        }
    }

    static List<String> getPatronList() {
        try {
            InputStream in2 = new URL("http://p455w0rd.net/mc/patrons.txt").openStream();
            return IOUtils.readLines((InputStream)in2);
        }
        catch (MalformedURLException in2) {
        }
        catch (IOException in2) {
            // empty catch block
        }
        return null;
    }

    public static boolean doesPlayerHaveCape(AbstractClientPlayer player) {
        for (int i = 0; i < PATRON_LIST.size(); ++i) {
            String uuid = player.getUniqueID().toString();
            if (!uuid.equals(PATRON_LIST.get(i))) continue;
            return true;
        }
        return false;
    }

    public static boolean doesPlayerHaveMMDCape(AbstractClientPlayer player) {
        for (int i = 0; i < PATRON_LIST.size(); ++i) {
            String uuid = player.getUniqueID().toString() + "_MMD";
            if (!uuid.equals(PATRON_LIST.get(i))) continue;
            return true;
        }
        return false;
    }
}

