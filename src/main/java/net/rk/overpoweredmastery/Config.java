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

    public static final ModConfigSpec.DoubleValue STRANGE_STONE_DROP_CHANCE = BUILDER
            .comment("The percentage chance that a strange stone will drop from blocks tagged 'can_drop_strange_stone'.")
            .defineInRange("strange_stone_drop_percentage", 0.07D, 0.0D, 1.0D);

    // wubs lasting time
    public static final ModConfigSpec.IntValue RED_WUB_ENERGY_BALL_LIFETIME = BUILDER
            .comment("How long Red Wub Energy Balls Last")
            .defineInRange("red_wub_energy_ball_lifetime", 240, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue CHICKEN_WUB_ENERGY_BALL_LIFETIME = BUILDER
            .comment("How long Chicken Wub Energy Balls Last")
            .defineInRange("chicken_wub_energy_ball_lifetime", 70, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue GREEN_WUB_ENERGY_BALL_LIFETIME = BUILDER
            .comment("How long Green Wub Energy Balls Last")
            .defineInRange("green_wub_energy_ball_lifetime", 150, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue WEEPING_WUB_ENERGY_BALL_LIFETIME = BUILDER
            .comment("How long Weeping Wub Energy Balls Last")
            .defineInRange("weeping_wub_energy_ball_lifetime", 200, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue TRIAL_WUB_ENERGY_BALL_LIFETIME = BUILDER
            .comment("How long Trial Wub Energy Balls Last")
            .defineInRange("trial_wub_energy_ball_lifetime", 120, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue PURPLE_WUB_ENERGY_BALL_LIFETIME = BUILDER
            .comment("How long Purple Wub Energy Balls Last")
            .defineInRange("purple_wub_energy_ball_lifetime", 100, 1, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();
}
