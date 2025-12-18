package net.rk.overpoweredmastery.datagen;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.BlockModelWrapper;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.SpecialModelWrapper;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredItem;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.block.InertEssenceOre;
import net.rk.overpoweredmastery.block.OMBlocks;
import net.rk.overpoweredmastery.entity.renderer.MultiAssemblerSpecialModelRenderer;
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
                    Optional.of(ResourceLocation.parse("overpoweredmastery:block/base_overlay_block_emissive")),
                    Optional.empty(),
                    MAIN,
                    OVERLAY
            );

    public static TextureMapping makeTextureMappingEmissiveBase(ResourceLocation main, ResourceLocation overlay){
        return new TextureMapping().put(MAIN, main).put(OVERLAY, overlay);
    }

    public static TextureMapping makeTextureMappingEmissiveBase(String main, String overlay){
        main = main.replace("overpoweredmastery:","");
        overlay = overlay.replace("overpoweredmastery:","");
        StringBuilder builder = new StringBuilder();
        builder.append("block/");
        builder.append(main);
        StringBuilder builderOverlay = new StringBuilder();
        builderOverlay.append("block/");
        builderOverlay.append(overlay);
        return new TextureMapping().put(MAIN,ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,builder.toString())).put(OVERLAY,ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,builderOverlay.toString()));
    }

    public OMModels(PackOutput output) {
        super(output, OverpoweredMastery.MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        gen = blockModels;
        genItem = itemModels;
        itemModels.generateCrossbow(OMItems.ENDARKENED_CROSSBOW.asItem());
        blockModels.createTrivialCube(OMBlocks.MOVING_PROBABLE_BLOCK.get());

        // deepslate essence ores
        BASE_BLOCK_WITH_EMISSIVE_OVERLAY_TEMPLATE.create(OMBlocks.DEEPSLATE_INERT_BLUE_ESSENCE_ORE.get(),
                makeTextureMappingEmissiveBase(OMBlocks.DEEPSLATE_INERT_BLUE_ESSENCE_ORE.getRegisteredName(),
                        "inert_generic_essence_ore_overlay"),blockModels.modelOutput);
        BASE_BLOCK_WITH_EMISSIVE_OVERLAY_TEMPLATE.create(OMBlocks.DEEPSLATE_INERT_GREEN_ESSENCE_ORE.get(),
                makeTextureMappingEmissiveBase(OMBlocks.DEEPSLATE_INERT_GREEN_ESSENCE_ORE.getRegisteredName(),
                        "inert_generic_essence_ore_overlay"),blockModels.modelOutput);
        BASE_BLOCK_WITH_EMISSIVE_OVERLAY_TEMPLATE.create(OMBlocks.DEEPSLATE_INERT_YELLOW_ESSENCE_ORE.get(),
                makeTextureMappingEmissiveBase(OMBlocks.DEEPSLATE_INERT_YELLOW_ESSENCE_ORE.getRegisteredName(),
                        "inert_generic_essence_ore_overlay"),blockModels.modelOutput);
        BASE_BLOCK_WITH_EMISSIVE_OVERLAY_TEMPLATE.create(OMBlocks.DEEPSLATE_INERT_ORANGE_ESSENCE_ORE.get(),
                makeTextureMappingEmissiveBase(OMBlocks.DEEPSLATE_INERT_ORANGE_ESSENCE_ORE.getRegisteredName(),
                        "inert_generic_essence_ore_overlay"),blockModels.modelOutput);
        BASE_BLOCK_WITH_EMISSIVE_OVERLAY_TEMPLATE.create(OMBlocks.DEEPSLATE_INERT_RED_ESSENCE_ORE.get(),
                makeTextureMappingEmissiveBase(OMBlocks.DEEPSLATE_INERT_RED_ESSENCE_ORE.getRegisteredName(),
                        "inert_generic_essence_ore_overlay"),blockModels.modelOutput);
        // deepslate special essence ores
        BASE_BLOCK_WITH_EMISSIVE_OVERLAY_TEMPLATE.create(OMBlocks.DEEPSLATE_INERT_LIGHT_ESSENCE_ORE.get(),
                makeTextureMappingEmissiveBase(OMBlocks.DEEPSLATE_INERT_LIGHT_ESSENCE_ORE.getRegisteredName(),
                        "inert_light_essence_ore_overlay"),blockModels.modelOutput);
        BASE_BLOCK_WITH_EMISSIVE_OVERLAY_TEMPLATE.create(OMBlocks.DEEPSLATE_INERT_AURORAN_ESSENCE_ORE.get(),
                makeTextureMappingEmissiveBase(OMBlocks.DEEPSLATE_INERT_AURORAN_ESSENCE_ORE.getRegisteredName(),
                        "inert_auroran_essence_ore_overlay"),blockModels.modelOutput);
        blockModels.createTrivialCube(OMBlocks.DEEPSLATE_INERT_DARK_ESSENCE_ORE.get());



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

        itemModels.generateFlatItem(OMItems.ULTIMATE_SWORD.asItem(),ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(OMItems.ULTIMATE_HOE.asItem(),ModelTemplates.FLAT_HANDHELD_ITEM);

        itemModels.itemModelOutput.accept(OMItems.MULTI_ASSEMBLER.asItem(),
                new SpecialModelWrapper.Unbaked(
                        obtainItemModelLocation(OMItems.MULTI_ASSEMBLER),
                        new MultiAssemblerSpecialModelRenderer.Unbaked(
                                ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,
                                        "multi_assembler")
                        )
                ));

        itemModels.generateFishingRod(OMItems.ULTIMATE_FISHING_ROD.asItem());
        itemModels.generateFlatItem(OMItems.PENULTIMATE_SWORD_CATALYST.asItem(),ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE.asItem(),ModelTemplates.FLAT_ITEM);
    }

    public static ResourceLocation obtainItemModelLocation(DeferredItem<Item> item){
        return ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,getItemModelLocationFromItem(item.getRegisteredName()));
    }

    public static String getItemModelLocationFromItem(String registeredName){
        return registeredName.replaceAll("overpoweredmastery:","item/");
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
