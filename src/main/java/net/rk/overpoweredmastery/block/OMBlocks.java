package net.rk.overpoweredmastery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rk.overpoweredmastery.OverpoweredMastery;

import java.util.function.Function;

public class OMBlocks{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(OverpoweredMastery.MODID);

    public static final DeferredBlock<Block> MOVING_PROBABLE_BLOCK = BLOCKS.registerBlock("moving_probable_block",
            MovingProbableBlock::new,BlockBehaviour.Properties.of().isValidSpawn(OMBlocks::never)
                    .setId(makeResourceKey("moving_probable_block")));

    public static final DeferredBlock<Block> SELECTION_BLOCK = BLOCKS.registerBlock("selection_block",
            SelectionBlock::new,BlockBehaviour.Properties.of().isValidSpawn(OMBlocks::never)
                    .setId(makeResourceKey("selection_block")));


    // ores
    public static final DeferredBlock<Block> INERT_BLUE_ESSENCE_ORE = BLOCKS.registerBlock(
            "inert_blue_essence_ore",
            InertEssenceOre::new,BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("inert_blue_essence_ore")));
    public static final DeferredBlock<Block> INERT_GREEN_ESSENCE_ORE = BLOCKS.registerBlock(
            "inert_green_essence_ore",
            InertEssenceOre::new,BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("inert_green_essence_ore")));


    public static ResourceKey<Block> makeResourceKey(String name){
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,name));
    }

    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos) {
        return true;
    }
    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos) {return false;}
    private static boolean always(BlockState blockState, BlockGetter getter, BlockPos blockPos, EntityType<?> type) {return true;}
    private static boolean never(BlockState blockState, BlockGetter getter, BlockPos blockPos, EntityType<?> type) {return false;}
}
