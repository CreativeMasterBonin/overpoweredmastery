package net.rk.overpoweredmastery;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue MOUSE_EARS_HAX = BUILDER
            .comment("Enable client-side mouse ears for your skin (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.mouse_ears_hax")
            .define("mouseEarsHax",false);

    static final ModConfigSpec SPEC = BUILDER.build();
}
