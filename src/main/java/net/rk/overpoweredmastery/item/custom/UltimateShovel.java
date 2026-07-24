package net.rk.overpoweredmastery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.datagen.OMTags;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.ArrayList;
import java.util.List;

public class UltimateShovel extends ShovelItem {
    public UltimateShovel(ToolMaterial material, float attackDamage, float attackSpeed, Properties properties) {
        super(material, attackDamage, attackSpeed, properties.fireResistant());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();

        if(player != null && level instanceof ServerLevel serverLevel){
            if(player instanceof ServerPlayer serverPlayer){
                if(serverPlayer.isSecondaryUseActive()){
                    if(serverLevel.getBlockState(context.getClickedPos()).is(OMTags.CAN_EXTRUDE)){
                        List<BlockPos> positions = getClippingBlocks(2,2,3,
                                ClipContext.Fluid.NONE,
                                ClipContext.Block.COLLIDER,
                                context.getClickedPos().relative(context.getHorizontalDirection()),serverPlayer);

                        BlockState blockTypeToExtrude = serverLevel.getBlockState(context.getClickedPos());

                        for(BlockPos pos : positions){
                            // if the position is air (nothing blocking something from being placed)
                            if(serverLevel.getBlockState(pos).is(Blocks.AIR)){

                                serverLevel.setBlock(pos.above(),blockTypeToExtrude,3);

                                serverPlayer.getItemInHand(context.getHand())
                                        .hurtAndBreak(50,serverPlayer,context.getHand());
                                serverLevel.sendParticles(ParticleTypes.END_ROD,
                                        pos.getX() + 0.5D,pos.getY() + 0.5D,pos.getZ() + 0.5D,
                                        1,0D,0D,0D,0.02D);
                                serverLevel.playSound(serverPlayer,pos,
                                        SoundEvents.ILLUSIONER_CAST_SPELL,SoundSource.PLAYERS,
                                        0.5f, OPUtil.nextFloatBetweenInclusive(0.95f,1.1f));
                            }
                        }
                        return InteractionResult.SUCCESS_SERVER;
                    }
                }
            }
        }
        return super.useOn(context);
    }

    // https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Tutorial-1.21.X/blob/main/src/main/java/net/kaupenjoe/tutorialmod/item/custom/HammerItem.java
    // MIT (2024 Kaupenjoe) - edited version (reduced code amount, changed names, added extra functionality and customization)
    public List<BlockPos> getClippingBlocks(int blockRangeX, int blockRangeY, int blockRangeZ, ClipContext.Fluid allowedFluidClipType, ClipContext.Block allowedBlockClipType, BlockPos start, ServerPlayer serverPlayer){
        List<BlockPos> clippingBlocks = new ArrayList<>();

        BlockHitResult result = serverPlayer.level().clip(new ClipContext(
                serverPlayer.getEyePosition(1f),
                (serverPlayer.getEyePosition(1f).add(serverPlayer.getViewVector(1f).scale(6f))),
                allowedBlockClipType,
                allowedFluidClipType,
                serverPlayer
        ));

        // more functionality for staff strength
        Direction resultDir = result.getDirection();
        double resultDist = result.getBlockPos().distToCenterSqr(start.getX(),start.getY(),start.getZ()); // for staff power in future

        if(result.getType() == HitResult.Type.MISS){
            return clippingBlocks;
        }

        // first check x, then y, then z
        for(int x = -blockRangeX; x <= blockRangeX; x++){
            for(int y = -blockRangeY; y <= blockRangeY; y++){
                for(int z = -blockRangeZ; z <= blockRangeZ; z++){
                    if(resultDir == Direction.DOWN || resultDir == Direction.UP){
                        int posX = start.getX() + x;
                        int posY = start.getY() + z;
                        int posZ = start.getZ() + y;
                        clippingBlocks.add(new BlockPos(posX,posY,posZ));
                    }
                    if(resultDir == Direction.NORTH || resultDir == Direction.SOUTH){
                        int posX = start.getX() + x;
                        int posY = start.getY() + y;
                        int posZ = start.getZ() + z;
                        clippingBlocks.add(new BlockPos(posX,posY,posZ));
                    }
                    if(resultDir == Direction.EAST || resultDir == Direction.WEST){
                        int posX = start.getX() + z;
                        int posY = start.getY() + y;
                        int posZ = start.getZ() + x;
                        clippingBlocks.add(new BlockPos(posX,posY,posZ));
                    }
                }
            }
        }

        return clippingBlocks;
    }
}
