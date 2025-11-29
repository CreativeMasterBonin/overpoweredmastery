package net.rk.overpoweredmastery.item;

import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class BoneSword extends Item {
    public BoneSword(Properties properties) {
        super(properties);
    }

    public static Tool createToolProperties(){
        return new Tool(
                List.of(
                        Tool.Rule.minesAndDrops(HolderSet.direct(Blocks.COBWEB.builtInRegistryHolder()), 20.0f)),
                0.45f,
                3,
                false
        );
    }
}
