package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockStateMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.block.OMBlocks;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class OMWorldgen extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder WORLD_GEN_BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE,OMWorldgen::configuredFeatureBootstrap)
            .add(Registries.PLACED_FEATURE,OMWorldgen::placedFeatureBootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS,OMWorldgen::biomeModifierBootstrap)
            ;
    // configured features
    public static final ResourceKey<ConfiguredFeature<?, ?>>  INERT_BLUE_ESSENCE_STONE_ORE = registerKey("inert_blue_essence_ore_generic_size");
    public static final ResourceKey<ConfiguredFeature<?,?>> INERT_GREEN_ESSENCE_STONE_ORE = registerKey("inert_green_essence_ore_generic_size");
    public static final ResourceKey<ConfiguredFeature<?,?>> INERT_YELLOW_ESSENCE_STONE_ORE = registerKey("inert_yellow_essence_ore_generic_size");
    public static final ResourceKey<ConfiguredFeature<?,?>> INERT_ORANGE_ESSENCE_STONE_ORE = registerKey("inert_orange_essence_ore_generic_size");
    public static final ResourceKey<ConfiguredFeature<?,?>> INERT_RED_ESSENCE_STONE_ORE = registerKey("inert_red_essence_ore_generic_size");
    public static final ResourceKey<ConfiguredFeature<?,?>> INERT_LIGHT_ESSENCE_STONE_ORE = registerKey("inert_light_essence_ore_generic_size");
    public static final ResourceKey<ConfiguredFeature<?,?>> INERT_AURORAN_ESSENCE_STONE_ORE = registerKey("inert_auroran_essence_ore_generic_size");
    public static final ResourceKey<ConfiguredFeature<?,?>> INERT_DARK_ESSENCE_STONE_ORE = registerKey("inert_dark_essence_ore_generic_size");
    // placed features
    public static final ResourceKey<PlacedFeature> INERT_BLUE_ESSENCE_ORE_GENERIC = createPlacedFeatureKey("inert_blue_essence_ore_generic");
    public static final ResourceKey<PlacedFeature> INERT_BLUE_ESSENCE_ORE_GENERIC_LOWER = createPlacedFeatureKey("inert_blue_essence_ore_generic_lower");
    public static final ResourceKey<PlacedFeature> INERT_GREEN_ESSENCE_ORE_GENERIC = createPlacedFeatureKey("inert_green_essence_ore_generic");
    public static final ResourceKey<PlacedFeature> INERT_GREEN_ESSENCE_ORE_GENERIC_LOWER = createPlacedFeatureKey("inert_green_essence_ore_generic_lower");
    public static final ResourceKey<PlacedFeature> INERT_YELLOW_ESSENCE_ORE_GENERIC = createPlacedFeatureKey("inert_yellow_essence_ore_generic");
    public static final ResourceKey<PlacedFeature> INERT_YELLOW_ESSENCE_ORE_GENERIC_LOWER = createPlacedFeatureKey("inert_yellow_essence_ore_generic_lower");
    public static final ResourceKey<PlacedFeature> INERT_ORANGE_ESSENCE_ORE_GENERIC = createPlacedFeatureKey("inert_orange_essence_ore_generic");
    public static final ResourceKey<PlacedFeature> INERT_ORANGE_ESSENCE_ORE_GENERIC_LOWER = createPlacedFeatureKey("inert_orange_essence_ore_generic_lower");
    public static final ResourceKey<PlacedFeature> INERT_RED_ESSENCE_ORE_GENERIC = createPlacedFeatureKey("inert_red_essence_ore_generic");
    public static final ResourceKey<PlacedFeature> INERT_RED_ESSENCE_ORE_GENERIC_LOWER = createPlacedFeatureKey("inert_red_essence_ore_generic_lower");
    public static final ResourceKey<PlacedFeature> INERT_LIGHT_ESSENCE_ORE_GENERIC = createPlacedFeatureKey("inert_light_essence_ore_generic");
    public static final ResourceKey<PlacedFeature> INERT_LIGHT_ESSENCE_ORE_GENERIC_LOWER = createPlacedFeatureKey("inert_light_essence_ore_generic_lower");
    public static final ResourceKey<PlacedFeature> INERT_AURORAN_ESSENCE_ORE_GENERIC = createPlacedFeatureKey("inert_auroran_essence_ore_generic");
    public static final ResourceKey<PlacedFeature> INERT_AURORAN_ESSENCE_ORE_GENERIC_LOWER = createPlacedFeatureKey("inert_auroran_essence_ore_generic_lower");
    public static final ResourceKey<PlacedFeature> INERT_DARK_ESSENCE_ORE_GENERIC = createPlacedFeatureKey("inert_dark_essence_ore_generic");
    public static final ResourceKey<PlacedFeature> INERT_DARK_ESSENCE_ORE_GENERIC_LOWER = createPlacedFeatureKey("inert_dark_essence_ore_generic_lower");
    // biome modifiers
    public static final ResourceKey<BiomeModifier> ESSENCE_ORE_MODIFIER = ResourceKey.create(
            NeoForgeRegistries.Keys.BIOME_MODIFIERS,
            ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID, "essence_ore_modifier")
    );

    // seed for testing: 7663407759271754169 (should be a pillager outpost nearby)

    @Override
    public String getName() {
        return "Overpowered Mastery Worldgen Provider";
    }

    public OMWorldgen(PackOutput output, CompletableFuture<RegistrySetBuilder.PatchedRegistries> registries) {
        super(output, registries, Set.of(OverpoweredMastery.MODID));
    }

    public static void configuredFeatureBootstrap(BootstrapContext<ConfiguredFeature<?,?>> context){
        //HolderGetter<Biome> biomeHG = context.lookup(Registries.BIOME);
        HolderGetter<Block> blockHG = context.lookup(Registries.BLOCK);
        HolderGetter<ConfiguredFeature<?,?>> configuredFeatureHG = context.lookup(Registries.CONFIGURED_FEATURE);

        RuleTest baseStoneOverworld = new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD);
        RuleTest stoneOreReplaceables = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateOreReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        RuleTest netherrackMatches = new BlockMatchTest(Blocks.NETHERRACK);
        RuleTest baseStoneNether = new TagMatchTest(BlockTags.BASE_STONE_NETHER);

        FeatureUtils.register(context, INERT_BLUE_ESSENCE_STONE_ORE, Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(stoneOreReplaceables, OMBlocks.INERT_BLUE_ESSENCE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(deepslateOreReplaceables, OMBlocks.DEEPSLATE_INERT_BLUE_ESSENCE_ORE.get().defaultBlockState())
                ),5));
        FeatureUtils.register(context, INERT_GREEN_ESSENCE_STONE_ORE, Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(stoneOreReplaceables, OMBlocks.INERT_GREEN_ESSENCE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(deepslateOreReplaceables, OMBlocks.DEEPSLATE_INERT_GREEN_ESSENCE_ORE.get().defaultBlockState())
                ),5));
        FeatureUtils.register(context, INERT_YELLOW_ESSENCE_STONE_ORE, Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(stoneOreReplaceables, OMBlocks.INERT_YELLOW_ESSENCE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(deepslateOreReplaceables, OMBlocks.DEEPSLATE_INERT_YELLOW_ESSENCE_ORE.get().defaultBlockState())
                ),3));
        FeatureUtils.register(context, INERT_ORANGE_ESSENCE_STONE_ORE, Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(stoneOreReplaceables, OMBlocks.INERT_ORANGE_ESSENCE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(deepslateOreReplaceables, OMBlocks.DEEPSLATE_INERT_ORANGE_ESSENCE_ORE.get().defaultBlockState())
                ),4));
        FeatureUtils.register(context, INERT_RED_ESSENCE_STONE_ORE, Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(stoneOreReplaceables, OMBlocks.INERT_RED_ESSENCE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(deepslateOreReplaceables, OMBlocks.DEEPSLATE_INERT_RED_ESSENCE_ORE.get().defaultBlockState())
                ),4));
        FeatureUtils.register(context, INERT_LIGHT_ESSENCE_STONE_ORE, Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(stoneOreReplaceables, OMBlocks.INERT_LIGHT_ESSENCE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(deepslateOreReplaceables, OMBlocks.DEEPSLATE_INERT_LIGHT_ESSENCE_ORE.get().defaultBlockState())
                ),4,0.0f));
        FeatureUtils.register(context, INERT_AURORAN_ESSENCE_STONE_ORE, Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(stoneOreReplaceables, OMBlocks.INERT_AURORAN_ESSENCE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(deepslateOreReplaceables, OMBlocks.DEEPSLATE_INERT_AURORAN_ESSENCE_ORE.get().defaultBlockState())
                ),4,0.02f));
        FeatureUtils.register(context, INERT_DARK_ESSENCE_STONE_ORE, Feature.ORE, new OreConfiguration(
                List.of(
                        OreConfiguration.target(stoneOreReplaceables, OMBlocks.INERT_DARK_ESSENCE_ORE.get().defaultBlockState()),
                        OreConfiguration.target(deepslateOreReplaceables, OMBlocks.DEEPSLATE_INERT_DARK_ESSENCE_ORE.get().defaultBlockState())
                ),4,0.04f));
    }

    public static void placedFeatureBootstrap(BootstrapContext<PlacedFeature> context){
        HolderGetter<Biome> biomeHG = context.lookup(Registries.BIOME);
        HolderGetter<ConfiguredFeature<?,?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        Holder<ConfiguredFeature<?,?>> inertBlueEssenceOreGeneric = configuredFeatures.getOrThrow(INERT_BLUE_ESSENCE_STONE_ORE);
        Holder<ConfiguredFeature<?,?>> inertGreenEssenceOreGeneric = configuredFeatures.getOrThrow(INERT_GREEN_ESSENCE_STONE_ORE);
        Holder<ConfiguredFeature<?,?>> inertYellowEssenceOreGeneric = configuredFeatures.getOrThrow(INERT_YELLOW_ESSENCE_STONE_ORE);
        Holder<ConfiguredFeature<?,?>> inertOrangeEssenceOreGeneric = configuredFeatures.getOrThrow(INERT_ORANGE_ESSENCE_STONE_ORE);
        Holder<ConfiguredFeature<?,?>> inertRedEssenceOreGeneric = configuredFeatures.getOrThrow(INERT_RED_ESSENCE_STONE_ORE);
        Holder<ConfiguredFeature<?,?>> inertLightEssenceOreGeneric = configuredFeatures.getOrThrow(INERT_LIGHT_ESSENCE_STONE_ORE);
        Holder<ConfiguredFeature<?,?>> inertAuroranEssenceOreGeneric = configuredFeatures.getOrThrow(INERT_AURORAN_ESSENCE_STONE_ORE);
        Holder<ConfiguredFeature<?,?>> inertDarkEssenceOreGeneric = configuredFeatures.getOrThrow(INERT_DARK_ESSENCE_STONE_ORE);

        //blue
        PlacementUtils.register(
                context,
                INERT_BLUE_ESSENCE_ORE_GENERIC,
                inertBlueEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(1),
                        CountPlacement.of(7),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(10),VerticalAnchor.absolute(210))
                )
        );
        PlacementUtils.register(
                context,
                INERT_BLUE_ESSENCE_ORE_GENERIC_LOWER,
                inertBlueEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(1),
                        CountPlacement.of(5),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-42),VerticalAnchor.absolute(0))
                )
        );
        // green
        PlacementUtils.register(
                context,
                INERT_GREEN_ESSENCE_ORE_GENERIC,
                inertGreenEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(1),
                        CountPlacement.of(5),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(10),VerticalAnchor.absolute(128))
                )
        );
        PlacementUtils.register(
                context,
                INERT_GREEN_ESSENCE_ORE_GENERIC_LOWER,
                inertGreenEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(1),
                        CountPlacement.of(7),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-42),VerticalAnchor.absolute(0))
                )
        );
        // yellow
        PlacementUtils.register(
                context,
                INERT_YELLOW_ESSENCE_ORE_GENERIC,
                inertYellowEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(1),
                        CountPlacement.of(7),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(10),VerticalAnchor.absolute(128))
                )
        );
        PlacementUtils.register(
                context,
                INERT_YELLOW_ESSENCE_ORE_GENERIC_LOWER,
                inertYellowEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(1),
                        CountPlacement.of(6),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-42),VerticalAnchor.absolute(0))
                )
        );
        // orange
        PlacementUtils.register(
                context,
                INERT_ORANGE_ESSENCE_ORE_GENERIC,
                inertOrangeEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(1),
                        CountPlacement.of(6),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(10),VerticalAnchor.absolute(64))
                )
        );
        PlacementUtils.register(
                context,
                INERT_ORANGE_ESSENCE_ORE_GENERIC_LOWER,
                inertOrangeEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(1),
                        CountPlacement.of(7),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-52),VerticalAnchor.absolute(0))
                )
        );
        // red
        PlacementUtils.register(
                context,
                INERT_RED_ESSENCE_ORE_GENERIC,
                inertRedEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(1),
                        CountPlacement.of(7),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(3),VerticalAnchor.absolute(128))
                )
        );
        PlacementUtils.register(
                context,
                INERT_RED_ESSENCE_ORE_GENERIC_LOWER,
                inertRedEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(1),
                        CountPlacement.of(5),
                        HeightRangePlacement.triangle(VerticalAnchor.absolute(-52),VerticalAnchor.absolute(0))
                )
        );
        // light
        PlacementUtils.register(
                context,
                INERT_LIGHT_ESSENCE_ORE_GENERIC,
                inertLightEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(2),
                        CountPlacement.of(4),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(2),VerticalAnchor.absolute(128))
                )
        );
        PlacementUtils.register(
                context,
                INERT_LIGHT_ESSENCE_ORE_GENERIC_LOWER,
                inertLightEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(3),
                        CountPlacement.of(3),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-20),VerticalAnchor.absolute(2))
                )
        );
        // auroran
        PlacementUtils.register(
                context,
                INERT_AURORAN_ESSENCE_ORE_GENERIC,
                inertAuroranEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(3),
                        CountPlacement.of(2),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(5),VerticalAnchor.absolute(128))
                )
        );
        PlacementUtils.register(
                context,
                INERT_AURORAN_ESSENCE_ORE_GENERIC_LOWER,
                inertAuroranEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(3),
                        CountPlacement.of(3),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-55),VerticalAnchor.absolute(0))
                )
        );
        // dark
        PlacementUtils.register(
                context,
                INERT_DARK_ESSENCE_ORE_GENERIC,
                inertDarkEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(2),
                        CountPlacement.of(2),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(8),VerticalAnchor.absolute(128))
                )
        );
        PlacementUtils.register(
                context,
                INERT_DARK_ESSENCE_ORE_GENERIC_LOWER,
                inertDarkEssenceOreGeneric,
                List.of(
                        BiomeFilter.biome(),
                        InSquarePlacement.spread(),
                        RarityFilter.onAverageOnceEvery(2),
                        CountPlacement.of(3),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-58),VerticalAnchor.absolute(0))
                )
        );
    }

    public static void biomeModifierBootstrap(BootstrapContext<BiomeModifier> context){
        HolderGetter<Biome> biomeHG = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);

        context.register(ESSENCE_ORE_MODIFIER,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomeHG.getOrThrow(Tags.Biomes.IS_OVERWORLD),
                        HolderSet.direct(
                                placedFeatures.getOrThrow(INERT_BLUE_ESSENCE_ORE_GENERIC),
                                placedFeatures.getOrThrow(INERT_BLUE_ESSENCE_ORE_GENERIC_LOWER),
                                placedFeatures.getOrThrow(INERT_GREEN_ESSENCE_ORE_GENERIC),
                                placedFeatures.getOrThrow(INERT_GREEN_ESSENCE_ORE_GENERIC_LOWER),
                                placedFeatures.getOrThrow(INERT_YELLOW_ESSENCE_ORE_GENERIC),
                                placedFeatures.getOrThrow(INERT_YELLOW_ESSENCE_ORE_GENERIC_LOWER),
                                placedFeatures.getOrThrow(INERT_ORANGE_ESSENCE_ORE_GENERIC),
                                placedFeatures.getOrThrow(INERT_ORANGE_ESSENCE_ORE_GENERIC_LOWER),
                                placedFeatures.getOrThrow(INERT_RED_ESSENCE_ORE_GENERIC),
                                placedFeatures.getOrThrow(INERT_RED_ESSENCE_ORE_GENERIC_LOWER),
                                placedFeatures.getOrThrow(INERT_LIGHT_ESSENCE_ORE_GENERIC),
                                placedFeatures.getOrThrow(INERT_LIGHT_ESSENCE_ORE_GENERIC_LOWER),
                                placedFeatures.getOrThrow(INERT_AURORAN_ESSENCE_ORE_GENERIC),
                                placedFeatures.getOrThrow(INERT_AURORAN_ESSENCE_ORE_GENERIC_LOWER),
                                placedFeatures.getOrThrow(INERT_DARK_ESSENCE_ORE_GENERIC),
                                placedFeatures.getOrThrow(INERT_DARK_ESSENCE_ORE_GENERIC_LOWER)
                        ),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                ));
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,name));
    }

    public static ResourceKey<PlacedFeature> createPlacedFeatureKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE,ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,name));
    }
}
