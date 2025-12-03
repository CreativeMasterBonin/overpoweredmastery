package net.rk.overpoweredmastery.util;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

import java.util.Random;

public class OPUtil {
    public static Holder<Enchantment> getEnchantmentHolderFromKeyStatic(Level lvl, ResourceKey<Enchantment> enchantmentResourceKey){
        return lvl.registryAccess().getOrThrow(enchantmentResourceKey);
    }

    public static float nextFloatBetweenInclusive(float min, float max) {
        Random random = new Random();
        return random.nextFloat(max - min + 1) + min;
    }

    public static double nextDoubleBetweenInclusive(double min, double max) {
        Random random = new Random();
        return random.nextDouble(max - min + 1) + min;
    }
}
