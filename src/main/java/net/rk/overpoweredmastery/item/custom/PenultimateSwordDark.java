package net.rk.overpoweredmastery.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PenultimateSwordDark extends Item{
    public PenultimateSwordDark(Properties properties) {
        super(properties);
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
            interactionTarget.addEffect(new MobEffectInstance(
                    MobEffects.UNLUCK,
                    80,20,
                    false,true,true));
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }
}
