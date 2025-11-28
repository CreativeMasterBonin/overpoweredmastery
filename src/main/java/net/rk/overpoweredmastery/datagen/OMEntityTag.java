package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.entity.OMEntityTypes;

import java.util.concurrent.CompletableFuture;

public class OMEntityTag extends EntityTypeTagsProvider {
    public OMEntityTag(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, OverpoweredMastery.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        /*this.tag(EntityTypeTags.DEFLECTS_PROJECTILES)
                .add(OMEntityTypes.MULTIPURPOSE_VEHICLE.get())
        ;
        this.tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER)
                .add(OMEntityTypes.MULTIPURPOSE_VEHICLE.get())
        ;
        this.tag(EntityTypeTags.IMMUNE_TO_INFESTED)
                .add(OMEntityTypes.MULTIPURPOSE_VEHICLE.get())
        ;
        this.tag(EntityTypeTags.IMMUNE_TO_OOZING)
                .add(OMEntityTypes.MULTIPURPOSE_VEHICLE.get())
        ;
        this.tag(EntityTypeTags.FALL_DAMAGE_IMMUNE)
                .add(OMEntityTypes.MULTIPURPOSE_VEHICLE.get())
        ;*/
    }
}
