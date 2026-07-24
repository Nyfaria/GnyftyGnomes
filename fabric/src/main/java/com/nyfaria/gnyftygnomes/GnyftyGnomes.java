package com.nyfaria.gnyftygnomes;

import com.nyfaria.gnyftygnomes.config.GnomeConfig;
import com.nyfaria.gnyftygnomes.init.EntityInit;
import fuzs.forgeconfigapiport.fabric.api.neoforge.v4.NeoForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.neoforged.fml.config.ModConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;

public class GnyftyGnomes implements ModInitializer {

    @Override
    public void onInitialize() {
        NeoForgeConfigRegistry.INSTANCE.register(Constants.MODID, ModConfig.Type.COMMON, GnomeConfig.SPEC);
        CommonClass.init();
        EntityInit.attributeSuppliers.forEach(register ->
                FabricDefaultAttributeRegistry.register((EntityType<? extends LivingEntity>) register.entityTypeSupplier().get(), register.factory().get().build()));
    }
}
