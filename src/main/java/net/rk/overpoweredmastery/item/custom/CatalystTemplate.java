package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class CatalystTemplate extends Item {
    public final String makesItemType;
    public final int requiresIngredientColor;

    public CatalystTemplate(Properties properties, String makesItemType, int requiresIngredientColor) {
        super(properties);
        this.makesItemType = makesItemType;
        this.requiresIngredientColor = requiresIngredientColor;
    }

    public Item getItem(){
        return Items.STONE;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        String translatedString = Component.translatable(getItem().getDescriptionId()).getString();
        String translatedMakesString = Component.translatable(makesItemType).getString();

        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.catalyst_template.requires_item_base",translatedString)
                .withColor(requiresIngredientColor));
        tooltipAdder.accept(Component.translatable("item.catalyst_template.overpoweredmastery.can_make",
                        translatedMakesString)
                .withStyle(ChatFormatting.YELLOW).withStyle(ChatFormatting.ITALIC));
    }
}
