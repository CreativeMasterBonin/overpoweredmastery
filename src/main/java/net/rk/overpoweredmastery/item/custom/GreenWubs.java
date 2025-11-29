package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.neoforged.neoforge.common.Tags;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.entity.custom.GreenWubEnergyBall;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class GreenWubs extends AbstractWubs {
    public GreenWubs(Properties p) {
        super(p.fireResistant().enchantable(22).rarity(Rarity.EPIC).stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.green_wubs.desc").withStyle(ChatFormatting.GREEN));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 1500;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.NONE;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        livingEntity.setData(OverpoweredMastery.USING_WUB_ITEM,false);
        return stack;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        return true;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(livingEntity instanceof Player){
            if(!livingEntity.getData(OverpoweredMastery.USING_WUB_ITEM)){
                livingEntity.setData(OverpoweredMastery.USING_WUB_ITEM,true);
            }
            if(level instanceof ServerLevel serverLevel){
                if(remainingUseDuration % 15 == 0 && remainingUseDuration < 1498 && !((Player) livingEntity).isSecondaryUseActive()){
                    GreenWubEnergyBall fireball = new GreenWubEnergyBall(livingEntity,livingEntity.getViewVector(remainingUseDuration),level);
                    fireball.setPos(fireball.getX(),livingEntity.getEyeY() - 0.5D,fireball.getZ());
                    serverLevel.addFreshEntity(fireball);

                    if(!livingEntity.hasEffect(MobEffects.SPEED)){
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.SPEED,14,12,true,false));
                    }
                }
            }
        }
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
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() instanceof Item;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 42;
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        if(shooter instanceof Player){

        }
    }
}
