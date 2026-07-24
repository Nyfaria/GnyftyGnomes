package com.nyfaria.gnyftygnomes.init;

import com.nyfaria.gnyftygnomes.Constants;
import com.nyfaria.gnyftygnomes.entity.AbstractGnomeEntity;
import com.nyfaria.gnyftygnomes.entity.ArcherGnomeEntity;
import com.nyfaria.gnyftygnomes.entity.GnomeType;
import com.nyfaria.gnyftygnomes.entity.HealingGnomeEntity;
import com.nyfaria.gnyftygnomes.entity.WarriorGnomeEntity;
import com.nyfaria.gnyftygnomes.registration.RegistrationProvider;
import com.nyfaria.gnyftygnomes.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EntityInit {
    public static final RegistrationProvider<EntityType<?>> ENTITIES = RegistrationProvider.get(Registries.ENTITY_TYPE, Constants.MODID);
    public static final List<AttributesRegister<?>> attributeSuppliers = new ArrayList<>();

    public static final RegistryObject<EntityType<?>, EntityType<ArcherGnomeEntity>> ARCHER_GNOME = registerLivingEntity("archer_gnome",
            () -> EntityType.Builder.of(ArcherGnomeEntity::new, MobCategory.CREATURE).sized(0.6F, 1.4F), ArcherGnomeEntity::createAttributes);
    public static final RegistryObject<EntityType<?>, EntityType<HealingGnomeEntity>> HEALING_GNOME = registerLivingEntity("healing_gnome",
            () -> EntityType.Builder.of(HealingGnomeEntity::new, MobCategory.CREATURE).sized(0.6F, 1.4F), HealingGnomeEntity::createAttributes);
    public static final RegistryObject<EntityType<?>, EntityType<WarriorGnomeEntity>> WARRIOR_GNOME = registerLivingEntity("warrior_gnome",
            () -> EntityType.Builder.of(WarriorGnomeEntity::new, MobCategory.CREATURE).sized(0.6F, 1.4F), WarriorGnomeEntity::createAttributes);

    public static EntityType<? extends AbstractGnomeEntity> getEntityType(GnomeType type) {
        return switch (type) {
            case ARCHER -> ARCHER_GNOME.get();
            case HEALING -> HEALING_GNOME.get();
            case WARRIOR -> WARRIOR_GNOME.get();
        };
    }



    protected static <T extends Entity> RegistryObject<EntityType<?>,EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITIES.register(name, () -> supplier.get().build(Constants.MODID + ":" + name));
    }

    protected static <T extends LivingEntity> RegistryObject<EntityType<?>,EntityType<T>> registerLivingEntity(String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                                 Supplier<AttributeSupplier.Builder> attributeSupplier) {
        RegistryObject<EntityType<?>,EntityType<T>> entityTypeSupplier = registerEntity(name, supplier);
        attributeSuppliers.add(new AttributesRegister<>(entityTypeSupplier, attributeSupplier));
        return entityTypeSupplier;
    }

    protected static <T extends LivingEntity> RegistryObject<EntityType<?>,EntityType<T>> registerEntityWithEgg(String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                                  Supplier<AttributeSupplier.Builder> attributeSupplier,
                                                                                                  int secondaryColor) {
        return registerEntityWithEgg(name, supplier, attributeSupplier, 0x392F24, secondaryColor);
    }

    protected static <T extends LivingEntity> RegistryObject<EntityType<?>,EntityType<T>> registerEntityWithEgg(String name, Supplier<EntityType.Builder<T>> supplier,
                                                                                                  Supplier<AttributeSupplier.Builder> attributeSupplier,
                                                                                                  int primaryColor, int secondaryColor) {
        RegistryObject<EntityType<?>,EntityType<T>> entityTypeSupplier = registerLivingEntity(name, supplier, attributeSupplier);
        return entityTypeSupplier;
    }

    public static void loadClass() {
    }


    public record AttributesRegister<E extends LivingEntity>(Supplier<EntityType<E>> entityTypeSupplier,
                                                             Supplier<AttributeSupplier.Builder> factory) {
    }
}
