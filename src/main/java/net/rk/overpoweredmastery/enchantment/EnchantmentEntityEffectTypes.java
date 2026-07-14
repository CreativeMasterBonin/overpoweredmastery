package net.rk.overpoweredmastery.enchantment;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rk.overpoweredmastery.OverpoweredMastery;

public class EnchantmentEntityEffectTypes{
    public static final DeferredRegister<MapCodec<? extends EnchantmentEntityEffect>> ENCHANTMENT_ENTITY_EFFECTS =
            DeferredRegister.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE,OverpoweredMastery.MODID);

    public static final DeferredHolder<MapCodec<? extends EnchantmentEntityEffect>,MapCodec<EvocationMasterEffect>> EVOCATION_MASTER_EFFECT =
            ENCHANTMENT_ENTITY_EFFECTS.register("evocation_master_effect", () -> EvocationMasterEffect.CODEC);
}
