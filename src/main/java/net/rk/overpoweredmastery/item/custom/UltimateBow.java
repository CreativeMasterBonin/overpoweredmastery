package net.rk.overpoweredmastery.item.custom;

import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.datagen.OMEnchantments;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.item.OMRarity;
import net.rk.overpoweredmastery.util.OPUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class UltimateBow extends BowItem {
    public UltimateBow(Properties p) {
        super(p.fireResistant().enchantable(5).durability(OPUtil.ULTIMATE_SHARED_DURABILITY)
                .rarity(OMRarity.ULTIMATE.getValue())
                .repairable(OMItems.ULTIMATE_INGOT.asItem()));
    }

    @Override
    public int getDefaultProjectileRange() {
        return 120;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(EnchantmentTags.BOW_EXCLUSIVE)
                || enchantment.is(EnchantmentTags.CROSSBOW_EXCLUSIVE)
                || enchantment.is(OMEnchantments.INSTAREPAIR)
                || enchantment.is(EnchantmentTags.DAMAGE_EXCLUSIVE)
                || enchantment.is(Enchantments.MENDING);
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return pr -> pr.getItem() instanceof Item;
    }

    @Override
    public boolean releaseUsing(ItemStack itemStack, Level level, LivingEntity livingEntity, int useDuration) {
        if (!(livingEntity instanceof Player player)) {
            return false;
        }
        else {
            ItemStack itemstack1 = new ItemStack(Items.TIPPED_ARROW,1);
            List<Holder<Potion>> listpotion = BuiltInRegistries.POTION
                    .listElements()
                    .filter(holderrefpotion -> !holderrefpotion.value().getEffects().isEmpty() && level.potionBrewing().isBrewablePotion(holderrefpotion))
                    .collect(Collectors.toList());
            Holder<Potion> holder = Util.getRandom(listpotion,level.getRandom());
            DataComponentPatch componentPatch = DataComponentPatch.builder()
                    .set(DataComponents.POTION_CONTENTS,new PotionContents(holder))
                    .set(DataComponents.POTION_DURATION_SCALE, 0.05F)
                    .build();
            itemstack1.applyComponents(componentPatch);

            int i = 5; //this.getUseDuration(itemStack, livingEntity) - useDuration;
            i = net.neoforged.neoforge.event.EventHooks.onArrowLoose(itemStack, level, player, i, !itemstack1.isEmpty());
            if (i < 0) return false;

            List<ItemStack> list = draw(OMItems.ULTIMATE_BOW.toStack(), itemstack1, player);
            if (level instanceof ServerLevel serverlevel && !list.isEmpty()) {
                this.shoot(serverlevel, player, player.getUsedItemHand(), itemStack, list, 100f, 0.15f, true, null);
            }

            level.playSound(
                    null,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.ARROW_SHOOT,
                    SoundSource.PLAYERS,
                    0.95f,
                    0.97f / (livingEntity.getRandom().nextFloat() * 0.4f)
            );
            player.awardStat(Stats.ITEM_USED.get(this));
            return true;
        }
    }

    @Override
    protected void shootProjectile(LivingEntity livingEntity, Projectile projectile, int i1, float f1, float f2, float f3, @Nullable LivingEntity target) {
        if(projectile instanceof AbstractArrow abstractArrow){
            projectile.setRemainingFireTicks(20);
            abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        super.shootProjectile(livingEntity, projectile, i1, f1, f2, f3, target);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand){
        ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW,1);
        List<Holder<Potion>> list = BuiltInRegistries.POTION
                .listElements()
                .filter(holderrefpotion -> !holderrefpotion.value().getEffects().isEmpty() && level.potionBrewing().isBrewablePotion(holderrefpotion))
                .collect(Collectors.toList());
        Holder<Potion> holder = Util.getRandom(list,level.getRandom());
        DataComponentPatch componentPatch = DataComponentPatch.builder()
                .set(DataComponents.POTION_CONTENTS,new PotionContents(holder))
                .set(DataComponents.POTION_DURATION_SCALE, 0.05F)
                .set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE)
                .build();
        itemstack.applyComponents(componentPatch);
        InteractionResult result = net.neoforged.neoforge.event.EventHooks.onArrowNock(itemstack, level, player, hand, true);
        if(result != null){
            return result;
        }
        player.startUsingItem(hand);
        return InteractionResult.CONSUME;
    }
}
