package net.rk.overpoweredmastery.block;

import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

public class InertEssenceOre extends DropExperienceBlock {
    public InertEssenceOre(Properties properties) {
        super(ConstantInt.of(1),properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.CHIME)
                .requiresCorrectToolForDrops().strength(3.0f,15.0f));
    }
}
