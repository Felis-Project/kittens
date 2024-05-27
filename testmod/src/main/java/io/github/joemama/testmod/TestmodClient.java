package io.github.joemama.testmod;

import com.mojang.blaze3d.platform.InputConstants;
import felis.kittens.core.client.ClientEntrypoint;
import felis.kittens.event.GameEvents;
import felis.kittens.keymapping.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TestmodClient implements ClientEntrypoint {
    public static final KeyMapping MY_MAPPING = new KeyMapping("test.key.mapping", InputConstants.Type.KEYSYM, InputConstants.KEY_U, "category1");

    @Override
    public void onClientInit() {
        KeyMappingRegistry.register(MY_MAPPING);
        GameEvents.Client.Render.gui.register(ctx -> {
            if (MY_MAPPING.isDown()) Testmod.LOGGER.info("Stuff happened");
            ctx.getGfx().renderItem(new ItemStack(Items.COBBLED_DEEPSLATE), 0, 0);
        });
        Testmod.LOGGER.info("Initialized");
    }
}
