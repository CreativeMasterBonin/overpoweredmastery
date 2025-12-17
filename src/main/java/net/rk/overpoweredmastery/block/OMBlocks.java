package net.rk.overpoweredmastery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rk.overpoweredmastery.OverpoweredMastery;

public class OMBlocks{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(OverpoweredMastery.MODID);

    public static final DeferredBlock<Block> MOVING_PROBABLE_BLOCK = BLOCKS.registerBlock("moving_probable_block",
            MovingProbableBlock::new,BlockBehaviour.Properties.of().isValidSpawn(OMBlocks::never)
                    .setId(makeResourceKey("moving_probable_block")));

    public static final DeferredBlock<Block> SELECTION_BLOCK = BLOCKS.registerBlock("selection_block",
            SelectionBlock::new,BlockBehaviour.Properties.of().isValidSpawn(OMBlocks::never)
                    .setId(makeResourceKey("selection_block")));

    public static final DeferredBlock<Block> MULTI_ASSEMBLER = BLOCKS.registerBlock("multi_assembler",
            MultiAssembler::new,BlockBehaviour.Properties.of()
                    .isValidSpawn(OMBlocks::never).isViewBlocking(OMBlocks::never)
                    .isSuffocating(OMBlocks::never).isRedstoneConductor(OMBlocks::never)
                            .lightLevel(light -> 15)
                    .setId(makeResourceKey("multi_assembler")));

    // ores
    public static final DeferredBlock<Block> INERT_BLUE_ESSENCE_ORE = BLOCKS.registerBlock(
            "inert_blue_essence_ore",
            properties -> new InertEssenceOre(
                    0,0,200,
                    1, 1,255,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("inert_blue_essence_ore"))); // water and ice (cold)

    public static final DeferredBlock<Block> INERT_GREEN_ESSENCE_ORE = BLOCKS.registerBlock(
            "inert_green_essence_ore",
            properties -> new InertEssenceOre(0,240,0,
                    2,255,2,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("inert_green_essence_ore"))); // plant and earth

    public static final DeferredBlock<Block> INERT_YELLOW_ESSENCE_ORE = BLOCKS.registerBlock(
            "inert_yellow_essence_ore",
            properties -> new InertEssenceOre(253,253,0,
                    255,255,1,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("inert_yellow_essence_ore"))); // lightning

    public static final DeferredBlock<Block> INERT_ORANGE_ESSENCE_ORE = BLOCKS.registerBlock(
            "inert_orange_essence_ore",
            properties -> new InertEssenceOre(240,240,0,
                    255,245,1,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("inert_orange_essence_ore"))); // air

    public static final DeferredBlock<Block> INERT_RED_ESSENCE_ORE = BLOCKS.registerBlock(
            "inert_red_essence_ore",
            properties -> new InertEssenceOre(240,0,0,
                    255,1,1,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("inert_red_essence_ore"))); // fire and lava

    // special ores
    public static final DeferredBlock<Block> INERT_LIGHT_ESSENCE_ORE = BLOCKS.registerBlock(
            "inert_light_essence_ore",
            properties -> new InertEssenceOre(254,254,254,
                    255,255,255,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("inert_light_essence_ore"))); // light (literal and virtually)

    public static final DeferredBlock<Block> INERT_AURORAN_ESSENCE_ORE = BLOCKS.registerBlock(
            "inert_auroran_essence_ore",
            properties -> new InertEssenceOre(4,240,240,
                    5,255,255,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("inert_auroran_essence_ore"))); // technology

    public static final DeferredBlock<Block> INERT_DARK_ESSENCE_ORE = BLOCKS.registerBlock(
            "inert_dark_essence_ore",
            properties -> new InertEssenceOre(128,0,128,
                    140,5,140,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("inert_dark_essence_ore"))); // dark (literal and virtually)

    // deepslate ores (only found in deep underground areas)
    public static final DeferredBlock<Block> DEEPSLATE_INERT_BLUE_ESSENCE_ORE = BLOCKS.registerBlock(
            "deepslate_inert_blue_essence_ore",
            properties -> new InertEssenceOre(
                    0,0,200,
                    1, 1,255,true,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("deepslate_inert_blue_essence_ore"))); // water and ice (cold)

    public static final DeferredBlock<Block> DEEPSLATE_INERT_GREEN_ESSENCE_ORE = BLOCKS.registerBlock(
            "deepslate_inert_green_essence_ore",
            properties -> new InertEssenceOre(0,240,0,
                    2,255,2,true,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("deepslate_inert_green_essence_ore"))); // plant and earth

    public static final DeferredBlock<Block> DEEPSLATE_INERT_YELLOW_ESSENCE_ORE = BLOCKS.registerBlock(
            "deepslate_inert_yellow_essence_ore",
            properties -> new InertEssenceOre(253,253,0,
                    255,255,1,true,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("deepslate_inert_yellow_essence_ore"))); // lightning

    public static final DeferredBlock<Block> DEEPSLATE_INERT_ORANGE_ESSENCE_ORE = BLOCKS.registerBlock(
            "deepslate_inert_orange_essence_ore",
            properties -> new InertEssenceOre(240,240,0,
                    255,245,1,true,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("deepslate_inert_orange_essence_ore"))); // air

    public static final DeferredBlock<Block> DEEPSLATE_INERT_RED_ESSENCE_ORE = BLOCKS.registerBlock(
            "deepslate_inert_red_essence_ore",
            properties -> new InertEssenceOre(240,0,0,
                    255,1,1,true,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("deepslate_inert_red_essence_ore"))); // fire and lava

    // special deepslate ores (very sparse)
    public static final DeferredBlock<Block> DEEPSLATE_INERT_LIGHT_ESSENCE_ORE = BLOCKS.registerBlock(
            "deepslate_inert_light_essence_ore",
            properties -> new InertEssenceOre(254,254,254,
                    255,255,255,true,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("deepslate_inert_light_essence_ore"))); // light (literal and virtually)

    public static final DeferredBlock<Block> DEEPSLATE_INERT_AURORAN_ESSENCE_ORE = BLOCKS.registerBlock(
            "deepslate_inert_auroran_essence_ore",
            properties -> new InertEssenceOre(4,240,240,
                    5,255,255,true,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("deepslate_inert_auroran_essence_ore"))); // technology

    public static final DeferredBlock<Block> DEEPSLATE_INERT_DARK_ESSENCE_ORE = BLOCKS.registerBlock(
            "deepslate_inert_dark_essence_ore",
            properties -> new InertEssenceOre(128,0,128,
                    140,5,140,true,properties),BlockBehaviour.Properties.of()
                    .setId(makeResourceKey("deepslate_inert_dark_essence_ore"))); // dark (literal and virtually)



    public static ResourceKey<Block> makeResourceKey(String name){
        return ResourceKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,name));
    }
    private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos pos){return true;}
    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos){return false;}
    private static boolean always(BlockState blockState, BlockGetter getter, BlockPos blockPos, EntityType<?> type){return true;}
    private static boolean never(BlockState blockState, BlockGetter getter, BlockPos blockPos, EntityType<?> type){return false;}
}
