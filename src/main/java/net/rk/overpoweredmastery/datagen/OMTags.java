package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.rk.overpoweredmastery.OverpoweredMastery;

public class OMTags{
    public static final TagKey<Item> MUSIC_DISC_WUBS = omItemTag("music_disc_wubs");
    public static final TagKey<Item> SPEARS = omItemTag("spears");
    public static final TagKey<Item> CAN_HAVE_INSTAREPAIR_ENCHANTMENT = omItemTag("can_have_instarepair_enchantment");
    public static final TagKey<Block> BANNED_PROBABLE_REWARD_BLOCKS = omBlockTag("banned_probable_reward_blocks");
    public static final TagKey<Block> PROBABLE_CANNOT_REPLACE = omBlockTag("probable_cannot_replace");
    public static final TagKey<Block> CORRECT_FOR_SPEAR = omBlockTag("correct_for_spear");
    public static final TagKey<Block> UNSAFE_FOR_SELECTION = omBlockTag("unsafe_for_selection");
    public static final TagKey<Enchantment> SPEAR_SUPPORTED = omEnchantmentTag("spear_supported");

    private static TagKey<Block> omBlockTag(String name){
        return BlockTags.create(ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID, name));
    }

    private static TagKey<Item> omItemTag(String name){
        return ItemTags.create(ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID, name));
    }

    private static TagKey<Enchantment> omEnchantmentTag(String name){
        return TagKey.create(Registries.ENCHANTMENT,ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,name));
    }
}
