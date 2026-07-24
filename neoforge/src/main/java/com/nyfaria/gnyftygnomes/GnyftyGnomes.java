package com.nyfaria.gnyftygnomes;


import com.nyfaria.gnyftygnomes.config.GnomeConfig;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(Constants.MODID)
public class GnyftyGnomes {

    public GnyftyGnomes(IEventBus eventBus, ModContainer container) {
        CommonClass.init();
        container.registerConfig(ModConfig.Type.COMMON, GnomeConfig.SPEC);
    }
}