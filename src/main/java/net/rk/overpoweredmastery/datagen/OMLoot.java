package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.item.OMItems;

import java.util.Set;

public class OMLoot extends BlockLootSubProvider {
    public OMLoot(HolderLookup.Provider lookupProvider) {
        super(Set.of(),FeatureFlags.DEFAULT_FLAGS,lookupProvider);
    }

    @Override
    protected void generate() {
        System.out.println("Generating loot tables for OMLoot");
        HolderLookup.RegistryLookup<Enchantment> registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);

        this.add(OMBlocks.INERT_BLUE_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_BLUE_ESSENCE.asItem()));
        this.add(OMBlocks.INERT_GREEN_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_GREEN_ESSENCE.asItem()));
        this.add(OMBlocks.INERT_YELLOW_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_YELLOW_ESSENCE.asItem()));
        this.add(OMBlocks.INERT_ORANGE_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_ORANGE_ESSENCE.asItem()));
        this.add(OMBlocks.INERT_RED_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_RED_ESSENCE.asItem()));
        this.add(OMBlocks.INERT_LIGHT_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_LIGHT_ESSENCE.asItem()));
        this.add(OMBlocks.INERT_AURORAN_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_AURORAN_ESSENCE.asItem()));
        this.add(OMBlocks.INERT_DARK_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_DARK_ESSENCE.asItem()));

        this.add(OMBlocks.DEEPSLATE_INERT_BLUE_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_BLUE_ESSENCE.asItem()));
        this.add(OMBlocks.DEEPSLATE_INERT_GREEN_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_GREEN_ESSENCE.asItem()));
        this.add(OMBlocks.DEEPSLATE_INERT_YELLOW_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_YELLOW_ESSENCE.asItem()));
        this.add(OMBlocks.DEEPSLATE_INERT_ORANGE_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_ORANGE_ESSENCE.asItem()));
        this.add(OMBlocks.DEEPSLATE_INERT_RED_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_RED_ESSENCE.asItem()));
        this.add(OMBlocks.DEEPSLATE_INERT_LIGHT_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_LIGHT_ESSENCE.asItem()));
        this.add(OMBlocks.DEEPSLATE_INERT_AURORAN_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_AURORAN_ESSENCE.asItem()));
        this.add(OMBlocks.DEEPSLATE_INERT_DARK_ESSENCE_ORE.get(),block -> this.createOreDrop(block,OMItems.INERT_DARK_ESSENCE.asItem()));

        this.add(OMBlocks.MULTI_ASSEMBLER.get(),this.createSingleItemTable(OMItems.MULTI_ASSEMBLER.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return OMBlocks.BLOCKS.getEntries().stream().filter(
                x -> !x.is(OMBlocks.MOVING_PROBABLE_BLOCK)
        ).map(blk -> (Block) blk.value()).toList();
    }
}
