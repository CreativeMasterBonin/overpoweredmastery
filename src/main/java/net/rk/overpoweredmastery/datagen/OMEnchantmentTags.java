package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EnchantmentTagsProvider;
import net.minecraft.world.item.enchantment.Enchantments;
import net.rk.overpoweredmastery.OverpoweredMastery;

import java.util.concurrent.CompletableFuture;

public class OMEnchantmentTags extends EnchantmentTagsProvider {
    public OMEnchantmentTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, OverpoweredMastery.MODID);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        tag(OMTags.SPEAR_SUPPORTED)
                .add(OMEnchantments.INSTAREPAIR)
                .add(Enchantments.RIPTIDE)
                .add(Enchantments.EFFICIENCY)
                .add(Enchantments.SHARPNESS)
                .add(Enchantments.LOOTING)
                .add(Enchantments.IMPALING)
                .add(Enchantments.MENDING)
                .add(Enchantments.UNBREAKING)
                .add(Enchantments.SMITE)
                .add(Enchantments.PIERCING)
                .add(Enchantments.FIRE_ASPECT)
                .add(Enchantments.WIND_BURST)
        ;
    }
}
