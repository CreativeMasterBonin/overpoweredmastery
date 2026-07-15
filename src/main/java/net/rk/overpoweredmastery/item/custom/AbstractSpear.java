package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.KineticWeapon;
import net.minecraft.world.item.component.PiercingWeapon;
import net.minecraft.world.item.component.SwingAnimation;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.OverpoweredMastery;

import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractSpear extends Item {
    public static final Identifier BASE_SPEAR_REACH = Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,"base_spear_reach");
    public AbstractSpear(Properties properties) {
        super(properties.stacksTo(1).component(DataComponents.SWING_ANIMATION,
                new SwingAnimation(SwingAnimationType.STAB,8)));
    }

    public AbstractSpear(Properties properties,
                         Holder<SoundEvent> useSound,
                         Holder<SoundEvent> attackSound,
                         Holder<SoundEvent> hitSound,
                         float damageMultiplier,
                         float delay,
                         float dismountMaxDuration,
                         float dismountMinSpeed,
                         float knockbackMaxDuration,
                         float knockbackMinSpeed,
                         float damageMaxDuration,
                         float damageMinSpeed){
        super(properties.stacksTo(1).component(DataComponents.SWING_ANIMATION,
                new SwingAnimation(SwingAnimationType.STAB,8))
                .component(
                        DataComponents.KINETIC_WEAPON,
                        new KineticWeapon(
                                10,
                                (int)(delay * 7.0f),
                                KineticWeapon.Condition.ofAttackerSpeed((int)(dismountMaxDuration * 7.0f), dismountMinSpeed),
                                KineticWeapon.Condition.ofAttackerSpeed((int)(knockbackMaxDuration * 7.0f), knockbackMinSpeed),
                                KineticWeapon.Condition.ofRelativeSpeed((int)(damageMaxDuration * 7.0f), damageMinSpeed),
                                0.47f,
                                damageMultiplier,
                                Optional.of(useSound),
                                Optional.of(hitSound)
                        )
                )
                .component(
                        DataComponents.PIERCING_WEAPON,
                        new PiercingWeapon(
                                true,
                                true,
                                Optional.of(attackSound),
                                Optional.of(hitSound)
                        )
                ));
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.SPEAR;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        return stack;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.spear.desc")
                .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(level.isClientSide()){
            player.playSound(SoundEvents.SPEAR_USE.value());
            return InteractionResult.SUCCESS;
        }
        else{
            if(level instanceof ServerLevel serverLevel){
                return ItemUtils.startUsingInstantly(level,player,hand);
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        return true;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration){
        extraOnUseTick(level,livingEntity,stack,remainingUseDuration);
    }

    public abstract void extraOnUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int remainingDuration);
}
