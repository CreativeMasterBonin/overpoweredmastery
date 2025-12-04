package net.rk.overpoweredmastery.item.custom;

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
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.entity.custom.TrialWubEnergyBall;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class TrialWubs extends AbstractWubs{
    public TrialWubs(Properties p) {
        super(p.enchantable(27).rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.trial_wubs.desc")
                .withColor(16757617));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 6020;
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
        return 72;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(livingEntity instanceof Player){
            if(!livingEntity.getData(OverpoweredMastery.USING_WUB_ITEM)){
                livingEntity.setData(OverpoweredMastery.USING_WUB_ITEM,true);
            }
            if(level instanceof ServerLevel serverLevel){
                if(remainingUseDuration % 15 == 0){
                    if(remainingUseDuration < 5878 && !((Player) livingEntity).isSecondaryUseActive()){
                        if(!livingEntity.hasEffect(MobEffects.RESISTANCE)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.RESISTANCE,15,40,true,false,true));
                        }
                    }
                    if(remainingUseDuration < 5806 && !((Player)livingEntity).isSecondaryUseActive()){
                        if(!livingEntity.hasEffect(MobEffects.HASTE)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.HASTE,15,10,true,false,true));
                        }
                    }
                    if(remainingUseDuration < 5720 && !((Player)livingEntity).isSecondaryUseActive()){
                        if(!livingEntity.hasEffect(MobEffects.STRENGTH)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.STRENGTH,15,20,true,false,true));
                        }
                    }
                    if(remainingUseDuration < 5650 && !((Player)livingEntity).isSecondaryUseActive()){
                        if(!livingEntity.hasEffect(MobEffects.SATURATION)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.SATURATION,15,2,true,false,true));
                        }
                    }
                    if(remainingUseDuration < 5600 && !((Player) livingEntity).isSecondaryUseActive()){
                        TrialWubEnergyBall wubEnergyBall = new TrialWubEnergyBall(livingEntity,livingEntity.getViewVector(remainingUseDuration),level);
                        wubEnergyBall.setPos(wubEnergyBall.getX(),livingEntity.getEyeY() - 0.5D, wubEnergyBall.getZ());
                        serverLevel.addFreshEntity(wubEnergyBall);
                    }
                }
            }
        }
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {

    }
}
