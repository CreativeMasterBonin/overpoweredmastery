package net.rk.overpoweredmastery.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

public class PurpleWubEnergyBall extends AbstractHurtingProjectile{
    int count = 0;

    public PurpleWubEnergyBall(double x, double y, double z, Vec3 vector, Level level) {
        super(OMEntityTypes.PURPLE_WUB_ENERGY_BALL.get(), x, y, z, vector, level);
    }

    public PurpleWubEnergyBall(LivingEntity livingEntity, Vec3 vector, Level level) {
        super(OMEntityTypes.PURPLE_WUB_ENERGY_BALL.get(), livingEntity, vector, level);
    }

    public PurpleWubEnergyBall(EntityType<PurpleWubEnergyBall> redWubEnergyBallEntityType, Level level) {
        super(redWubEnergyBallEntityType,level);
    }

    @Nullable
    @Override
    public Entity getOwner(){
        if(this.owner != null && this.level() instanceof ServerLevel serverLevel){
            return this.owner.getEntity(uuid1 -> findOwner(serverLevel,uuid1), Entity.class);
        }
        return super.getOwner();
    }

    public Entity findOwner(ServerLevel level, UUID uuid){
        Entity entity = level.getEntity(uuid);
        if(entity != null) {
            return entity;
        }
        else{
            for(ServerLevel serverlevel : level.getServer().getAllLevels()) {
                if(serverlevel != level) {
                    entity = serverlevel.getEntity(uuid);
                    if(entity != null) {
                        return entity;
                    }
                }
            }
            return null;
        }
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
        if(count >= 100){
            this.discard();
        }
        if(isMovingX || isMovingY || isMovingZ){
            // particles for a wub gun
            int greenish = 11141375;
            int bluegreenish = 9437439;
            this.level().addParticle(new DustParticleOptions(count % 2 == 0 ? greenish : bluegreenish,2.0f),this.getX(),this.getY() + 0.5,this.getZ(),
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
            BlockPos hitPos = result.getBlockPos();
            if(!this.getOwner().mayInteract(serverLevel,hitPos)){
                this.discard();
                return;
            }

            if(BlockPos.breadthFirstTraversal(
                    hitPos,
                    8,
                    32,
                    (pos1, consumer) -> {
                        for (Direction direction : Direction.values()) {
                            consumer.accept(pos1.relative(direction));
                        }
                    },
                    pos -> {
                        if(pos.equals(hitPos)){
                            //serverLevel.getGameRules().getRule(GameRules.RULE_MOBGRIEFING).get()

                            // only do this one if mobs can grief

                            if(serverLevel.getBlockState(pos).is(Tags.Blocks.STONES)){
                                if(serverLevel.getRandom().nextIntBetweenInclusive(1,10) >= 7){
                                    serverLevel.setBlock(pos,Blocks.AIR.defaultBlockState(),3);
                                }
                            }
                            else if(serverLevel.getBlockState(pos).is(Tags.Blocks.SANDS) || serverLevel.getBlockState(pos).is(Tags.Blocks.SANDSTONE_BLOCKS)){
                                serverLevel.setBlock(pos,Blocks.AIR.defaultBlockState(),3);
                            }
                            else if(serverLevel.getBlockState(pos).is(Tags.Blocks.ORES)){
                                serverLevel.destroyBlock(pos,true,getOwner());
                                serverLevel.setBlock(pos,Blocks.COAL_BLOCK.defaultBlockState(),3);
                            }
                            else if (serverLevel.getBlockState(pos).is(Blocks.COAL_BLOCK)) {
                                serverLevel.destroyBlock(pos,true,getOwner());
                                serverLevel.setBlock(pos,Blocks.COAL_BLOCK.defaultBlockState(),3);
                            }
                            else if(serverLevel.getBlockState(pos).is(Blocks.OBSIDIAN)){
                                serverLevel.explode(null,pos.getX(),pos.getY(),pos.getZ(),16, Level.ExplosionInteraction.BLOCK);
                            }
                            return BlockPos.TraversalNodeStatus.ACCEPT;
                        }
                        else{
                            return BlockPos.TraversalNodeStatus.SKIP;
                        }
                    }
            ) > 1){

            }
        }
        this.discard();
    }

    @Override
    public void onHitEntity(EntityHitResult result){
        if(getOwner() == null){
            this.discard();
        }
        //
        if(result.getEntity() != getOwner()){
            if(result.getEntity() instanceof LivingEntity livingEntity){
                if(this.level() instanceof ServerLevel serverLevel){
                    if(result.getEntity() instanceof Player){
                        if(result.getEntity().getTeam() == this.getTeam()){
                            this.discard();
                        }
                    }
                    if(livingEntity.canFreeze()){
                        livingEntity.setTicksFrozen(30);
                    }
                    livingEntity.hurtServer(serverLevel,this.damageSources().magic(),livingEntity.getHealth() - 2);
                }
            }
        }
        this.discard();
    }
}
