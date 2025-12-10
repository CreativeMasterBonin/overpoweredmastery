package net.rk.overpoweredmastery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.datagen.OMTags;

import java.util.function.Consumer;

public class PenultimateSwordLight extends Item {
    public PenultimateSwordLight(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.penultimate_sword_light.desc")
                .withColor(16764416));
    }

    @Override
    public InteractionResult useOn(UseOnContext context){
        if(!context.getLevel().isClientSide()){
            BlockState state = context.getLevel().getBlockState(context.getClickedPos());
            if(!state.is(OMTags.BANNED_PROBABLE_REWARD_BLOCKS)){
                context.getLevel().setBlock(context.getClickedPos(), Blocks.AIR.defaultBlockState(),3);
                context.getLevel().setBlocksDirty(context.getClickedPos(),state,Blocks.AIR.defaultBlockState());

                makeFallingState(context.getLevel(),context.getClickedPos(),state);
                return InteractionResult.SUCCESS_SERVER;
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    public void makeFallingState(Level level, BlockPos pos, BlockState state){
        FallingBlockEntity fbe = new FallingBlockEntity(level,pos.getX(),pos.getY(),pos.getZ(),state);
        fbe.addDeltaMovement(new Vec3(0D,1.21D,0D));
        fbe.setGlowingTag(true);
        fbe.setPos(pos.getX() + 0.5D,pos.getY(),pos.getZ() + 0.5D);
        level.addFreshEntity(fbe);
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if(player.level() instanceof ServerLevel){
            if(entity instanceof Player && !player.hasEffect(MobEffects.RESISTANCE)){
                player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE,100,10));
                ((Player) entity).addEffect(new MobEffectInstance(MobEffects.SATURATION,10,20,true,false,false));
                return true;
            }
        }
        return false;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if(player.level() instanceof ServerLevel){
            if(interactionTarget instanceof Monster && !player.hasEffect(MobEffects.RESISTANCE)){
                player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE,40,10));
                interactionTarget.hurtServer((ServerLevel)player.level(),player.damageSources().fellOutOfWorld(),20.0f);
            }
            else if(interactionTarget instanceof AbstractIllager && !player.hasEffect(MobEffects.RESISTANCE)){
                player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE,20,20));
                interactionTarget.hurtServer((ServerLevel)player.level(),player.damageSources().fellOutOfWorld(),20.0f);
            }
            else if(interactionTarget instanceof Player){
                interactionTarget.addEffect(new MobEffectInstance(MobEffects.GLOWING,20,1));
                interactionTarget.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,100,4));
                interactionTarget.addEffect(new MobEffectInstance(MobEffects.SATURATION,10,10));
            }
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.PASS;
    }
}
