package net.rk.overpoweredmastery.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.rk.overpoweredmastery.OverpoweredMastery;
import net.rk.overpoweredmastery.resource.OMSoundEvents;

public class OMSound extends SoundDefinitionsProvider {
    protected OMSound(PackOutput output) {
        super(output,OverpoweredMastery.MODID);
    }

    @Override
    public void registerSounds() {
        // music
        this.add(OMSoundEvents.RED_WUBS.get(), SoundDefinition.definition().with(
                sound("overpoweredmastery:red_wubs")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(8)
                        .stream(false)
                        .preload(true)
        ).subtitle("overpoweredmastery.subtitle.red_wubs"));
        this.add(OMSoundEvents.GREEN_WUBS.get(), SoundDefinition.definition().with(
                sound("overpoweredmastery:green_wubs")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(8)
                        .stream(false)
                        .preload(true)
        ).subtitle("overpoweredmastery.subtitle.green_wubs"));
        this.add(OMSoundEvents.PURPLE_WUBS.get(), SoundDefinition.definition().with(
                sound("overpoweredmastery:purple_wubs")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(8)
                        .stream(false)
                        .preload(true)
        ).subtitle("overpoweredmastery.subtitle.purple_wubs"));

        // fx
        this.add(OMSoundEvents.EFFECT.get(), SoundDefinition.definition().with(
                sound("overpoweredmastery:general/effect")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(16)
                        .stream(false)
                        .preload(true)
        ).subtitle("overpoweredmastery.subtitle.effect"));
    }
}
