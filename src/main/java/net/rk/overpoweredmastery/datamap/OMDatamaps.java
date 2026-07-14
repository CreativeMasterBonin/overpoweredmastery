package net.rk.overpoweredmastery.datamap;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.rk.overpoweredmastery.OverpoweredMastery;

public class OMDatamaps {
    public static final DataMapType<Block, Smeltable> SMELTABLES = DataMapType.builder(
            datamapId("smeltables"),Registries.BLOCK,Smeltable.CODEC).synced(Smeltable.SMELTABLE_BLOCK_CODEC, false).build();

    public static Identifier datamapId(final String name){
        return Identifier.fromNamespaceAndPath(OverpoweredMastery.MODID,name);
    }
}
