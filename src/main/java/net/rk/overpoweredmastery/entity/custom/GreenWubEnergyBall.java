package net.rk.overpoweredmastery.entity.custom;

import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class GreenWubEnergyBall extends AbstractHurtingProjectile {
    int count = 0;

    public GreenWubEnergyBall(double x, double y, double z, Vec3 vector, Level level) {
        super(OMEntityTypes.GREEN_WUB_ENERGY_BALL.get(), x, y, z, vector, level);
    }

    public GreenWubEnergyBall(LivingEntity livingEntity, Vec3 vector, Level level) {
        super(OMEntityTypes.GREEN_WUB_ENERGY_BALL.get(), livingEntity, vector, level);
    }

    public GreenWubEnergyBall(EntityType<GreenWubEnergyBall> redWubEnergyBallEntityType, Level level) {
        super(redWubEnergyBallEntityType,level);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput value) {
        super.addAdditionalSaveData(value);
        value.putInt("count",count);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput value) {
        super.readAdditionalSaveData(value);
        count = value.getIntOr("count",0);
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        count++;
        boolean isMovingX = getDeltaMovement().x > 0 || getDeltaMovement().x < 0;
        boolean isMovingY = getDeltaMovement().y > 0 || getDeltaMovement().y < 0;
        boolean isMovingZ = getDeltaMovement().z > 0 || getDeltaMovement().z < 0;
        // this energy ball doesn't last forever!
        if(count >= 150){
            this.discard();
        }
        if(isMovingX || isMovingY || isMovingZ){
            // red particles for a red wub gun
            int greenish = 6604850;
            int bluegreenish = 65350;
            this.level().addParticle(new DustParticleOptions(count % 2 == 0 ? greenish : bluegreenish,1.0f),this.getX(),this.getY() + 0.5,this.getZ(),
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
        if(this.level() instanceof ServerLevel serverLevel){
            BlockState state = this.level().getBlockState(result.getBlockPos().above());
            if(state.getBlock() instanceof CropBlock){
                CropBlock crop = (CropBlock)state.getBlock();
                crop.growCrops(this.level(),result.getBlockPos().above(),state);
            }
            else if(this.level().getBlockState(result.getBlockPos()).is(BlockTags.DIRT)){
                this.level().setBlock(result.getBlockPos(),Blocks.FARMLAND.defaultBlockState().setValue(BlockStateProperties.MOISTURE,7),3);
            }
            else if(this.level().getBlockState(result.getBlockPos()).is(BlockTags.BASE_STONE_OVERWORLD)){
                this.level().setBlock(result.getBlockPos(),Blocks.DIRT.defaultBlockState(),3);
            }
            else if(this.level().getBlockState(result.getBlockPos()).is(BlockTags.LOGS)){
                Optional<Holder.Reference<Block>> blockRef = BuiltInRegistries.BLOCK.getRandom(this.level().getRandom()).filter(
                        x -> x.is(BlockTags.LOGS)
                );
                if(blockRef.isPresent()){
                    this.level().setBlock(result.getBlockPos(),blockRef.get().value().defaultBlockState(),3);
                    this.level().playSound(null,result.getBlockPos(),SoundEvents.ENDERMAN_TELEPORT,SoundSource.BLOCKS,0.75f,1.0f);
                }
            }
        }
        this.discard();
    }

    @Override
    public void onHitEntity(EntityHitResult result){
        if(result.getEntity() instanceof LivingEntity livingEntity){
            if(this.level() instanceof ServerLevel serverLevel){
                if(livingEntity instanceof Monster){
                    ((Monster) livingEntity).setNoAi(true);
                    livingEntity.hurtServer(serverLevel,this.damageSources().magic(),7);
                }
                else{
                    livingEntity.hurtServer(serverLevel,this.damageSources().magic(),3);
                }
            }
        }
        this.discard();
    }
}
