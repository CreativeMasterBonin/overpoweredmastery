package net.rk.overpoweredmastery.entity.custom;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ConversionParams;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import org.jetbrains.annotations.Nullable;

public class NetherWubEnergyBall extends AbstractHurtingProjectile {
    int count = 0;

    public NetherWubEnergyBall(double x, double y, double z, Vec3 vector, Level level) {
        super(OMEntityTypes.NETHER_WUB_ENERGY_BALL.get(), x, y, z, vector, level);
    }

    public NetherWubEnergyBall(LivingEntity livingEntity, Vec3 vector, Level level) {
        super(OMEntityTypes.NETHER_WUB_ENERGY_BALL.get(), livingEntity, vector, level);
    }

    public NetherWubEnergyBall(EntityType<NetherWubEnergyBall> energyBallEntityType, Level level) {
        super(energyBallEntityType,level);
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
        if(count >= 125){
            this.discard();
        }
        if(isMovingX || isMovingY || isMovingZ){
            // red particles for a red wub gun
            int yellowish = 16758528;
            int orangeish = 16732160;
            this.level().addParticle(new DustParticleOptions(count % 2 == 0 ? yellowish : orangeish,1.0f),this.getX(),this.getY() + 0.5,this.getZ(),
                    0,0,0);
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
        BlockState blockState = this.level().getBlockState(result.getBlockPos());
        if(blockState.is(BlockTags.MINEABLE_WITH_PICKAXE) && (blockState.is(BlockTags.NEEDS_IRON_TOOL) || (blockState.is(BlockTags.NEEDS_STONE_TOOL)))){
            this.level().setBlock(result.getBlockPos(), Blocks.LAVA.defaultBlockState().setValue(LiquidBlock.LEVEL,5),3);
            this.level().playSound(null,result.getBlockPos(),
                    SoundEvents.FIREWORK_ROCKET_BLAST,
                    SoundSource.PLAYERS,0.5f,0.75f);
        } else if (blockState.is(BlockTags.BASE_STONE_NETHER)) {
            this.level().setBlock(result.getBlockPos(),Blocks.LAVA.defaultBlockState().setValue(LiquidBlock.LEVEL,4),3);
            this.level().playSound(null,result.getBlockPos(),
                    SoundEvents.LAVA_POP,
                    SoundSource.PLAYERS,0.45f,0.48f);
        } else if(blockState.is(BlockTags.FLOWERS) || blockState.is(Blocks.SHORT_GRASS) || blockState.is(Blocks.TALL_GRASS)){
            this.level().setBlock(result.getBlockPos(),Blocks.SHORT_DRY_GRASS.defaultBlockState(),3);
            this.level().playSound(null,result.getBlockPos(),
                    SoundEvents.FIREWORK_ROCKET_TWINKLE,
                    SoundSource.PLAYERS,0.5f,0.75f);
        }
        else if (blockState.is(BlockTags.SAPLINGS)){
            this.level().setBlock(result.getBlockPos(),Blocks.DEAD_BUSH.defaultBlockState(),3);
            this.level().playSound(null,result.getBlockPos(),
                    SoundEvents.FIREWORK_ROCKET_TWINKLE,
                    SoundSource.PLAYERS,0.45f,0.74f);
        }
        else if(blockState.is(BlockTags.DIRT)){
            this.level().setBlock(result.getBlockPos(),Blocks.GRAVEL.defaultBlockState(),3);
            this.level().playSound(null,result.getBlockPos(),
                    SoundEvents.GRAVEL_HIT,
                    SoundSource.PLAYERS,0.4f,0.95f);
        }
        else if(blockState.is(Blocks.GRAVEL)){
            this.level().setBlock(result.getBlockPos(),Blocks.SAND.defaultBlockState(),3);
            this.level().playSound(null,result.getBlockPos(),
                    SoundEvents.SAND_HIT,
                    SoundSource.PLAYERS,0.4f,0.85f);
        }
        this.discard();
    }

    @Override
    public void onHitEntity(EntityHitResult result) {
        if(result.getEntity().level() instanceof ServerLevel serverLevel){
            if(result.getEntity().fireImmune()){
                result.getEntity().hurtServer(serverLevel,result.getEntity().damageSources().outOfBorder(),2.5f);
                result.getEntity().setAirSupply(0);
            }
            else if(result.getEntity() instanceof AbstractPiglin piglin){
                this.level().playSound(null,result.getEntity().getOnPos(),SoundEvents.BEACON_POWER_SELECT,SoundSource.NEUTRAL,0.5f,0.95f);
                piglin.setImmuneToZombification(false);
                piglin.setTimeInOverworld(301);
                piglin.setRemainingFireTicks(80);
                piglin.drop(piglin.getMainHandItem(),true,false);
                piglin.drop(piglin.getOffhandItem(),true,false);
                piglin.drop(piglin.getBodyArmorItem(),true,false);
                piglin.convertTo(EntityType.ZOMBIFIED_PIGLIN,
                        ConversionParams.single(piglin,false,false),entity -> {
                            entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 20, 0));
                            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 120, 0));
                            EventHooks.onLivingConvert(piglin, entity);
                        });
            } else if (result.getEntity() instanceof Hoglin hoglin) {
                this.level().playSound(null,result.getEntity().getOnPos(),SoundEvents.BEACON_POWER_SELECT,SoundSource.NEUTRAL,0.5f,0.87f);
                hoglin.setImmuneToZombification(false);
                hoglin.setTimeInOverworld(301);
                hoglin.setRemainingFireTicks(80);
                hoglin.convertTo(EntityType.ZOGLIN,
                        ConversionParams.single(hoglin,false,false),entity -> {
                            entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 40, 0));
                            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 0));
                            EventHooks.onLivingConvert(hoglin, entity);
                        });
            }
        }
        this.discard();
    }
}
