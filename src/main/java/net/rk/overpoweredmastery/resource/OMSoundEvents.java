package net.rk.overpoweredmastery.resource;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.rk.overpoweredmastery.OverpoweredMastery;

import java.util.function.Supplier;

public class OMSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, OverpoweredMastery.MODID);


    public static final Supplier<SoundEvent> RED_WUBS = SOUND_EVENTS.register("red_wubs",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID, "red_wubs")));
    public static final Supplier<SoundEvent> GREEN_WUBS = SOUND_EVENTS.register("green_wubs",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID, "green_wubs")));
    public static final Supplier<SoundEvent> PURPLE_WUBS = SOUND_EVENTS.register("purple_wubs",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID, "purple_wubs")));

    public static final Supplier<SoundEvent> CHICKEN_WUBS = SOUND_EVENTS.register("chicken_wubs",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(OverpoweredMastery.MODID, "chicken_wubs")));
}
