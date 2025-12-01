package net.rk.overpoweredmastery.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public class PenultimateSwordDark extends Item{
    public PenultimateSwordDark(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.penultimate_sword_dark.desc")
                .withColor(4993695));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(level instanceof ServerLevel){
            if(player.hasEffect(MobEffects.DARKNESS)){
                player.removeEffect(MobEffects.DARKNESS);
                player.playSound(SoundEvents.INK_SAC_USE,0.75f,1.0f);
                player.hurtServer((ServerLevel)level,player.damageSources().magic(),2.0f);
            }
            if(player.hasEffect(MobEffects.WITHER)){
                player.removeEffect(MobEffects.WITHER);
                player.playSound(SoundEvents.WITHER_SKELETON_HURT,0.75f,1.0f);
                player.hurtServer((ServerLevel)level,player.damageSources().magic(),2.0f);
            }
            if(player.hasEffect(MobEffects.SLOWNESS)){
                player.removeEffect(MobEffects.SLOWNESS);
                player.playSound(SoundEvents.PLAYER_SPLASH_HIGH_SPEED,0.75f,1.0f);
                player.hurtServer((ServerLevel)level,player.damageSources().magic(),2.0f);
            }
            if(player.hasEffect(MobEffects.BLINDNESS)){
                player.removeEffect(MobEffects.BLINDNESS);
                player.playSound(SoundEvents.ENDER_EYE_LAUNCH,0.75f,1.0f);
                player.hurtServer((ServerLevel)level,player.damageSources().magic(),2.0f);
            }
            if(player.hasEffect(MobEffects.HUNGER)){
                player.removeEffect(MobEffects.HUNGER);
                player.playSound(SoundEvents.GENERIC_EAT.value(),0.75f,1.0f);
                player.hurtServer((ServerLevel)level,player.damageSources().magic(),2.0f);
            }
            if(player.hasEffect(MobEffects.MINING_FATIGUE)){
                player.removeEffect(MobEffects.MINING_FATIGUE);
                player.playSound(SoundEvents.GILDED_BLACKSTONE_BREAK,0.75f,1.0f);
                player.hurtServer((ServerLevel)level,player.damageSources().magic(),2.0f);
            }
            return InteractionResult.SUCCESS_SERVER;
        }
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand){
        if(interactionTarget instanceof Animal){
            interactionTarget.addEffect(new MobEffectInstance(
                    MobEffects.SLOWNESS,
                    200,30,
                    true,false));
            interactionTarget.addEffect(new MobEffectInstance(
                    MobEffects.WEAKNESS,
                    200,30,
                    true,false));
            interactionTarget.addEffect(new MobEffectInstance(
                    MobEffects.WITHER,
                    200,10,
                    true,false));
            interactionTarget.addEffect(new MobEffectInstance(
                    MobEffects.HUNGER,
                    40,5,
                    true,false));
            interactionTarget.addEffect(new MobEffectInstance(
                    MobEffects.MINING_FATIGUE,
                    40,10,
                    true,false));
            player.swing(usedHand);
            return InteractionResult.CONSUME;
        }
        else if(interactionTarget instanceof Player){
            interactionTarget.addEffect(new MobEffectInstance(
                    MobEffects.WEAKNESS,
                    200,2,
                    true,false));
            interactionTarget.addEffect(new MobEffectInstance(
                    MobEffects.UNLUCK,
                    200,20,
                    true,false,false));
        }
        return InteractionResult.PASS;
    }
}
