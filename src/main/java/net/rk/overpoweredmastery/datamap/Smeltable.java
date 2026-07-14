package net.rk.overpoweredmastery.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

public record Smeltable(Block smeltInto) {
    public static final Codec<Smeltable> SMELTABLE_BLOCK_CODEC = BuiltInRegistries.BLOCK.byNameCodec().xmap(Smeltable::new, Smeltable::smeltInto);
    public static final Codec<Smeltable> CODEC = Codec.withAlternative(RecordCodecBuilder.create((inst) -> {
        return inst.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("smelt_into").forGetter(Smeltable::smeltInto)).apply(inst, Smeltable::new);
    }), SMELTABLE_BLOCK_CODEC);

    public Smeltable(Block smeltInto) {
        this.smeltInto = smeltInto;
    }

    public Block smeltInto() {
        return this.smeltInto;
    }
}
