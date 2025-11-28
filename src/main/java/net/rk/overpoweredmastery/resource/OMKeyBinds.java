package net.rk.overpoweredmastery.resource;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.rk.overpoweredmastery.OverpoweredMastery;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = OverpoweredMastery.MODID, value = Dist.CLIENT)
public class OMKeyBinds {
    public static final KeyMapping VEHICLE_FIRE = new KeyMapping(
                    "key.overpoweredmastery.vehicle_fire",
                    KeyConflictContext.IN_GAME, // where it will be used and allowed
                    KeyModifier.NONE, // default key extra to use mapping
                    InputConstants.Type.KEYSYM, // default mapping interface
                    GLFW.GLFW_KEY_APOSTROPHE, // default key to press
                    "key.categories.misc"
    );

    public static final KeyMapping VEHICLE_DRILL = new KeyMapping(
                    "key.overpoweredmastery.vehicle_drill",
                    KeyConflictContext.IN_GAME, // where it will be used and allowed
                    KeyModifier.NONE, // default key extra to use mapping
                    InputConstants.Type.KEYSYM, // default mapping interface
                    GLFW.GLFW_KEY_SEMICOLON, // default key to press
                    "key.categories.misc"
    );

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event){
        //event.register(VEHICLE_FIRE);
        //event.register(VEHICLE_DRILL);
    }
}
