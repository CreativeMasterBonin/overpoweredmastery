package net.rk.overpoweredmastery.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.datagen.OMEnchantments;
import net.rk.overpoweredmastery.datagen.OMTags;
import net.rk.overpoweredmastery.entity.blockentity.SelectionBlockEntity;
import net.rk.overpoweredmastery.item.custom.AbstractStaff;
import net.rk.overpoweredmastery.resource.OMSoundEvents;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.List;

public class UltimateStaff extends AbstractStaff {
    public UltimateStaff(Properties p) {
        super(p.enchantable(30).rarity(OMRarity.ULTIMATE.getValue()).durability(9999).fireResistant()
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(AbstractStaff.STAFF_MOVEMENT_SPEED_MODIFIER,0.05f,AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND)
                        .add(Attributes.BLOCK_INTERACTION_RANGE,
                                new AttributeModifier(AbstractStaff.STAFF_BLOCK_REACH_MODIFIER,3,AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND)
                        .add(Attributes.ENTITY_INTERACTION_RANGE,
                                new AttributeModifier(AbstractStaff.STAFF_ENTITY_REACH_MODIFIER,5,AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HAND)
                        .add(Attributes.SAFE_FALL_DISTANCE,
                                new AttributeModifier(AbstractStaff.STAFF_SAFE_FALL_DISTANCE,384,AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.ANY)
                        .add(Attributes.FALL_DAMAGE_MULTIPLIER,
                                new AttributeModifier(AbstractStaff.STAFF_FALL_DAMAGE_MULTIPLIER,-0.5D,AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
                                EquipmentSlotGroup.ANY)
                        .build()));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 800;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        boolean didAction = false;
        if(entity instanceof Player player){
            if(player.getCooldowns().isOnCooldown(stack)){
                return false;
            }
            if(player.isSecondaryUseActive()){
                didAction = true;
                entity.addDeltaMovement(entity.getLookAngle()
                        .multiply(1.2, 1.2, 1.2)
                        .normalize()
                        .scale(0.91 / (entity.getAttributeValue(Attributes.MOVEMENT_EFFICIENCY) + 1.0f)));
                player.getCooldowns().addCooldown(stack,20);
            }
            else{
                return false;
            }
        }
        return didAction ? true : false;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity){
        boolean isBad = entity instanceof Monster;
        if(isBad){
            int anyItemsTakenCount = 0;
            entity.setDeltaMovement(new Vec3(0,1.2,0));

            if(player.level() instanceof ServerLevel serverLevel){
                if(((Monster) entity).hasItemInSlot(EquipmentSlot.MAINHAND) && ((Monster) entity).hasItemInSlot(EquipmentSlot.OFFHAND)){
                    ItemStack mainItem = ((Monster) entity).getItemBySlot(EquipmentSlot.MAINHAND);
                    ((Monster) entity).setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);
                    serverLevel.addFreshEntity(new ItemEntity(serverLevel,player.getX(),player.getY(),player.getZ(),mainItem));

                    ItemStack offItem = ((Monster) entity).getItemBySlot(EquipmentSlot.OFFHAND);
                    ((Monster) entity).setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);
                    serverLevel.addFreshEntity(new ItemEntity(serverLevel,player.getX(),player.getY(),player.getZ(),offItem));
                    anyItemsTakenCount++;
                }
                else if(((Monster) entity).hasItemInSlot(EquipmentSlot.OFFHAND)){
                    ItemStack offItem = ((Monster) entity).getItemBySlot(EquipmentSlot.OFFHAND);
                    ((Monster) entity).setItemSlot(EquipmentSlot.OFFHAND,ItemStack.EMPTY);
                    serverLevel.addFreshEntity(new ItemEntity(serverLevel,player.getX(),player.getY(),player.getZ(),offItem));
                    anyItemsTakenCount++;
                }
                else if(((Monster) entity).hasItemInSlot(EquipmentSlot.MAINHAND)){
                    ItemStack mainItem = ((Monster) entity).getItemBySlot(EquipmentSlot.MAINHAND);
                    ((Monster) entity).setItemSlot(EquipmentSlot.MAINHAND,ItemStack.EMPTY);
                    serverLevel.addFreshEntity(new ItemEntity(serverLevel,player.getX(),player.getY(),player.getZ(),mainItem));
                    anyItemsTakenCount++;
                }

                if(anyItemsTakenCount > 0){
                    serverLevel.playSound(player,player.getOnPos(),OMSoundEvents.EFFECT.get(), SoundSource.PLAYERS,0.45f,serverLevel.getRandom().triangle(0.84f,0.97f));
                }
            }

            if(!(player.getItemInHand(player.getUsedItemHand()).getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(player.level(),OMEnchantments.INSTAREPAIR)) > 0)){
                player.getItemInHand(player.getUsedItemHand()).hurtAndBreak(1,player,player.getUsedItemHand());
            }
            return true;
        }
        else if(!isBad && (entity instanceof AbstractVillager || entity instanceof AbstractGolem)){
            if(((PathfinderMob) entity).hasEffect(MobEffects.REGENERATION)){
                ((PathfinderMob) entity).addEffect(new MobEffectInstance(MobEffects.REGENERATION,100,20,true,true,false));
            }
            if(!(player.getItemInHand(player.getUsedItemHand()).getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(player.level(),OMEnchantments.INSTAREPAIR)) > 0)){
                player.getItemInHand(player.getUsedItemHand()).hurtAndBreak(1,player,player.getUsedItemHand());
            }
            return true;
        }
        else if(!isBad && entity instanceof Player){
            try{
                int anyItemsTakenCount = 0;
                if(player.level() instanceof ServerLevel serverLevel){
                    if(((Player) entity).getInventory().hasAnyMatching(itemStack -> {
                        boolean okItem = !itemStack.isDamageableItem() && (itemStack.is(ItemTags.BEACON_PAYMENT_ITEMS) || itemStack.is(ItemTags.TRIM_MATERIALS));
                        return okItem;
                    })){
                        for(int invSlot = 0; invSlot < ((Player) entity).getInventory().getContainerSize(); invSlot++){
                            ItemStack currentStack = ((Player) entity).getInventory().getItem(invSlot);
                            if(!currentStack.isDamageableItem() && (currentStack.is(ItemTags.BEACON_PAYMENT_ITEMS) || currentStack.is(ItemTags.TRIM_MATERIALS))){
                                ((Player) entity).getInventory().setItem(invSlot,ItemStack.EMPTY);
                                serverLevel.addFreshEntity(new ItemEntity(serverLevel,player.getX(),player.getY(),player.getZ(),currentStack));
                                anyItemsTakenCount++;
                            }
                        }
                        if(anyItemsTakenCount > 0){
                            serverLevel.playSound(entity,entity.getOnPos(),OMSoundEvents.EFFECT.get(), SoundSource.PLAYERS,1.0f,0.75f);
                        }
                    }
                }

                if(!(player.getItemInHand(player.getUsedItemHand()).getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(player.level(),OMEnchantments.INSTAREPAIR)) > 0)){
                    player.getItemInHand(player.getUsedItemHand()).hurtAndBreak(1,player,player.getUsedItemHand());
                }
            }
            catch (Exception e){

            }
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context){
        // if on cooldown stop all actions and perform the normal item one
        if(context.getPlayer().getCooldowns().isOnCooldown(context.getItemInHand())){
            return InteractionResult.PASS;
        }

        int successfulChanges = 0;

        if(context.getPlayer().isUsingItem()){
            return InteractionResult.PASS;
        }

        // must be server player
        if(context.getPlayer() instanceof ServerPlayer player){
            List<BlockPos> blockPosToCheck = getClippingBlocks(1, 1, 5, ClipContext.Fluid.ANY, ClipContext.Block.COLLIDER,context.getClickedPos(),player);
            for(BlockPos pos : blockPosToCheck){
                BlockState replacementBlockState = context.getLevel().getBlockState(pos);

                boolean stateIsMasterGameBlock = replacementBlockState.getBlock() instanceof GameMasterBlock;
                boolean unsafeState = replacementBlockState.is(OMTags.UNSAFE_FOR_SELECTION);
                boolean stateContainsBlockEntity = replacementBlockState.getBlock() instanceof EntityBlock;

                // if ok to replace block, go ahead and change it out for the selection block (saving the blockstate to the BE)
                if(!stateContainsBlockEntity && !unsafeState && !stateIsMasterGameBlock && !context.getLevel().getBlockState(pos).isAir() && !context.getLevel().getBlockState(pos).is(OMBlocks.SELECTION_BLOCK)){
                    context.getLevel().setBlock(pos,OMBlocks.SELECTION_BLOCK.get().defaultBlockState(),3);

                    if(context.getLevel().getBlockEntity(pos) instanceof SelectionBlockEntity sbe){
                        sbe.setState(replacementBlockState);
                        successfulChanges++;
                    }
                }
            }
            if(successfulChanges > 0){
                if(!context.getPlayer().getCooldowns().isOnCooldown(context.getItemInHand())){
                    context.getPlayer().getCooldowns().addCooldown(context.getItemInHand(),5);

                    context.getPlayer().playSound(OMSoundEvents.EFFECT.get(),0.75f,context.getPlayer().getRandom().triangle(0.87f,1.0f));
                    if(!(context.getItemInHand().getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(context.getLevel(),OMEnchantments.INSTAREPAIR)) > 0)){
                        context.getItemInHand().hurtAndBreak(1,context.getPlayer(),context.getHand());
                    }
                    else{
                        if(!context.getPlayer().hasEffect(MobEffects.RESISTANCE)){
                            context.getPlayer().addEffect(new MobEffectInstance(MobEffects.RESISTANCE,200,20,true,false,false));
                        }
                    }
                }
            }
            return InteractionResult.CONSUME;
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void extraOnUse(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration){
        if(livingEntity instanceof Player player){
            if(player.getCooldowns().isOnCooldown(stack)){
                player.stopUsingItem();
                return;
            }
        }

        if(remainingUseDuration < 780) {
            staffDefaultParticles(level,livingEntity);
            if(remainingUseDuration % 4 == 0){
                if(remainingUseDuration < 200){
                    action(level,livingEntity,stack,remainingUseDuration,2);
                }
            }

            if(remainingUseDuration % 10 == 0){
                if(remainingUseDuration < 400){
                    action(level,livingEntity,stack,remainingUseDuration,1);
                }
                else{
                    action(level,livingEntity,stack,remainingUseDuration,0);
                }
            }

            if(stack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level,OMEnchantments.INSTAREPAIR)) > 0){
                if(livingEntity.getHealth() < livingEntity.getMaxHealth() / 2){
                    if(livingEntity instanceof Player){
                        livingEntity.addEffect(new MobEffectInstance(MobEffects.INSTANT_HEALTH,5,2,true,false,false));
                    }
                    else{
                        livingEntity.heal(1);
                    }
                }
            }
            else{
                stack.hurtAndBreak(1,livingEntity,livingEntity.getUsedItemHand());
            }
        }
    }

    @Override
    public void action(Level level, LivingEntity entity, ItemStack stack, int remainingDuration, int stage){
        if(entity.getRandom().nextIntBetweenInclusive(0,100) <= 1){
            entity.playSound(OMSoundEvents.EFFECT.get(),0.75f,entity.getRandom().triangle(0.95f,1.1f));
        }
        if(level instanceof ServerLevel serverLevel){
            switch(stage){
                case 0:{
                    entity.playSound(SoundEvents.FIRECHARGE_USE,0.45f,entity.getRandom().triangle(0.95f,1.1f));
                    SmallFireball fireball = new SmallFireball(level,entity,entity.getViewVector(5));
                    fireball.setPos(fireball.getX(),entity.getEyeY(),fireball.getZ());
                    serverLevel.addFreshEntity(fireball);
                    break;
                }
                case 1:{
                    entity.playSound(SoundEvents.FIRECHARGE_USE,0.45f,entity.getRandom().triangle(0.95f,1.1f));
                    LargeFireball largeFireball = new LargeFireball(level,entity,entity.getViewVector(4),2);
                    largeFireball.setPos(largeFireball.getX(),entity.getEyeY() + 0.51D,largeFireball.getZ());
                    serverLevel.addFreshEntity(largeFireball);
                    break;
                }
                case 2:{
                    entity.playSound(SoundEvents.LAVA_POP,0.45f,entity.getRandom().triangle(0.95f,1.1f));
                    WitherSkull witherSkull = new WitherSkull(level,entity,entity.getViewVector(7));
                    witherSkull.setCustomName(Component.literal("Grumm"));
                    witherSkull.setPos(witherSkull.getX(),entity.getEyeY() + 0.81D,witherSkull.getZ());
                    serverLevel.addFreshEntity(witherSkull);
                }
            }
        }
    }
}
