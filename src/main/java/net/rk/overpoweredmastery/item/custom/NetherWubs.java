package net.rk.overpoweredmastery.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.entity.custom.NetherWubEnergyBall;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class NetherWubs extends AbstractWubs{
    public NetherWubs(Properties p) {
        super(p.enchantable(30).rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.nether_wubs.desc")
                .withColor(16732160));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 3002;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        livingEntity.setData(OverpoweredMastery.USING_WUB_ITEM,false);
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
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return stack -> stack.getItem() instanceof Item;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 72;
    }

    @Override
    public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
        if(livingEntity instanceof Player){
            if(!livingEntity.getData(OverpoweredMastery.USING_WUB_ITEM)){
                livingEntity.setData(OverpoweredMastery.USING_WUB_ITEM,true);
            }
            if(level instanceof ServerLevel serverLevel){
                if(remainingUseDuration % 15 == 0 && !((Player) livingEntity).isSecondaryUseActive()){
                    if(remainingUseDuration < 2974){
                        if(!livingEntity.hasEffect(MobEffects.FIRE_RESISTANCE)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE,15,5,true,false,true));
                        }
                    }
                    if(remainingUseDuration < 2852){
                        if(!livingEntity.hasEffect(MobEffects.JUMP_BOOST)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST,15,8,true,false,true));
                        }
                    }
                    if(remainingUseDuration < 2742){
                        if(!livingEntity.hasEffect(MobEffects.ABSORPTION)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION,15,10,true,false,true));
                        }
                    }
                    if(remainingUseDuration < 2646){
                        if(!livingEntity.hasEffect(MobEffects.INVISIBILITY)){
                            livingEntity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY,45,1,true,false,false));
                        }
                    }
                    if(remainingUseDuration < 2522){
                        NetherWubEnergyBall fireball = new NetherWubEnergyBall(livingEntity,livingEntity.getViewVector(remainingUseDuration),level);
                        fireball.setPos(fireball.getX(),livingEntity.getEyeY() - 0.5D,fireball.getZ());
                        serverLevel.addFreshEntity(fireball);
                    }
                }
            }
        }
    }

    @Override
    protected void shootProjectile(LivingEntity shooter, Projectile projectile, int index, float velocity, float inaccuracy, float angle, @Nullable LivingEntity target) {

    }
}
