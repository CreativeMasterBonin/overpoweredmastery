package net.rk.overpoweredmastery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.Tags;
import net.rk.overpoweredmastery.datagen.OMTags;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.List;

public class SpearItem extends AbstractSpear{
    public SpearItem(Properties properties) {
        super(properties
                .component(DataComponents.WEAPON,new Weapon(1,2))
                .component(DataComponents.BREAK_SOUND,Holder.direct(SoundEvents.ARMOR_EQUIP_GENERIC.value()))
                .component(DataComponents.ENCHANTABLE,new Enchantable(5))
                .repairable(Items.LEATHER)
                .durability(1999)
                .tool(ToolMaterial.GOLD, BlockTags.SWORD_INSTANTLY_MINES,1,2,1)
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.ENTITY_INTERACTION_RANGE,
                                new AttributeModifier(BASE_SPEAR_REACH,
                                        2.5f,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND)
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,2.0f,AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,2.0f,AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HAND)
                .build()));
    }

    public SpearItem(Properties properties, ResourceKey id,
                     int damage, float speed,
                     Weapon weapon, Holder<SoundEvent> breakSound,
                     Enchantable enchantable, Tool tool,
                     int usesTillBreak, Item repairItem) {
        super(properties
                .setId(id)
                .component(DataComponents.WEAPON,weapon)
                .component(DataComponents.BREAK_SOUND,breakSound)
                .component(DataComponents.ENCHANTABLE,enchantable)
                .component(DataComponents.TOOL,tool)
                .repairable(repairItem)
                .durability(usesTillBreak)
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.ENTITY_INTERACTION_RANGE,
                                new AttributeModifier(BASE_SPEAR_REACH,
                                        2.5f,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND)
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID,damage,AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HAND)
                .add(Attributes.ATTACK_SPEED,
                        new AttributeModifier(Item.BASE_ATTACK_SPEED_ID,speed,AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HAND)
                .build()));
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment){
        if(enchantment.is(OMTags.SPEAR_SUPPORTED) || enchantment.is(Enchantments.MENDING) || enchantment.is(Tags.Enchantments.INCREASE_ENTITY_DROPS) || enchantment.is(Tags.Enchantments.WEAPON_DAMAGE_ENHANCEMENTS)){
            return true;
        }
        return false;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 1700;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand){
        if(entity instanceof Player player){
            if(player.getCooldowns().isOnCooldown(stack)){
                entity.stopUsingItem();
                return false;
            }
        }

        if(entity.getUseItemRemainingTicks() <= 40){
            if(stack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(entity.level(),Enchantments.RIPTIDE)) > 0 && entity.isSwimming()){
                int riptideLevel = stack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(entity.level(),Enchantments.RIPTIDE));
                entity.addDeltaMovement(entity.getLookAngle()
                        .multiply(1.0 + riptideLevel, 0.0, 1.0 + riptideLevel)
                        .normalize()
                        .scale(0.41 / (entity.getAttributeValue(Attributes.MOVEMENT_EFFICIENCY) + 1.0f)));
                stack.hurtAndBreak(riptideLevel + 1,entity,entity.getUsedItemHand());
                if(entity instanceof Player player){
                    player.getCooldowns().addCooldown(stack,15);
                }
                return true;
            }
            return true;
        }
        return false;
    }

    // go for the gold! make the player feel tired by adding a cooldown after the sprint is completed (could be any length)
    @Override
    public void onStopUsing(ItemStack stack, LivingEntity entity, int count) {
        if(entity instanceof Player player){
            int timeAccumulated = (int)(entity.getTicksUsingItem() / 5f);
            int clampedTime = Mth.clamp(20 + timeAccumulated,20,200);
            player.getCooldowns().addCooldown(stack,clampedTime);
        }
    }

    @Override
    public void extraOnUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int remainingDuration) {
        if(livingEntity instanceof Player player){
            if(player.getCooldowns().isOnCooldown(itemStack)){
                livingEntity.stopUsingItem();
                return;
            }
        }

        if(livingEntity.getUseItemRemainingTicks() <= 0){
            livingEntity.stopUsingItem();
            return;
        }
        if(remainingDuration >= 40 && !livingEntity.isSwimming()){
            double effSpeed = livingEntity.getAttributeValue(Attributes.MOVEMENT_EFFICIENCY);
            if(level.isRainingAt(livingEntity.getOnPos().above()) && itemStack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(livingEntity.level(),Enchantments.RIPTIDE)) > 0){
                if(livingEntity.getDeltaMovement().x > -20.0D && livingEntity.getDeltaMovement().x < 20.0D && livingEntity.getDeltaMovement().z > -20.0D && livingEntity.getDeltaMovement().z < 20.0D && livingEntity.getDeltaMovement().y > -20.0D && livingEntity.getDeltaMovement().y < 20.0D){
                    livingEntity.addDeltaMovement(livingEntity.getLookAngle()
                            .multiply(0.75, 0.75, 0.75)
                            .normalize()
                            .scale(0.2 / (effSpeed + 1.0f)));
                }
            }
            else{
                if(!livingEntity.isJumping() && livingEntity.onGround()){
                    livingEntity.addDeltaMovement(livingEntity.getLookAngle()
                            .multiply(1.0, 0.0, 1.0)
                            .normalize()
                            .scale(0.2 / (effSpeed + 1.0f)));
                }
            }
            if(livingEntity.getDeltaMovement().x != 0.00000000f || livingEntity.getDeltaMovement().z != 0.00000000f){
                itemStack.hurtAndBreak(1,livingEntity,livingEntity.getUsedItemHand());
            }
        }
        AABB boxToCheckForEntities = AABB.ofSize(livingEntity.getOnPos().getCenter(),4,2,4);
        List<BlockPos> blockPoses = BlockPos.betweenClosedStream(boxToCheckForEntities).toList();

        if(!level.getEntities(livingEntity,boxToCheckForEntities, EntitySelector.NO_CREATIVE_OR_SPECTATOR).isEmpty()){
            List<Entity> entities = level.getEntities(livingEntity,boxToCheckForEntities,EntitySelector.NO_CREATIVE_OR_SPECTATOR);
            int dividen = this.getUseDuration(itemStack,livingEntity) / 2;
            for(Entity entity : entities){
                if(livingEntity.isUsingItem()){
                    int deltaBasedDamage = 1 + (int)Math.round(livingEntity.getDeltaMovement().x * livingEntity.getDeltaMovement().z);
                    if(itemStack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level,Enchantments.IMPALING)) > 0){
                        deltaBasedDamage += itemStack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level,Enchantments.IMPALING));
                    }
                    entity.hurt(livingEntity.damageSources().generic(),deltaBasedDamage);
                    itemStack.hurtAndBreak(deltaBasedDamage,livingEntity,livingEntity.getUsedItemHand());
                }
            }
        }
        if(!blockPoses.isEmpty()){
            for(BlockPos pos: blockPoses){
                if(level.getBlockState(pos).is(OMTags.CORRECT_FOR_SPEAR)){
                    level.destroyBlock(pos,true,livingEntity);
                    itemStack.hurtAndBreak(1,livingEntity,livingEntity.getUsedItemHand());
                }
            }
        }
    }
}
