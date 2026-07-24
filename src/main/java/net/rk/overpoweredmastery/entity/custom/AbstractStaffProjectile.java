package net.rk.overpoweredmastery.entity.custom;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jspecify.annotations.Nullable;

public abstract class AbstractStaffProjectile extends AbstractHurtingProjectile {
    public int count = 0;

    public AbstractStaffProjectile(EntityType<? extends AbstractHurtingProjectile> projectileType, Level level) {
        super(projectileType, level);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput value){
        super.addAdditionalSaveData(value);
        value.putInt("count",count);
    }

    @Override
    public void readAdditionalSaveData(ValueInput value){
        super.readAdditionalSaveData(value);
        count = value.getIntOr("count",0);
    }

    @Override
    public boolean shouldBurn() {
        return false;
    }

    /**
     * The ambient sound this projectile will play while it exists and moving
     * @return The SoundEvent to play
     */
    public abstract SoundEvent ambientDroneSound();

    /**
     * The ticks until the ambient sound can play again (and for the first time)
     * @return An integer of the range of more than 1 but less than or equal to Integer.MAX_VALUE
     */
    public abstract int getAmbientSoundWaitTicks();

    /**
     * The lifetime this projectile will last in 'ticks'
     * @return The ticks to be used
     */
    public abstract int getLifetime();

    /**
     * The scale of the particles for this projectile
     * @return The floating-point scale to be used
     */
    public abstract float getScale();

    /**
     * The color of the particles for this projectile (can be randomly generated or a static decimal color)
     * @return The decimal/randomized color to be used
     */
    public abstract int getColor();

    /**
     * Show particle effects while the projectile exists
     * This method can be overwritten to add custom particles
     */
    public void doParticles(){
        this.level().addParticle(new DustParticleOptions(getColor(),getScale()),this.getX(),this.getY() + 0.5,this.getZ(),
                0,0,0);
    };

    @Override
    public @Nullable ParticleOptions getTrailParticle(){return null;}

    @Override
    public void tick() {
        super.tick();
        count++;
        boolean isMovingX = getDeltaMovement().x > 0 || getDeltaMovement().x < 0;
        boolean isMovingY = getDeltaMovement().y > 0 || getDeltaMovement().y < 0;
        boolean isMovingZ = getDeltaMovement().z > 0 || getDeltaMovement().z < 0;
        // this projectile doesn't last forever!
        if(count >= getLifetime()){
            this.discard();
        }
        // the projectile needs to be moving or else is considered 'for deletion'
        if(isMovingX || isMovingY || isMovingZ){
            // particles for display instead of a custom model
            this.doParticles();
            int waitTillPlayTime = Mth.clamp(getAmbientSoundWaitTicks(),2,Integer.MAX_VALUE);

            if(level().getGameTime() % waitTillPlayTime == 0){
                this.playSound(ambientDroneSound(),1.0f,1.0f);
            }
        }
        else{
            this.discard();
        }
    }

    /**
     * Extends the onHitBlock method further to allow any mods' additions to the super method to apply
     * @param result The BlockHitResult of the block this projectile just hit (contains all needed elements for the level and entity)
     */
    public abstract void projectileHitBlock(BlockHitResult result);

    /**
     * Extends the onHitEntity method further to allow any mods' additions to the super method to apply
     * @param result The EntityHitResult of the entity this projectile just hit (contains the entity that was hit, which can access other objects as needed)
     */
    public abstract void projectileHitEntity(EntityHitResult result);

    @Override
    public void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        projectileHitBlock(result);
        this.discard();
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        projectileHitEntity(result);
        this.discard();
    }
}
