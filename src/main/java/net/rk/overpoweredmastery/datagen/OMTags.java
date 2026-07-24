package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.rk.overpoweredmastery.OverpoweredMastery;

public class OMTags{
    public static final TagKey<Item> MUSIC_DISC_WUBS = omItemTag("music_disc_wubs");
    public static final TagKey<Item> SPEARS = omItemTag("spears");
    public static final TagKey<Item> CAN_HAVE_INSTAREPAIR_ENCHANTMENT = omItemTag("can_have_instarepair_enchantment");
    public static final TagKey<Item> INERT_ESSENCES = omItemTag("inert_essences");
    public static final TagKey<Item> ULTIMATE_TOOLS = omItemTag("ultimate_tools");
    public static final TagKey<Item> ULTRA_TOOLS = omItemTag("ultra_tools");
    public static final TagKey<Item> ULTIMATE_MATERIALS = omItemTag("ultimate_materials");
    public static final TagKey<Item> ULTRA_MATERIALS = omItemTag("ultra_materials");
    public static final TagKey<Item> SUPPORTS_DELICATE_TOUCH_ITEM = omItemTag("supports_delicate_touch");
    public static final TagKey<Item> FRAUD_WANTS = omItemTag("fraud_wants");
    public static final TagKey<Block> BANNED_PROBABLE_REWARD_BLOCKS = omBlockTag("banned_probable_reward_blocks");
    public static final TagKey<Block> PROBABLE_CANNOT_REPLACE = omBlockTag("probable_cannot_replace");
    public static final TagKey<Block> CORRECT_FOR_SPEAR = omBlockTag("correct_for_spear");
    public static final TagKey<Block> INCORRECT_FOR_ULTRA = omBlockTag("incorrect_for_ultra");
    public static final TagKey<Block> INCORRECT_FOR_ULTIMATE = omBlockTag("incorrect_for_ultimate");
    public static final TagKey<Block> ULTIMATE_TIER_CAN_MINE = omBlockTag("ultimate_tier_can_mine");
    public static final TagKey<Block> ULTRA_TIER_CAN_MINE = omBlockTag("ultra_tier_can_mine");
    public static final TagKey<Block> UNSAFE_FOR_SELECTION = omBlockTag("unsafe_for_selection");
    public static final TagKey<Block> CAN_DROP_STRANGE_STONE = omBlockTag("can_drop_strange_stone");
    public static final TagKey<Block> FRAUD_WANTS_TO_GO_TO = omBlockTag("fraud_wants_to_go_to");
    public static final TagKey<Block> SUPPORTS_DELICATE_TOUCH = omBlockTag("supports_delicate_touch");
    public static final TagKey<Block> CAN_EXTRUDE = omBlockTag("can_extrude");
    public static final TagKey<Enchantment> SPEAR_SUPPORTED = omEnchantmentTag("spear_supported");
    public static final TagKey<Enchantment> SPAWNS_EXTRAS_OR_LANDS_MULTIPLE_HITS = omEnchantmentTag("spawns_extras_or_lands_multiple_hits");
    public static final TagKey<Enchantment> ULTRA_SWORD_SUPPORTED = omEnchantmentTag("ultra_sword_supported");
    public static final TagKey<Enchantment> ULTRA_PICKAXE_SUPPORTED = omEnchantmentTag("ultra_pickaxe_supported");
    public static final TagKey<Enchantment> ULTRA_AXE_SUPPORTED = omEnchantmentTag("ultra_axe_supported");
    public static final TagKey<Enchantment> ULTRA_SHOVEL_SUPPORTED = omEnchantmentTag("ultra_shovel_supported");
    public static final TagKey<Enchantment> ULTRA_HOE_SUPPORTED = omEnchantmentTag("ultra_hoe_supported");
    public static final TagKey<EntityType<?>> ULTRA_SEEK_IMMUNE = omEntityTypeTag("ultra_seek_immune");


    private static TagKey<Block> omBlockTag(String name){
        return BlockTags.create(Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID, name));
    }

    private static TagKey<Item> omItemTag(String name){
        return ItemTags.create(Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID, name));
    }

    private static TagKey<Enchantment> omEnchantmentTag(String name){
        return TagKey.create(Registries.ENCHANTMENT,Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,name));
    }

    private static TagKey<EntityType<?>> omEntityTypeTag(String name){
        return TagKey.create(Registries.ENTITY_TYPE,Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,name));
    }
}
