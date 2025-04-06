package dev.xvcf.yawline.client.mixin;

import dev.xvcf.yawline.client.MainClient;
import dev.xvcf.yawline.client.mixininterface.IKeyBinding;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding implements IKeyBinding {

    @Shadow public abstract void setPressed(boolean pressed);

    @Accessor("boundKey")
    public abstract InputUtil.Key getBoundKey();

    @Accessor("CATEGORY_ORDER_MAP")
    public abstract Map<String, Integer> getCategoryOrderMap();

    @Inject(method = "<init>(Ljava/lang/String;ILjava/lang/String;)V", at = @At("TAIL"))
    private void init(String translationKey, int code, String category, CallbackInfo ci) {
        getCategoryOrderMap().putIfAbsent("key.categories.yawline", 9);
    }

    @Override
    public boolean isActuallyPressed() {
        long handle = MainClient.getInstance().getClient().getWindow().getHandle();
        int code = getBoundKey().getCode();
        return InputUtil.isKeyPressed(handle, code);
    }

    @Override
    public void resetPressedState() {
        setPressed(isActuallyPressed());
    }
}
