package felis.kittens.mixins.client;

import felis.kittens.event.GameEvents;
import felis.kittens.event.RenderGuiEventContext;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class MixinGui {
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/LayeredDraw;render(Lnet/minecraft/client/gui/GuiGraphics;F)V"))
    public void render(GuiGraphics gfx, float partialTicks, CallbackInfo ci) {
        GameEvents.Client.Render.gui.fire(new RenderGuiEventContext(gfx, partialTicks));
    }
}
