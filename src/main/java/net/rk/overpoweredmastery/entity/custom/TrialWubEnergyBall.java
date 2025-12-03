package net.rk.overpoweredmastery.entity.custom;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ThrownLingeringPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;
import net.rk.overpoweredmastery.entity.OMEntityTypes;
import net.rk.overpoweredmastery.util.OPUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TrialWubEnergyBall extends AbstractHurtingProjectile {
    int count = 0;

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
        if(count >= 120){
            this.discard();
        }
        if(isMovingX || isMovingY || isMovingZ){
            // particles for wubs
            int lightorangeish = 16761442;
            int orangeish = 16751202;
            this.level().addParticle(new DustParticleOptions(count % 2 == 0 ? lightorangeish : orangeish,1.0f),this.getX(),this.getY() + 0.5,this.getZ(),
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
        if(this.level() instanceof ServerLevel serverLevel){
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
        this.discard();
    }

    @Override
    public void onHitEntity(EntityHitResult result){
        result.getEntity().setDeltaMovement(0,result.getEntity().getDeltaMovement().y * 2,0);
        this.discard();
    }
}
