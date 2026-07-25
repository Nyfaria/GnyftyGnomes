package com.nyfaria.gnyftygnomes;

import com.nyfaria.gnyftygnomes.init.BlockInit;
import com.nyfaria.gnyftygnomes.init.EntityInit;
import com.nyfaria.gnyftygnomes.init.ItemInit;
import com.nyfaria.gnyftygnomes.init.StructureProcessorInit;
import com.nyfaria.gnyftygnomes.init.TagInit;
import net.minecraft.util.*;

public class CommonClass {

    public static RandomSource RANDOM = RandomSource.create();

    public static void init() {
        ItemInit.loadClass();
        BlockInit.loadClass();
        EntityInit.loadClass();
        StructureProcessorInit.loadClass();
        TagInit.loadClass();
    }
}