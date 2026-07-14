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
        smeltables.add(BlockTags.LOGS_THAT_BURN,new Smeltable(Blocks.COAL_BLOCK),false);
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

        smeltables.build();
    }

    @Override
    public String getName() {
        return "Overpowered Mastery Datamap Provider";
    }
}
