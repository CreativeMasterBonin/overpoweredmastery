package net.rk.overpoweredmastery.item;

import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.rk.overpoweredmastery.util.OPUtil;

import java.util.function.UnaryOperator;

public class OMRarity{
    public static final EnumProxy<Rarity> ULTIMATE = new EnumProxy<>(
            Rarity.class, 4, "overpoweredmastery:ultimate", (UnaryOperator<Style>) style -> style.withColor(OPUtil.ULTIMATE_COLOR)
    );
    public static final EnumProxy<Rarity> ULTRA = new EnumProxy<>(
            Rarity.class, 5, "overpoweredmastery:ultra", (UnaryOperator<Style>) style -> style.withColor(OPUtil.ULTRA_COLOR)
    );
}
