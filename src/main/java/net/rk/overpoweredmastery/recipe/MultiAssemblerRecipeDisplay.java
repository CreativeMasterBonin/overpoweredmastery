package net.rk.overpoweredmastery.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.rk.overpoweredmastery.OverpoweredMastery;

public record MultiAssemblerRecipeDisplay(
        SlotDisplay essenceStack1, // first item
        SlotDisplay essenceStack2,
        SlotDisplay essenceStack3,
        SlotDisplay essenceStack4,
        SlotDisplay extraEssence,
        SlotDisplay craftingItem,
        SlotDisplay additiveItem, // last item
        SlotDisplay resultItem, // recipe display result
        SlotDisplay assembler // recipe display crafting station
) implements RecipeDisplay {
    public static final MapCodec<MultiAssemblerRecipeDisplay> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                SlotDisplay.CODEC.fieldOf("essenceStack1").forGetter(MultiAssemblerRecipeDisplay::essenceStack1),
                    SlotDisplay.CODEC.fieldOf("essenceStack2").forGetter(MultiAssemblerRecipeDisplay::essenceStack2),
                    SlotDisplay.CODEC.fieldOf("essenceStack3").forGetter(MultiAssemblerRecipeDisplay::essenceStack3),
                    SlotDisplay.CODEC.fieldOf("essenceStack4").forGetter(MultiAssemblerRecipeDisplay::essenceStack4),
                    SlotDisplay.CODEC.fieldOf("extraEssence").forGetter(MultiAssemblerRecipeDisplay::extraEssence),
                    SlotDisplay.CODEC.fieldOf("craftingItem").forGetter(MultiAssemblerRecipeDisplay::craftingItem),
                    SlotDisplay.CODEC.fieldOf("additiveItem").forGetter(MultiAssemblerRecipeDisplay::additiveItem),
                    SlotDisplay.CODEC.fieldOf("resultItem").forGetter(MultiAssemblerRecipeDisplay::resultItem),
                    SlotDisplay.CODEC.fieldOf("assembler").forGetter(MultiAssemblerRecipeDisplay::assembler)
            ).apply(instance,MultiAssemblerRecipeDisplay::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf,MultiAssemblerRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
            SlotDisplay.STREAM_CODEC,
            MultiAssemblerRecipeDisplay::essenceStack1,
            SlotDisplay.STREAM_CODEC,
            MultiAssemblerRecipeDisplay::essenceStack2,
            SlotDisplay.STREAM_CODEC,
            MultiAssemblerRecipeDisplay::essenceStack3,
            SlotDisplay.STREAM_CODEC,
            MultiAssemblerRecipeDisplay::essenceStack4,
            SlotDisplay.STREAM_CODEC,
            MultiAssemblerRecipeDisplay::extraEssence,
            SlotDisplay.STREAM_CODEC,
            MultiAssemblerRecipeDisplay::craftingItem,
            SlotDisplay.STREAM_CODEC,
            MultiAssemblerRecipeDisplay::additiveItem,
            SlotDisplay.STREAM_CODEC,
            MultiAssemblerRecipeDisplay::resultItem,
            SlotDisplay.STREAM_CODEC,
            MultiAssemblerRecipeDisplay::assembler,
            MultiAssemblerRecipeDisplay::new
    );

    @Override
    public SlotDisplay result() {
        return this.resultItem;
    }

    @Override
    public SlotDisplay craftingStation() {
        return this.assembler;
    }

    @Override
    public Type<? extends RecipeDisplay> type() {
        return OverpoweredMastery.MULTI_ASSEMBLER_RECIPE_DISPLAY.get();
    }
}
