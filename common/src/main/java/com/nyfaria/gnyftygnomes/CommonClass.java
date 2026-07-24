package com.nyfaria.gnyftygnomes;

import com.nyfaria.gnyftygnomes.init.BlockInit;
import com.nyfaria.gnyftygnomes.init.EntityInit;
import com.nyfaria.gnyftygnomes.init.ItemInit;
import com.nyfaria.gnyftygnomes.init.TagInit;
import com.nyfaria.gnyftygnomes.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;

public class CommonClass {

    public static void init() {
        ItemInit.loadClass();
        BlockInit.loadClass();
        EntityInit.loadClass();
        TagInit.loadClass();
    }
}