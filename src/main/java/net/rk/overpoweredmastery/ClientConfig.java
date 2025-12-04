package net.rk.overpoweredmastery;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue MOUSE_EARS_HAX = BUILDER
            .comment("Enable client-side mouse ears for your skin (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.mouse_ears_hax")
            .define("mouseEarsHax",false);

    public static final ModConfigSpec.BooleanValue UPSIDE_DOWN_HAX = BUILDER
            .comment("Enable client-side upside down model (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.upside_down_hax")
            .define("upsideDownHax",false);

    public static final ModConfigSpec.DoubleValue UPSIDE_DOWN_HAX_Y_OFFSET = BUILDER
            .comment("The Y-Offset for the player model when upside down (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.upside_down_hax_y_offset")
            .defineInRange("upsideDownHaxYOffset",3.75D,-32.0D,32.0D);

    static final ModConfigSpec SPEC = BUILDER.build();
}
