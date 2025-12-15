package net.rk.overpoweredmastery.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.item.OMItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MultiAssemblerRecipe implements Recipe<MultiAssemblerRecipeInput>{
    public MultiAssemblerRecipe(Ingredient firstEssence, Ingredient secondEssence, Ingredient thirdEssence, Ingredient fourEssence, Ingredient extraEssence, Ingredient craftingItem, Ingredient additiveItem, int assemblyTimeTicks, ItemStack result){
        this.firstEssence = firstEssence;
        this.secondEssence = secondEssence;
        this.thirdEssence = thirdEssence;
        this.fourEssence = fourEssence;
        this.extraEssence = extraEssence;
        this.craftingItem = craftingItem;
        this.additiveItem = additiveItem;
        this.assemblyTimeTicks = assemblyTimeTicks;
        this.resultItem = result;
    }
    // essences
    private Ingredient firstEssence;
    private Ingredient secondEssence;
    private Ingredient thirdEssence;
    private Ingredient fourEssence;
    // xtra items
    private Ingredient extraEssence;
    private Ingredient craftingItem;
    private Ingredient additiveItem;
    // time to process and result item to make
    private int assemblyTimeTicks;
    private ItemStack resultItem;

    private PlacementInfo placementInfo;

    @Override
    public boolean matches(MultiAssemblerRecipeInput input, Level level){
        return this.firstEssence.test(input.essenceStack1()) &&
                        this.secondEssence.test(input.essenceStack2()) &&
                        this.thirdEssence.test(input.essenceStack3()) &&
                        this.fourEssence.test(input.essenceStack4()) &&
                        this.extraEssence.test(input.extraEssence()) &&
                        this.craftingItem.test(input.craftingItem()) &&
                        this.additiveItem.test(input.additiveItem());
    }

    @Override
    public ItemStack assemble(MultiAssemblerRecipeInput input, HolderLookup.Provider registries) {
        return this.resultItem.copy();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public List<RecipeDisplay> display() {
        return List.of(
                new MultiAssemblerRecipeDisplay(
                        this.firstEssence.display(),
                        this.secondEssence.display(),
                        this.thirdEssence.display(),
                        this.fourEssence.display(),
                        this.extraEssence.display(),
                        this.craftingItem.display(),
                        this.additiveItem.display(),
                        new SlotDisplay.ItemStackSlotDisplay(this.resultItem),
                        new SlotDisplay.ItemStackSlotDisplay(OMItems.AURORAN_PROCESSOR.toStack())
                )
        );
    }

    @Override
    public RecipeSerializer<? extends Recipe<MultiAssemblerRecipeInput>> getSerializer() {
        return OverpoweredMastery.MULTI_ASSEMBLER_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends Recipe<MultiAssemblerRecipeInput>> getType() {
        return OverpoweredMastery.MULTI_ASSEMBLER_RECIPE.get();
    }

    @Override
    public PlacementInfo placementInfo() {
        if(this.placementInfo == null){
            // make a new list of ingredients
            List<Optional<Ingredient>> ingredients = new ArrayList<>();
            ingredients.add(Optional.of(this.firstEssence));
            ingredients.add(Optional.of(this.secondEssence));
            ingredients.add(Optional.of(this.thirdEssence));
            ingredients.add(Optional.of(this.fourEssence));
            ingredients.add(Optional.of(this.extraEssence));
            ingredients.add(Optional.of(this.craftingItem));
            ingredients.add(Optional.of(this.additiveItem));
            // make our placement info
            this.placementInfo = PlacementInfo.createFromOptionals(ingredients);
        }
        return this.placementInfo;
    }

    @Override
    public RecipeBookCategory recipeBookCategory() {
        return OverpoweredMastery.MULTI_ASSEMBLER_CATEGORY.get();
    }

    public Ingredient getFirstEssence() {
        return firstEssence;
    }

    public Ingredient getSecondEssence() {
        return secondEssence;
    }

    public Ingredient getThirdEssence() {
        return thirdEssence;
    }

    public Ingredient getFourEssence() {
        return fourEssence;
    }

    public Ingredient getExtraEssence() {
        return extraEssence;
    }

    public Ingredient getCraftingItem() {
        return craftingItem;
    }

    public Ingredient getAdditiveItem() {
        return additiveItem;
    }

    public ItemStack getResultItem() {
        return resultItem;
    }

    public int getAssemblyTimeTicks() {
        return assemblyTimeTicks;
    }
}
