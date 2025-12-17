package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.item.OMRarity;
import net.rk.overpoweredmastery.resource.OMSoundEvents;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.List;
import java.util.function.Consumer;

public class UltimateSword extends Item {
    public static final ResourceLocation ULTIMATE_SWORD_ATTACK_DAMAGE =
            ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"ultimate_sword_attack_damage");
    public static final ResourceLocation ULTIMATE_SWORD_ATTACK_SPEED =
            ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"ultimate_sword_attack_speed");

    public UltimateSword(Properties properties) {
        super(properties.sword(ToolMaterial.NETHERITE,38,10)
                .enchantable(30).rarity(OMRarity.ULTIMATE.getValue()).durability(OPUtil.ULTIMATE_SHARED_DURABILITY).fireResistant()
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.ATTACK_DAMAGE,
                                new AttributeModifier(ULTIMATE_SWORD_ATTACK_DAMAGE,20.0f,AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND)
                        .add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(ULTIMATE_SWORD_ATTACK_SPEED,1.25f,AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND)
                        .build()).repairable(OMItems.ULTIMATE_INGOT.asItem()));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.ultimate_sword.desc")
                .withStyle(ChatFormatting.GOLD));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        int actionsDone = 0;
        if(entity.level() instanceof ServerLevel serverLevel && entity instanceof ServerPlayer serverPlayer){
            if(!serverPlayer.hasEffect(MobEffects.SATURATION)){
                serverPlayer.addEffect(new MobEffectInstance(MobEffects.SATURATION,20,20,true,false,false));
                actionsDone++;
            }
        }

        if(actionsDone > 0){
            return true;
        }
        return false;
    }


    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        int successfulInteractions = 0;
        if(level instanceof ServerLevel){
            List<MobEffectInstance> effects = player.getActiveEffects().stream().toList();
            List<SoundEvent> randomSoundsToPlay = List.of(
                    SoundEvents.WANDERING_TRADER_DRINK_POTION,
                    SoundEvents.INK_SAC_USE,
                    SoundEvents.WITHER_SKELETON_HURT,
                    SoundEvents.PLAYER_SPLASH_HIGH_SPEED,
                    SoundEvents.ENDER_EYE_LAUNCH,
                    SoundEvents.GILDED_BLACKSTONE_BREAK
            );

            for(MobEffectInstance instance : effects){
                if(!instance.getEffect().value().isBeneficial()){
                    player.removeEffect(instance.getEffect());
                    successfulInteractions++;
                }
            }

            if(successfulInteractions > 0){
                float randomPitch = level.getRandom().triangle(0.95f,1.0f);
                player.playSound(randomSoundsToPlay.get(Mth.randomBetweenInclusive(
                        level.getRandom(),
                        0,randomSoundsToPlay.size() - 1)),0.75f,randomPitch);
            }
            return InteractionResult.SUCCESS_SERVER;
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if(player.level() instanceof ServerLevel serverLevel){
            int successfulInteractions = 0;
            if(interactionTarget instanceof Monster && !player.hasEffect(MobEffects.RESISTANCE)){
                // stop monsters and gain power
                player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE,40,10));
                interactionTarget.hurtServer((ServerLevel)player.level(),player.damageSources().fellOutOfWorld(),20.0f);
                successfulInteractions++;
            }
            else if(interactionTarget instanceof AbstractIllager && !player.hasEffect(MobEffects.RESISTANCE)){
                // stop them enemies and gain power
                player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE,20,20));
                interactionTarget.hurtServer((ServerLevel)player.level(),player.damageSources().fellOutOfWorld(),20.0f);
                successfulInteractions++;
            }
            else if(interactionTarget instanceof Player){
                // share the abilities!
                interactionTarget.addEffect(new MobEffectInstance(MobEffects.GLOWING,20,1));
                interactionTarget.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,100,4));
                interactionTarget.addEffect(new MobEffectInstance(MobEffects.SATURATION,10,10));
                successfulInteractions++;
            }
            else if (interactionTarget instanceof AbstractVillager) {
                // heal villagers without throwing potions at them
                if(!interactionTarget.hasEffect(MobEffects.REGENERATION) && !interactionTarget.hasEffect(MobEffects.RESISTANCE) && !interactionTarget.hasEffect(MobEffects.FIRE_RESISTANCE)){
                    interactionTarget.addEffect(new MobEffectInstance(MobEffects.REGENERATION,100,10));
                    interactionTarget.addEffect(new MobEffectInstance(MobEffects.RESISTANCE,100,10));
                    interactionTarget.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,100,10));
                    successfulInteractions++;
                }
            }
            if(successfulInteractions > 0){
                serverLevel.playSound(player,player.getOnPos(),
                        OMSoundEvents.EFFECT.get(),SoundSource.PLAYERS,
                        0.85f,serverLevel.getRandom().triangle(0.97f,1.0f));
                player.getCooldowns().addCooldown(stack,20);
                return InteractionResult.SUCCESS_SERVER;
            }
            return InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }
}
