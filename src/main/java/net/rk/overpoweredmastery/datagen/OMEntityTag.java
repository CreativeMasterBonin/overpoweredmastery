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
        this.tag(EntityTypeTags.IMPACT_PROJECTILES)
                .add(OMEntityTypes.CHICKEN_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.RED_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.GREEN_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.PURPLE_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.TRIAL_WUB_ENERGY_BALL.get())
                .add(OMEntityTypes.NETHER_WUB_ENERGY_BALL.get())
        ;
    }
}
