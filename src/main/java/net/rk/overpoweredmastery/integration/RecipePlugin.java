package net.rk.overpoweredmastery.integration;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.rk.overpoweredmastery.OverpoweredMastery;

@JeiPlugin
public class RecipePlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID,"jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {

    }
}
