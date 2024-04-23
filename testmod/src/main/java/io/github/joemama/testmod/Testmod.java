package io.github.joemama.testmod;

import felis.kittens.core.CommonEntrypoint;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class Testmod implements CommonEntrypoint {
    public static final Item TEST_ITEM = new Item(new Item.Properties());
    @Override
    public void onInit() {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("testmod", "test_item"), TEST_ITEM);
    }
}
