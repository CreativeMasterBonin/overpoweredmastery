package net.rk.overpoweredmastery.entity.custom;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ThrownLingeringPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.EventHooks;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.util.OPUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class TrialWubEnergyBall extends AbstractHurtingProjectile {
    int count = 0;
    private static final EntityDataAccessor<Boolean> DATA_OXIDIZED = SynchedEntityData.defineId(TrialWubEnergyBall.class, EntityDataSerializers.BOOLEAN);

    public TrialWubEnergyBall(double x, double y, double z, Vec3 vector, Level level) {
        super(OMEntityTypes.TRIAL_WUB_ENERGY_BALL.get(), x, y, z, vector, level);
    }

    public TrialWubEnergyBall(LivingEntity livingEntity, Vec3 vector, Level level) {
        super(OMEntityTypes.TRIAL_WUB_ENERGY_BALL.get(), livingEntity, vector, level);
    }

    public TrialWubEnergyBall(EntityType<TrialWubEnergyBall> energyBallEntityType, Level level) {
        super(energyBallEntityType,level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_OXIDIZED,false);
    }

    public void setOxidized(boolean oxidizedVersion) {
        this.entityData.set(DATA_OXIDIZED,oxidizedVersion);
    }

    public boolean getOxidized() {
        return this.entityData.get(DATA_OXIDIZED);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput value) {
        super.addAdditionalSaveData(value);
        value.putInt("count",count);
        value.putBoolean("oxidized",this.getOxidized());
    }

    @Override
    protected void readAdditionalSaveData(ValueInput value) {
        super.readAdditionalSaveData(value);
        count = value.getIntOr("count",0);
        this.setOxidized(value.getBooleanOr("oxidized",false));
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    protected ClipContext.Block getClipType() {
        return ClipContext.Block.OUTLINE;
    }

    @Override
    public void tick() {
        super.tick();
        count++;
        boolean isMovingX = getDeltaMovement().x > 0 || getDeltaMovement().x < 0;
        boolean isMovingY = getDeltaMovement().y > 0 || getDeltaMovement().y < 0;
        boolean isMovingZ = getDeltaMovement().z > 0 || getDeltaMovement().z < 0;
        // this energy ball doesn't last forever!
        if(count >= 120){
            this.discard();
        }
        if(isMovingX || isMovingY || isMovingZ){
            // particles for wubs
            int lightorangeish = 16761442;
            int orangeish = 16751202;
            int oxidizedDarkCopper = 5279864;
            int oxidizedLightCopper = 7583379;
            if(this.getOxidized()){
                this.level().addParticle(new DustParticleOptions(count % 2 == 0 ? oxidizedLightCopper : oxidizedDarkCopper,1.0f),this.getX(),this.getY() + 0.5,this.getZ(),
                        0,0,0);
            }
            else{
                this.level().addParticle(new DustParticleOptions(count % 2 == 0 ? lightorangeish : orangeish,1.0f),this.getX(),this.getY() + 0.5,this.getZ(),
                        0,0,0);
            }
        }
        else{
            this.discard();
        }
    }

    @Nullable
    @Override
    protected ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    public void onHitBlock(BlockHitResult result) {
        if(this.level() instanceof ServerLevel serverLevel){
            if(!this.getOxidized()){
                if(OPUtil.nextFloatBetweenInclusive(0.0f,1.0f) <= 0.02f){
                    ItemStack lingeringPotion = new ItemStack(Items.LINGERING_POTION);
                    List<Holder<Potion>> list = BuiltInRegistries.POTION
                            .listElements()
                            .filter(holderrefpotion -> !holderrefpotion.value().getEffects().isEmpty() && this.level().potionBrewing().isBrewablePotion(holderrefpotion))
                            .collect(Collectors.toList());
                    Holder<Potion> holder = Util.getRandom(list, this.level().getRandom());

                    DataComponentPatch componentPatch = DataComponentPatch.builder()
                            .set(DataComponents.POTION_CONTENTS,new PotionContents(holder))
                            .set(DataComponents.POTION_DURATION_SCALE, 0.02F)
                            .build();
                    lingeringPotion.applyComponents(componentPatch);
                    ThrownLingeringPotion potion = new ThrownLingeringPotion(serverLevel,this.getX(),this.getY(),this.getZ(),
                            lingeringPotion);
                    potion.setSilent(true);
                    potion.setGlowingTag(true);
                    serverLevel.addFreshEntity(potion);
                }
                boolean canCauseFire = EventHooks.canEntityGrief(serverLevel,this.getOwner()) && !serverLevel.getDifficulty().equals(Difficulty.PEACEFUL);
                serverLevel.explode(this,this.getX(),this.getY(),this.getZ(),this.level().getRandom().nextIntBetweenInclusive(1,2),canCauseFire,Level.ExplosionInteraction.BLOCK);
            }
            else{
                if(serverLevel.getBlockState(result.getBlockPos()).getBlock().getDescriptionId().contains("Copper") || serverLevel.getBlockState(result.getBlockPos()).getBlock().getDescriptionId().contains("Tuff")){
                    serverLevel.playSound(this,result.getBlockPos(), SoundEvents.ILLUSIONER_CAST_SPELL, SoundSource.NEUTRAL,1.0f,1.0f);
                    serverLevel.setBlock(result.getBlockPos(),Blocks.AIR.defaultBlockState(),3);
                }
                else if(serverLevel.getBlockState(result.getBlockPos()).getBlock().getDescriptionId().contains("Oxidized")){
                    serverLevel.explode(this,getX(),getY(),getZ(),10.0f,Level.ExplosionInteraction.TNT);
                    serverLevel.explode(this,getX(),getY() + 2.0,getZ(),1.15f,Level.ExplosionInteraction.MOB);
                    serverLevel.explode(this,getX(),getY() - 2.0,getZ(),1.15f,Level.ExplosionInteraction.MOB);
                    serverLevel.explode(this,getX() - 2.0,getY(),getZ(),1.1f,Level.ExplosionInteraction.MOB);
                    serverLevel.explode(this,getX() + 2.0,getY(),getZ(),1.1f,Level.ExplosionInteraction.MOB);
                    serverLevel.explode(this,getX(),getY(),getZ() - 2.0,1.1f,Level.ExplosionInteraction.MOB);
                    serverLevel.explode(this,getX(),getY(),getZ() + 2.0,1.1f,Level.ExplosionInteraction.MOB);
                }
            }
        }
        this.discard();
    }

    @Override
    public void onHitEntity(EntityHitResult result){
        if(result.getEntity().level() instanceof ServerLevel serverLevel){
            result.getEntity().setDeltaMovement(0,result.getEntity().getDeltaMovement().y * 2,0);
            if(result.getEntity() instanceof LivingEntity && this.getOxidized()){
                if(BuiltInRegistries.ENTITY_TYPE.getRandom(serverLevel.getRandom()).isPresent()){
                    if(!BuiltInRegistries.ENTITY_TYPE.getRandom(serverLevel.getRandom()).get().value().is(Tags.EntityTypes.BOSSES) && !BuiltInRegistries.ENTITY_TYPE.getRandom(serverLevel.getRandom()).get().value().is(Tags.EntityTypes.BOATS) && !BuiltInRegistries.ENTITY_TYPE.getRandom(serverLevel.getRandom()).get().value().is(Tags.EntityTypes.MINECARTS)){
                        this.discard();
                        return;
                    }
                    else{
                        Entity copyEntity = result.getEntity();
                        copyEntity.remove(RemovalReason.DISCARDED);
                        Entity entity = BuiltInRegistries.ENTITY_TYPE.getRandom(serverLevel.getRandom()).get().value().create(
                                serverLevel,null,
                                copyEntity.getOnPos(),
                                EntitySpawnReason.MOB_SUMMONED,true,false);
                        serverLevel.addFreshEntity(entity);
                    }
                }
            }
        }
        this.discard();
    }
}
