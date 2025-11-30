package net.rk.overpoweredmastery.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class SpearItem extends AbstractSpear{
    public SpearItem(Properties properties) {
        super(properties.stacksTo(1).durability(750));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 1700;
    }

    @Override
    public void extraOnUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int remainingDuration) {
        if(livingEntity.getUseItemRemainingTicks() <= 0){
            livingEntity.stopUsingItem();
            return;
        }
        if(!livingEntity.hasEffect(MobEffects.SPEED)){
            livingEntity.addEffect(new MobEffectInstance(MobEffects.SPEED,2,20,true,false,false));
            livingEntity.setSprinting(true);
            itemStack.hurtAndBreak(1,livingEntity,livingEntity.getUsedItemHand());
        }
        AABB boxToCheckForEntities = AABB.ofSize(livingEntity.getOnPos().getCenter(),4,2,4);
        if(!level.getEntities(livingEntity,boxToCheckForEntities, EntitySelector.NO_CREATIVE_OR_SPECTATOR).isEmpty()){
            List<Entity> entities = level.getEntities(livingEntity,boxToCheckForEntities,EntitySelector.NO_CREATIVE_OR_SPECTATOR);
            int dividen = this.getUseDuration(itemStack,livingEntity) / 2;
            for(Entity entity : entities){
                if(livingEntity.isUsingItem()){
                    int deltaBasedDamage = 1 + (int)Math.round(livingEntity.getDeltaMovement().x * livingEntity.getDeltaMovement().z);
                    entity.hurt(livingEntity.damageSources().generic(),deltaBasedDamage);
                    itemStack.hurtAndBreak(deltaBasedDamage,livingEntity,livingEntity.getUsedItemHand());
                }
            }
        }
    }
}
