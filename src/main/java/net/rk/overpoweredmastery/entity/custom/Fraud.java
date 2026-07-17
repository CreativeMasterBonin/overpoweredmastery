package net.rk.overpoweredmastery.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
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
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
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

public class Fraud extends PathfinderMob implements EquipmentUser, Targeting, RangedAttackMob {
    public boolean isUnhappy = false;
    public boolean breakDoors = false;
    public SimpleContainer fraudInventory = new SimpleContainer(4);

    private final RangedBowAttackGoal fraudUseBowGoal = new RangedBowAttackGoal<>(this, 1.0, 20, 15.0F);

    public Fraud(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        if(level instanceof ServerLevel){
            this.registerGoals();
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
                stack.is(ItemTags.HEAD_ARMOR) || stack.is(ItemTags.CHEST_ARMOR) || stack.is(ItemTags.LEG_ARMOR) || stack.is(ItemTags.FOOT_ARMOR);
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
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 2.35f, 17, 7.0f));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this,1.19f,false){
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
        });
        this.goalSelector.addGoal(1,new PanicGoal(this,1.5f));
        this.goalSelector.addGoal(1,new AvoidEntityGoal<>(this, WanderingTrader.class,
                16.0f,0.95f,1.25f));

        this.goalSelector.addGoal(1, new MoveTowardsTargetGoal(this, 1.22f, 64.0f));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new MoveBackToVillageGoal(this, 1.31f, false));

        this.goalSelector.addGoal(10,new LookAtPlayerGoal(this, Player.class,6.0f));
        this.goalSelector.addGoal(10,new LookAtPlayerGoal(this, Villager.class,6.0f));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.1f,
                stack -> stack.is(ItemTags.BEACON_PAYMENT_ITEMS), false));

        this.goalSelector.addGoal(5,new RandomStrollGoal(this,0.95f));
        this.goalSelector.addGoal(2,new MoveThroughVillageGoal(this, 1.0f, true, 4,this::canBreakDoors));
        this.goalSelector.addGoal(2,new OpenDoorGoal(this,true));

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
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 20, true, true, (livingEntity, serverLevel) -> {
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

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float v) {
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
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("unhappy",this.isUnhappy);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.isUnhappy = input.getBooleanOr("unhappy",false);
    }
}
