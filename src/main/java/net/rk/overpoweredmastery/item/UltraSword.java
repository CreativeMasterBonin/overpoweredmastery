package net.rk.overpoweredmastery.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.function.Consumer;

public class UltraSword extends Item {
    public UltraSword(Properties properties) {
        super(properties.stacksTo(1).durability(99999).repairable(OMItems.ULTRA_INGOT.asItem())
                .rarity(OMRarity.ULTRA.getValue()));
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.translatable("item.overpoweredmastery.ultra_sword.desc")
                .withColor(OPUtil.ULTRA_COLOR));
    }
}
