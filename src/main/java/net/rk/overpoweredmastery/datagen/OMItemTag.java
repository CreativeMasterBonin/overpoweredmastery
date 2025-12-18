package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.item.OMItems;

import java.util.concurrent.CompletableFuture;

public class OMItemTag extends ItemTagsProvider {
    public OMItemTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider,OverpoweredMastery.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(OMTags.ULTIMATE_TOOLS)
                .add(OMItems.ULTIMATE_SWORD.asItem())
                .add(OMItems.ULTIMATE_HOE.asItem())
                .add(OMItems.ULTIMATE_BOW.asItem())
                .add(OMItems.ULTIMATE_FISHING_ROD.asItem())
                .add(OMItems.ULTIMATE_STAFF.asItem())
        ;
        tag(OMTags.ULTRA_TOOLS)
                .add(OMItems.ULTRA_SWORD.asItem())
        ;
        tag(OMTags.MUSIC_DISC_WUBS)
                .add(OMItems.RED_WUBS.asItem())
                .add(OMItems.GREEN_WUBS.asItem())
                .add(OMItems.PURPLE_WUBS.asItem())
                .add(OMItems.CHICKEN_WUBS.asItem())
                .add(OMItems.NETHER_WUBS.asItem())
                .add(OMItems.TRIAL_WUBS.asItem())
                .add(OMItems.OXIDIZED_TRIAl_WUBS.asItem())
        ;
        tag(OMTags.SPEARS)
                .add(OMItems.TEST_SPEAR.asItem())
                .add(OMItems.WOODEN_SPEAR.asItem())
                .add(OMItems.STONE_SPEAR.asItem())
                .add(OMItems.GOLD_SPEAR.asItem())
                .add(OMItems.IRON_SPEAR.asItem())
                .add(OMItems.DIAMOND_SPEAR.asItem())
                .add(OMItems.NETHERITE_SPEAR.asItem())
        ;
        tag(ItemTags.CROSSBOW_ENCHANTABLE)
                .add(OMItems.ENDARKENED_CROSSBOW.asItem())
        ;
        tag(ItemTags.BOW_ENCHANTABLE)
                .add(OMItems.ENDARKENED_CROSSBOW.asItem())
        ;
        tag(Tags.Items.TOOLS_BOW)
                .add(OMItems.ULTIMATE_BOW.asItem())
        ;
        tag(ItemTags.SWORDS)
                .add(OMItems.BONE_SWORD.asItem())
                .add(OMItems.PENULTIMATE_SWORD_DARK.asItem())
                .add(OMItems.PENULTIMATE_SWORD_LIGHT.asItem())
                .addTag(OMTags.SPEARS)
                .add(OMItems.ULTRA_SWORD.asItem())
                .add(OMItems.ULTIMATE_SWORD.asItem())
        ;
        tag(ItemTags.FISHING_ENCHANTABLE)
                .add(OMItems.ULTIMATE_FISHING_ROD.asItem())
        ;
        tag(Tags.Items.MELEE_WEAPON_TOOLS)
                .addTag(OMTags.SPEARS)
        ;
        tag(ItemTags.TRIDENT_ENCHANTABLE)
                .addTag(OMTags.SPEARS)
        ;
        tag(ItemTags.DURABILITY_ENCHANTABLE)
                .addTag(OMTags.SPEARS)
        ;
        tag(OMTags.CAN_HAVE_INSTAREPAIR_ENCHANTMENT)
                .addTag(OMTags.MUSIC_DISC_WUBS)
                .addTag(OMTags.SPEARS)
                .addTag(Tags.Items.TOOLS)
                .addTag(ItemTags.HEAD_ARMOR)
                .addTag(ItemTags.CHEST_ARMOR)
                .addTag(ItemTags.LEG_ARMOR)
                .addTag(ItemTags.FOOT_ARMOR)
                .addTag(OMTags.ULTIMATE_TOOLS)
                .addTag(OMTags.ULTRA_TOOLS)
        ;
        tag(Tags.Items.ORES)
                .add(OMItems.INERT_BLUE_ESSENCE_ORE.asItem())
                .add(OMItems.INERT_GREEN_ESSENCE_ORE.asItem())
                .add(OMItems.INERT_YELLOW_ESSENCE_ORE.asItem())
                .add(OMItems.INERT_ORANGE_ESSENCE_ORE.asItem())
                .add(OMItems.INERT_RED_ESSENCE_ORE.asItem())
                .add(OMItems.INERT_LIGHT_ESSENCE_ORE.asItem())
                .add(OMItems.INERT_AURORAN_ESSENCE_ORE.asItem())
                .add(OMItems.INERT_DARK_ESSENCE_ORE.asItem())
                .add(OMItems.DEEPSLATE_INERT_BLUE_ESSENCE_ORE.get())
                .add(OMItems.DEEPSLATE_INERT_GREEN_ESSENCE_ORE.get())
                .add(OMItems.DEEPSLATE_INERT_YELLOW_ESSENCE_ORE.get())
                .add(OMItems.DEEPSLATE_INERT_ORANGE_ESSENCE_ORE.get())
                .add(OMItems.DEEPSLATE_INERT_RED_ESSENCE_ORE.get())
                .add(OMItems.DEEPSLATE_INERT_LIGHT_ESSENCE_ORE.get())
                .add(OMItems.DEEPSLATE_INERT_AURORAN_ESSENCE_ORE.get())
                .add(OMItems.DEEPSLATE_INERT_DARK_ESSENCE_ORE.get())
        ;
        tag(Tags.Items.INGOTS)
                .add(OMItems.ULTIMATE_INGOT.asItem())
                .add(OMItems.ULTRA_INGOT.asItem())
        ;
        tag(OMTags.INERT_ESSENCES)
                .add(OMItems.INERT_BLUE_ESSENCE.asItem())
                .add(OMItems.INERT_GREEN_ESSENCE.asItem())
                .add(OMItems.INERT_YELLOW_ESSENCE.asItem())
                .add(OMItems.INERT_ORANGE_ESSENCE.asItem())
                .add(OMItems.INERT_RED_ESSENCE.asItem())
                .add(OMItems.INERT_LIGHT_ESSENCE.asItem())
                .add(OMItems.INERT_AURORAN_ESSENCE.asItem())
                .add(OMItems.INERT_DARK_ESSENCE.asItem())
        ;
    }
}
