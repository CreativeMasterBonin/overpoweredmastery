package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.illager.AbstractIllager;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MaceItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.datagen.OMEnchantments;
import net.rk.overpoweredmastery.item.OMRarity;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class UltimateMace extends MaceItem {
    public UltimateMace(Properties properties) {
        super(properties.rarity(OMRarity.ULTIMATE.getValue())
                .fireResistant()
                .enchantable(30)
                .durability(500 + OPUtil.ULTIMATE_SHARED_DURABILITY)
        );
    }

    public static ItemAttributeModifiers createAttributes() {
        return ItemAttributeModifiers.builder()
                .add(
                        Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, 20.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .add(
                        Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, -1.3F, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND
                )
                .build();
    }

    public static Tool createToolProperties(){return new Tool(List.of(), 2.0f, 1, false);}

    @Override
    public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (canSmashAttack(attacker)) {
            ServerLevel serverlevel = (ServerLevel)attacker.level();
            attacker.setDeltaMovement(attacker.getDeltaMovement().with(Direction.Axis.Y, 0.01F));

            int lootingLevel = attacker.getItemInHand(attacker.getUsedItemHand()).getEnchantmentLevel(
                    OPUtil.getEnchantmentHolderFromKeyStatic(serverlevel,Enchantments.LOOTING));

            int clampedLogicalHeight = Mth.clamp(serverlevel.getLogicalHeight(),25,72);
            int requiredHeightForBonus = Mth.clamp(20 * lootingLevel,20,clampedLogicalHeight);


            if (attacker instanceof ServerPlayer serverplayer) {
                serverplayer.currentImpulseImpactPos = this.calculateImpactPosServer(serverplayer);
                serverplayer.setIgnoreFallDamageFromCurrentImpulse(true);
                serverplayer.connection.send(new ClientboundSetEntityMotionPacket(serverplayer));
            }

            if (target.onGround()) {
                if (attacker instanceof ServerPlayer serverPlayerAttacker) {
                    serverPlayerAttacker.setSpawnExtraParticlesOnFall(true);
                }

                SoundEvent soundevent = attacker.fallDistance > 5.0 ? SoundEvents.MACE_SMASH_GROUND_HEAVY : SoundEvents.MACE_SMASH_GROUND;
                if(lootingLevel > 0 && attacker.fallDistance > (int)Math.round(requiredHeightForBonus * 0.5) + 1){
                    serverlevel.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), soundevent, attacker.getSoundSource(), 1.0f, 0.75f);
                }
                else{
                    serverlevel.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), soundevent, attacker.getSoundSource(), 1.0f, 1.0f);
                }
            } else {
                serverlevel.playSound(
                        null, attacker.getX(), attacker.getY(), attacker.getZ(), SoundEvents.MACE_SMASH_AIR, attacker.getSoundSource(), 1.0F, 1.0F
                );
            }

            knockbackTargetServer(serverlevel, attacker, target);
        }
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if(!entity.hasEffect(MobEffects.LEVITATION) && entity.fallDistance > entity.getMaxFallDistance()){
            entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION,5,1,true,false));
            return true;
        }
        return false;
    }

    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if(!attacker.hasEffect(MobEffects.REGENERATION)){
            attacker.addEffect(new MobEffectInstance(MobEffects.REGENERATION,10,20,true,false));
        }
    }

    public Vec3 calculateImpactPosServer(ServerPlayer player){
        return player.currentImpulseImpactPos != null
                && player.currentImpulseImpactPos.y <= player.position().y
                ? player.currentImpulseImpactPos
                : player.position();
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(Enchantments.MENDING) ||
                enchantment.is(Enchantments.UNBREAKING) ||
                enchantment.is(Enchantments.DENSITY) ||
                enchantment.is(Enchantments.WIND_BURST) ||
                enchantment.is(Enchantments.DENSITY) ||
                enchantment.is(Enchantments.BREACH) ||
                enchantment.is(Enchantments.SMITE) ||
                enchantment.is(Enchantments.BANE_OF_ARTHROPODS) ||
                enchantment.is(Enchantments.FIRE_ASPECT) ||
                enchantment.is(Enchantments.LOOTING) ||
                enchantment.is(Enchantments.THORNS) ||
                enchantment.is(Enchantments.CHANNELING) ||
                enchantment.is(OMEnchantments.INSTAREPAIR);
    }

    // derived from knockbackTarget, but with added features
    public void knockbackTargetServer(ServerLevel serverLevel, LivingEntity attacker, LivingEntity target){
        // this is where the particles after you hit the target come from
        serverLevel.levelEvent(2013, target.getOnPos(), 750);
        // find entities in range and apply knockback to them all
        serverLevel.getEntitiesOfClass(LivingEntity.class, target.getBoundingBox().inflate(12.712),knockbackPredicate(attacker, target))
                .forEach(entity -> {
                    int fireAspectLevel = attacker.getItemInHand(attacker.getUsedItemHand()).getEnchantmentLevel(
                            OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel,Enchantments.FIRE_ASPECT));
                    int lootingLevel = attacker.getItemInHand(attacker.getUsedItemHand()).getEnchantmentLevel(
                            OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel,Enchantments.LOOTING));

                    int channelingLevel = attacker.getItemInHand(attacker.getUsedItemHand()).getEnchantmentLevel(
                            OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel,Enchantments.CHANNELING));
                    int thornsLevel = attacker.getItemInHand(attacker.getUsedItemHand()).getEnchantmentLevel(
                            OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel,Enchantments.THORNS));

                    if(fireAspectLevel > 0 && !entity.fireImmune()){
                        entity.setRemainingFireTicks(40 * fireAspectLevel);
                    }

                    if(channelingLevel > 0 && serverLevel.isThundering()){
                        LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT,serverLevel);
                        lightningBolt.setPos(entity.getX(),entity.getY(),entity.getZ());
                        serverLevel.addFreshEntity(lightningBolt);
                    }

                    if(thornsLevel > 0){
                        entity.hurtServer(serverLevel,serverLevel.damageSources().thorns(entity),OPUtil.nextFloatBetweenInclusive(1.0f,3.0f));
                        // placing cactus is not too great of a feature
                        /*if(thornsLevel >= 10){
                            serverLevel.setBlock(new BlockPos(entity.getBlockX(),entity.getBlockY(),entity.getBlockZ()),
                                    Blocks.CACTUS.defaultBlockState(),3);
                        }*/
                    }

                    // the world logical height dictates the looting height requirement afterward
                    // however this clamping is lower than the true height limit of a world
                    int clampedLogicalHeight = Mth.clamp(serverLevel.getLogicalHeight(),25,72);
                    int requiredHeightForBonus = Mth.clamp(20 * lootingLevel,20,clampedLogicalHeight);
                    boolean entityIsOkToHit = !(entity instanceof TamableAnimal) && (entity instanceof Mob || entity instanceof AbstractPiglin || entity instanceof AbstractIllager);
                    // looting could be modded to be 255+, so clamp that value here
                    if(lootingLevel > 0 && attacker.fallDistance > (int)Math.round(requiredHeightForBonus * 0.5) + 1 && entityIsOkToHit){
                        // a reward for putting effort in the ultimate mace with looting
                        Optional<ResourceKey<LootTable>> loot = target.getLootTable();
                        // make sure loot table is present, as otherwise this would throw an error
                        if(loot.isPresent()) {
                            // hard cap of only 5 rolls per entity checked (more than this and looting was spawning too many items)
                            if(lootingLevel > 5){
                                lootingLevel = 5;
                            }
                            for (int lootRolls = 0; lootRolls < lootingLevel; lootRolls++) {
                                entity.dropFromLootTable(serverLevel,
                                        serverLevel.damageSources().mace(attacker),
                                        true,loot.get());
                                entity.skipDropExperience();
                                entity.kill(serverLevel);
                            }
                        }
                    }
                    Vec3 vec3 = entity.position().subtract(target.position());
                    double knockbackPower = getKnockbackPower(attacker, entity, vec3);
                    Vec3 vec31 = vec3.normalize().scale(knockbackPower);
                    if (knockbackPower > 0.0) {
                        entity.push(vec31.x, 0.97231F, vec31.z);
                        if (entity instanceof ServerPlayer serverplayer) {
                            serverplayer.connection.send(new ClientboundSetEntityMotionPacket(serverplayer));
                        }
                    }
                });
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.ultimate_mace.desc")
                .withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.ITALIC));
    }
}
