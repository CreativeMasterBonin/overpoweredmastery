package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.rk.overpoweredmastery.datamap.OMDatamaps;
import net.rk.overpoweredmastery.datamap.Smeltable;

import java.util.concurrent.CompletableFuture;

public class OMSmeltables extends DataMapProvider {
    public OMSmeltables(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    public void gather(HolderLookup.Provider provider) {

        final var smeltables = builder(OMDatamaps.SMELTABLES);
        smeltables.add(Blocks.OBSIDIAN.builtInRegistryHolder(),new Smeltable(Blocks.LAVA),false);

        smeltables.add(Blocks.IRON_ORE.builtInRegistryHolder(),new Smeltable(Blocks.RAW_IRON_BLOCK),false);
        smeltables.add(Blocks.GOLD_ORE.builtInRegistryHolder(),new Smeltable(Blocks.RAW_GOLD_BLOCK),false);
        smeltables.add(Blocks.COPPER_ORE.builtInRegistryHolder(),new Smeltable(Blocks.RAW_COPPER_BLOCK),false);

        smeltables.add(Blocks.RAW_IRON_BLOCK.builtInRegistryHolder(),new Smeltable(Blocks.IRON_BLOCK),false);
        smeltables.add(Blocks.RAW_GOLD_BLOCK.builtInRegistryHolder(),new Smeltable(Blocks.GOLD_BLOCK),false);
        smeltables.add(Blocks.RAW_COPPER_BLOCK.builtInRegistryHolder(),new Smeltable(Blocks.COPPER_BLOCK),false);

        smeltables.add(Blocks.COBBLESTONE.builtInRegistryHolder(),new Smeltable(Blocks.STONE),false);
        smeltables.add(Blocks.STONE.builtInRegistryHolder(),new Smeltable(Blocks.SMOOTH_STONE),false);

        smeltables.add(Blocks.COBBLED_DEEPSLATE.builtInRegistryHolder(),new Smeltable(Blocks.DEEPSLATE),false);
        smeltables.add(Blocks.DEEPSLATE.builtInRegistryHolder(),new Smeltable(Blocks.POLISHED_DEEPSLATE),false);

        smeltables.add(Blocks.BLACKSTONE.builtInRegistryHolder(),new Smeltable(Blocks.POLISHED_BLACKSTONE),false);

        smeltables.add(Blocks.DEEPSLATE_IRON_ORE.builtInRegistryHolder(),new Smeltable(Blocks.RAW_IRON_BLOCK),false);
        smeltables.add(Blocks.DEEPSLATE_GOLD_ORE.builtInRegistryHolder(),new Smeltable(Blocks.RAW_GOLD_BLOCK),false);
        smeltables.add(Blocks.DEEPSLATE_COPPER_ORE.builtInRegistryHolder(),new Smeltable(Blocks.RAW_COPPER_BLOCK),false);

        smeltables.add(Blocks.ICE.builtInRegistryHolder(),new Smeltable(Blocks.WATER),false);
        smeltables.add(Blocks.PACKED_ICE.builtInRegistryHolder(),new Smeltable(Blocks.WATER),false);
        smeltables.add(Blocks.FROSTED_ICE.builtInRegistryHolder(),new Smeltable(Blocks.WATER),false);
        smeltables.add(Blocks.BLUE_ICE.builtInRegistryHolder(),new Smeltable(Blocks.WATER),false);

        smeltables.add(Blocks.SOUL_SAND.builtInRegistryHolder(),new Smeltable(Blocks.SOUL_SOIL),false);
        smeltables.add(Blocks.SHORT_GRASS.builtInRegistryHolder(),new Smeltable(Blocks.SHORT_DRY_GRASS),false);
        smeltables.add(Blocks.GRASS_BLOCK.builtInRegistryHolder(),new Smeltable(Blocks.GRAVEL),false);
        smeltables.add(Blocks.DIRT.builtInRegistryHolder(),new Smeltable(Blocks.GRAVEL),false);
        smeltables.add(Blocks.GRAVEL.builtInRegistryHolder(),new Smeltable(Blocks.SAND),false);
        smeltables.add(Blocks.SAND.builtInRegistryHolder(),new Smeltable(Blocks.GLASS),false);
        smeltables.add(Blocks.SNOW_BLOCK.builtInRegistryHolder(),new Smeltable(Blocks.SNOW),false);
        smeltables.add(Blocks.POWDER_SNOW.builtInRegistryHolder(),new Smeltable(Blocks.SNOW),false);
        smeltables.add(Blocks.ATTACHED_MELON_STEM.builtInRegistryHolder(),new Smeltable(Blocks.DEAD_BUSH),false);
        smeltables.add(Blocks.ATTACHED_PUMPKIN_STEM.builtInRegistryHolder(),new Smeltable(Blocks.DEAD_BUSH),false);
        smeltables.add(Blocks.MELON_STEM.builtInRegistryHolder(),new Smeltable(Blocks.DEAD_BUSH),false);
        smeltables.add(Blocks.PUMPKIN_STEM.builtInRegistryHolder(),new Smeltable(Blocks.DEAD_BUSH),false);
        smeltables.add(Blocks.WHEAT.builtInRegistryHolder(),new Smeltable(Blocks.SHORT_DRY_GRASS),false);
        smeltables.add(Blocks.CARROTS.builtInRegistryHolder(),new Smeltable(Blocks.SHORT_DRY_GRASS),false);
        smeltables.add(Blocks.POTATOES.builtInRegistryHolder(),new Smeltable(Blocks.SHORT_DRY_GRASS),false);
        smeltables.add(Blocks.BEETROOTS.builtInRegistryHolder(),new Smeltable(Blocks.SHORT_DRY_GRASS),false);
        smeltables.add(Blocks.BEEHIVE.builtInRegistryHolder(),new Smeltable(Blocks.SUSPICIOUS_SAND),false);
        smeltables.add(Blocks.CLAY.builtInRegistryHolder(),new Smeltable(Blocks.TERRACOTTA),false);
        smeltables.add(Blocks.SANDSTONE.builtInRegistryHolder(),new Smeltable(Blocks.SMOOTH_SANDSTONE),false);
        smeltables.add(Blocks.SMOOTH_SANDSTONE.builtInRegistryHolder(),new Smeltable(Blocks.CHISELED_SANDSTONE),false);
        smeltables.add(Blocks.RED_SANDSTONE.builtInRegistryHolder(),new Smeltable(Blocks.SMOOTH_RED_SANDSTONE),false);
        smeltables.add(Blocks.SMOOTH_RED_SANDSTONE.builtInRegistryHolder(),new Smeltable(Blocks.CHISELED_RED_SANDSTONE),false);

        smeltables.add(Blocks.WHITE_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.WHITE_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.LIGHT_GRAY_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.GRAY_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.GRAY_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.BLACK_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.BLACK_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.BROWN_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.BROWN_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.RED_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.RED_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.ORANGE_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.ORANGE_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.YELLOW_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.YELLOW_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.LIME_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.LIME_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.GREEN_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.GREEN_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.CYAN_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.CYAN_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.LIGHT_BLUE_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.BLUE_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.BLUE_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.PURPLE_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.PURPLE_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.MAGENTA_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.MAGENTA_GLAZED_TERRACOTTA),false);
        smeltables.add(Blocks.PINK_TERRACOTTA.builtInRegistryHolder(),new Smeltable(Blocks.PINK_GLAZED_TERRACOTTA),false);

        smeltables.build();
    }

    @Override
    public String getName() {
        return "Overpowered Mastery Datamap Provider";
    }
}
