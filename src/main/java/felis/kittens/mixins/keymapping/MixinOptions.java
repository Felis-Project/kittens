package felis.kittens.mixins.keymapping;

import felis.kittens.keymapping.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class MixinOptions {
    @Shadow
    @Mutable
    public KeyMapping[] keyMappings;

    @Inject(method = "load", at = @At("HEAD"))
    private void injectCustomMappings(CallbackInfo ci) {
        this.keyMappings = KeyMappingRegistry.INSTANCE.inject(this.keyMappings);
    }
}
