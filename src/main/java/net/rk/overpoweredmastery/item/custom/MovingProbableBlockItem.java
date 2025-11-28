package net.rk.overpoweredmastery.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.rk.overpoweredmastery.block.OMBlocks;

import java.util.function.Consumer;

public class MovingProbableBlockItem extends BlockItem {
    public MovingProbableBlockItem(Properties properties) {
        super(OMBlocks.MOVING_PROBABLE_BLOCK.get(),properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("block.thingamajigsgoodies.moving_probable_block.desc")
                .withStyle(ChatFormatting.GOLD).withStyle(ChatFormatting.ITALIC));
    }
}
