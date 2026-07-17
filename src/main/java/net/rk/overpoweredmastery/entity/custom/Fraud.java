package net.rk.overpoweredmastery.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
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
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidType;
import net.rk.overpoweredmastery.datagen.OMTags;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.util.OPUtil;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class Fraud extends PathfinderMob implements EquipmentUser, Targeting, RangedAttackMob, CrossbowAttackMob {
    public boolean isUnhappy = false;
    public boolean breakDoors = false;
    public boolean chargingCrossbow = false;
    public SimpleContainer fraudInventory = new SimpleContainer(4);

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

    public Fraud(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        if(level instanceof ServerLevel){
            this.registerGoals();
            this.reassignWeaponGoals();
        }
    }

    public static AttributeSupplier createAttributes() {
        return AttributeSupplier.builder()
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
                .add(Attributes.TEMPT_RANGE,16.0f)
                .add(net.neoforged.neoforge.common.NeoForgeMod.SWIM_SPEED)
                .add(net.neoforged.neoforge.common.NeoForgeMod.NAMETAG_DISTANCE)
                .build();
    }

    public Fraud(Level level){
        this(OMEntityTypes.FRAUD.get(),level);
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
    public InteractionResult mobInteract(Player player, InteractionHand hand){
        this.setAggressive(true);
        return InteractionResult.PASS;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        level.explode(this,getX(),getY(),getZ(),5.0f, Level.ExplosionInteraction.MOB);
        this.discard();
    }

    @Override
    public void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.NETHERITE_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST,new ItemStack(Items.NETHERITE_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS,new ItemStack(Items.NETHERITE_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET,new ItemStack(Items.NETHERITE_BOOTS));
    }

    @Override
    public boolean wantsToPickUp(ServerLevel level, ItemStack stack) {
        return stack.is(ItemTags.SWORDS)
                || stack.is(ItemTags.AXES)
                || stack.is(ItemTags.PARROT_POISONOUS_FOOD) ||
                stack.is(Items.POTION) ||
                stack.is(ItemTags.BOW_ENCHANTABLE) || stack.is(ItemTags.CROSSBOW_ENCHANTABLE) ||
                stack.is(ItemTags.MACE_ENCHANTABLE) || stack.is(ItemTags.TRIDENT_ENCHANTABLE) ||
                stack.is(ItemTags.HEAD_ARMOR) ||
                stack.is(ItemTags.CHEST_ARMOR) ||
                stack.is(ItemTags.LEG_ARMOR) ||
                stack.is(ItemTags.FOOT_ARMOR) ||
                stack.is(OMTags.MUSIC_DISC_WUBS);
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
                        interval = 12;
                    }
                    default -> {
                        interval = 15;
                    }
                }
                this.fraudUseBowGoal.setMinAttackInterval(interval);
                this.goalSelector.addGoal(3, this.fraudUseBowGoal);
            }
            else if(itemStackCrossbow.getItem() instanceof CrossbowItem){
                this.goalSelector.addGoal(3,this.fraudRangedCrossbowGoal);
            }
            else {
                this.goalSelector.addGoal(3, this.meleeGoal);
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
            if(this.hurtTime > 0 || this.hurtDuration > 0 || this.attackAnim > 0.0f){
                this.isUnhappy = true;
            }
            else{
                this.isUnhappy = false;
            }
            ItemStack itemInHand = this.getItemInHand(InteractionHand.MAIN_HAND);

            // play a sound as if drinking the potion
            if(itemInHand.get(DataComponents.POTION_CONTENTS) != null && tickCount % 18 == 0){
                serverLevel.playSound(this,getX(),getY(),getZ(),SoundEvents.HONEY_DRINK, SoundSource.NEUTRAL,
                        0.57f, OPUtil.nextFloatBetweenInclusive(0.98f,1.0f));
            }
            // actually perform the selective potion action
            if(itemInHand.get(DataComponents.POTION_CONTENTS) != null && tickCount % 57 == 0){
                PotionContents contents = itemInHand.get(DataComponents.POTION_CONTENTS);
                if(contents != null) {
                    for(MobEffectInstance effectInstance : contents.getAllEffects()){
                        if(!this.hasEffect(effectInstance.getEffect()) && effectInstance.getEffect().value().isBeneficial()){
                            this.addEffect(effectInstance);
                        }
                    }
                }
                itemInHand.shrink(1);
                serverLevel.playSound(this,getX(),getY(),getZ(),SoundEvents.GLASS_BREAK, SoundSource.NEUTRAL,
                        0.75f, OPUtil.nextFloatBetweenInclusive(0.95f,1.2f));
            }
        }

        fraudEntityFiller.pop();
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
        this.goalSelector.addGoal(10, new WaterAvoidingRandomStrollGoal(this, 1.02f));

        //this.goalSelector.addGoal(3, new RangedAttackGoal(this, 2.35f, 17, 32.0f));

        this.goalSelector.addGoal(3, new MoveTowardsTargetGoal(this, 1.22f, 64.0f));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(15, new MoveBackToVillageGoal(this, 1.31f, false));

        this.goalSelector.addGoal(10,new LookAtPlayerGoal(this, Player.class,6.0f));
        this.goalSelector.addGoal(10,new LookAtPlayerGoal(this, Villager.class,6.0f));

        this.goalSelector.addGoal(12,new RandomStrollGoal(this,0.95f));

        this.goalSelector.addGoal(12,new OpenDoorGoal(this,true));

        this.goalSelector.addGoal(12, new MoveToBlockGoal(this,1.0f,14,4) {
            @Override
            public boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
                return levelReader.getBlockState(blockPos).is(OMTags.FRAUD_WANTS_TO_GO_TO);
            }
        });

        // targets to attack and seek after (the behavior of this fraud)

        // target anyone who attacks, but not a certain list of entities
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this,
                LightningBolt.class,
                PrimedTnt.class,
                VehicleEntity.class,
                AbstractHurtingProjectile.class
        ));
        // not exactly the nicest guy, but if you are down he will ignore you, or if you are on the same team
        this.targetSelector.addGoal(12, new NearestAttackableTargetGoal<>(this,Player.class,120,true,true,
                ((livingEntity, serverLevel) -> {
                    if((livingEntity.getHealth() <= livingEntity.getMaxHealth() / 2) || livingEntity.isFreezing() || livingEntity.isOnFire() || livingEntity.isInvulnerable() || livingEntity.isInvisible()){
                        return false;
                    }
                    return true;
                })));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Mob.class, 20, true, true, (livingEntity, serverLevel) -> {
            return livingEntity instanceof Enemy;
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
        else{
            super.setAggressive(aggressive);
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

    @Override
    public void performCrossbowAttack(LivingEntity user, float velocity) {
        ItemStack itemstack = user.getItemInHand(user.getUsedItemHand());
        Item stack = itemstack.getItem();

        itemstack = new ItemStack(Items.CROSSBOW);
        itemstack.set(DataComponents.CHARGED_PROJECTILES,ChargedProjectiles.of(List.of(
                new ItemStack(Items.ARROW),new ItemStack(Items.FIREWORK_ROCKET)
        )));

        if (stack instanceof CrossbowItem crossbowitem) {
            crossbowitem.performShooting(user.level(), user, user.getUsedItemHand(), itemstack, velocity, (float)(14 - user.level().getDifficulty().getId() * 4), this.getTarget());
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
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("unhappy",this.isUnhappy);
        output.putBoolean("charging_crossbow",this.chargingCrossbow);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.isUnhappy = input.getBooleanOr("unhappy",false);
        this.chargingCrossbow = input.getBooleanOr("charging_crossbow",false);
        this.reassignWeaponGoals();
    }

    public boolean isChargingCrossbow() {
        return chargingCrossbow;
    }

    @Override
    public void setChargingCrossbow(boolean b) {
        this.chargingCrossbow = b;
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }
}
