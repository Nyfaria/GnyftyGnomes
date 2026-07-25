package com.nyfaria.gnyftygnomes;

import com.nyfaria.gnyftygnomes.config.GnomeConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod(Constants.MODID)
public class GnyftyGnomes {

    public GnyftyGnomes() {
        CommonClass.init();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GnomeConfig.SPEC);
    }
}
