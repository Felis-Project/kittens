package felis.kittens.mixins.client;

import felis.kittens.event.GameEvents;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MixinMinecraft {
    @Inject(method = "tick", at = @At("HEAD"))
    private void fireStartEvent(CallbackInfo ci) {
        GameEvents.Client.tick.start.fire((Minecraft) (Object) this);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void fireEndEvents(CallbackInfo ci) {
        GameEvents.Client.tick.end.fire((Minecraft) (Object) this);
    }
}
