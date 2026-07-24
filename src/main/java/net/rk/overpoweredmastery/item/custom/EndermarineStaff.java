package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.rk.overpoweredmastery.resource.OMSoundEvents;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.function.Consumer;

public class EndermarineStaff extends AbstractStaff{
    public EndermarineStaff(Properties p) {
        super(p.enchantable(10).durability(OPUtil.ENDERMARINE_STAFF_DURABILITY));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.endermarine_staff.desc")
                .withStyle(ChatFormatting.YELLOW));
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.staff.max_teleport_distance",10)
                .withStyle(ChatFormatting.GOLD));
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        if(entity.level() instanceof ServerLevel serverLevel){
            if(entity instanceof ServerPlayer serverPlayer){
                if(!serverPlayer.isSecondaryUseActive()){
                    BlockHitResult result = serverPlayer.level().clip(new ClipContext(
                            serverPlayer.getEyePosition(3f),
                            (serverPlayer.getEyePosition(3f)).add(serverPlayer.getViewVector(3f).scale(10f)),
                            ClipContext.Block.COLLIDER,
                            ClipContext.Fluid.ANY,
                            serverPlayer
                    ));
                    if(!result.isWorldBorderHit() && (serverLevel.isInWorldBounds(result.getBlockPos().above()) && serverLevel.isInWorldBounds(result.getBlockPos()) && serverLevel.isInWorldBounds(result.getBlockPos().below()))){
                        serverLevel.sendParticles(ParticleTypes.POOF,
                                result.getBlockPos().getX(),result.getBlockPos().above().getY(),result.getBlockPos().getZ(),
                                2,0.5D,0.5D,0.5D,0D);
                        serverLevel.sendParticles(ParticleTypes.POOF,
                                result.getBlockPos().getX(),result.getBlockPos().getY(),result.getBlockPos().getZ(),
                                2,0.5D,0.5D,0.5D,0D);
                        serverLevel.sendParticles(ParticleTypes.POOF,
                                result.getBlockPos().below().getX(),result.getBlockPos().below().getY(),result.getBlockPos().below().getZ(),
                                2,0.5D,0.5D,0.5D,0D);
                    }
                    else{
                        serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                                result.getBlockPos().getX(),result.getBlockPos().above().getY(),result.getBlockPos().getZ(),
                                1,0.5D,0.5D,0.5D,0.1D);
                        serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                                result.getBlockPos().getX(),result.getBlockPos().getY(),result.getBlockPos().getZ(),
                                1,0.5D,0.5D,0.5D,0.1D);
                        serverLevel.sendParticles(ParticleTypes.ANGRY_VILLAGER,
                                result.getBlockPos().below().getX(),result.getBlockPos().below().getY(),result.getBlockPos().below().getZ(),
                                1,0.5D,0.5D,0.5D,0.1D);
                    }
                    return false;
                }
                else{
                    BlockHitResult result = serverPlayer.level().clip(new ClipContext(
                            serverPlayer.getEyePosition(3f),
                            (serverPlayer.getEyePosition(3f)).add(serverPlayer.getViewVector(3f).scale(10f)),
                            ClipContext.Block.COLLIDER,
                            ClipContext.Fluid.ANY,
                            serverPlayer
                    ));
                    if(!result.isWorldBorderHit() && (serverLevel.isInWorldBounds(result.getBlockPos().above()) && serverLevel.isInWorldBounds(result.getBlockPos()) && serverLevel.isInWorldBounds(result.getBlockPos().below()))){
                        Vec3 oldPos = serverPlayer.getPosition(1f); // old player pos

                        // make sure the teleport is safe
                        if(!serverLevel.getBlockState(result.getBlockPos()).isAir() && serverLevel.getBlockState(result.getBlockPos().above()).isAir()){
                            // teleport the player using the transition system (just like vanilla items do)
                            serverPlayer.teleport(new TeleportTransition(
                                    serverLevel,
                                    new Vec3(result.getBlockPos().getX() + 0.5D,result.getBlockPos().getY() + 1.1D,result.getBlockPos().getZ() + 0.5D),
                                    serverPlayer.getDeltaMovement(),serverPlayer.getYRot(),serverPlayer.getXRot(),TeleportTransition.DO_NOTHING));
                            // play teleport sound
                            serverLevel.playSound(serverPlayer,serverPlayer.getX(),serverPlayer.getY(),serverPlayer.getZ(),
                                    SoundEvents.PLAYER_TELEPORT, SoundSource.PLAYERS,0.75f,1.0f);

                            serverPlayer.setDeltaMovement(serverPlayer.getDeltaMovement().x,serverPlayer.getDeltaMovement().y + 0.41D,serverPlayer.getDeltaMovement().z);
                            // reset some parameters as we want the player to not take damage for the teleport height falling from
                            serverPlayer.resetFallDistance();
                            serverPlayer.resetCurrentImpulseContext();

                            serverLevel.gameEvent(GameEvent.TELEPORT,result.getBlockPos(),GameEvent.Context.of(serverPlayer));


                            if(serverPlayer.isOnPortalCooldown()){
                                serverPlayer.setPortalCooldown();
                            }
                            serverPlayer.getItemInHand(hand).hurtAndBreak(1,serverPlayer,hand);
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 250;
    }

    @Override
    public void extraOnUse(Level level, LivingEntity livingEntity, ItemStack itemStack, int remainDuration) {
        if(remainDuration < 250){
            if(remainDuration % 15 == 0 && livingEntity instanceof Player player){
                if(player.isSecondaryUseActive()){
                    action(level,livingEntity,itemStack,remainDuration,0);
                    itemStack.hurtAndBreak(4,livingEntity,livingEntity.getUsedItemHand());
                }
                else{
                    action(level,livingEntity,itemStack,remainDuration,1);
                    itemStack.hurtAndBreak(1,livingEntity,livingEntity.getUsedItemHand());
                }
            }
        }
    }

    @Override
    public void action(Level level, LivingEntity entity, ItemStack stack, int remainingDuration, int stage) {
        if(entity.getRandom().nextIntBetweenInclusive(0,100) <= 1){
            entity.playSound(OMSoundEvents.EFFECT.get(),0.65f,entity.getRandom().triangle(0.95f,1.1f));
        }
        if(stage == 0){
            // teleporting is powerful, so add a short cooldown to the staff
            if(entity instanceof Player player){
                if(!player.getCooldowns().isOnCooldown(stack)){
                    player.getCooldowns().addCooldown(stack,37);
                }
            }
        }
        else{
            if(!entity.hasEffect(MobEffects.REGENERATION)){
                entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION,50,10,true,false));
            }
            if(level instanceof ServerLevel serverLevel){
                WitherSkull skull = new WitherSkull(serverLevel,entity,
                        entity.getViewVector((float)remainingDuration + 0.75f));
                skull.setDangerous(serverLevel.getRandom().nextBoolean());
                skull.setPos(skull.getX(),entity.getEyeY() + 1.5f,skull.getZ());
                serverLevel.addFreshEntity(skull);
            }
        }
    }
}
