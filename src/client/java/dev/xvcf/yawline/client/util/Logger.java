package dev.xvcf.yawline.client.util;

import com.mojang.logging.LogUtils;
import dev.xvcf.yawline.client.MainClient;
import net.minecraft.text.Text;

public class Logger {

    private static final org.slf4j.Logger MCLOGGER = LogUtils.getLogger();

    public static final String LOG_PREFIX = "[Yawline]";
    private static final String INGAME_PREFIX = "§7" + "[" + "§6" + "Yawline" + "§7" + "] ";

    public void ingame_info(String message) {
        MainClient.getInstance().getClient().inGameHud.getChatHud().addMessage(Text.of(INGAME_PREFIX + "§f" + message));
    }

    public void ingame_warn(String message) {
        MainClient.getInstance().getClient().inGameHud.getChatHud().addMessage(Text.of(INGAME_PREFIX + "§e" + message));
    }

    public void ingame_error(String message) {
        MainClient.getInstance().getClient().inGameHud.getChatHud().addMessage(Text.of(INGAME_PREFIX + "§c" + message));
    }

    public void console_info(String message) {
        MCLOGGER.info(LOG_PREFIX + " {}", message);
    }

    public void console_warn(String message) {
        MCLOGGER.warn(LOG_PREFIX + " {}", message);
    }

    public void console_error(String message) {
        MCLOGGER.error(LOG_PREFIX + " {}", message);
    }

    public void console_debug(String message) {
        if (MainClient.getInstance().isDebug()) MCLOGGER.info("[Yawline - Debug]  {}", message);
    }

}
