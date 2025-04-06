package dev.xvcf.yawline.client.input.keys;

import dev.xvcf.yawline.client.MainClient;
import dev.xvcf.yawline.client.input.IKey;
import dev.xvcf.yawline.client.ui.SettingsPopupScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class OpenMenuKey implements IKey {
    @Override
    public void tick() {

    }

    @Override
    public void keyPressed() {
        // OPEN MENU LOGIC
        MainClient.getInstance().getClient().setScreen(new SettingsPopupScreen());
    }

    private static final KeyBinding mapping = new KeyBinding("key.openMenu", InputUtil.GLFW_KEY_N, "key.categories.yawline");

    public static KeyBinding getMapping() {
        return mapping;
    }

    @Override
    public void keyReleased() {

    }
}
