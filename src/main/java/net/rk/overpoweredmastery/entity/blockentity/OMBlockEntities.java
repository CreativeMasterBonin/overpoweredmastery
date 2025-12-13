package net.rk.overpoweredmastery.entity.blockentity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.block.OMBlocks;

import java.util.function.Supplier;

public class OMBlockEntities{
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE, OverpoweredMastery.MODID);

    public static final Supplier<BlockEntityType<SelectionBlockEntity>> SELECTION_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "selection_block_entity",() ->
                    new BlockEntityType<>(SelectionBlockEntity::new,true,OMBlocks.SELECTION_BLOCK.get()));

    public static final Supplier<BlockEntityType<MultiAssemblerBlockEntity>> MULTI_ASSEMBLER_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            "multi_assembler",() ->
                    new BlockEntityType<>(MultiAssemblerBlockEntity::new,false,OMBlocks.MULTI_ASSEMBLER.get()));

}
