package com.nyfaria.gnyftygnomes.init;

import com.nyfaria.gnyftygnomes.Constants;
import com.nyfaria.gnyftygnomes.registration.RegistrationProvider;
import com.nyfaria.gnyftygnomes.registration.RegistryObject;
import com.nyfaria.gnyftygnomes.worldgen.GnomeStructureProcessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class StructureProcessorInit {
    public static final RegistrationProvider<StructureProcessorType<?>> STRUCTURE_PROCESSORS = RegistrationProvider.get(Registries.STRUCTURE_PROCESSOR, Constants.MODID);

    public static final RegistryObject<StructureProcessorType<?>, StructureProcessorType<GnomeStructureProcessor>> GNOME_FARM =
            STRUCTURE_PROCESSORS.register("gnome_farm", () -> () -> GnomeStructureProcessor.CODEC);

    public static void loadClass() {
    }
}
