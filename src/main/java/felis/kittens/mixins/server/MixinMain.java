package felis.kittens.mixins.server;

import felis.kittens.core.server.ServerApiInit;
import net.minecraft.server.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Main.class)
public class MixinMain {
    @Inject(method = "main", at = @At(value = "INVOKE", target = "Ljava/io/File;<init>(Ljava/lang/String;)V"), allow = 1, expect = 1)
    private static void initializeServerEntrypoint(String[] args, CallbackInfo ci) {
        ServerApiInit.serverApiInit();
    }
}
