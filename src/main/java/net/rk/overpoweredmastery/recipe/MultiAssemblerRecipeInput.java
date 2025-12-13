package net.rk.overpoweredmastery.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

// only 4 stacks of essences are allowed, plus an extra for any that may be added in the future or by other mods
// the crafting item is whatever item needs to be modified, and the additive item is an augment of some kind
public record MultiAssemblerRecipeInput(
        ItemStack essenceStack1,
        ItemStack essenceStack2,
        ItemStack essenceStack3,
        ItemStack essenceStack4,
        ItemStack extraEssence,
        ItemStack craftingItem,
        ItemStack additiveItem
) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        if(index < 0 || index > 6){
            throw new IllegalArgumentException("Multi Assembler Recipe Input Slot index does not exist: " + index);
        }
        switch(index){
            case 0: {
                return this.essenceStack1();
            }
            case 1: {
                return this.essenceStack2();
            }
            case 2: {
                return this.essenceStack3();
            }
            case 3: {
                return this.essenceStack4();
            }
            case 4: {
                return this.extraEssence();
            }
            case 5: {
                return this.craftingItem();
            }
            case 6: {
                return this.additiveItem();
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public int size() {
        return 7;
    }
}
