package net.rk.overpoweredmastery.datagen;

import com.mojang.math.Quadrant;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.block.InertEssenceOre;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.item.OMItems;
import net.rk.overpoweredmastery.item.custom.AbstractSpear;
import net.rk.overpoweredmastery.item.custom.AbstractWubs;
import net.rk.overpoweredmastery.item.custom.InertEssence;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class OMModels extends ModelProvider {
    public BlockModelGenerators gen;
    public ItemModelGenerators genItem;

    public static final TextureSlot MAIN = TextureSlot.create("main",null);
    public static final TextureSlot OVERLAY = TextureSlot.create("overlay",null);

    public static final ModelTemplate BASE_BLOCK_WITH_EMISSIVE_OVERLAY_TEMPLATE =
            new ModelTemplate(
                    Optional.of(ModelLocationUtils.decorateBlockModelLocation("base_overlay_block_emissive")),
                    Optional.empty(),
                    MAIN,
                    OVERLAY
            );

    public OMModels(PackOutput output) {
        super(output, OverpoweredMastery.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        gen = blockModels;
        genItem = itemModels;
        itemModels.generateCrossbow(OMItems.ENDARKENED_CROSSBOW.asItem());
        blockModels.createTrivialCube(OMBlocks.MOVING_PROBABLE_BLOCK.get());

        itemModels.generateFlatItem(OMItems.BONE_SWORD.asItem(),ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(OMItems.PENULTIMATE_SWORD_DARK.asItem(),ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(OMItems.PENULTIMATE_SWORD_LIGHT.asItem(),ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModels.generateFlatItem(OMItems.WOODEN_TOOL_BINDING.asItem(),ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(OMItems.METAL_TOOL_BINDING.asItem(),ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(OMItems.DIAMOND_TOOL_BINDING.asItem(),ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(OMItems.NETHERITE_TOOL_BINDING.asItem(),ModelTemplates.FLAT_ITEM);

        itemModels.generateFlatItem(OMItems.ULTIMATE_STAFF.asItem(),
                ModelTemplates.FLAT_HANDHELD_ITEM);

        blockModels.createTrivialCube(OMBlocks.INERT_DARK_ESSENCE_ORE.get());

        itemModels.declareCustomModelItem(OMItems.INERT_BLUE_ESSENCE_ORE.asItem());
        itemModels.declareCustomModelItem(OMItems.INERT_GREEN_ESSENCE_ORE.asItem());
        itemModels.declareCustomModelItem(OMItems.INERT_YELLOW_ESSENCE_ORE.asItem());
        itemModels.declareCustomModelItem(OMItems.INERT_ORANGE_ESSENCE_ORE.asItem());
        itemModels.declareCustomModelItem(OMItems.INERT_RED_ESSENCE_ORE.asItem());
        itemModels.declareCustomModelItem(OMItems.INERT_LIGHT_ESSENCE_ORE.asItem());
        itemModels.declareCustomModelItem(OMItems.INERT_AURORAN_ESSENCE_ORE.asItem());
        itemModels.declareCustomModelItem(OMItems.INERT_DARK_ESSENCE_ORE.asItem());

        itemModels.generateFlatItem(OMItems.ESSENCE_ELECTRONIC_CORE.asItem(),ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(OMItems.AURORAN_PROCESSOR.asItem(),ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(OMItems.REDSTONE_BASE_COMPONENT.asItem(),ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(OMItems.STRANGE_STONE.asItem(),ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(OMItems.ULTIMATE_INGOT.asItem(),ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(OMItems.ULTRA_INGOT.asItem(),ModelTemplates.FLAT_ITEM);

        itemModels.declareCustomModelItem(OMItems.PLACEHOLDER_ITEM.asItem());
        itemModels.generateFlatItem(OMItems.CONCENTRATED_MULTI_ESSENCE.asItem(),ModelTemplates.FLAT_ITEM);

        itemModels.generateBow(OMItems.ULTIMATE_BOW.asItem());
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return OMBlocks.BLOCKS.getEntries().stream().filter((x) ->
                !(x.is(OMBlocks.MULTI_ASSEMBLER)) &&
                !(x.value() instanceof InertEssenceOre) &&
                !(x.is(OMBlocks.SELECTION_BLOCK)));
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return OMItems.ITEMS.getEntries().stream().filter((x)->
                !x.is(OMItems.ULTRA_SWORD) &&
                !(x.value() instanceof InertEssence) &&
                !(x.value() instanceof AbstractWubs)
                && !(x.value() instanceof AbstractSpear)
        );
    }

    public void customItemModel(Block block, String customLocation){
        ResourceLocation rs = ResourceLocation.parse(customLocation);
        gen.itemModelOutput.register(
                block.asItem(),
                new ClientItem(
                        new BlockModelWrapper.Unbaked(
                                rs,
                                Collections.emptyList()
                        ),
                        new ClientItem.Properties(
                                true,false
                        )));
    }

    public void customItemModel(Block block){
        gen.itemModelOutput.register(
                block.asItem(),
                new ClientItem(
                        // Defines the model to render
                        new BlockModelWrapper.Unbaked(
                                // Located at 'assets/MODID/models/item/ITEMNAME.json'
                                ModelLocationUtils.getModelLocation(block.asItem()),
                                Collections.emptyList()
                        ),
                        new ClientItem.Properties(
                                true,false
                        )));
    }

    public void customItemModel(Item item){
        gen.itemModelOutput.register(
                item,
                new ClientItem(
                        // Defines the model to render
                        new BlockModelWrapper.Unbaked(
                                // Located at 'assets/MODID/models/item/ITEMNAME.json'
                                ModelLocationUtils.getModelLocation(item),
                                Collections.emptyList()
                        ),
                        new ClientItem.Properties(
                                true,false
                        )));
    }

    public void customItemModelNoSwap(Item item){
        gen.itemModelOutput.register(
                item,
                new ClientItem(
                        // Defines the model to render
                        new BlockModelWrapper.Unbaked(
                                // Located at 'assets/MODID/models/item/ITEMNAME.json'
                                ModelLocationUtils.getModelLocation(item),
                                Collections.emptyList()
                        ),
                        new ClientItem.Properties(
                                false,false
                        )));
    }

    public void customItemModelNoSwapOversized(Item item){
        gen.itemModelOutput.register(
                item,
                new ClientItem(
                        // Defines the model to render
                        new BlockModelWrapper.Unbaked(
                                // Located at 'assets/MODID/models/item/ITEMNAME.json'
                                ModelLocationUtils.getModelLocation(item),
                                Collections.emptyList()
                        ),
                        new ClientItem.Properties(
                                false,true
                        )));
    }

    public void customItemModel(Item item, String customLocation){
        gen.itemModelOutput.register(
                item,
                new ClientItem(
                        new BlockModelWrapper.Unbaked(
                                ResourceLocation.parse(customLocation),
                                Collections.emptyList()
                        ),
                        new ClientItem.Properties(
                                true,false
                        )));
    }

    public void customItemModel(Item item, String customLocation, List<ItemTintSource> sources){
        gen.itemModelOutput.register(
                item,
                new ClientItem(
                        new BlockModelWrapper.Unbaked(
                                ResourceLocation.parse(customLocation),
                                sources
                        ),
                        new ClientItem.Properties(
                                true,false
                        )));
    }
}
