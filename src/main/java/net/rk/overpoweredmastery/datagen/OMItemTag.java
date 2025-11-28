package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.item.OMItems;

import java.util.concurrent.CompletableFuture;

public class OMItemTag extends ItemTagsProvider {
    public OMItemTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider,OverpoweredMastery.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(OMTags.MUSIC_DISC_WUBS)
                .add(OMItems.RED_WUBS.asItem())
                .add(OMItems.GREEN_WUBS.asItem())
                .add(OMItems.PURPLE_WUBS.asItem())
                .add(OMItems.CHICKEN_WUBS.asItem())
        ;
    }
}
