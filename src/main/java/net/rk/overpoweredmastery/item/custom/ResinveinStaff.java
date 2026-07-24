package net.rk.overpoweredmastery.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.entity.custom.ResinveinStaffProjectile;
import net.rk.overpoweredmastery.util.OPUtil;

public class ResinveinStaff extends AbstractStaff{
    public ResinveinStaff(Properties p) {
        super(p.rarity(Rarity.UNCOMMON).enchantable(15).durability(OPUtil.RESINVEIN_STAFF_DURABILITY)
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.BLOCK_INTERACTION_RANGE,

                                new AttributeModifier(AbstractStaff.STAFF_BLOCK_REACH_MODIFIER,1,AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND)
                        .add(Attributes.ENTITY_INTERACTION_RANGE,
                                new AttributeModifier(AbstractStaff.STAFF_ENTITY_REACH_MODIFIER,2,AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND)
                        .add(Attributes.SAFE_FALL_DISTANCE,
                                new AttributeModifier(AbstractStaff.STAFF_SAFE_FALL_DISTANCE,32,AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.ANY)
                        .add(Attributes.FALL_DAMAGE_MULTIPLIER,
                                new AttributeModifier(AbstractStaff.STAFF_FALL_DAMAGE_MULTIPLIER,-0.2D,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                                EquipmentSlotGroup.ANY)
                        .build()));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 400;
    }

    @Override
    public void extraOnUse(Level level, LivingEntity livingEntity, ItemStack itemStack, int remainDuration) {
        if(remainDuration < 400){
            if(remainDuration % 7 == 0){
                action(level,livingEntity,itemStack,remainDuration,0);
            }
            if(remainDuration % 2 == 0){
                action(level,livingEntity,itemStack,remainDuration,1);
            }
        }
    }

    @Override
    public void action(Level level, LivingEntity entity, ItemStack stack, int remainingDuration, int stage) {
        if(stage == 0){
            if(level instanceof ServerLevel serverLevel){
                ResinveinStaffProjectile projectile = new ResinveinStaffProjectile(OMEntityTypes.RESINVEIN_STAFF_PROJECTILE.get(),serverLevel);
                projectile.setPos(entity.getX(),entity.getEyeY() + 0.5f,entity.getZ());

                projectile.setDeltaMovement(
                        entity.getViewVector(
                                (float)remainingDuration + 0.75f).normalize().scale(2));
                projectile.needsSync = true;
                serverLevel.addFreshEntity(projectile);

                int flameLevel = stack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel,Enchantments.FLAME));
                int fireAspectLevel = stack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel,Enchantments.FIRE_ASPECT));
                //int witheringLevel = stack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel,)); ???

                if(remainingDuration % 4 == 0 && (flameLevel > 0 || fireAspectLevel > 0)){
                     LargeFireball largeFireball = new LargeFireball(serverLevel,
                            entity,
                            entity.getViewVector((float)remainingDuration + 0.75f),
                            3);
                    largeFireball.setPos(largeFireball.getX(),entity.getEyeY() - 0.15f,largeFireball.getZ());
                    largeFireball.setSilent(true);
                    serverLevel.addFreshEntity(largeFireball);
                }

                stack.hurtAndBreak(2,entity,entity.getUsedItemHand());

                serverLevel.playSound(entity,entity.getOnPos().above(),
                        SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.PLAYERS,0.5f,OPUtil.nextFloatBetweenInclusive(0.91f,1.0f));
            }
        }
        else if(stage == 1){
            if(level instanceof ServerLevel serverLevel){
                if(!entity.hasEffect(MobEffects.SATURATION) && entity.getHealth() <= entity.getMaxHealth() / 2){
                    entity.addEffect(new MobEffectInstance(MobEffects.SATURATION,70,5,true,false));
                    stack.hurtAndBreak(1,entity,entity.getUsedItemHand());
                }
                else if(!entity.hasEffect(MobEffects.RESISTANCE)){
                    entity.addEffect(new MobEffectInstance(MobEffects.RESISTANCE,45,10,true,false));
                    stack.hurtAndBreak(1,entity,entity.getUsedItemHand());
                }
            }
        }
    }
}
