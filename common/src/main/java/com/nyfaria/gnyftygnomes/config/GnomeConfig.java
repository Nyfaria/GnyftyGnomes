package com.nyfaria.gnyftygnomes.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class GnomeConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.DoubleValue WARRIOR_HEALTH;
    public static final ModConfigSpec.DoubleValue WARRIOR_ATTACK_DAMAGE;
    public static final ModConfigSpec.DoubleValue WARRIOR_SPEED;
    public static final ModConfigSpec.IntValue WARRIOR_ATTACK_INTERVAL;

    public static final ModConfigSpec.DoubleValue ARCHER_HEALTH;
    public static final ModConfigSpec.DoubleValue ARCHER_SPEED;
    public static final ModConfigSpec.IntValue ARCHER_ATTACK_INTERVAL;
    public static final ModConfigSpec.DoubleValue ARCHER_ATTACK_RADIUS;
    public static final ModConfigSpec.DoubleValue ARCHER_STRAFE_DISTANCE;

    public static final ModConfigSpec.DoubleValue HEALER_HEALTH;
    public static final ModConfigSpec.DoubleValue HEALER_SPEED;
    public static final ModConfigSpec.IntValue HEALER_THROW_INTERVAL;
    public static final ModConfigSpec.DoubleValue HEALER_HEAL_RADIUS;

    public static final ModConfigSpec.DoubleValue FOLLOW_RANGE;
    public static final ModConfigSpec.DoubleValue FOLLOW_STOP_DISTANCE;
    public static final ModConfigSpec.DoubleValue FOLLOW_TELEPORT_DISTANCE;

    public static final ModConfigSpec.IntValue HEALER_BLOCK_INTERVAL;
    public static final ModConfigSpec.IntValue HEALER_BLOCK_RADIUS;
    public static final ModConfigSpec.IntValue WARRIOR_BLOCK_INTERVAL;
    public static final ModConfigSpec.DoubleValue WARRIOR_BLOCK_RADIUS;
    public static final ModConfigSpec.IntValue WARRIOR_BLOCK_STRENGTH_AMPLIFIER;
    public static final ModConfigSpec.IntValue WARRIOR_BLOCK_STRENGTH_DURATION;
    public static final ModConfigSpec.IntValue ARCHER_BLOCK_INTERVAL;
    public static final ModConfigSpec.DoubleValue ARCHER_BLOCK_RADIUS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("entities");

        builder.push("warrior");
        WARRIOR_HEALTH = builder.comment("Max health of the warrior gnome").defineInRange("health", 30.0D, 1.0D, 1024.0D);
        WARRIOR_ATTACK_DAMAGE = builder.comment("Melee attack damage of the warrior gnome").defineInRange("attack_damage", 5.0D, 0.0D, 1024.0D);
        WARRIOR_SPEED = builder.comment("Movement speed of the warrior gnome").defineInRange("movement_speed", 0.28D, 0.0D, 2.0D);
        WARRIOR_ATTACK_INTERVAL = builder.comment("Ticks between warrior gnome melee attacks").defineInRange("attack_interval", 20, 1, 200);
        builder.pop();

        builder.push("archer");
        ARCHER_HEALTH = builder.comment("Max health of the archer gnome").defineInRange("health", 18.0D, 1.0D, 1024.0D);
        ARCHER_SPEED = builder.comment("Movement speed of the archer gnome").defineInRange("movement_speed", 0.26D, 0.0D, 2.0D);
        ARCHER_ATTACK_INTERVAL = builder.comment("Ticks between archer gnome shots").defineInRange("attack_interval", 20, 1, 200);
        ARCHER_ATTACK_RADIUS = builder.comment("Maximum shooting distance of the archer gnome").defineInRange("attack_radius", 15.0D, 1.0D, 64.0D);
        ARCHER_STRAFE_DISTANCE = builder.comment("Distance the archer gnome tries to keep from its target").defineInRange("strafe_distance", 8.0D, 1.0D, 64.0D);
        builder.pop();

        builder.push("healer");
        HEALER_HEALTH = builder.comment("Max health of the healer gnome").defineInRange("health", 24.0D, 1.0D, 1024.0D);
        HEALER_SPEED = builder.comment("Movement speed of the healer gnome").defineInRange("movement_speed", 0.24D, 0.0D, 2.0D);
        HEALER_THROW_INTERVAL = builder.comment("Ticks between healer gnome splash potion throws").defineInRange("throw_interval", 80, 1, 600);
        HEALER_HEAL_RADIUS = builder.comment("Radius the healer gnome searches for hurt players").defineInRange("heal_radius", 12.0D, 1.0D, 64.0D);
        builder.pop();

        builder.push("shared");
        FOLLOW_RANGE = builder.comment("Follow range attribute for all gnomes").defineInRange("follow_range", 24.0D, 1.0D, 128.0D);
        FOLLOW_STOP_DISTANCE = builder.comment("Distance at which a gnome stops following its owner").defineInRange("follow_stop_distance", 3.0D, 1.0D, 32.0D);
        FOLLOW_TELEPORT_DISTANCE = builder.comment("Distance at which a gnome teleports to its owner").defineInRange("follow_teleport_distance", 15.0D, 4.0D, 128.0D);
        builder.pop();

        builder.pop();

        builder.push("blocks");

        builder.push("healer");
        HEALER_BLOCK_INTERVAL = builder.comment("Ticks between healer block grow pulses").defineInRange("grow_interval", 100, 1, 2400);
        HEALER_BLOCK_RADIUS = builder.comment("Radius of the healer block grow pulse").defineInRange("grow_radius", 4, 1, 16);
        builder.pop();

        builder.push("warrior");
        WARRIOR_BLOCK_INTERVAL = builder.comment("Ticks between warrior block strength pulses").defineInRange("aura_interval", 40, 1, 400);
        WARRIOR_BLOCK_RADIUS = builder.comment("Radius of the warrior block strength aura").defineInRange("aura_radius", 8.0D, 1.0D, 64.0D);
        WARRIOR_BLOCK_STRENGTH_AMPLIFIER = builder.comment("Strength effect amplifier granted by the warrior block (0 = Strength I)").defineInRange("strength_amplifier", 0, 0, 4);
        WARRIOR_BLOCK_STRENGTH_DURATION = builder.comment("Strength effect duration in ticks granted by the warrior block").defineInRange("strength_duration", 60, 20, 1200);
        builder.pop();

        builder.push("archer");
        ARCHER_BLOCK_INTERVAL = builder.comment("Ticks between archer block shots").defineInRange("shoot_interval", 40, 1, 400);
        ARCHER_BLOCK_RADIUS = builder.comment("Radius the archer block searches for enemies").defineInRange("shoot_radius", 12.0D, 1.0D, 64.0D);
        builder.pop();

        builder.pop();

        SPEC = builder.build();
    }

    private GnomeConfig() {
    }

    public static double dbl(ModConfigSpec.DoubleValue value) {
        return SPEC.isLoaded() ? value.get() : value.getDefault();
    }

    public static float flt(ModConfigSpec.DoubleValue value) {
        return (float) dbl(value);
    }

    public static int integer(ModConfigSpec.IntValue value) {
        return SPEC.isLoaded() ? value.get() : value.getDefault();
    }
}
