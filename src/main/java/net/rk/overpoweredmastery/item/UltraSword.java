package net.rk.overpoweredmastery.item;

import net.minecraft.world.item.Item;

public class UltraSword extends Item {
    public UltraSword(Properties properties) {
        super(properties.stacksTo(1).durability(99999).repairable(OMItems.ULTRA_INGOT.asItem())
                .rarity(OMRarity.ULTRA.getValue()));
    }
}
