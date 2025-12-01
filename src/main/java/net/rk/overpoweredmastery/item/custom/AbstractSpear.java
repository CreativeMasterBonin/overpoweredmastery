package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.OverpoweredMastery;

import java.util.function.Consumer;

public abstract class AbstractSpear extends Item {
    public static final ResourceLocation BASE_SPEAR_REACH = ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"base_spear_reach");
    public AbstractSpear(Properties properties) {
        super(properties.stacksTo(1));
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
        if(level instanceof ServerLevel serverLevel){
            return ItemUtils.startUsingInstantly(level,player,hand);
        }
        else {
            return InteractionResult.SUCCESS;
        }
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
