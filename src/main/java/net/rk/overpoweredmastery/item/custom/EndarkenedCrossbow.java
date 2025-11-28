package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EndarkenedCrossbow extends CrossbowItem {
    public EndarkenedCrossbow(Properties p) {
        super(p.durability(10000).enchantable(32).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return pr -> pr.is(ItemTags.ARROWS) || pr.is(Items.SNOWBALL) || pr.is(Items.FIRE_CHARGE) || pr.is(Items.SHULKER_SHELL);
    }

    @Nullable
    public ShulkerBullet getNearestEntityOfType(Level level, LivingEntity shooter, double x, double y, double z, double distance) {
        boolean successfullyFoundEntity = false;
        Vec3 vec3 = shooter.getUpVector(1.0F);
        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis((double)(shooter.getLookAngle().y * 0.017453292F), vec3.x, vec3.y, vec3.z);
        Vec3 vec31 = shooter.getViewVector(1.0F);
        ShulkerBullet wc = null;
        LivingEntity ent = null;

        if(level instanceof ServerLevel){
            AABB bounds = new AABB(shooter.getX() - distance,shooter.getY() - distance,shooter.getZ() - distance,
                    shooter.getZ() + distance, shooter.getY() + distance, shooter.getZ() + distance);
            ent = ((ServerLevel) level).getNearestEntity(Monster.class,
                    TargetingConditions.forCombat(),shooter,shooter.getX(),shooter.getEyeY(),shooter.getZ(),
                    bounds);
            if(ent != null){
                successfullyFoundEntity = true;
            }
        }
        //
        if(successfullyFoundEntity){
            wc = new ShulkerBullet(level,shooter,ent, Direction.Axis.Y);
            wc.setPos(shooter.getX(),shooter.getEyeY() + 0.10000001,shooter.getZ());
            return wc;
        }
        return null;
    }

    @Override
    protected Projectile createProjectile(Level level, LivingEntity entity, ItemStack weapon, ItemStack ammo, boolean p_40866_) {
        if (ammo.is(Items.FIREWORK_ROCKET)) {
            return new FireworkRocketEntity(level, ammo, entity, entity.getX(), entity.getEyeY() - 0.15F, entity.getZ(), true);
        }
        else if(ammo.is(Items.FIRE_CHARGE)){
            return new SmallFireball(level,entity,entity.getViewVector(1));
        }
        else if(ammo.is(Items.SNOWBALL)){
            return new Snowball(level,entity,ammo);
        }
        else if(ammo.is(Items.SHULKER_SHELL)){
            ShulkerBullet sb = getNearestEntityOfType(level,entity,entity.getX(),entity.getEyeY(),entity.getZ(),20);
            if(sb != null){
                return new ShulkerBullet(level,entity,sb, Direction.Axis.Y);
            }
            else{
                Projectile projectile = super.createProjectile(level, entity, weapon, ammo, p_40866_);
                if (projectile instanceof AbstractArrow abstractarrow) {
                    abstractarrow.setSoundEvent(SoundEvents.SHULKER_BOX_CLOSE);
                }
                return projectile;
            }
        }
        else {
            Projectile projectile = super.createProjectile(level, entity, weapon, ammo, p_40866_);
            if (projectile instanceof AbstractArrow abstractarrow) {
                abstractarrow.setSoundEvent(SoundEvents.CROSSBOW_HIT);
            }

            return projectile;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.endarkened_crossbow.desc")
                .withStyle(ChatFormatting.RED));
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity livingEntity) {
        return 4000;
    }
}
