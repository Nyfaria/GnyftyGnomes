package com.nyfaria.gnyftygnomes.platform;

import com.nyfaria.gnyftygnomes.platform.services.IPlatformHelper;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.loading.*;


public class NeoForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }
}