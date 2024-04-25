package io.github.joemama.testmod.mixin;

import io.github.joemama.testmod.Testmod;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientHandshakePacketListenerImpl.class)
public class MixinClientHandshakePacketListenerImpl {
    @Inject(method = "handleHello", at = @At("HEAD"))
    private void onHello(CallbackInfo ci) {
        Testmod.LOGGER.info("Hello");
    }
}
