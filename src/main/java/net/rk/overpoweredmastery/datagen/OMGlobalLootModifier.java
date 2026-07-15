package net.rk.overpoweredmastery.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;
import net.rk.overpoweredmastery.OverpoweredMastery;

import java.util.concurrent.CompletableFuture;

public class OMGlobalLootModifier extends GlobalLootModifierProvider {
    public OMGlobalLootModifier(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, OverpoweredMastery.MODID);
    }

    @Override
    public void start() {
        add("ultra_catalyst_template_in_ancient_city_ice_box",
                new AddTableLootModifier(new LootItemCondition[] {
                        LootTableIdCondition.builder(BuiltInLootTables.ANCIENT_CITY_ICE_BOX.identifier()).build()
                },ResourceKey.create(Registries.LOOT_TABLE,Identifier.parse("overpoweredmastery:add_ultra_catalyst_template"))));
    }
}
