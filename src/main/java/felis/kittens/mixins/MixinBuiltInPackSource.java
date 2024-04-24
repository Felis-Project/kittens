package felis.kittens.mixins;

import felis.kittens.core.pack.ModPackSource;
import net.minecraft.server.packs.repository.BuiltInPackSource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.RepositorySource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(BuiltInPackSource.class)
public abstract class MixinBuiltInPackSource implements RepositorySource {
    @Inject(method = "listBundledPacks", at = @At("RETURN"))
    private void kittens$registerModPacks(Consumer<Pack> registrar, CallbackInfo ci) {
        ModPackSource.INSTANCE.loadPacks(registrar);
    }
}
