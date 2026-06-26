package net.rk.overpoweredmastery.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public abstract class AbstractPenultimateCatalyst extends Item {
    public AbstractPenultimateCatalyst(Properties properties) {
        super(properties.stacksTo(4).fireResistant().rarity(Rarity.EPIC));
    }
}
