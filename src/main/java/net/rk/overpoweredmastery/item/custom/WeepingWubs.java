package net.rk.overpoweredmastery.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.entity.custom.WeepingWubEnergyBall;

public class WeepingWubs extends AbstractWubs{
    public WeepingWubs(Properties p) {
        super(p.fireResistant().enchantable(32).rarity(Rarity.EPIC).stacksTo(1));
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.NONE;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 3501;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 67;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(livingEntity instanceof Player){
            if(!livingEntity.getData(OverpoweredMastery.USING_WUB_ITEM)){
                livingEntity.setData(OverpoweredMastery.USING_WUB_ITEM,true);
            }
            if(level instanceof ServerLevel serverLevel){
                if(remainingUseDuration < 3101){
                    if(!livingEntity.hasEffect(MobEffects.SLOW_FALLING)){
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING,20,4,true,false));
                    }
                    WeepingWubEnergyBall wweb = new WeepingWubEnergyBall(livingEntity,livingEntity.getViewVector(remainingUseDuration),serverLevel);
                    wweb.setPos(wweb.getX(),livingEntity.getEyeY() - 0.5D,wweb.getZ());
                    serverLevel.addFreshEntity(wweb);
                }
            }
        }
    }
}
