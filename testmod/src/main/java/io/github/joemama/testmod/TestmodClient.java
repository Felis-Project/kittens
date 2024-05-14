package io.github.joemama.testmod;

import com.mojang.blaze3d.platform.InputConstants;
import felis.kittens.core.client.ClientEntrypoint;
import felis.kittens.event.GameEvents;
import felis.kittens.keymapping.KeyMappingRegistry;
import net.minecraft.client.KeyMapping;

public class TestmodClient implements ClientEntrypoint {
    public static final KeyMapping MY_MAPPING = new KeyMapping("Stuff and things", InputConstants.Type.KEYSYM, InputConstants.KEY_U, "category1");

    @Override
    public void onClientInit() {
        KeyMappingRegistry.register(MY_MAPPING);
        GameEvents.Client.Render.gui.register(ctx -> {
            if (MY_MAPPING.consumeClick()) Testmod.LOGGER.info("Stuff happened");
        });
        Testmod.LOGGER.info("Initialized");
    }
}
