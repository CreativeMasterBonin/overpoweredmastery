package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
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
import net.rk.overpoweredmastery.OverpoweredMasteryClient;
import net.rk.overpoweredmastery.util.ClientActionHandler;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.List;
import java.util.function.Consumer;

public class PenultimateSwordDark extends Item{
    public PenultimateSwordDark(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.penultimate_sword_dark.desc")
                .withColor(4993695));
        if(ClientActionHandler.keyMappingPressed(OverpoweredMasteryClient.DESCRIPTION_KEY_MAPPING)){
            tooltipAdder.accept(Component.translatable("item.overpoweredmastery.penultimate_sword_dark.desc.detail")
                    .withStyle(ChatFormatting.GRAY));
        }
        else{
            tooltipAdder.accept(Component.translatable("item.overpoweredmastery.press_desc_key",Component.translatable(OverpoweredMasteryClient.DESCRIPTION_KEY_MAPPING.getKey().getName()))
                    .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand){
        List<MobEffectInstance> effects = player.getActiveEffects().stream().toList();
        if(level.isClientSide()){
            if(!effects.isEmpty()){
                List<SoundEvent> randomSoundsToPlay = List.of(
                        SoundEvents.WANDERING_TRADER_DRINK_POTION,
                        SoundEvents.INK_SAC_USE,
                        SoundEvents.WITHER_SKELETON_HURT,
                        SoundEvents.PLAYER_SPLASH_HIGH_SPEED,
                        SoundEvents.ENDER_EYE_LAUNCH,
                        SoundEvents.GILDED_BLACKSTONE_BREAK
                );
                int countOfEffectsBad = 0;
                for(MobEffectInstance instance : effects){
                    if(!instance.getEffect().value().isBeneficial()){
                        countOfEffectsBad++;
                    }
                }
                if(countOfEffectsBad > 0){
                    float randomPitch = level.getRandom().triangle(0.95f,1.0f);
                    player.playSound(randomSoundsToPlay.get(Mth.randomBetweenInclusive(
                            level.getRandom(),
                            0,randomSoundsToPlay.size() - 1)),0.75f,randomPitch);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        else{
            if(!effects.isEmpty()){
                if(level instanceof ServerLevel serverLevel){
                    int countOfEffectsBad = 0;
                    for(MobEffectInstance instance : effects){
                        if(!instance.getEffect().value().isBeneficial()){
                            countOfEffectsBad++;
                            player.removeEffect(instance.getEffect());
                            player.hurtServer(serverLevel,player.damageSources().magic(),0.95f);
                        }
                    }
                    return countOfEffectsBad > 0 ? InteractionResult.SUCCESS_SERVER : InteractionResult.PASS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand){
        Level level = player.level();
        if(level.isClientSide()){
            if(interactionTarget instanceof Animal || interactionTarget instanceof Player){
                player.playSound(SoundEvents.WARDEN_SONIC_BOOM,0.5f, OPUtil.nextFloatBetweenInclusive(0.83f,0.97f));
                return InteractionResult.SUCCESS;
            }
        }
        else{
            if(level instanceof ServerLevel serverLevel){
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
                    player.hurtServer(serverLevel,serverLevel.damageSources().magic(),1.0f);
                    return InteractionResult.SUCCESS_SERVER;
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
                    player.hurtServer(serverLevel,serverLevel.damageSources().magic(),OPUtil.nextFloatBetweenInclusive(1.0f,3.0f));
                    return InteractionResult.SUCCESS_SERVER;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
