package net.rk.overpoweredmastery.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.rk.overpoweredmastery.OverpoweredMastery;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = OverpoweredMastery.MODID)
public class DataGen{
    @SubscribeEvent
    public static void gatherDataClient(GatherDataEvent.Client event){
        DataGenerator gen = event.getGenerator();
        PackOutput packOutput = gen.getPackOutput();

        gen.addProvider(true,new OMSound(packOutput));
        gen.addProvider(true,new OMLang(packOutput));
        gen.addProvider(true,new OMModels(packOutput));
        gen.addProvider(true,new LootTableProvider(packOutput,Set.of(),
                List.of(new LootTableProvider.SubProviderEntry(OMLoot::new,LootContextParamSets.BLOCK)),event.getLookupProvider()));
        gen.addProvider(true,new OMEntityTag(packOutput,event.getLookupProvider()));
        gen.addProvider(true,new OMItemTag(packOutput,event.getLookupProvider()));
        gen.addProvider(true,new OMBlockTag(packOutput,event.getLookupProvider()));
        gen.addProvider(true,new OMRecipe.RecipeRunner(packOutput,event.getLookupProvider()));
    }
}
