package dev.xvcf.yawline.client.input.keys;

import dev.xvcf.yawline.client.MainClient;
import dev.xvcf.yawline.client.input.IKey;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class SetYawPitchKey implements IKey {
    @Override
    public void tick() {

    }

    @Override
    public void keyPressed() {
        if(MainClient.getInstance().getClient().player == null) {
            return;
        }
        for (int i = 0; i < mappings.length; i++) {
            if (mappings[i].isPressed()) {
                MainClient.getInstance().getClient().player.setYaw(MainClient.yawValues[i]);
                MainClient.getInstance().getClient().player.setPitch(MainClient.pitchValues[i]);
                break;
            }
        }
    }

    private static final KeyBinding[] mappings = {
            new KeyBinding("key.setYawPitch1", InputUtil.GLFW_KEY_F6, "key.categories.yawline"),
            new KeyBinding("key.setYawPitch2", InputUtil.GLFW_KEY_F7, "key.categories.yawline"),
            new KeyBinding("key.setYawPitch3", InputUtil.GLFW_KEY_F8, "key.categories.yawline"),
            new KeyBinding("key.setYawPitch4", InputUtil.GLFW_KEY_F9, "key.categories.yawline"),
            new KeyBinding("key.setYawPitch5", InputUtil.GLFW_KEY_F10, "key.categories.yawline")
    };

    public static KeyBinding[] getMapping() {
        return mappings;
    }

    @Override
    public void keyReleased() {

    }
}
