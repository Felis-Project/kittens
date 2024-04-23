package io.github.joemama.testmod;

import felis.kittens.core.CommonEntrypoint;
import felis.kittens.core.event.GameEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class Testmod implements CommonEntrypoint {
    public static final Item TEST_ITEM = new Item(new Item.Properties());
    public static final Logger LOGGER = LoggerFactory.getLogger(Testmod.class);

    @Override
    public void onInit() {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("testmod", "test_item"), TEST_ITEM);
        GameEvents.Player.Tick.end.register(player -> LOGGER.info(Objects.requireNonNull(player.getDisplayName()).getString()));
    }
}
