package net.rk.overpoweredmastery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;

public class InertEssenceOre extends DropExperienceBlock{
    int redMaxNumber = 255;
    int greenMaxNumber = 255;
    int blueMaxNumber = 255;
    int redMinNumber = 0;
    int greenMinNumber = 0;
    int blueMinNumber = 0;
    boolean isDeep = false; // whether this ore is deepslate

    public InertEssenceOre(Properties properties) {
        super(ConstantInt.of(1),properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.CHIME)
                .requiresCorrectToolForDrops().strength(3.0f,15.0f));
    }

    public InertEssenceOre(int redMinNumber, int greenMinNumber, int blueMinNumber, int redMaxNumber, int greenMaxNumber, int blueMaxNumber, Properties properties) {
        super(ConstantInt.of(1),properties.mapColor(MapColor.STONE).instrument(NoteBlockInstrument.CHIME)
                .requiresCorrectToolForDrops().strength(3.0f,15.0f));
        this.redMinNumber = redMinNumber;
        this.greenMinNumber = greenMinNumber;
        this.blueMinNumber = blueMinNumber;
        this.redMaxNumber = redMaxNumber;
        this.greenMaxNumber = greenMaxNumber;
        this.blueMaxNumber = blueMaxNumber;
    }

    public InertEssenceOre(boolean isDeep, Properties properties) {
        super(ConstantInt.of(2),properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.CHIME)
                .requiresCorrectToolForDrops().strength(4.5f,20.0f).sound(SoundType.DEEPSLATE));
        this.isDeep = isDeep;
    }

    public InertEssenceOre(int redMinNumber, int greenMinNumber, int blueMinNumber, int redMaxNumber, int greenMaxNumber, int blueMaxNumber, boolean isDeep, Properties properties) {
        super(ConstantInt.of(2),properties.mapColor(MapColor.DEEPSLATE).instrument(NoteBlockInstrument.CHIME)
                .requiresCorrectToolForDrops().strength(4.5f,20.0f).sound(SoundType.DEEPSLATE));
        this.redMinNumber = redMinNumber;
        this.greenMinNumber = greenMinNumber;
        this.blueMinNumber = blueMinNumber;
        this.redMaxNumber = redMaxNumber;
        this.greenMaxNumber = greenMaxNumber;
        this.blueMaxNumber = blueMaxNumber;
        this.isDeep = isDeep;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid){
        if(level instanceof ServerLevel serverLevel){
            int allTheColors = ARGB.color(Mth.randomBetweenInclusive(
                            level.getRandom(), redMinNumber, redMaxNumber),
                    Mth.randomBetweenInclusive(level.getRandom(), greenMinNumber, greenMaxNumber),
                    Mth.randomBetweenInclusive(level.getRandom(), blueMinNumber, blueMaxNumber));
            serverLevel.sendParticles(
                    new DustParticleOptions(allTheColors,level.getRandom().triangle(0.51f,0.97f)),
                    pos.getX() + 0.0 - level.getRandom().triangle(-0.5f,0.5f),
                    pos.getY() + 0.5,
                    pos.getZ() + 0.0 - level.getRandom().triangle(-0.5f,0.5f),
                    this.isDeep ? 5 : 10,
                    0,0,0,0.25D);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }
}
