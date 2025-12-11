package net.rk.overpoweredmastery.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.Tags;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.datagen.OMEnchantments;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class AbstractStaff extends ProjectileWeaponItem {
    public static final ResourceLocation STAFF_MOVEMENT_SPEED_MODIFIER =
            ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"staff_movement_speed");
    public static final ResourceLocation STAFF_BLOCK_REACH_MODIFIER =
            ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"staff_block_reach");
    public static final ResourceLocation STAFF_ENTITY_REACH_MODIFIER =
            ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"staff_entity_reach");
    public static final ResourceLocation STAFF_SAFE_FALL_DISTANCE =
            ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"staff_safe_fall_distance");
    public static final ResourceLocation STAFF_FALL_DAMAGE_MULTIPLIER =
            ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"staff_fall_damage_multiplier");

    public AbstractStaff(Properties p) {
        super(p.stacksTo(1));
    }

    @Override
    public boolean makesPiglinsNeutral(ItemStack stack, LivingEntity wearer) {
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.staff.desc"));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        return stack;
    }

    @Override
    public boolean releaseUsing(ItemStack stack, Level level, LivingEntity livingEntity, int timeLeft) {
        return true;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if(level instanceof ServerLevel serverLevel){
            return ItemUtils.startUsingInstantly(level,player,hand);
        }
        else {
            return InteractionResult.SUCCESS;
        }
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(OMEnchantments.INSTAREPAIR) || enchantment.is(Enchantments.UNBREAKING) || enchantment.is(Enchantments.MENDING) || enchantment.is(Tags.Enchantments.INCREASE_ENTITY_DROPS);
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(livingEntity.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED)){
            if(livingEntity.getAttributes().getValue(Attributes.MOVEMENT_SPEED) < 0.7){
                if(livingEntity.isUsingItem() && !livingEntity.hasEffect(MobEffects.SPEED)){
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.SPEED,10,2,true,false,false));
                }
            }
        }
        extraOnUse(level,livingEntity,stack,remainingUseDuration);
    }

    public abstract void extraOnUse(Level level, LivingEntity livingEntity, ItemStack itemStack, int remainDuration);

    /**
     * Performs a complex action for this staff
     * @param level The current level (should be server level)
     * @param entity The entity doing the action
     * @param stack The item stack being used
     * @param remainingDuration The ticks left till reset
     * @param stage The stage of action, which determines what happens and if a projectile is involved, which projectile is fired
     */
    public abstract void action(Level level, LivingEntity entity, ItemStack stack, int remainingDuration, int stage);

    /**
     * The universal color method that display friendly particles above the user of the staff
     * @param level The level that the particles will appear in
     * @param livingEntity The entity that the particles will appear near
     */
    public void staffDefaultParticles(Level level, LivingEntity livingEntity){
        int allTheColors = ARGB.color(Mth.randomBetweenInclusive(
                        level.getRandom(), 0, 255),
                Mth.randomBetweenInclusive(level.getRandom(), 0, 255),
                Mth.randomBetweenInclusive(level.getRandom(), 0, 255));

        // make the math better and lined up with item in hand
        if (livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND) {
            double x = livingEntity.getDeltaMovement().x;
            double z = livingEntity.getDeltaMovement().z;

            if(x < 0 && z < 0){
                level.addParticle(new DustParticleOptions(allTheColors, 1.0f),
                        livingEntity.getX() + 0.3,
                        livingEntity.getY() + 2.5,
                        livingEntity.getZ() + 0.3,
                        0, 1, 0);
            } else if (x > 0 && z < 0) {
                level.addParticle(new DustParticleOptions(allTheColors, 1.0f),
                        livingEntity.getX() + 0.3,
                        livingEntity.getY() + 2.5,
                        livingEntity.getZ() + 0.3,
                        0, 1, 0);
            } else if (x > 0 && z > 0) {
                level.addParticle(new DustParticleOptions(allTheColors, 1.0f),
                        livingEntity.getX() + 0.3,
                        livingEntity.getY() + 2.5,
                        livingEntity.getZ() - 0.3,
                        0, 1, 0);
            } else if (x < 0 && z > 0) {
                level.addParticle(new DustParticleOptions(allTheColors, 1.0f),
                        livingEntity.getX() + 0.3,
                        livingEntity.getY() + 2.5,
                        livingEntity.getZ() + 0.3,
                        0, 1, 0);
            } else {
                level.addParticle(new DustParticleOptions(allTheColors, 1.0f),
                        livingEntity.getX(),
                        livingEntity.getY() + 2.5,
                        livingEntity.getZ(),
                        0, 1, 0);
            }
        } else if (livingEntity.getUsedItemHand() == InteractionHand.OFF_HAND) {
            level.addParticle(new DustParticleOptions(allTheColors, 1.0f),
                    livingEntity.getX() + livingEntity.yHeadRot,
                    livingEntity.getY() + 2.5,
                    livingEntity.getZ() - livingEntity.yHeadRot,
                    0, 1, 0);
        }
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() instanceof Item;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 100;
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {
        shooter.playSound(SoundEvents.ILLUSIONER_CAST_SPELL,0.5f,shooter.getRandom().triangle(0.87f,1.0f));
    }

    // https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Tutorial-1.21.X/blob/main/src/main/java/net/kaupenjoe/tutorialmod/item/custom/HammerItem.java
    // MIT (2024 Kaupenjoe) - edited version (reduced code amount, changed names, added extra functionality and customization)
    public List<BlockPos> getClippingBlocks(int blockRangeX, int blockRangeY, int blockRangeZ, ClipContext.Fluid allowedFluidClipType, ClipContext.Block allowedBlockClipType, BlockPos start, ServerPlayer serverPlayer){
        List<BlockPos> clippingBlocks = new ArrayList<>();

        BlockHitResult result = serverPlayer.level().clip(new ClipContext(
                serverPlayer.getEyePosition(1f),
                (serverPlayer.getEyePosition(1f).add(serverPlayer.getViewVector(1f).scale(6f))),
                allowedBlockClipType,
                allowedFluidClipType,
                serverPlayer
        ));

        // more functionality for staff strength
        Direction resultDir = result.getDirection();
        double resultDist = result.getBlockPos().distToCenterSqr(start.getX(),start.getY(),start.getZ()); // for staff power in future

        if(result.getType() == HitResult.Type.MISS){
            return clippingBlocks;
        }

        // first check x, then y, then z
        for(int x = -blockRangeX; x <= blockRangeX; x++){
            for(int y = -blockRangeY; y <= blockRangeY; y++){
                for(int z = -blockRangeZ; z <= blockRangeZ; z++){
                    if(resultDir == Direction.DOWN || resultDir == Direction.UP){
                        int posX = start.getX() + x;
                        int posY = start.getY() + z;
                        int posZ = start.getZ() + y;
                        clippingBlocks.add(new BlockPos(posX,posY,posZ));
                    }
                    if(resultDir == Direction.NORTH || resultDir == Direction.SOUTH){
                        int posX = start.getX() + x;
                        int posY = start.getY() + y;
                        int posZ = start.getZ() + z;
                        clippingBlocks.add(new BlockPos(posX,posY,posZ));
                    }
                    if(resultDir == Direction.EAST || resultDir == Direction.WEST){
                        int posX = start.getX() + z;
                        int posY = start.getY() + y;
                        int posZ = start.getZ() + x;
                        clippingBlocks.add(new BlockPos(posX,posY,posZ));
                    }
                }
            }
        }

        return clippingBlocks;
    }
}
