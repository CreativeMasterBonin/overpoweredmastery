package net.rk.overpoweredmastery.entity.custom;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.Config;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import org.jetbrains.annotations.Nullable;

public class ChickenWubEnergyBall extends AbstractHurtingProjectile{
    int count = 0;

    public ChickenWubEnergyBall(double x, double y, double z, Vec3 vector, Level level) {
        super(OMEntityTypes.CHICKEN_WUB_ENERGY_BALL.get(), x, y, z, vector, level);
    }

    public ChickenWubEnergyBall(LivingEntity livingEntity, Vec3 vector, Level level) {
        super(OMEntityTypes.CHICKEN_WUB_ENERGY_BALL.get(), livingEntity, vector, level);
    }

    public ChickenWubEnergyBall(EntityType<ChickenWubEnergyBall> redWubEnergyBallEntityType, Level level) {
        super(redWubEnergyBallEntityType,level);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput value){
        super.addAdditionalSaveData(value);
        value.putInt("count",count);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput value){
        super.readAdditionalSaveData(value);
        count = value.getIntOr("count",0);
    }

    @Override
    protected boolean shouldBurn(){
        return false;
    }

    @Override
    public void tick(){
        super.tick();
        count++;
        boolean isMovingX = getDeltaMovement().x > 0 || getDeltaMovement().x < 0;
        boolean isMovingY = getDeltaMovement().y > 0 || getDeltaMovement().y < 0;
        boolean isMovingZ = getDeltaMovement().z > 0 || getDeltaMovement().z < 0;
        // this energy ball doesn't last forever!
        if(count >= 70){
            this.discard();
        }
        if(isMovingX || isMovingY || isMovingZ){
            // red particles for a red wub gun
            int allTheColors = level().getRandom().nextInt(-16000,16000);
            this.level().addParticle(new DustParticleOptions(allTheColors,1.0f),this.getX(),this.getY() + 0.5,this.getZ(),
                    0,0,0);
        }
        else{
            this.discard();
        }
    }

    @Nullable
    @Override
    protected ParticleOptions getTrailParticle(){
        return null;
    }

    @Override
    public void onHitBlock(BlockHitResult result){
        if(level() instanceof ServerLevel serverLevel){
            boolean hasChickenInName = serverLevel.getBlockState(result.getBlockPos()).getBlock()
                    .getDescriptionId().toLowerCase().contains("chicken");
            boolean hasJockeyInName = serverLevel.getBlockState(result.getBlockPos()).getBlock()
                    .getDescriptionId().toLowerCase().contains("jockey");
            if(!(this.getOwner() == null)){
                if((hasChickenInName || hasJockeyInName) && getBlockStateOn().canEntityDestroy(level(),result.getBlockPos(),this.getOwner())){
                    serverLevel.destroyBlock(result.getBlockPos(),true,this,0);
                    serverLevel.updateNeighborsAt(result.getBlockPos(),serverLevel.getBlockState(result.getBlockPos()).getBlock());
                }
            }
        }
        this.discard();
    }

    @Override
    public void onHitEntity(EntityHitResult result){
        if(result.getEntity() instanceof Chicken chicken){
            if(this.level() instanceof ServerLevel serverLevel){
                chicken.setRemainingFireTicks(120);
                chicken.hurtServer(serverLevel,this.damageSources().magic(),Config.CHICKEN_WUBS_DAMAGE_BASE_CHICKEN.get());
            }
        }
        else if(result.getEntity() instanceof Zombie zombie){
            if(zombie.isBaby()){
                if(this.level() instanceof ServerLevel serverLevel){
                    zombie.setAggressive(true);
                    zombie.setRemainingFireTicks(120);
                    zombie.hurtServer(serverLevel,this.damageSources().magic(),Config.CHICKEN_WUBS_DAMAGE_BASE_BABY_ZOMBIE.get());
                }
            }
        }
        else if(result.getEntity() instanceof LivingEntity livingEntity){
            if(this.level() instanceof ServerLevel serverLevel){
                if(!(livingEntity instanceof Player)){
                    boolean hasChicken = livingEntity.getName().getString().toLowerCase().contains("chicken");
                    boolean hasJockey = livingEntity.getName().getString().toLowerCase().contains("jockey");
                    if(hasChicken || hasJockey){
                        livingEntity.hurtServer(serverLevel,this.damageSources().magic(),Config.CHICKEN_WUBS_DAMAGE_BASE_IS_CHICKEN_JOCKEY.get());
                        serverLevel.addFreshEntity(new ItemEntity(serverLevel,this.getX(),this.getY(),this.getZ(),
                                new ItemStack(Items.EGG)));
                    }
                    else{
                        livingEntity.hurtServer(serverLevel,this.damageSources().magic(),5);
                    }
                }
            }
        }
        this.discard();
    }
}
