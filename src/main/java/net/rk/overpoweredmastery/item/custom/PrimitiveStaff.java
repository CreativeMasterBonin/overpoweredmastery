package net.rk.overpoweredmastery.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.hurtingprojectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.resource.OMSoundEvents;
import net.rk.overpoweredmastery.util.OPUtil;

public class PrimitiveStaff extends AbstractStaff{
    public PrimitiveStaff(Properties p) {
        super(p.enchantable(2).durability(OPUtil.PRIMITIVE_STAFF_DURABILITY)
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(AbstractStaff.STAFF_MOVEMENT_SPEED_MODIFIER,0.01f,AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND)
                        .add(Attributes.SAFE_FALL_DISTANCE,
                                new AttributeModifier(AbstractStaff.STAFF_SAFE_FALL_DISTANCE,2,AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.ANY)
                        .build()));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 120;
    }

    @Override
    public void extraOnUse(Level level, LivingEntity livingEntity, ItemStack itemStack, int remainDuration) {
        if(remainDuration < 120){
            if(remainDuration % 20 == 0){
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
            if(serverLevel.getRandom().nextIntBetweenInclusive(0,100) <= 89){
                entity.playSound(SoundEvents.FIRECHARGE_USE,0.45f,entity.getRandom().triangle(0.95f,1.1f));
                SmallFireball fireball = new SmallFireball(level,entity,entity.getViewVector(5));
                fireball.setPos(fireball.getX(),entity.getEyeY(),fireball.getZ());
                serverLevel.addFreshEntity(fireball);
            }
        }
    }
}
