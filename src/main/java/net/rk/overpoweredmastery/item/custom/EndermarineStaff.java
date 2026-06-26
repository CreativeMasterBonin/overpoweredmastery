package net.rk.overpoweredmastery.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.resource.OMSoundEvents;
import net.rk.overpoweredmastery.util.OPUtil;

public class EndermarineStaff extends AbstractStaff{
    public EndermarineStaff(Properties p) {
        super(p.enchantable(10).durability(OPUtil.ENDERMARINE_STAFF_DURABILITY));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 250;
    }

    @Override
    public void extraOnUse(Level level, LivingEntity livingEntity, ItemStack itemStack, int remainDuration) {
        if(remainDuration < 250){
            if(remainDuration % 15 == 0){
                action(level,livingEntity,itemStack,remainDuration,0);
                itemStack.hurtAndBreak(1,livingEntity,livingEntity.getUsedItemHand());
            }
        }
    }

    @Override
    public void action(Level level, LivingEntity entity, ItemStack stack, int remainingDuration, int stage) {
        if(entity.getRandom().nextIntBetweenInclusive(0,100) <= 1){
            entity.playSound(OMSoundEvents.EFFECT.get(),0.75f,entity.getRandom().triangle(0.95f,1.1f));
        }
        if(level instanceof ServerLevel serverLevel){
            if(serverLevel.getRandom().nextIntBetweenInclusive(0,100) <= 90){
                entity.playSound(SoundEvents.ENDER_PEARL_THROW,0.45f,entity.getRandom().triangle(0.95f,1.1f));
                Projectile.spawnProjectileFromRotation(ThrownEnderpearl::new, serverLevel, new ItemStack(OMItems.INERT_AURORAN_ESSENCE.asItem()), entity, 0.0F, 1.5f, 1.0f);
                if(!entity.hasEffect(MobEffects.REGENERATION)){
                    entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION,50,10,true,false));
                }
            }
        }
    }
}
