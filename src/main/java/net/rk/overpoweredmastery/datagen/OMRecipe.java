package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.rk.overpoweredmastery.item.OMItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class OMRecipe extends RecipeProvider {
    protected OMRecipe(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
        this.items = registries.lookupOrThrow(Registries.ITEM);
    }

    public final HolderGetter<Item> items;

    @Override
    protected void buildRecipes() {
        shapeless(RecipeCategory.COMBAT,OMItems.ENDARKENED_CROSSBOW,1)
                .requires(Ingredient.of(Items.CROSSBOW))
                .requires(Ingredient.of(Items.CRYING_OBSIDIAN))
                .requires(Ingredient.of(Items.CRYING_OBSIDIAN))
                .requires(Ingredient.of(Items.HEART_OF_THE_SEA))
                .unlockedBy("has_thingy",has(Items.CROSSBOW))
                .save(this.output,"endarkened_crossbow");

        shaped(RecipeCategory.COMBAT, OMItems.RED_WUBS,1)
                .define('i', Items.IRON_INGOT)
                .define('g', Items.CROSSBOW)
                .define('d', Items.MUSIC_DISC_BLOCKS)
                .define('t', ItemTags.TRAPDOORS)
                .define('j', Items.JUKEBOX)
                .pattern("gii")
                .pattern("idt")
                .pattern("ijt")
                .unlockedBy("has_thingy",has(Items.MUSIC_DISC_CHIRP))
                .save(this.output,"red_wubs");

        shaped(RecipeCategory.COMBAT, OMItems.GREEN_WUBS,1)
                .define('i', Items.IRON_INGOT)
                .define('g', Items.CROSSBOW)
                .define('d', Items.MUSIC_DISC_FAR)
                .define('c', Items.CACTUS)
                .define('t', ItemTags.TRAPDOORS)
                .define('j', Items.JUKEBOX)
                .pattern("gii")
                .pattern("cdt")
                .pattern("ijt")
                .unlockedBy("has_thingy",has(Items.MUSIC_DISC_CAT))
                .save(this.output,"green_wubs");

        shaped(RecipeCategory.COMBAT, OMItems.PURPLE_WUBS,1)
                .define('o', Items.OBSIDIAN)
                .define('g', Items.CROSSBOW)
                .define('d', Items.MUSIC_DISC_MELLOHI)
                .define('c', Items.CRYING_OBSIDIAN)
                .define('t', ItemTags.TRAPDOORS)
                .define('j', Items.JUKEBOX)
                .pattern("goo")
                .pattern("cdt")
                .pattern("ojt")
                .unlockedBy("has_thingy",has(Items.MUSIC_DISC_MALL))
                .save(this.output,"purple_wubs");

        shaped(RecipeCategory.COMBAT, OMItems.CHICKEN_WUBS,1)
                .define('i', Items.IRON_INGOT)
                .define('g', Items.CROSSBOW)
                .define('d', Items.MUSIC_DISC_LAVA_CHICKEN)
                .define('s', Items.SMOKER)
                .define('l', Items.LAVA_BUCKET)
                .define('t', ItemTags.TRAPDOORS)
                .define('j', Items.JUKEBOX)
                .pattern("gii")
                .pattern("sdl")
                .pattern("ijt")
                .unlockedBy("has_thingy",has(Items.MUSIC_DISC_LAVA_CHICKEN))
                .save(this.output,"chicken_wubs");

        shaped(RecipeCategory.COMBAT, OMItems.NETHER_WUBS,1)
                .define('r', Items.NETHERRACK)
                .define('g', Items.CROSSBOW)
                .define('d', Items.MUSIC_DISC_PIGSTEP)
                .define('n', Items.NETHERITE_BLOCK)
                .define('b', Items.LAVA_BUCKET)
                .define('t', ItemTags.TRAPDOORS)
                .define('j', Items.JUKEBOX)
                .pattern("grr")
                .pattern("bdn")
                .pattern("rjt")
                .unlockedBy("has_thingy",has(Items.MUSIC_DISC_PIGSTEP))
                .save(this.output,"nether_wubs");

        shaped(RecipeCategory.COMBAT, OMItems.TRIAL_WUBS,1)
                .define('c', Items.COPPER_BLOCK)
                .define('g', Items.CROSSBOW)
                .define('d', Items.MUSIC_DISC_PRECIPICE)
                .define('n', Items.CHISELED_TUFF_BRICKS)
                .define('j', Items.JUKEBOX)
                .pattern("gcc")
                .pattern("ndn")
                .pattern("cjn")
                .unlockedBy("has_thingy",has(Items.MUSIC_DISC_PRECIPICE))
                .save(this.output,"trial_wubs");

        shaped(RecipeCategory.COMBAT, OMItems.OXIDIZED_TRIAl_WUBS,1)
                .define('c', Items.OXIDIZED_COPPER)
                .define('g', Items.CROSSBOW)
                .define('d', Items.MUSIC_DISC_CREATOR)
                .define('n', Items.CHISELED_TUFF_BRICKS)
                .define('j', Items.JUKEBOX)
                .pattern("gcc")
                .pattern("ndn")
                .pattern("cjn")
                .unlockedBy("has_thingy",has(Items.MUSIC_DISC_CREATOR))
                .save(this.output,"oxidized_trial_wubs");

        shapeless(RecipeCategory.MISC,OMItems.MOVING_PROBABLE_BLOCK_ITEM,1)
                .requires(Ingredient.of(Items.LAPIS_BLOCK))
                .requires(ItemTags.TRIM_MATERIALS)
                .requires(ItemTags.TRIM_MATERIALS)
                .requires(ItemTags.LOGS)
                .requires(ItemTags.STONE_CRAFTING_MATERIALS)
                .unlockedBy("has_thingy",has(Items.LAPIS_BLOCK))
                .save(this.output,"moving_probable_block");

        shaped(RecipeCategory.COMBAT,OMItems.BONE_SWORD)
                .define('s', Items.STICK)
                .define('b', Items.BONE)
                .pattern("b")
                .pattern("b")
                .pattern("s")
                .unlockedBy("has_thingy",has(Items.STICK))
                .save(this.output,"bone_sword");

        shaped(RecipeCategory.MISC,OMItems.WOODEN_TOOL_BINDING)
                .define('m', ItemTags.PLANKS)
                .define('s', Items.STICK)
                .pattern("s s")
                .pattern(" m ")
                .pattern("s s")
                .unlockedBy("has_thingy",has(ItemTags.PLANKS))
                .save(this.output,"wooden_tool_binding");

        shaped(RecipeCategory.MISC,OMItems.METAL_TOOL_BINDING)
                .define('b', OMItems.WOODEN_TOOL_BINDING)
                .define('m', Tags.Items.INGOTS_IRON)
                .pattern("m m")
                .pattern(" b ")
                .pattern("m m")
                .unlockedBy("has_thingy",has(Tags.Items.INGOTS_IRON))
                .save(this.output,"metal_tool_binding");

        shaped(RecipeCategory.MISC,OMItems.DIAMOND_TOOL_BINDING)
                .define('b', OMItems.METAL_TOOL_BINDING)
                .define('m', Tags.Items.GEMS_DIAMOND)
                .pattern("m m")
                .pattern(" b ")
                .pattern("m m")
                .unlockedBy("has_thingy",has(Tags.Items.GEMS_DIAMOND))
                .save(this.output,"diamond_tool_binding");


        createSpearRecipePair("wooden_spear",
                OMItems.WOODEN_SPEAR.asItem(),
                ItemTags.PLANKS,
                OMItems.WOODEN_TOOL_BINDING.asItem(),
                Items.COPPER_INGOT);
        createSpearRecipePair("stone_spear",
                OMItems.STONE_SPEAR.asItem(),
                ItemTags.STONE_CRAFTING_MATERIALS,
                OMItems.WOODEN_TOOL_BINDING.asItem(),
                Items.COPPER_INGOT);
        createSpearRecipePair("gold_spear",
                OMItems.GOLD_SPEAR.asItem(),
                ItemTags.GOLD_TOOL_MATERIALS,
                OMItems.WOODEN_TOOL_BINDING.asItem(),
                Items.COPPER_INGOT);
        createSpearRecipePair("iron_spear",
                OMItems.IRON_SPEAR.asItem(),
                ItemTags.IRON_TOOL_MATERIALS,
                OMItems.METAL_TOOL_BINDING.asItem(),
                Items.COPPER_INGOT);
        createSpearRecipePair("diamond_spear",
                OMItems.DIAMOND_SPEAR.asItem(),
                ItemTags.DIAMOND_TOOL_MATERIALS,
                OMItems.DIAMOND_TOOL_BINDING.asItem(),
                Items.COPPER_INGOT);

        netheriteSmithing(OMItems.DIAMOND_TOOL_BINDING.asItem(),RecipeCategory.COMBAT,OMItems.NETHERITE_TOOL_BINDING.asItem());
        netheriteSmithing(OMItems.DIAMOND_SPEAR.asItem(),RecipeCategory.COMBAT,OMItems.NETHERITE_SPEAR.asItem());

        shaped(RecipeCategory.MISC,OMItems.REDSTONE_BASE_COMPONENT)
                .define('r', Items.REDSTONE)
                .define('s', Items.SMOOTH_STONE)
                .define('c', OMItems.STRANGE_STONE)
                .pattern("srs")
                .pattern("rcr")
                .pattern("srs")
                .unlockedBy("has_thingy",has(OMItems.STRANGE_STONE))
                .save(this.output,"redstone_base_component");

        shaped(RecipeCategory.MISC,OMItems.AURORAN_PROCESSOR)
                .define('g', Items.GOLD_INGOT)
                .define('a', OMItems.INERT_AURORAN_ESSENCE)
                .define('c', Items.GREEN_CONCRETE)
                .pattern("cgc")
                .pattern("gag")
                .pattern("cgc")
                .unlockedBy("has_thingy",has(OMItems.INERT_AURORAN_ESSENCE))
                .save(this.output,"auroran_processor");

        shaped(RecipeCategory.MISC,OMItems.ESSENCE_ELECTRONIC_CORE)
                .define('b', OMItems.INERT_BLUE_ESSENCE)
                .define('y', OMItems.INERT_YELLOW_ESSENCE)
                .define('g', OMItems.INERT_GREEN_ESSENCE)
                .define('r', OMItems.INERT_RED_ESSENCE)
                .define('c', OMItems.AURORAN_PROCESSOR)
                .define('i', Items.IRON_INGOT)
                .pattern("iyi")
                .pattern("rcg")
                .pattern("ibi")
                .unlockedBy("has_thingy",has(Tags.Items.INGOTS_IRON))
                .save(this.output,"essence_electronic_core");

        createInertOreSmeltingAndBlasting(OMItems.INERT_BLUE_ESSENCE_ORE.asItem(),OMItems.INERT_BLUE_ESSENCE.asItem());
        createInertOreSmeltingAndBlasting(OMItems.INERT_GREEN_ESSENCE_ORE.asItem(),OMItems.INERT_GREEN_ESSENCE.asItem());
        createInertOreSmeltingAndBlasting(OMItems.INERT_YELLOW_ESSENCE_ORE.asItem(),OMItems.INERT_YELLOW_ESSENCE.asItem());
        createInertOreSmeltingAndBlasting(OMItems.INERT_ORANGE_ESSENCE_ORE.asItem(),OMItems.INERT_ORANGE_ESSENCE.asItem());
        createInertOreSmeltingAndBlasting(OMItems.INERT_RED_ESSENCE_ORE.asItem(),OMItems.INERT_RED_ESSENCE.asItem());
        createInertOreSmeltingAndBlasting(OMItems.INERT_LIGHT_ESSENCE_ORE.asItem(),OMItems.INERT_LIGHT_ESSENCE.asItem());
        createInertOreSmeltingAndBlasting(OMItems.INERT_AURORAN_ESSENCE_ORE.asItem(),OMItems.INERT_AURORAN_ESSENCE.asItem());
        createInertOreSmeltingAndBlasting(OMItems.INERT_DARK_ESSENCE_ORE.asItem(),OMItems.INERT_DARK_ESSENCE.asItem());

        shaped(RecipeCategory.MISC,OMItems.MULTI_ASSEMBLER)
                .define('c', OMItems.ESSENCE_ELECTRONIC_CORE)
                .define('b', OMItems.NETHERITE_TOOL_BINDING)
                .define('i', Items.NETHERITE_INGOT)
                .define('n', Items.NETHERITE_BLOCK)
                .define('p',Items.POLISHED_BLACKSTONE_PRESSURE_PLATE)
                .pattern("ccc")
                .pattern("bpb")
                .pattern("ini")
                .unlockedBy("has_thingy",has(Tags.Items.INGOTS_NETHERITE))
                .save(this.output,"multi_assembler");

        // multi assembler recipes
        multiAssembler(
                Ingredient.of(OMItems.INERT_BLUE_ESSENCE),
                Ingredient.of(OMItems.INERT_YELLOW_ESSENCE),
                Ingredient.of(OMItems.INERT_GREEN_ESSENCE),
                Ingredient.of(OMItems.INERT_RED_ESSENCE),
                Ingredient.of(OMItems.AURORAN_PROCESSOR),
                Ingredient.of(Items.IRON_INGOT),
                Ingredient.of(Items.IRON_INGOT),
                2,
                new ItemStack(OMItems.ESSENCE_ELECTRONIC_CORE.asItem(),2),
                OMItems.AURORAN_PROCESSOR.asItem()
        );

        multiAssembler(
                Ingredient.of(items.getOrThrow(OMTags.INERT_ESSENCES)),
                Ingredient.of(items.getOrThrow(OMTags.INERT_ESSENCES)),
                Ingredient.of(items.getOrThrow(OMTags.INERT_ESSENCES)),
                Ingredient.of(items.getOrThrow(OMTags.INERT_ESSENCES)),
                Ingredient.of(items.getOrThrow(OMTags.INERT_ESSENCES)),
                Ingredient.of(items.getOrThrow(Tags.Items.DYES)),
                Ingredient.of(Items.ENDER_PEARL),
                80,
                new ItemStack(OMItems.CONCENTRATED_MULTI_ESSENCE.asItem(),1),
                Items.ENDER_PEARL.asItem()
        );

        multiAssembler(
                Ingredient.of(OMItems.CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.INERT_DARK_ESSENCE),
                Ingredient.of(OMItems.INERT_LIGHT_ESSENCE),
                Ingredient.of(OMItems.INERT_AURORAN_ESSENCE),
                Ingredient.of(Items.NETHERITE_SWORD),
                Ingredient.of(Items.IRON_INGOT),
                320,
                new ItemStack(OMItems.PENULTIMATE_SWORD_CATALYST.asItem(),1),
                OMItems.CONCENTRATED_MULTI_ESSENCE.asItem()
        );

        multiAssembler(
                Ingredient.of(Items.GLOWSTONE),
                Ingredient.of(Items.GLOWSTONE),
                Ingredient.of(Items.GLOWSTONE),
                Ingredient.of(OMItems.INERT_LIGHT_ESSENCE),
                Ingredient.of(OMItems.INERT_LIGHT_ESSENCE),
                Ingredient.of(OMItems.PENULTIMATE_SWORD_CATALYST),
                Ingredient.of(Items.TORCHFLOWER),
                720,
                new ItemStack(OMItems.PENULTIMATE_SWORD_LIGHT.asItem(),1),
                OMItems.PENULTIMATE_SWORD_CATALYST.asItem()
        );

        multiAssembler(
                Ingredient.of(Items.GILDED_BLACKSTONE),
                Ingredient.of(Items.GILDED_BLACKSTONE),
                Ingredient.of(Items.GILDED_BLACKSTONE),
                Ingredient.of(OMItems.INERT_DARK_ESSENCE),
                Ingredient.of(OMItems.INERT_DARK_ESSENCE),
                Ingredient.of(OMItems.PENULTIMATE_SWORD_CATALYST),
                Ingredient.of(Items.NETHER_STAR),
                720,
                new ItemStack(OMItems.PENULTIMATE_SWORD_DARK.asItem(),1),
                OMItems.PENULTIMATE_SWORD_CATALYST.asItem()
        );

        multiAssembler(
                Ingredient.of(Items.NETHERITE_BLOCK),
                Ingredient.of(Items.SHULKER_BOX),
                Ingredient.of(Items.OMINOUS_BOTTLE),
                Ingredient.of(OMItems.CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.PENULTIMATE_SWORD_LIGHT),
                Ingredient.of(OMItems.PENULTIMATE_SWORD_DARK),
                Ingredient.of(OMItems.ULTIMATE_INGOT),
                1200,
                new ItemStack(OMItems.ULTIMATE_SWORD.asItem(),1),
                OMItems.CONCENTRATED_MULTI_ESSENCE.asItem()
        );

        multiAssembler(
                Ingredient.of(OMItems.STRANGE_STONE),
                Ingredient.of(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE),
                Ingredient.of(Items.NETHERITE_INGOT),
                Ingredient.of(Items.NETHERITE_INGOT),
                900,
                new ItemStack(OMItems.ULTIMATE_INGOT.asItem(),1),
                OMItems.INFUSED_CONCENTRATED_MULTI_ESSENCE.asItem()
        );
    }

    public void multiAssembler(Ingredient firstEssence,
                               Ingredient secondEssence,
                               Ingredient thirdEssence,
                               Ingredient fourthEssence,
                               Ingredient extraEssence,
                               Ingredient craftingItem,
                               Ingredient additiveItem,
                               int assemblyTime,
                               ItemStack resultItem, Item unlockItem){
        MultiAssemblerRecipeBuilder builder = new MultiAssemblerRecipeBuilder(
                firstEssence,secondEssence,thirdEssence,fourthEssence,extraEssence,craftingItem,additiveItem,assemblyTime,resultItem
        );
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(getItemName(resultItem.getItem()));
        nameBuilder.append("_from_multi_assembler");
        builder.unlockedBy("has_thingy",has(unlockItem));
        builder.save(this.output,nameBuilder.toString());
    }

    public void multiAssemblerCustom(Ingredient firstEssence,
                               Ingredient secondEssence,
                               Ingredient thirdEssence,
                               Ingredient fourthEssence,
                               Ingredient extraEssence,
                               Ingredient craftingItem,
                               Ingredient additiveItem,
                               int assemblyTime,
                               ItemStack resultItem, Item unlockItem, String customName){
        MultiAssemblerRecipeBuilder builder = new MultiAssemblerRecipeBuilder(
                firstEssence,secondEssence,thirdEssence,fourthEssence,extraEssence,craftingItem,additiveItem,assemblyTime,resultItem
        );
        builder.unlockedBy("has_thingy",has(unlockItem));
        builder.save(this.output,customName);
    }

    public void multiAssembler(Ingredient firstEssence,
                               Ingredient secondEssence,
                               Ingredient thirdEssence,
                               Ingredient fourthEssence,
                               Ingredient extraEssence,
                               Ingredient craftingItem,
                               Ingredient additiveItem,
                               int assemblyTime,
                               ItemStack resultItem, TagKey<Item> unlockTag){
        MultiAssemblerRecipeBuilder builder = new MultiAssemblerRecipeBuilder(
                firstEssence,secondEssence,thirdEssence,fourthEssence,extraEssence,craftingItem,additiveItem,assemblyTime,resultItem
        );
        StringBuilder nameBuilder = new StringBuilder();
        nameBuilder.append(getItemName(resultItem.getItem()));
        nameBuilder.append("_from_multi_assembler");
        builder.unlockedBy("has_thingy",has(unlockTag));
        builder.save(this.output,nameBuilder.toString());
    }

    public void createInertOreSmeltingAndBlasting(Item oreItem, Item resultItem){
        oreSmelting(List.of(oreItem),RecipeCategory.MISC,
                resultItem,
                1.0f,100,
                "inert_essence_smelting");
        oreBlasting(List.of(oreItem),RecipeCategory.MISC,
                resultItem,
                1.2f,50,
                "inert_essence_blasting");
    }

    public void createSpearRecipePair(String recipeId, Item spearToMake, TagKey<Item> spearHeadMaterials, Item toolBinding, Item heldEndItem){
        shaped(RecipeCategory.COMBAT,spearToMake)
                .define('m',spearHeadMaterials)
                .define('s',toolBinding)
                .define('i',heldEndItem)
                .pattern("m  ")
                .pattern(" s ")
                .pattern("  i")
                .unlockedBy("has_thingy",has(spearHeadMaterials))
                .save(this.output,recipeId);
        shaped(RecipeCategory.COMBAT,spearToMake)
                .define('m',spearHeadMaterials)
                .define('s',toolBinding)
                .define('i',heldEndItem)
                .pattern("  m")
                .pattern(" s ")
                .pattern("i  ")
                .unlockedBy("has_thingy",has(spearHeadMaterials))
                .save(this.output,(recipeId + "_alt"));
    }

    public static class RecipeRunner extends RecipeProvider.Runner{
        protected RecipeRunner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new OMRecipe(registries,output);
        }

        @Override
        public String getName() {
            return "Overpowered Mastery Recipes";
        }
    }
}
