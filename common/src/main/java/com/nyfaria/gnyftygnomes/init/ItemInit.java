package com.nyfaria.gnyftygnomes.init;

import com.nyfaria.gnyftygnomes.Constants;
import com.nyfaria.gnyftygnomes.entity.*;
import com.nyfaria.gnyftygnomes.registration.RegistrationProvider;
import com.nyfaria.gnyftygnomes.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.util.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class ItemInit {
    public static final RegistrationProvider<Item> ITEMS = RegistrationProvider.get(Registries.ITEM, Constants.MODID);
    private static RandomSource RANDOM = RandomSource.create();
    public static final RegistrationProvider<CreativeModeTab> CREATIVE_MODE_TABS = RegistrationProvider.get(Registries.CREATIVE_MODE_TAB, Constants.MODID);
    public static final RegistryObject<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register(Constants.MODID, () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(BlockInit.getBlock(GnomeType.values()[(int)((System.currentTimeMillis()/1000) % GnomeType.values().length)])))
            .displayItems(
                    (itemDisplayParameters, output) -> {
                        ITEMS.getEntries().forEach(item -> output.accept(new ItemStack(item.get())));
                    }).title(Component.translatable("itemGroup." + Constants.MODID + ".tab"))
            .build());


    public static Item.Properties getItemProperties() {
        return new Item.Properties();
    }

    public static void loadClass() {
    }

}
