package net.rk.overpoweredmastery.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class MultiAssemblerRecipeSerializer implements RecipeSerializer<MultiAssemblerRecipe>{
    public static final MapCodec<MultiAssemblerRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Ingredient.CODEC.fieldOf("firstEssence").forGetter(MultiAssemblerRecipe::getFirstEssence),
                    Ingredient.CODEC.fieldOf("secondEssence").forGetter(MultiAssemblerRecipe::getSecondEssence),
                    Ingredient.CODEC.fieldOf("thirdEssence").forGetter(MultiAssemblerRecipe::getThirdEssence),
                    Ingredient.CODEC.fieldOf("fourEssence").forGetter(MultiAssemblerRecipe::getFourEssence),
                    Ingredient.CODEC.fieldOf("extraEssence").forGetter(MultiAssemblerRecipe::getExtraEssence),
                    Ingredient.CODEC.fieldOf("craftingItem").forGetter(MultiAssemblerRecipe::getCraftingItem),
                    Ingredient.CODEC.fieldOf("additiveItem").forGetter(MultiAssemblerRecipe::getAdditiveItem),
                    Codec.INT.fieldOf("assemblyTimeTicks").orElse(20).forGetter(MultiAssemblerRecipe::getAssemblyTimeTicks),
                    ItemStack.CODEC.fieldOf("resultItem").forGetter(MultiAssemblerRecipe::getResultItem)
            ).apply(instance,MultiAssemblerRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf,MultiAssemblerRecipe> STREAM_CODEC =
            StreamCodec.of(MultiAssemblerRecipeSerializer::toNetwork,MultiAssemblerRecipeSerializer::fromNetwork);

    @Override
    public MapCodec<MultiAssemblerRecipe> codec() {
        return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, MultiAssemblerRecipe> streamCodec() {
        return STREAM_CODEC;
    }

    public static MultiAssemblerRecipe fromNetwork(RegistryFriendlyByteBuf buffer){
        Ingredient essence1 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Ingredient essence2 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Ingredient essence3 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Ingredient essence4 = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Ingredient extraEssence = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Ingredient craftingItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        Ingredient additiveItem = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
        int assemblyTime = buffer.readInt();
        ItemStack resultItemStack = ItemStack.STREAM_CODEC.decode(buffer);
        return new MultiAssemblerRecipe(essence1,essence2,essence3,essence4,extraEssence,craftingItem,additiveItem,assemblyTime,resultItemStack);
    }

    public static void toNetwork(RegistryFriendlyByteBuf buffer, MultiAssemblerRecipe recipe){
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer,recipe.getFirstEssence());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer,recipe.getSecondEssence());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer,recipe.getThirdEssence());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer,recipe.getFourEssence());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer,recipe.getExtraEssence());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer,recipe.getCraftingItem());
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer,recipe.getAdditiveItem());
        buffer.writeInt(recipe.getAssemblyTimeTicks());
        ItemStack.STREAM_CODEC.encode(buffer,recipe.getResultItem());
    }
}
