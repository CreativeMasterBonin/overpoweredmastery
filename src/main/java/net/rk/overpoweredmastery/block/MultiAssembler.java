package net.rk.overpoweredmastery.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.rk.overpoweredmastery.entity.blockentity.MultiAssemblerBlockEntity;
import net.rk.overpoweredmastery.entity.blockentity.OMBlockEntities;
import org.jetbrains.annotations.Nullable;

public class MultiAssembler extends BaseEntityBlock{
    public static final MapCodec<MultiAssembler> CODEC = simpleCodec(MultiAssembler::new);

    public MultiAssembler(Properties properties){
        super(properties.strength(1.2f,50f));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(!level.isClientSide()){
            if(level.getBlockEntity(pos) instanceof MultiAssemblerBlockEntity mabe){
                if(player.isSecondaryUseActive()){
                    mabe.dropAll();
                    return InteractionResult.SUCCESS_SERVER;
                }
                ((ServerPlayer)player).openMenu(new SimpleMenuProvider(mabe,
                        Component.translatable("screen.overpoweredmastery.multi_assembler")),pos);
            }
            else{
                throw new IllegalStateException("Overpowered Mastery - MultiAssembler Container Provider missing");
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return OMBlockEntities.MULTI_ASSEMBLER_BLOCK_ENTITY.get().create(pos,state);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == OMBlockEntities.MULTI_ASSEMBLER_BLOCK_ENTITY.get() ? (BlockEntityTicker<T>) MultiAssemblerBlockEntity::serverTick : null;
    }
}
