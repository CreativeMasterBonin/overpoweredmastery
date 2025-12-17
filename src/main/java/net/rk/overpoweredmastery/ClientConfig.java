package net.rk.overpoweredmastery;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue MOUSE_EARS_HAX = BUILDER
            .comment("Enable client-side mouse ears for your skin (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.mouse_ears_hax")
            .define("mouseEarsHax",false);

    public static final ModConfigSpec.ConfigValue<String> MOUSE_EARS_RL = BUILDER
            .comment("Mouse Ears Custom Texture Location (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.mouse_ears_hax_texture_location")
            .define("mouseEarsTextureLocation","minecraft:textures/entity/player/wide/steve.png");

    public static final ModConfigSpec.BooleanValue UPSIDE_DOWN_HAX = BUILDER
            .comment("Enable client-side upside down model (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.upside_down_hax")
            .define("upsideDownHax",false);

    public static final ModConfigSpec.DoubleValue UPSIDE_DOWN_HAX_Y_OFFSET = BUILDER
            .comment("The Y-Offset for the player model when upside down (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.upside_down_hax_y_offset")
            .defineInRange("upsideDownHaxYOffset",3.75D,-32.0D,32.0D);

    public static final ModConfigSpec.BooleanValue MINECART_ON_HEAD_HAX = BUILDER
            .comment("Enable client-side minecart on head (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.minecart_on_head_hax")
            .define("minecartOnHeadHax",false);

    public static final ModConfigSpec.DoubleValue MINECART_HAX_X_ROTATION = BUILDER
            .comment("The X Rotation for the head minecart (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.minecart_on_head_hax_x_rotation")
            .defineInRange("minecartOnHeadHaxXRot",0.0D,-180.0D,180.0D);

    public static final ModConfigSpec.DoubleValue MINECART_HAX_Y_ROTATION = BUILDER
            .comment("The Y Rotation for the head minecart (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.minecart_on_head_hax_y_rotation")
            .defineInRange("minecartOnHeadHaxYRot",0.0D,-180.0D,180.0D);

    public static final ModConfigSpec.DoubleValue MINECART_HAX_Z_ROTATION = BUILDER
            .comment("The Z Rotation for the head minecart (very janky and experimental)")
            .translation("overpoweredmastery.configuration.client.minecart_on_head_hax_z_rotation")
            .defineInRange("minecartOnHeadHaxZRot",0.0D,-180.0D,180.0D);

    static final ModConfigSpec SPEC = BUILDER.build();
}
