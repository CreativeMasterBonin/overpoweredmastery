package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.datagen.OMEnchantments;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.item.OMRarity;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.function.Consumer;

public class UltimateFishingRod extends AbstractFishingRod{
    public UltimateFishingRod(Properties p) {
        super(p.fireResistant()
                .rarity(OMRarity.ULTIMATE.getValue())
                .repairable(OMItems.ULTIMATE_INGOT.asItem()));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.ultimate_fishing_rod.desc")
                .withStyle(ChatFormatting.GOLD));
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return enchantment.is(Enchantments.MENDING) ||
                enchantment.is(Enchantments.UNBREAKING) ||
                enchantment.is(Enchantments.LURE) ||
                enchantment.is(Enchantments.LUCK_OF_THE_SEA) ||
                enchantment.is(Enchantments.FORTUNE) ||
                enchantment.is(Enchantments.FIRE_ASPECT) ||
                enchantment.is(Enchantments.FLAME)
                || enchantment.is(OMEnchantments.INSTAREPAIR);
    }

    @Override
    public void bobberRetrieve(Level level, Player player){
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.FISHING_BOBBER_RETRIEVE,
                SoundSource.NEUTRAL,
                0.45f,
                OPUtil.nextFloatBetweenInclusive(0.4f,0.95f)
        );
    }

    @Override
    public void bobberThrow(Level level, Player player) {
        level.playSound(
                null,
                player.getX(),
                player.getY(),
                player.getZ(),
                SoundEvents.FISHING_BOBBER_THROW,
                SoundSource.NEUTRAL,
                0.35f,
                OPUtil.nextFloatBetweenInclusive(0.4f,0.95f)
        );
    }

    @Override
    public void hurtItem(ItemStack itemStack, ServerLevel serverLevel, Player player, InteractionHand hand) {
        int i = player.fishing.retrieve(itemStack);
        ItemStack original = itemStack.copy();
        int unbreakingLevel = original.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel,Enchantments.UNBREAKING));
        int instarepairLevel = original.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(serverLevel,OMEnchantments.INSTAREPAIR));

        if(instarepairLevel > 0){
            if(!player.hasEffect(MobEffects.SATURATION)){
                player.addEffect(new MobEffectInstance(MobEffects.SATURATION,20,5,true,false,false));
            }
        }
        else{
            if(unbreakingLevel > 0){
                unbreakingLevel = Mth.clamp(unbreakingLevel,0,9);
                if(serverLevel.getRandom().nextIntBetweenInclusive(0,100) <= unbreakingLevel * 10){
                    itemStack.hurtAndBreak(i, player, LivingEntity.getSlotForHand(hand));
                }
            }
            else{
                itemStack.hurtAndBreak(i, player, LivingEntity.getSlotForHand(hand));
            }
        }

        player.getCooldowns().addCooldown(itemStack,5);

        if(itemStack.isEmpty()) {
            net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(player, original, hand);
        }
    }

    @Override
    public void fishingHookCreate(ItemStack itemstack, Level level, Player player){
        if(level instanceof ServerLevel serverlevel){
            int luckValue = 80;
            int lureSpeed = 20;
            if(itemstack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level,Enchantments.FORTUNE)) > 0){
                luckValue += itemstack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level,Enchantments.FORTUNE));
            }
            if(itemstack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level,Enchantments.LURE)) > 0){
                lureSpeed += itemstack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level,Enchantments.LURE));
            }
            if(!player.hasEffect(MobEffects.LUCK)){
                player.addEffect(new MobEffectInstance(MobEffects.LUCK,80,10,true,false,false));
            }
            FishingHook hook = new FishingHook(player, level, luckValue, lureSpeed);
            hook.timeUntilHooked = 1;
            hook.timeUntilLured = 0;
            hook.biting = true;
            hook.outOfWaterTime = 0;
            if(itemstack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level,Enchantments.FLAME)) > 0){
                hook.setRemainingFireTicks(200);
                hook.setGlowingTag(true);
            }
            if(itemstack.getEnchantmentLevel(OPUtil.getEnchantmentHolderFromKeyStatic(level,Enchantments.FIRE_ASPECT)) > 0){
                hook.setRemainingFireTicks(80);
            }
            Projectile.spawnProjectile(hook, serverlevel, itemstack);
        }
    }
}
