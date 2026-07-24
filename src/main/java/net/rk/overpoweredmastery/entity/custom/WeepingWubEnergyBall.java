package net.rk.overpoweredmastery.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.projectile.hurtingprojectile.AbstractHurtingProjectile;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DriedGhastBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.Config;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.util.OPUtil;
import org.jspecify.annotations.Nullable;

public class WeepingWubEnergyBall extends AbstractHurtingProjectile {
    int count = 0;

    public WeepingWubEnergyBall(double x, double y, double z, Vec3 vector, Level level) {
        super(OMEntityTypes.WEEPING_WUB_ENERGY_BALL.get(), x, y, z, vector, level);
    }

    public WeepingWubEnergyBall(LivingEntity livingEntity, Vec3 vector, Level level) {
        super(OMEntityTypes.WEEPING_WUB_ENERGY_BALL.get(), livingEntity, vector, level);
    }

    public WeepingWubEnergyBall(EntityType<WeepingWubEnergyBall> weepingEnergyBallType, Level level) {
        super(weepingEnergyBallType,level);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("count",this.count);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        input.getIntOr("count",0);
    }

    @Override
    public void tick() {
        super.tick();
        count++;
        boolean isMovingX = getDeltaMovement().x > 0 || getDeltaMovement().x < 0;
        boolean isMovingY = getDeltaMovement().y > 0 || getDeltaMovement().y < 0;
        boolean isMovingZ = getDeltaMovement().z > 0 || getDeltaMovement().z < 0;
        // this energy ball doesn't last forever!
        if(count >= Config.WEEPING_WUB_ENERGY_BALL_LIFETIME.getAsInt()){
            this.discard();
        }
        if(isMovingX || isMovingY || isMovingZ){
            int weepingBlueHighlight = 15400959;
            int weepingBlueLow = 12771562;
            this.level().addParticle(new DustParticleOptions(count % 2 == 0 ? weepingBlueHighlight : weepingBlueLow,
                            OPUtil.nextFloatBetweenInclusive(0.91f,1.0f)),this.getX(),this.getY() + 0.5,this.getZ(),
                    0,0,0);
        }
        else{
            this.discard();
        }
    }

    @Override
    public boolean shouldBurn() {
        return false;
    }

    @Override
    public @Nullable ParticleOptions getTrailParticle() {
        return null;
    }

    @Override
    public void onHitBlock(BlockHitResult result) {
        BlockPos posHit = result.getBlockPos();
        if(level() instanceof ServerLevel serverLevel){
            if(serverLevel.getBlockState(posHit).is(Blocks.DRIED_GHAST)){
                serverLevel.setBlock(posHit,Blocks.DRIED_GHAST.defaultBlockState().setValue(DriedGhastBlock.HYDRATION_LEVEL,3),3);
            }
            else if(serverLevel.getBlockState(posHit).is(Blocks.SOUL_SOIL) || serverLevel.getBlockState(posHit).is(Blocks.SOUL_SAND)){
                if(serverLevel.getBlockState(posHit.below()).isAir()){
                    FallingBlockEntity fallingBlock = new FallingBlockEntity(serverLevel,posHit.getX(), posHit.getY(),posHit.getZ(),serverLevel.getBlockState(posHit));
                    serverLevel.setBlock(posHit,Blocks.AIR.defaultBlockState(),3); // prevent duping from offset positioned entity
                    serverLevel.addFreshEntity(fallingBlock);
                }
                else{
                    serverLevel.explode(this,posHit.getX(),
                            posHit.getY(),posHit.getZ(),
                            10.0f,
                            Level.ExplosionInteraction.MOB);
                }
            }
        }
        this.discard();
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        if(level() instanceof ServerLevel serverLevel){
            if(target instanceof Ghast){
                target.hurtServer(serverLevel,serverLevel.damageSources().magic(),20.0f);
            }
            else{
                serverLevel.explode(this,
                        target.getX(),target.getY(),target.getZ(),
                        10.0f,
                        true,Level.ExplosionInteraction.MOB);
            }
        }
        this.discard();
    }
}
