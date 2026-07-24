package com.nyfaria.gnyftygnomes.entity;

import net.minecraft.util.StringRepresentable;

public enum GnomeType implements StringRepresentable {
    ARCHER("archer_gnome"),
    HEALING("healing_gnome"),
    WARRIOR("warrior_gnome");

    public static final StringRepresentable.EnumCodec<GnomeType> CODEC = StringRepresentable.fromEnum(GnomeType::values);

    private final String registryName;

    GnomeType(String registryName) {
        this.registryName = registryName;
    }

    public String getRegistryName() {
        return registryName;
    }

    @Override
    public String getSerializedName() {
        return registryName;
    }
}
