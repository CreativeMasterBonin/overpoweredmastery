package net.rk.overpoweredmastery.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
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
        this.add(OMSoundEvents.RED_WUBS, SoundDefinition.definition().with(
                sound("overpoweredmastery:red_wubs")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(8)
                        .stream(false)
                        .preload(true)
        ).subtitle("overpoweredmastery.subtitle.red_wubs"));
        this.add(OMSoundEvents.GREEN_WUBS, SoundDefinition.definition().with(
                sound("overpoweredmastery:green_wubs")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(8)
                        .stream(false)
                        .preload(true)
        ).subtitle("overpoweredmastery.subtitle.green_wubs"));
        this.add(OMSoundEvents.PURPLE_WUBS, SoundDefinition.definition().with(
                sound("overpoweredmastery:purple_wubs")
                        .volume(1.0f)
                        .pitch(1.0f)
                        .attenuationDistance(8)
                        .stream(false)
                        .preload(true)
        ).subtitle("overpoweredmastery.subtitle.purple_wubs"));
    }
}
