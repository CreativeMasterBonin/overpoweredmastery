package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class PenultimateHoeCatalyst extends AbstractPenultimateCatalyst{
    public PenultimateHoeCatalyst(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.penultimate_hoe_catalyst.desc")
                .withStyle(ChatFormatting.GRAY));
    }
}
