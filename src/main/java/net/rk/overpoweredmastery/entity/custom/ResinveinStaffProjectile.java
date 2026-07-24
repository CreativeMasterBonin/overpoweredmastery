package net.rk.overpoweredmastery.entity.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.util.OPUtil;

public class ResinveinStaffProjectile extends AbstractStaffProjectile{
    public ResinveinStaffProjectile(EntityType<? extends AbstractStaffProjectile> staffProjectile, Level level) {
        super(OMEntityTypes.RESINVEIN_STAFF_PROJECTILE.get(), level);
    }

    @Override
    public SoundEvent ambientDroneSound() {
        return SoundEvents.BEACON_AMBIENT;
    }

    @Override
    public int getAmbientSoundWaitTicks() {
        return 81;
    }

    @Override
    public int getLifetime() {
        return 200;
    }

    @Override
    public float getScale() {
        return 1.0f;
    }

    @Override
    public int getColor() {
        return OPUtil.nextIntBetweenInclusive(111110,1283100);
    }

    @Override
    public void projectileHitBlock(BlockHitResult result) {
        if(level() instanceof ServerLevel serverLevel){
            serverLevel.explode(this,getX(),getY(),getZ(),2.0f,false, Level.ExplosionInteraction.MOB);
        }
    }

    @Override
    public void projectileHitEntity(EntityHitResult result) {
        if(level() instanceof ServerLevel serverLevel){
            Entity hitEntity = result.getEntity();
            float damageValuedByTimeLeft = count * 0.05f;

            if(serverLevel.getRandom().nextBoolean()){
                serverLevel.playSound(this,getX(),getY(),getZ(),SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.NEUTRAL,0.9f,OPUtil.nextFloatBetweenInclusive(0.9f,1.0f));
                hitEntity.hurtServer(serverLevel,serverLevel.damageSources().magic(),7.0f + damageValuedByTimeLeft);
            }
            else{
                hitEntity.hurtServer(serverLevel,serverLevel.damageSources().magic(),1.0f + damageValuedByTimeLeft);
            }
        }
    }
}
