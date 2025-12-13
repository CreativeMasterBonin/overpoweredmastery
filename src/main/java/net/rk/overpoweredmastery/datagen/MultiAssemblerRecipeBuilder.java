package net.rk.overpoweredmastery.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.rk.overpoweredmastery.recipe.MultiAssemblerRecipe;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class MultiAssemblerRecipeBuilder implements RecipeBuilder {
    private final Ingredient firstEssence;
    private final Ingredient secondEssence;
    private final Ingredient thirdEssence;
    private final Ingredient fourthEssence;
    private final Ingredient extraEssence;
    private final Ingredient craftingItem;
    private final Ingredient additiveItem;
    protected final int assemblyTime;
    protected final ItemStack RESULT;
    protected final Map<String,Criterion<?>> CRITERION = new LinkedHashMap<>();
    protected String GROUP;

    public MultiAssemblerRecipeBuilder(Ingredient firstEssence,
                                       Ingredient secondEssence,
                                       Ingredient thirdEssence,
                                       Ingredient fourthEssence,
                                       Ingredient extraEssence,
                                       Ingredient craftingItem,
                                       Ingredient additiveItem,
                                       int assemblyTime,
                                       ItemStack resultItem){
        this.firstEssence = firstEssence;
        this.secondEssence = secondEssence;
        this.thirdEssence = thirdEssence;
        this.fourthEssence = fourthEssence;
        this.extraEssence = extraEssence;
        this.craftingItem = craftingItem;
        this.additiveItem = additiveItem;
        this.assemblyTime = assemblyTime;
        this.RESULT = resultItem;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        this.CRITERION.put(name,criterion);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        this.GROUP = groupName;
        return this;
    }

    @Override
    public Item getResult() {
        return this.RESULT.getItem();
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> resourceKey) {
        Advancement.Builder advancementBuilder = output.advancement()
                .addCriterion("has_recipe", RecipeUnlockedTrigger.unlocked(resourceKey))
                .rewards(AdvancementRewards.Builder.recipe(resourceKey))
                .requirements(AdvancementRequirements.Strategy.OR);
        this.CRITERION.forEach(advancementBuilder::addCriterion);
        MultiAssemblerRecipe recipe = new MultiAssemblerRecipe(
                this.firstEssence,
                this.secondEssence,
                this.thirdEssence,
                this.fourthEssence,
                this.extraEssence,
                this.craftingItem,
                this.additiveItem,
                this.assemblyTime,
                this.RESULT
        );
        output.accept(resourceKey,recipe,advancementBuilder.build(resourceKey.location().withPrefix("recipes/")));
    }
}
