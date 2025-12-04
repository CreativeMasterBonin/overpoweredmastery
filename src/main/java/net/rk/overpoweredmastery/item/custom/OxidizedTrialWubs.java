package net.rk.overpoweredmastery.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.entity.custom.TrialWubEnergyBall;

import java.util.function.Consumer;

public class OxidizedTrialWubs extends AbstractWubs{
    public OxidizedTrialWubs(Properties p) {
        super(p.enchantable(25).rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.oxidized_trial_wubs.desc")
                .withColor(7390370));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 3510;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 60;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(livingEntity instanceof Player){
            if(!livingEntity.getData(OverpoweredMastery.USING_WUB_ITEM)){
                livingEntity.setData(OverpoweredMastery.USING_WUB_ITEM,true);
            }
            if(level instanceof ServerLevel serverLevel){
                if(remainingUseDuration % 20 == 0 && !((Player)livingEntity).isSecondaryUseActive()){
                    if(remainingUseDuration < 3201){
                        if(livingEntity.hasEffect(MobEffects.HEALTH_BOOST)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST,20,10,true,false,false));
                        }
                        if(!livingEntity.hasEffect(MobEffects.NIGHT_VISION)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION,20,1,true,false,false));
                        }
                    }
                    if(remainingUseDuration < 3107 && remainingUseDuration > 1869){
                        TrialWubEnergyBall wubEnergyBall = new TrialWubEnergyBall(livingEntity,livingEntity.getViewVector(remainingUseDuration),level);
                        wubEnergyBall.setOxidized(false);
                        wubEnergyBall.setPos(wubEnergyBall.getX(),livingEntity.getEyeY() - 0.5D, wubEnergyBall.getZ());
                        serverLevel.addFreshEntity(wubEnergyBall);
                    }
                    if(remainingUseDuration < 1869){
                        TrialWubEnergyBall wubEnergyBall = new TrialWubEnergyBall(livingEntity,livingEntity.getViewVector(remainingUseDuration),level);
                        wubEnergyBall.setOxidized(true);
                        wubEnergyBall.setPos(wubEnergyBall.getX(),livingEntity.getEyeY() - 0.5D, wubEnergyBall.getZ());
                        serverLevel.addFreshEntity(wubEnergyBall);
                    }
                }
            }
        }
    }
}
