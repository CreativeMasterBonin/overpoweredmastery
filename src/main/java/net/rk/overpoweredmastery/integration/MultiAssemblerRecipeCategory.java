package net.rk.overpoweredmastery.integration;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.rk.overpoweredmastery.recipe.MultiAssemblerRecipe;
import org.jetbrains.annotations.Nullable;

public class MultiAssemblerRecipeCategory implements IRecipeCategory<RecipeHolder<MultiAssemblerRecipe>> {
    @Override
    public IRecipeType<RecipeHolder<MultiAssemblerRecipe>> getRecipeType() {
        return null;
    }

    @Override
    public Component getTitle() {
        return null;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return null;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<MultiAssemblerRecipe> recipe, IFocusGroup focuses) {

    }
}
