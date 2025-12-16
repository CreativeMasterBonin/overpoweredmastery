package net.rk.overpoweredmastery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.item.OMRarity;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.List;

public class UltimateHoe extends AbstractSpecialHoe{
    public UltimateHoe(Properties properties) {
        super(ToolMaterial.NETHERITE, 25, 20, properties.rarity(OMRarity.ULTIMATE.getValue())
                .durability(OPUtil.ULTIMATE_SHARED_DURABILITY).fireResistant()
                .enchantable(30).repairable(OMItems.ULTIMATE_INGOT.asItem()));
    }

    @Override
    public InteractionResult useOn(UseOnContext context){
        if(context.getPlayer() instanceof ServerPlayer serverPlayer){
            int successfulAttempts = 0;
            List<BlockPos> blockPoses = getClippingBlocks(1,1,1, ClipContext.Fluid.NONE, ClipContext.Block.VISUAL,context.getClickedPos(),serverPlayer);
            for(BlockPos blockPos : blockPoses){
                if(context.getLevel().getBlockState(blockPos).is(BlockTags.DIRT)){
                    context.getLevel().setBlockAndUpdate(blockPos, Blocks.FARMLAND.defaultBlockState().setValue(BlockStateProperties.MOISTURE,7));
                    successfulAttempts++;
                }
                if(context.getLevel().getBlockState(blockPos).is(BlockTags.CROPS)){
                    boolean bonemealed = BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL,1),
                            context.getLevel(),blockPos,context.getPlayer());
                    if(bonemealed){
                        successfulAttempts++;
                    }
                }
            }
            if(successfulAttempts > 0){
                context.getLevel().playSound(null,context.getClickedPos(),
                        SoundEvents.VILLAGER_WORK_FARMER,SoundSource.PLAYERS,
                        0.94f,context.getLevel().getRandom().triangle(0.95f,1.0f));
                if(context.getLevel().isClientSide()){
                    return InteractionResult.SUCCESS;
                }
                else{
                    return InteractionResult.SUCCESS_SERVER;
                }
            }
        }
        return super.useOn(context);
    }
}
