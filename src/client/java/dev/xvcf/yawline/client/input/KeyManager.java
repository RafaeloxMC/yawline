package dev.xvcf.yawline.client.input;

import dev.xvcf.yawline.client.MainClient;
import dev.xvcf.yawline.client.util.Manager;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeyManager extends Manager<KeyBinding, IKey> {

    public void tick() {
        values().forEach(IKey::tick);
    }

    public void inject() {
        KeyBinding[] mappings = keys().toArray(KeyBinding[]::new);
        GameOptions options = MainClient.getInstance().getClient().options;

        KeyBinding[] newKeys = new KeyBinding[options.allKeys.length + mappings.length];

        System.arraycopy(options.allKeys, 0, newKeys, 0, options.allKeys.length);

        System.arraycopy(mappings, 0, newKeys, options.allKeys.length, mappings.length);

        try {
            Field field = GameOptions.class.getDeclaredField("allKeys");
            field.setAccessible(true);
            field.set(options, newKeys);

            MainClient.getInstance().getLogger().console_debug("Injected " + mappings.length + " keybindings");

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        preventConflicts(newKeys, mappings);
    }

    private void preventConflicts(KeyBinding[] existing, KeyBinding[] added) {
        Set<KeyBinding> mappings = new HashSet<>(List.of(existing));
        Arrays.asList(added).forEach(mappings::remove);
        Set<String> check = new HashSet<>(Arrays.stream(added).map(KeyBinding::getBoundKeyTranslationKey).toList());
        for(KeyBinding mapping : mappings) {
            if(check.contains(mapping.getBoundKeyTranslationKey())) {
                mapping.setBoundKey(InputUtil.Type.KEYSYM.createFromCode(-1));
                MainClient.getInstance().getLogger().console_warn("Keybinding " + mapping.getBoundKeyTranslationKey() + " was removed due to conflict with " + mapping.getBoundKeyTranslationKey());
            }
        }
    }

}
