package net.rk.overpoweredmastery.resource;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.rk.overpoweredmastery.OverpoweredMastery;

@EventBusSubscriber(modid = OverpoweredMastery.MODID, value = Dist.CLIENT)
public class OMKeyBinds {
    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event){

    }
}
