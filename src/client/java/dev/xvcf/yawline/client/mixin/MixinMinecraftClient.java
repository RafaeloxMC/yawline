package dev.xvcf.yawline.client.mixin;

import dev.xvcf.yawline.client.MainClient;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;handleBlockBreaking(Z)V", shift = At.Shift.BEFORE))
    private void handleInputEvents(CallbackInfo ci) {
        MainClient.getInstance().getKeyManager().entries().forEach(entry -> {
            boolean pressed = false;
            while (entry.getKey().wasPressed()) {
                entry.getValue().keyPressed();
                pressed = true;
            }
            if(pressed) entry.getValue().keyReleased();
        });
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tick(CallbackInfo ci) {
        MainClient.getInstance().tick();
    }
}
