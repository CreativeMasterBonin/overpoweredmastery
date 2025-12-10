package net.rk.overpoweredmastery;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue CHICKEN_WUBS_DAMAGE_BASE_CHICKEN = BUILDER
            .comment("The base damage the Chicken Wub does to Chickens (effects projectile)")
            .defineInRange("chicken_wub_damage_chickens", 20, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue CHICKEN_WUBS_DAMAGE_BASE_BABY_ZOMBIE = BUILDER
            .comment("The base damage the Chicken Wub does to Baby Zombies (effects projectile)")
            .defineInRange("chicken_wub_damage_baby_zombies", 10, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue CHICKEN_WUBS_DAMAGE_BASE_IS_CHICKEN_JOCKEY = BUILDER
            .comment("The base damage the Chicken Wub does to 'Chicken Jockeys' (effects projectile)")
            .defineInRange("chicken_wub_damage_chicken_jockeys", 30, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue PHASE_BLOCK_EXISTENCE_TIME = BUILDER
            .comment("The time in ticks that selection blocks can exist for")
            .defineInRange("phase_block_existence_time", 140, 60, 380);

    static final ModConfigSpec SPEC = BUILDER.build();
}
