package net.rk.overpoweredmastery.item.custom;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

/**
 * Item class with explicit resource key definition
 */
public class ItemWithResourceKey extends Item {
    public ItemWithResourceKey(Properties properties, ResourceKey key) {
        super(properties.setId(key));
    }
}
