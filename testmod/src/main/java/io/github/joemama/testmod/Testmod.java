package io.github.joemama.testmod;

import felis.kittens.core.CommonEntrypoint;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Testmod implements CommonEntrypoint {
    public static final Item TEST_ITEM = new Item(new Item.Properties());
    public static final Logger LOGGER = LoggerFactory.getLogger(Testmod.class);
    public static final Block AW_TEST = new StairBlock(Blocks.BLACK_WOOL.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_WOOL));

    @Override
    public void onInit() {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("testmod", "test_item"), TEST_ITEM);
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation("testmod", "aw_test"), AW_TEST);
        // TODO: Once registry sync is added this need to be removed, furthermore, this technically doesn't work on servers
        AW_TEST.getStateDefinition().getPossibleStates().forEach(Block.BLOCK_STATE_REGISTRY::add);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation("testmod", "aw_test"), new BlockItem(AW_TEST, new Item.Properties()));
        // GameEvents.Player.tick.end.register(player -> LOGGER.info(Objects.requireNonNull(player.getDisplayName()).getString()));
    }
}
