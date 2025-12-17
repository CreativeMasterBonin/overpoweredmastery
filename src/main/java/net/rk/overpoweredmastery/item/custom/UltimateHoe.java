package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.item.OMRarity;
import net.rk.overpoweredmastery.resource.OMSoundEvents;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.List;
import java.util.function.Consumer;

public class UltimateHoe extends AbstractSpecialHoe{
    public UltimateHoe(Properties properties) {
        super(ToolMaterial.NETHERITE, 25, 20, properties.rarity(OMRarity.ULTIMATE.getValue())
                .durability(OPUtil.ULTIMATE_SHARED_DURABILITY).fireResistant()
                .enchantable(30).repairable(OMItems.ULTIMATE_INGOT.asItem()));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.ultimate_hoe.desc")
                .withStyle(ChatFormatting.GOLD));
    }

    @Override
    public InteractionResult useOn(UseOnContext context){
        if(context.getPlayer() instanceof ServerPlayer serverPlayer){
            int successfulAttempts = 0;
            boolean isFarmlandChange = false;
            ItemStack offhandStack = serverPlayer.getOffhandItem();

            // use visual hit box for blockpos selection (allows non-solid plants and such to be selectable)
            List<BlockPos> blockPoses = getClippingBlocks(1,1,1, ClipContext.Fluid.NONE, ClipContext.Block.VISUAL,context.getClickedPos(),serverPlayer);
            for(BlockPos blockPos : blockPoses){
                if(context.getLevel().getBlockState(blockPos).is(BlockTags.CROPS) || context.getLevel().getBlockState(blockPos).getBlock() instanceof BonemealableBlock && !serverPlayer.isSecondaryUseActive()){
                    boolean bonemealed = BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL,1),
                            context.getLevel(),blockPos,context.getPlayer()); // fake bonemeal itemstack applied here
                    if(bonemealed){
                        context.getLevel().addParticle(ParticleTypes.HAPPY_VILLAGER,blockPos.getX(),blockPos.getY(),blockPos.getZ(),
                                0,0.75,0);
                        successfulAttempts++;
                        if(context.getLevel() instanceof ServerLevel serverLevel){
                            serverLevel.sendParticles(serverPlayer,ParticleTypes.HAPPY_VILLAGER,
                                    false,true,
                                    blockPos.getX() + 0.5,blockPos.getY() + 1.2,blockPos.getZ() + 0.5,1,
                                    0,0,0,0);
                        }
                        isFarmlandChange = true;
                    }
                }
                if(context.getLevel().getBlockState(blockPos).is(BlockTags.DIRT) && serverPlayer.isSecondaryUseActive()){
                    context.getLevel().setBlockAndUpdate(blockPos, Blocks.FARMLAND.defaultBlockState().setValue(BlockStateProperties.MOISTURE,7));

                    if(context.getLevel() instanceof ServerLevel serverLevel){
                        serverLevel.sendParticles(serverPlayer,ParticleTypes.HAPPY_VILLAGER,
                                false,true,
                                blockPos.getX() + 0.5,blockPos.getY() + 1.2,blockPos.getZ() + 0.5,1,
                                0,0,0,0);
                    }
                    /*
                    if(offhandStack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS) && offhandStack.getItem() instanceof BlockItem blockItem){
                        if(context.getLevel().getBlockState(blockPos).is(Tags.Blocks.VILLAGER_FARMLANDS) || context.getLevel().getBlockState(blockPos).getBlock() instanceof FarmBlock){
                            context.getLevel().setBlock(blockPos.above(2),blockItem.getBlock().defaultBlockState(),3);
                        }
                    }*/
                    successfulAttempts++;
                }
            }
            if(successfulAttempts > 0){
                if(isFarmlandChange){
                    context.getLevel().playSound(null,context.getClickedPos(),
                            OMSoundEvents.EFFECT.get(),SoundSource.PLAYERS,
                            0.34f,context.getLevel().getRandom().triangle(0.97f,1.1f));
                }
                else{
                    context.getLevel().playSound(null,context.getClickedPos(),
                            SoundEvents.VILLAGER_WORK_FARMER,SoundSource.PLAYERS,
                            0.54f,context.getLevel().getRandom().triangle(0.97f,1.2f));
                }
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
