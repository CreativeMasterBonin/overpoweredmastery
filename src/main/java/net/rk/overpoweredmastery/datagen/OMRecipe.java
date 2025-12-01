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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.rk.overpoweredmastery.item.OMItems;

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
                .define('d', Items.MUSIC_DISC_CHIRP)
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
                .define('d', Items.MUSIC_DISC_CAT)
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
                .define('d', Items.MUSIC_DISC_MALL)
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
