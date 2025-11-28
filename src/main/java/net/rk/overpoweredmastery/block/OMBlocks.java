package net.rk.overpoweredmastery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rk.overpoweredmastery.OverpoweredMastery;

import java.util.function.Function;

public class OMBlocks{
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(OverpoweredMastery.MODID);

    public static final DeferredBlock<Block> MOVING_PROBABLE_BLOCK = BLOCKS.registerBlock("moving_probable_block",
            MovingProbableBlock::new);

    public static <T extends Block> DeferredBlock<T> registerBlockWithoutItem(String name, Function<BlockBehaviour.Properties, T> function){
        DeferredBlock<T> block = BLOCKS.registerBlock(name, function);
        return block;
    }

    private static boolean always(BlockState bs, BlockGetter bg, BlockPos bp){return true;}
    private static boolean never(BlockState bs, BlockGetter bg, BlockPos bp){return false;}
}
