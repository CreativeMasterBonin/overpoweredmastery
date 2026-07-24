package net.rk.overpoweredmastery.entity.custom;

import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.SpecialDates;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;
import net.neoforged.neoforge.fluids.FluidType;
import net.rk.overpoweredmastery.datagen.OMTags;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.util.OPUtil;
import org.jspecify.annotations.Nullable;

import java.util.*;

public class Fraud extends PathfinderMob implements EquipmentUser, Targeting, RangedAttackMob, CrossbowAttackMob, InventoryCarrier, AttachmentsSupportedEntity, IEntityWithComplexSpawn {
    public boolean breakDoors = false;
    public boolean chargingCrossbow = false;
    public boolean isUsingWubItem = false;
    public SimpleContainer fraudInventory = new SimpleContainer(4);
    public static Set<Item> itemsThatClearPotionEffects = Set.of(Items.MILK_BUCKET);
    // befriending a Fraud causes them to stop attacking the player and focus on their own tasks
    public static final EntityDataAccessor<Integer> SKIN_DATA =
            SynchedEntityData.defineId(Fraud.class,EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> UNHAPPY_DATA =
            SynchedEntityData.defineId(Fraud.class,EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> BEFRIENDED_DATA =
            SynchedEntityData.defineId(Fraud.class,EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> BAD_FACTION_DATA =
            SynchedEntityData.defineId(Fraud.class,EntityDataSerializers.BOOLEAN);

    /**
     * A custom Goal that allows the Fraud to find the nearest item and collect it
     */
    public class SearchForItemGoal extends Goal{
        public SearchForItemGoal(){
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public void tick() {
            List<ItemEntity> itemsNearby = Fraud.this.level().getEntitiesOfClass(ItemEntity.class,
                    Fraud.this.getBoundingBox().inflate(16.0,16.0,16.0),
                    itemEntity -> !itemEntity.hasPickUpDelay() && itemEntity.isAlive());
            if(!itemsNearby.isEmpty()){
                for(ItemStack stack : Fraud.this.fraudInventory){
                    if(stack.isEmpty()){
                        Fraud.this.getNavigation().moveTo(itemsNearby.getFirst(),1.25f);
                    }
                }
            }
        }

        @Override
        public void start() {
            List<ItemEntity> itemsNearby = Fraud.this.level().getEntitiesOfClass(ItemEntity.class,
                    Fraud.this.getBoundingBox().inflate(16.0,16.0,16.0),
                    itemEntity -> !itemEntity.hasPickUpDelay() && itemEntity.isAlive());
            if(!itemsNearby.isEmpty()){
                Fraud.this.getNavigation().moveTo(itemsNearby.getFirst(),1.25f);
            }
        }

        @Override
        public boolean canUse() {
            if(!Fraud.this.fraudInventory.hasAnyMatching(item -> item.is(OMTags.FRAUD_WANTS))){
                return true;
            }
            else{
                return false;
            }
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SKIN_DATA,0);
        builder.define(UNHAPPY_DATA,false);
        builder.define(BEFRIENDED_DATA,false);
        builder.define(BAD_FACTION_DATA,false);
    }

    public final RangedBowAttackGoal fraudUseBowGoal = new RangedBowAttackGoal<>(this, 1.0, 20, 15.0F);
    public final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this,1.19f,false){
        @Override
        public void start() {
            super.start();
            Fraud.this.setAggressive(true);
        }
        @Override
        public void stop() {
            super.stop();
            Fraud.this.setAggressive(false);
        }
    };
    public final RangedCrossbowAttackGoal fraudRangedCrossbowGoal = new RangedCrossbowAttackGoal(this, 1.12f, 12.0f);

    /**
     * If desired the net.rk.overpoweredmastery.entity.custom.Fraud#itemsThatClearPotionEffects can be given a new Set of items using this method
     * @param items The new Set that will replace the current one
     */
    public void setPotionClearingItem(Set<Item> items){
        itemsThatClearPotionEffects = items;
    }

    /**
     * Returns the current net.rk.overpoweredmastery.entity.custom.Fraud#itemsThatClearPotionEffects Set
     * @return The current Set of Items that the Fraud checks for potion clearing items
     */
    public Set<Item> getPotionClearingItems(){
        return itemsThatClearPotionEffects;
    }

    public Fraud(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.getNavigation().setCanFloat(true);
        this.getNavigation().setCanOpenDoors(true);
        // dangers
        this.setPathfindingMalus(PathType.LAVA,8.0f);
        this.setPathfindingMalus(PathType.DANGER_FIRE,6.0f);
        this.setPathfindingMalus(PathType.DANGER_POWDER_SNOW,10.0f);
        this.setPathfindingMalus(PathType.DANGER_OTHER,8.0f);
        // damages
        this.setPathfindingMalus(PathType.DAMAGE_CAUTIOUS,-1.0f);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE,-1.0f);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER,-1.0f);
        // other
        this.setPathfindingMalus(PathType.STICKY_HONEY,4.0f);
        // this must be set on the server-side
        if(level instanceof ServerLevel){
            this.registerGoals();
            this.reassignWeaponGoals();
        }
    }

    // 1.0.5 - allow mixins for default attributes to run as well as its own custom values
    public static AttributeSupplier createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,20.0f)
                .add(Attributes.FOLLOW_RANGE,36.0f)
                .add(Attributes.MOVEMENT_SPEED,0.231f)
                .add(Attributes.ATTACK_SPEED,2.0f)
                .add(Attributes.ATTACK_DAMAGE,2.0f)
                .add(Attributes.BLOCK_INTERACTION_RANGE,3.0f)
                .add(Attributes.ENTITY_INTERACTION_RANGE,3.0f)
                .add(Attributes.ARMOR,1.0f)
                .add(Attributes.ARMOR_TOUGHNESS)
                .add(Attributes.MAX_ABSORPTION)
                .add(Attributes.WAYPOINT_TRANSMIT_RANGE,0.0f)
                .add(Attributes.WAYPOINT_RECEIVE_RANGE,4.0f)
                .add(Attributes.STEP_HEIGHT,1.25f)
                .add(Attributes.MOVEMENT_EFFICIENCY,1.0f)
                .add(Attributes.WATER_MOVEMENT_EFFICIENCY,1.0f)
                .add(Attributes.SCALE,1.0f)
                .add(Attributes.GRAVITY,1.0f)
                .add(Attributes.SAFE_FALL_DISTANCE,4.0f)
                .add(Attributes.FALL_DAMAGE_MULTIPLIER,0.25f)
                .add(Attributes.JUMP_STRENGTH,0.5f)
                .add(Attributes.OXYGEN_BONUS)
                .add(Attributes.BURNING_TIME)
                .add(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE)
                .add(Attributes.ATTACK_KNOCKBACK,0.75f)
                .add(Attributes.CAMERA_DISTANCE)
                .add(Attributes.KNOCKBACK_RESISTANCE,0.25f)
                .add(Attributes.TEMPT_RANGE,32.0f)
                .add(NeoForgeMod.SWIM_SPEED)
                .add(NeoForgeMod.NAMETAG_DISTANCE).build();
    }

    public Fraud(Level level){
        this(OMEntityTypes.FRAUD.get(),level);
    }

    @Override
    public void pickUpItem(ServerLevel level, ItemEntity entity) {
        InventoryCarrier.pickUpItem(level,this,this,entity);
    }

    @Override
    public int getBaseExperienceReward(ServerLevel serverLevel) {
        return serverLevel.getRandom().nextIntBetweenInclusive(1,5);
    }

    public boolean canBreakDoors(){
        return breakDoors;
    }

    @Override
    public boolean canPickUpLoot() {
        return true;
    }

    @Override
    public boolean shouldShowName() {
        return false;
    }

    @Override
    public float getSoundVolume() {
        return 0.9f;
    }

    @Override
    public float getVoicePitch() {
        return OPUtil.nextFloatBetweenInclusive(0.75f,0.85f);
    }

    @Override
    public @Nullable SoundEvent getDeathSound() {
        return SoundEvents.PILLAGER_DEATH;
    }

    @Override
    public @Nullable SoundEvent getAmbientSound() {
        switch(Mth.nextInt(this.getRandom(),0,3)){
            case 0:{
                return SoundEvents.WANDERING_TRADER_TRADE;
            }
            case 1:{
                return SoundEvents.WANDERING_TRADER_YES;
            }
            case 2:{
                return SoundEvents.WANDERING_TRADER_AMBIENT;
            }
            case 3:{
                return SoundEvents.WANDERING_TRADER_DRINK_MILK;
            }
            default:{
                return SoundEvents.VILLAGER_AMBIENT;
            }
        }
    }

    @Override
    public @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.WANDERING_TRADER_NO;
    }

    @Override
    public boolean canUsePortal(boolean allowPassengers) {
        boolean isDifficultMode = false;
        if(this.level() instanceof ServerLevel serverLevel){
            isDifficultMode = (serverLevel.getDifficulty() == Difficulty.HARD || serverLevel.getDifficulty() == Difficulty.NORMAL);
        }

        if(isDifficultMode){
            // frauds in a more difficult mode do not care for armor or weapons, they want to use portals anyway
            return super.canUsePortal(allowPassengers);
        }
        else{
            // Frauds do not want to use portals unless they have armor and a weapon of some sort (plus standard entity rules)
            return super.canUsePortal(allowPassengers)
                    && (!equipment.get(EquipmentSlot.HEAD).isEmpty() || !equipment.get(EquipmentSlot.BODY).isEmpty() || !equipment.get(EquipmentSlot.LEGS).isEmpty() || !equipment.get(EquipmentSlot.FEET).isEmpty())
                    && !equipment.get(EquipmentSlot.MAINHAND).isEmpty();
        }
    }

    public void serverEquipArmorAudioVisualFlair(ItemStack armor){
        if(this.level() instanceof ServerLevel serverLevel){
            if(armor.has(DataComponents.EQUIPPABLE)){
                Equippable equippable = armor.get(DataComponents.EQUIPPABLE);
                if(equippable != null){
                    if(equippable.equipSound() != null){
                        Holder<SoundEvent> equipSoundHolder = equippable.equipSound();
                        serverLevel.playSound(this,getX(),getY(),getZ(),
                                equipSoundHolder.value(),SoundSource.NEUTRAL,
                                0.95f,OPUtil.nextFloatBetweenInclusive(0.95f,1.0f));
                        serverLevel.sendParticles(ParticleTypes.POOF,
                                getX(),getY(),getZ(),10,
                                0D,0D,0D,0.1D);
                    }
                }
            }
        }
    }

    public void serverPlayEquipWeaponSound(ServerLevel serverLevel){
        serverLevel.playSound(this,getX(),getY(),getZ(),
                SoundEvents.IRON_HIT,SoundSource.NEUTRAL,
                0.95f,OPUtil.nextFloatBetweenInclusive(0.87f,0.92f));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand){
        if(level() instanceof ServerLevel serverLevel){
            if(player.getItemInHand(hand).is(ItemTags.BEACON_PAYMENT_ITEMS) && !getEntityData().get(BEFRIENDED_DATA)){
                player.getItemInHand(hand).shrink(1);
                this.getEntityData().set(BEFRIENDED_DATA,true);
                this.getEntityData().set(BAD_FACTION_DATA,false);
                return InteractionResult.SUCCESS_SERVER;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        level.explode(this,getX(),getY(),getZ(),5.0f, Level.ExplosionInteraction.MOB);
        // lightning makes Frauds drop their inventory completely
        for(ItemStack stackInInventory : this.getInventory().getItems()){
            level.addFreshEntity(new ItemEntity(level,getX(),getY(),getZ(),stackInInventory));
        }
        this.discard();
    }

    public EquipmentSlot obtainSlotFromItem(ItemStack stack){
        Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
        if(equippable != null)
            return equippable.slot();
        else
            return null;
    }

    @Override
    public void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        boolean alreadyGotRareItem = false;
        if(random.nextIntBetweenInclusive(0,120000) <= 1){
            this.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.NETHERITE_HELMET));
            this.setItemSlot(EquipmentSlot.CHEST,new ItemStack(Items.NETHERITE_CHESTPLATE));
            this.setItemSlot(EquipmentSlot.LEGS,new ItemStack(Items.NETHERITE_LEGGINGS));
            this.setItemSlot(EquipmentSlot.FEET,new ItemStack(Items.NETHERITE_BOOTS));
            alreadyGotRareItem = true;
        }
        if(random.nextIntBetweenInclusive(0,1200) <= 1){
            if(random.nextIntBetweenInclusive(0,5300) <= 1){
                this.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.CROSSBOW));
                alreadyGotRareItem = true;
            }
            else{
                this.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.BOW));
                alreadyGotRareItem = true;
            }
        }
        if(random.nextIntBetweenInclusive(0,3200) <= 1){
            this.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.NETHERITE_SWORD));
            alreadyGotRareItem = true;
        }

        if(!alreadyGotRareItem){
            super.populateDefaultEquipmentSlots(random,difficulty);
        }
    }

    public void checkForBetterEquipment(){
        // get the current main hand equipment item
        ItemStack mainHandSlot = equipment.get(EquipmentSlot.MAINHAND);

        // check if main hand equipment slot has a damageable item (and not just a regular item or none at all)
        boolean mainHandSlotHasExistingDamage = mainHandSlot.has(DataComponents.MAX_DAMAGE) && mainHandSlot.has(DataComponents.DAMAGE);

        // main find item and use/swap logic loop
        for(ItemStack stack : fraudInventory.getItems()){
            if(this.level() instanceof ServerLevel serverLevel){
                // potions are high priority, as they may be good
                if(stack.is(Items.POTION)){
                    if(stack.has(DataComponents.POTION_CONTENTS)){
                        // play a sound as if drinking the potion
                        if(stack.get(DataComponents.POTION_CONTENTS) != null){
                            PotionContents contents = stack.get(DataComponents.POTION_CONTENTS);
                            if(contents != null) {
                                for(MobEffectInstance effectInstance : contents.getAllEffects()){
                                    if(!this.hasEffect(effectInstance.getEffect()) && effectInstance.getEffect().value().isBeneficial()){
                                        this.addEffect(effectInstance);
                                        serverLevel.playSound(this,getX(),getY(),getZ(),SoundEvents.HONEY_DRINK, SoundSource.NEUTRAL,
                                                0.57f, OPUtil.nextFloatBetweenInclusive(0.98f,1.0f));
                                    }
                                }
                            }
                            stack.shrink(1);
                            serverLevel.playSound(this,getX(),getY(),getZ(),SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL,
                                    0.75f, OPUtil.nextFloatBetweenInclusive(0.95f,1.2f));
                            fraudInventory.setChanged();
                            break;
                        }
                    }
                } // got food? eat it and restore health
                else if(stack.has(DataComponents.FOOD)){
                    if(this.getHealth() < this.getMaxHealth()){
                        FoodProperties food = stack.get(DataComponents.FOOD);
                        int nutritionPoints = food.nutrition();
                        float saturation = food.saturation();

                        stack.shrink(1);
                        fraudInventory.setChanged();
                        this.setHealth(Mth.clamp(this.getHealth() + (nutritionPoints * saturation),0,this.getMaxHealth()));

                        serverLevel.playSound(this,getX(),getY(),getZ(),
                                SoundEvents.GENERIC_EAT,SoundSource.NEUTRAL,
                                0.97f,OPUtil.nextFloatBetweenInclusive(0.95f,1.0f));

                        serverLevel.playSound(this,getX(),getY(),getZ(),
                                SoundEvents.PLAYER_BURP,SoundSource.NEUTRAL,
                                0.97f,OPUtil.nextFloatBetweenInclusive(0.95f,1.0f));
                        break;
                    }
                }
            }

            boolean itemHasRequiredArmorData = stack.has(DataComponents.EQUIPPABLE) && stack.has(DataComponents.MAX_DAMAGE) && stack.has(DataComponents.DAMAGE);

            if(this.level() instanceof ServerLevel serverLevel){
                // obtain slot type then assign item if is better than what we have currently
                if(itemHasRequiredArmorData){
                    if(obtainSlotFromItem(stack) != null){
                        if(equipment.get(obtainSlotFromItem(stack)).isEmpty()){
                            equipment.set(obtainSlotFromItem(stack),stack.copy());
                            stack.shrink(1);
                            fraudInventory.setChanged();
                            serverEquipArmorAudioVisualFlair(stack);
                            break;
                        }
                        else{
                            boolean betterDamageValue = stack.getDamageValue() > equipment.get(obtainSlotFromItem(stack)).getDamageValue();
                            boolean betterMaxDamageValue = stack.getMaxDamage() > equipment.get(obtainSlotFromItem(stack)).getMaxStackSize();

                            if(betterDamageValue){
                                equipment.set(obtainSlotFromItem(stack),stack.copy());
                                stack.shrink(1);
                                fraudInventory.setChanged();
                                serverEquipArmorAudioVisualFlair(stack);
                                break;
                            }
                            else if(betterMaxDamageValue){
                                equipment.set(obtainSlotFromItem(stack),stack.copy());
                                stack.shrink(1);
                                fraudInventory.setChanged();
                                serverEquipArmorAudioVisualFlair(stack);
                                break;
                            }
                        }
                    }
                }
            }

            boolean itemHasRequiredWeaponData =
                    stack.has(DataComponents.WEAPON)
                            && stack.has(DataComponents.MAX_DAMAGE)
                            && stack.has(DataComponents.DAMAGE);

            // check if a main hand item can be equipped (server-side)
            if(this.level() instanceof ServerLevel serverLevel){
                // do not allow non-weapons in the main hand of a Fraud
                if(itemHasRequiredWeaponData){
                    // is the stack we are checking better than our current weapon?
                    if(stack.getDamageValue() > mainHandSlot.getDamageValue() || stack.getMaxDamage() > mainHandSlot.getMaxDamage()){
                        equipment.set(EquipmentSlot.MAINHAND,stack.copy());
                        stack.shrink(1);
                        fraudInventory.setChanged();
                        serverPlayEquipWeaponSound(serverLevel);
                        reassignWeaponGoals();
                        break;
                    }
                }
                // main hand slot
                if(mainHandSlot.isEmpty()){
                    if(stack.getItem() instanceof BowItem || stack.getItem() instanceof CrossbowItem){
                        equipment.set(EquipmentSlot.MAINHAND,stack.copy());
                        stack.shrink(1);
                        fraudInventory.setChanged();
                        serverPlayEquipWeaponSound(serverLevel);
                        reassignWeaponGoals();
                        break;
                    }
                    else if(itemHasRequiredWeaponData){
                        equipment.set(EquipmentSlot.MAINHAND,stack.copy());
                        stack.shrink(1);
                        fraudInventory.setChanged();
                        serverPlayEquipWeaponSound(serverLevel);
                        reassignWeaponGoals();
                        break;
                    }
                    else{
                        // support tridents and maces
                        if(stack.getItem() instanceof TridentItem || stack.getItem() instanceof MaceItem){
                            equipment.set(EquipmentSlot.MAINHAND,stack.copy());
                            stack.shrink(1);
                            fraudInventory.setChanged();
                            serverPlayEquipWeaponSound(serverLevel);
                            reassignWeaponGoals();
                            break;
                        }
                    }
                }
            }

            // if we have an item that matches an entry, try to remove mob effects that are not good (slowness, weakness, poison, etc.)
            if(itemsThatClearPotionEffects.contains(stack.getItem())){
                boolean foundBadEffect = false;
                List<MobEffectInstance> effectInstances = getActiveEffects().stream().toList();
                if(effectInstances.isEmpty()){
                    break;
                }

                for(MobEffectInstance instance : effectInstances){
                    if(!instance.getEffect().value().isBeneficial()){
                        this.removeEffect(instance.getEffect());
                        foundBadEffect = true;
                        // show particles for players if removing an ill effect
                        if(this.level() instanceof ServerLevel serverLevel){
                            serverLevel.sendParticles(ParticleTypes.TRIAL_SPAWNER_DETECTED_PLAYER,
                                    getX(),getY(),getZ(),7,
                                    0D,0.5D,0D,0.01D);
                        }
                    }
                }
                if(foundBadEffect) {
                    if (this.level() instanceof ServerLevel serverLevel) {
                        stack.shrink(1);
                        fraudInventory.setChanged();
                        serverLevel.playSound(this, getOnPos(),
                                SoundEvents.WANDERING_TRADER_REAPPEARED, SoundSource.NEUTRAL,
                                0.95f, OPUtil.nextFloatBetweenInclusive(0.75f, 0.95f));
                        reassignWeaponGoals();
                        break;
                    }
                }
            }
        }
    }

    // frauds want to eat, but they have to replenish what they take
    public boolean hasPlantableFood(){
        return this.fraudInventory.hasAnyMatching(stack -> stack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS));
    }

    @Override
    public void onAttack() {
        if(this.level() instanceof ServerLevel serverLevel){
            if(equipment.get(EquipmentSlot.MAINHAND).isEmpty()){
                return;
            }
            else{
                equipment.get(EquipmentSlot.MAINHAND).hurtAndBreak(1,this,EquipmentSlot.MAINHAND);
                swing(InteractionHand.MAIN_HAND,true);
            }
        }
    }

    // frauds use anything that is useful for survival or just to steal it for personal gain
    @Override
    public boolean wantsToPickUp(ServerLevel level, ItemStack stack) {
        return stack.is(ItemTags.SWORDS)
                || stack.is(ItemTags.AXES) ||
                stack.is(Tags.Items.BUCKETS_MILK)
                || stack.is(ItemTags.PARROT_POISONOUS_FOOD) ||
                stack.is(Items.POTION) ||
                stack.is(ItemTags.BOW_ENCHANTABLE) || stack.is(ItemTags.CROSSBOW_ENCHANTABLE) ||
                stack.is(ItemTags.MACE_ENCHANTABLE) || stack.is(ItemTags.TRIDENT_ENCHANTABLE) ||
                stack.is(ItemTags.HEAD_ARMOR) ||
                stack.is(ItemTags.CHEST_ARMOR) ||
                stack.is(ItemTags.LEG_ARMOR) ||
                stack.is(ItemTags.FOOT_ARMOR) ||
                stack.is(Tags.Items.SEEDS) ||
                stack.is(OMTags.MUSIC_DISC_WUBS)
                || stack.has(DataComponents.FOOD);
    }

    @Override
    public void playStepSound(BlockPos pos, BlockState state) {
        if(this.getScale() > 1.70000000f){
            state.playStepSound(level(),pos,this,1.0f,OPUtil.nextFloatBetweenInclusive(0.95f,1.05f));
            if(level() instanceof ServerLevel serverLevel){
                serverLevel.playSound(this,pos,
                        SoundEvents.MACE_SMASH_GROUND,
                        SoundSource.NEUTRAL,
                        1.0f,OPUtil.nextFloatBetweenInclusive(0.91f,0.94f));
                serverLevel.playSound(this,pos,
                        SoundEvents.ROOTED_DIRT_BREAK,
                        SoundSource.NEUTRAL,
                        0.5f,OPUtil.nextFloatBetweenInclusive(0.5f,0.52f));
            }
        }
        else{
            super.playStepSound(pos,state);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData spawnGroupData) {
        SpawnGroupData data = super.finalizeSpawn(level,difficulty,spawnReason,spawnGroupData);
        // the fraud may have armor on with enchants, who knows where he got it from...
        RandomSource randomsource = level.getRandom();

        this.entityData.set(SKIN_DATA,OPUtil.nextIntBetweenInclusive(0,3));
        this.entityData.set(BAD_FACTION_DATA,level.getRandom().nextBoolean()); // randomly assign the fraud as part of a bad faction

        this.populateDefaultEquipmentSlots(randomsource, difficulty);
        this.populateDefaultEquipmentEnchantments(level, randomsource, difficulty);

        this.reassignWeaponGoals();

        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && SpecialDates.isHalloween() && randomsource.nextFloat() < 0.25f) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(randomsource.nextFloat() < 0.1f ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
            this.setDropChance(EquipmentSlot.HEAD, 0.0f);
        }
        else if(this.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
                && SpecialDates.dayNow() == SpecialDates.CHRISTMAS && randomsource.nextFloat() < 0.25f){
            this.setItemSlot(EquipmentSlot.HEAD,
                    new ItemStack(Items.SPRUCE_LEAVES,1));
        }
        else if(this.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && SpecialDates.dayNow() == SpecialDates.NEW_YEAR && randomsource.nextFloat() < 0.15f){
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(randomsource.nextFloat() < 0.1f ? Blocks.FIRE_CORAL_FAN : Blocks.CAMPFIRE));
        }

        return data;
    }

    public void reassignWeaponGoals() {
        if (this.level() != null && !this.level().isClientSide()) {
            this.goalSelector.removeGoal(this.meleeGoal);
            this.goalSelector.removeGoal(this.fraudUseBowGoal);
            ItemStack itemstack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> {
                return item instanceof BowItem;
            }));
            ItemStack itemStackCrossbow = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this,(item -> {
                return item instanceof CrossbowItem;
            })));
            if (itemstack.getItem() instanceof BowItem) {
                int interval = 1;
                switch(this.level().getDifficulty()){
                    case EASY -> {
                        interval = 20;
                    }
                    case NORMAL -> {
                        interval = 17;
                    }
                    case HARD -> {
                        interval = 10;
                    }
                    default -> {
                        interval = 9;
                    }
                }
                this.fraudUseBowGoal.setMinAttackInterval(interval);
                this.goalSelector.addGoal(1, this.fraudUseBowGoal);
            }
            else if(itemStackCrossbow.getItem() instanceof CrossbowItem){
                this.goalSelector.addGoal(1,this.fraudRangedCrossbowGoal);
            }
            else {
                this.goalSelector.addGoal(1,this.meleeGoal);
            }
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        ProfilerFiller fraudEntityFiller = Profiler.get();
        fraudEntityFiller.push("fraudEntityBaseTick");

        // frauds can use potions like players can, but they can selectively pick the best effects out of the batch using strange magic
        if(this.level() instanceof ServerLevel serverLevel){
            ItemStack itemInHand = this.getItemInHand(InteractionHand.MAIN_HAND);

            // frauds check items they have
            if(serverLevel.getGameTime() % 97L == 0 && serverLevel.tickRateManager().runsNormally()){
                checkForBetterEquipment();
            }

            if(this.tickCount % 72 == 0 && this.entityData.get(UNHAPPY_DATA)){
                this.entityData.set(UNHAPPY_DATA,false);
            }

            if(hasPlantableFood()){
                if(serverLevel.getBlockState(getOnPos()).is(Tags.Blocks.VILLAGER_FARMLANDS)){
                    for(ItemStack stack : fraudInventory.getItems()){
                        if(stack.is(ItemTags.VILLAGER_PLANTABLE_SEEDS) && stack.getItem() instanceof BlockItem blockItem && serverLevel.getBlockState(getOnPos().above()).isAir()){
                            Block crop = blockItem.getBlock();
                            serverLevel.setBlock(getOnPos().above(),crop.defaultBlockState(),3);
                            stack.shrink(1);
                            serverLevel.playSound(null,getOnPos(),SoundEvents.CROP_PLANTED,SoundSource.NEUTRAL,
                                    0.9f,OPUtil.nextFloatBetweenInclusive(0.95f,1.1f));
                            serverLevel.levelEvent(this,2001,getOnPos(),Block.getId(crop.defaultBlockState()));
                        }
                    }
                }
            }
        }

        fraudEntityFiller.pop();
    }



    public static final DropChances dropEquipmentChances = new DropChances(
            Map.of(EquipmentSlot.HEAD,0.0f,
                    EquipmentSlot.BODY,0.0f,
                    EquipmentSlot.LEGS,0.0f,
                    EquipmentSlot.FEET,0.0f,
                    EquipmentSlot.MAINHAND,0.0f,EquipmentSlot.OFFHAND,0.0f));

    @Override
    public DropChances getDropChances() {
        return dropEquipmentChances;
    }

    @Override
    public boolean canSwimInFluidType(FluidType type) {
        if (type == NeoForgeMod.WATER_TYPE.value()) {
            return true;
        }
        else {
            return this.hasEffect(MobEffects.FIRE_RESISTANCE) && type == NeoForgeMod.LAVA_TYPE.value();
        }
    }



    @Override
    public void registerGoals(){
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(15, new WaterAvoidingRandomStrollGoal(this, 1.02f));

        this.goalSelector.addGoal(1, new MoveTowardsTargetGoal(this, 1.22f, 64.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(15, new MoveBackToVillageGoal(this, 1.31f, false));

        this.goalSelector.addGoal(14,new LookAtPlayerGoal(this, Player.class,6.0f));
        this.goalSelector.addGoal(14,new LookAtPlayerGoal(this, Villager.class,6.0f));

        this.goalSelector.addGoal(10,new LeapAtTargetGoal(this,0.35f));

        this.goalSelector.addGoal(17,new RandomStrollGoal(this,0.95f));

        this.goalSelector.addGoal(15,new OpenDoorGoal(this,true));

        this.goalSelector.addGoal(10, new MoveToBlockGoal(this,1.0f,14,4) {
            @Override
            public boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
                return levelReader.getBlockState(blockPos).is(OMTags.FRAUD_WANTS_TO_GO_TO);
            }
        });
        this.goalSelector.addGoal(2,new SearchForItemGoal());

        // targets to attack and seek after (the behavior of this fraud)

        // target anyone who attacks, but not a certain list of entities
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this,
                LightningBolt.class,
                PrimedTnt.class,
                VehicleEntity.class,
                AbstractHurtingProjectile.class
        ));
        // not exactly the nicest guy, but if you are down he will ignore you, or if you are on the same team
        this.targetSelector.addGoal(40, new NearestAttackableTargetGoal<>(this,Player.class,120,true,true,
                ((livingEntity, serverLevel) -> {
                    if(this.getEntityData().get(BEFRIENDED_DATA) || (livingEntity.getHealth() <= livingEntity.getMaxHealth() / 2) || livingEntity.isFreezing() || livingEntity.isOnFire() || livingEntity.isInvulnerable() || livingEntity.isInvisible()){
                        return false; // bad faction frauds will never be a friend of players
                    }
                    return true;
                })));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 2, true, true, (livingEntity, serverLevel) -> {
            return livingEntity instanceof Enemy;
        }));

        // frauds have infighting, but only with factions who are not so nice (and they don't fight themselves, that would be silly!)
        this.targetSelector.addGoal(30, new NearestAttackableTargetGoal<>(this, Fraud.class, 32, false, false, (livingEntity, serverLevel) -> {
            return (livingEntity instanceof Fraud fraud && fraud != this && fraud.getEntityData().get(BAD_FACTION_DATA));
        }));
    }

    @Override
    public void setAggressive(boolean aggressive){
        if(this.getTeam() != null){
            if(this.getTeam().isAlliedTo(this.getTeam())){
                super.setAggressive(false);
            }
            else{
                super.setAggressive(aggressive);
            }
        }
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    // from AbstractSkeleton
    public AbstractArrow getArrow(ItemStack pickupItemStack, float velocity, @Nullable ItemStack weapon){
        return ProjectileUtil.getMobArrow(this, pickupItemStack, velocity, weapon);
    }

    // from AbstractSkeleton
    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        boolean isBow = getItemInHand(InteractionHand.MAIN_HAND).is(Tags.Items.TOOLS_BOW) || getItemInHand(InteractionHand.OFF_HAND).is(Tags.Items.TOOLS_BOW);
        boolean isCrossbow = getItemInHand(InteractionHand.MAIN_HAND).is(Tags.Items.TOOLS_CROSSBOW) || getItemInHand(InteractionHand.OFF_HAND).is(Tags.Items.TOOLS_CROSSBOW);
        if(isBow){
            ItemStack weapon = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, (item) -> {
                return item instanceof BowItem;
            }));
            ItemStack itemstack1 = new ItemStack(Items.ARROW);

            AbstractArrow abstractarrow = this.getArrow(itemstack1, distanceFactor, weapon);
            Item weaponItem = weapon.getItem();
            if (weaponItem instanceof ProjectileWeaponItem projectileWeaponItem) {
                abstractarrow = projectileWeaponItem.customArrow(abstractarrow, itemstack1, weapon);
            }

            double d0 = target.getX() - this.getX();
            double d1 = target.getY(0.3333333333333333) - abstractarrow.getY();
            double d2 = target.getZ() - this.getZ();
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            Level var15 = this.level();
            if (var15 instanceof ServerLevel serverlevel) {
                Projectile.spawnProjectileUsingShoot(abstractarrow, serverlevel, itemstack1, d0, d1 + d3 * 0.20000000298023224, d2, 1.6F, (float)(14 - serverlevel.getDifficulty().getId() * 4));
            }

            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        }
        else if(isCrossbow){
            this.performCrossbowAttack(target,distanceFactor);
        }
        else{

        }
    }

    public Projectile createCustomProjectile(Level level, LivingEntity shooter, ItemStack weapon, ItemStack ammo, boolean isCrit){
        Item ammoItem = ammo.getItem();
        ArrowItem arrowItemType;
        if (ammoItem instanceof ArrowItem arrowitem1) {
            arrowItemType = arrowitem1;
        } else {
            arrowItemType = (ArrowItem)Items.ARROW;
        }

        ArrowItem arrowitem = arrowItemType;
        AbstractArrow abstractarrow = arrowitem.createArrow(level, ammo, shooter, weapon);
        if (isCrit) {
            abstractarrow.setCritArrow(true);
        }

        return abstractarrow;
    }

    @Override
    public void performCrossbowAttack(LivingEntity user, float velocity) {
        ItemStack itemstack = user.getItemInHand(user.getUsedItemHand());
        Item stack = itemstack.getItem();

        itemstack = new ItemStack(Items.CROSSBOW);
        itemstack.set(DataComponents.CHARGED_PROJECTILES,ChargedProjectiles.of(List.of(
                new ItemStack(Items.ARROW)
        )));

        if (user.level() instanceof ServerLevel serverLevel) {
            ItemStack weapon = user.getItemInHand(InteractionHand.MAIN_HAND);
            LivingEntity shooter = user;
            List<ItemStack> projectileItems = List.of(new ItemStack(Items.ARROW),new ItemStack(Items.ARROW),new ItemStack(Items.ARROW));

            float f = EnchantmentHelper.processProjectileSpread(serverLevel, weapon, shooter, 0.0F);
            float f1 = projectileItems.size() == 1 ? 0.0F : 2.0F * f / (float)(projectileItems.size() - 1);
            float f2 = (float)((projectileItems.size() - 1) % 2) * f1 / 2.0F;
            float f3 = 1.0F;

            boolean fireworks = false;

            for(int i = 0; i < projectileItems.size(); ++i) {
                ItemStack itemInList = projectileItems.get(i);
                if (!itemstack.isEmpty()) {
                    float f4 = f2 + f3 * (float)((i + 1) / 2) * f1;
                    f3 = -f3;
                    int j = i;

                    // crossbows don't exactly work without non-static methods, so this replaces a check to make a 'fake arrow'
                    Arrow replacementFakeArrow = new Arrow(user.level(), this,
                            new ItemStack(Items.ARROW),
                            new ItemStack(equipment.get(EquipmentSlot.MAINHAND).getItem()));

                    if(user.getItemInHand(InteractionHand.MAIN_HAND).is(OMItems.ENDARKENED_CROSSBOW)){
                        replacementFakeArrow.setRemainingFireTicks(20);
                    }
                    else{
                        // anything that is not the endarkened crossbow but like a crossbow
                        ItemStack currentCrossbowOrLike = equipment.get(EquipmentSlot.MAINHAND);
                        if(currentCrossbowOrLike.has(DataComponents.ENCHANTMENTS)){
                            ItemEnchantments enchantments = currentCrossbowOrLike.get(DataComponents.ENCHANTMENTS);
                            int flameLevel = enchantments.getLevel(
                                    OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel,Enchantments.FLAME));
                            if(flameLevel > 0){
                                replacementFakeArrow.setRemainingFireTicks(10 * flameLevel);
                            }
                        }
                        if(currentCrossbowOrLike.has(DataComponents.CHARGED_PROJECTILES)){
                            ChargedProjectiles chargedProjectiles = currentCrossbowOrLike.get(DataComponents.CHARGED_PROJECTILES);
                            if(chargedProjectiles != null){
                                if(chargedProjectiles.contains(Items.FIREWORK_ROCKET)){
                                    fireworks = true;
                                }
                            }
                        }
                    }
                    replacementFakeArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

                    double d0 = user.getX() - this.getX();
                    double d1 = user.getEyeY() - 1.0f;
                    double d2 = user.getZ() - this.getZ();
                    double d3 = Math.sqrt(d0 * d0 + d2 * d2) * 0.2f;

                    itemstack = new ItemStack(Items.ARROW);

                    if(fireworks){
                        ItemStack fireworkItem = new ItemStack(Items.FIREWORK_ROCKET);
                        fireworkItem.set(DataComponents.FIREWORKS,
                                new Fireworks(Mth.nextInt(serverLevel.getRandom(),
                                2,3),
                                        List.of(
                                                new FireworkExplosion(FireworkExplosion.Shape.SMALL_BALL,
                                                        IntList.of(OPUtil.ULTIMATE_SHARED_DURABILITY,OPUtil.ULTRA_SHARED_DURABILITY),
                                                        IntList.of(OPUtil.ULTIMATE_COLOR,OPUtil.ULTRA_COLOR),
                                                        true,false)
                                        )));
                        FireworkRocketEntity rocket =
                                new FireworkRocketEntity(serverLevel,
                                        fireworkItem, this,
                                        this.getX(), this.getEyeY() - 0.15F, this.getZ(),
                                        true);
                        rocket.addDeltaMovement(this.getDeltaMovement().vectorTo(getForward().add(-0.45D,0D,0D)));
                        rocket.setDeltaMovement(rocket.getDeltaMovement().x,0.01D,rocket.getDeltaMovement().z);
                        serverLevel.addFreshEntity(rocket);
                    }
                    else{
                        Projectile.spawnProjectile(
                                replacementFakeArrow,
                                serverLevel,
                                itemstack,
                                projectileToShoot -> projectileToShoot.shoot(d0, d1 + d3 - projectileToShoot.getY(), d2,
                                        2.25f, 0.2f)
                        );
                    }

                    if (weapon.isEmpty()) {
                        break;
                    }
                }
            }
        }

        this.onCrossbowAttackPerformed();
    }

    /*
    if(this.level() instanceof ServerLevel serverLevel){
            ItemStack itemInMainHand = livingEntity.getItemInHand(InteractionHand.MAIN_HAND);
            livingEntity.startUsingItem(InteractionHand.MAIN_HAND);

            // use the item if in hand
            if(itemInMainHand.is(Tags.Items.TOOLS_BOW)){

            }
            else if(itemInMainHand.is(Tags.Items.TOOLS_CROSSBOW)){
                if(itemInMainHand.has(DataComponents.CHARGED_PROJECTILES)){

                }
            }
            else{
                double d0 = livingEntity.getX() - this.getX();
                double d1 = livingEntity.getEyeY() - 1.0f;
                double d2 = livingEntity.getZ() - this.getZ();
                double d3 = Math.sqrt(d0 * d0 + d2 * d2) * 0.2f;
                if (this.level() instanceof ServerLevel serverlevel) {
                    ItemStack itemstack = new ItemStack(Items.END_CRYSTAL);
                    Projectile.spawnProjectile(
                            new Snowball(serverlevel, this, itemstack),
                            serverlevel,
                            itemstack,
                            snowball -> snowball.shoot(d0, d1 + d3 - snowball.getY(), d2, 2.0f, 1.0f)
                    );
                }

                this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            }
        }
     */

    @Override
    public void onEquipItem(EquipmentSlot slot, ItemStack oldItem, ItemStack newItem) {
        super.onEquipItem(slot, oldItem, newItem);
        if(!level().isClientSide()){
            this.reassignWeaponGoals();
        }
    }

    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND || slot == EquipmentSlot.OFFHAND;
    }

    @Override
    public boolean canUseNonMeleeWeapon(ItemStack stack) {
        return stack.is(Tags.Items.SKELETON_USABLE_BOWS) ||
                stack.is(Tags.Items.PIGLIN_USABLE_CROSSBOWS) ||
                stack.is(Tags.Items.PILLAGER_USABLE_CROSSBOWS)
                || stack.is(OMTags.MUSIC_DISC_WUBS);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("unhappy",this.entityData.get(UNHAPPY_DATA));
        output.putBoolean("charging_crossbow",this.chargingCrossbow);
        output.putBoolean("befriended",this.entityData.get(BEFRIENDED_DATA));
        output.putBoolean("bad_faction",this.entityData.get(BAD_FACTION_DATA));
        output.putInt("skin_variant",this.entityData.get(SKIN_DATA));

        // save items to disk
        ValueOutput.TypedOutputList<ItemStackWithSlot> typedoutputlist = output.list("Items", ItemStackWithSlot.CODEC);
        for(int i = 0; i < this.getInventory().getContainerSize(); ++i) {
            ItemStack itemstack = this.getInventory().getItem(i);
            if (!itemstack.isEmpty()) {
                typedoutputlist.add(new ItemStackWithSlot(i, itemstack));
            }
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(UNHAPPY_DATA,input.getBooleanOr("unhappy",false));
        this.chargingCrossbow = input.getBooleanOr("charging_crossbow",false);
        this.entityData.set(BEFRIENDED_DATA,input.getBooleanOr("befriended",false));
        this.entityData.set(BAD_FACTION_DATA, input.getBooleanOr("bad_faction",false));
        this.entityData.set(SKIN_DATA,input.getIntOr("skin_variant",(int)OPUtil.nextFloatBetweenInclusive(0,3)));

        this.reassignWeaponGoals();

        // get saved items from disk
        Iterator itemIterators = input.listOrEmpty("Items", ItemStackWithSlot.CODEC).iterator();
        while(itemIterators.hasNext()) {
            ItemStackWithSlot itemstackwithslot = (ItemStackWithSlot)itemIterators.next();
            if (itemstackwithslot.isValidInContainer(this.getInventory().getContainerSize())) {
                this.getInventory().setItem(itemstackwithslot.slot(), itemstackwithslot.stack());
            }
        }
    }

    @Override
    public void onDamageTaken(DamageContainer damageContainer) {
        this.entityData.set(UNHAPPY_DATA,true);
    }

    @Override
    public void setChargingCrossbow(boolean b) {
        this.chargingCrossbow = b;
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public SimpleContainer getInventory() {
        return fraudInventory;
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf registryFriendlyByteBuf) {

    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf registryFriendlyByteBuf) {

    }
}
