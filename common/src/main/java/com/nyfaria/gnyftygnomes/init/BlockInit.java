package com.nyfaria.gnyftygnomes.init;

import com.nyfaria.gnyftygnomes.Constants;
import com.nyfaria.gnyftygnomes.block.GnomeBlock;
import com.nyfaria.gnyftygnomes.block.GnomeBlockEntity;
import com.nyfaria.gnyftygnomes.entity.GnomeType;
import com.nyfaria.gnyftygnomes.registration.RegistrationProvider;
import com.nyfaria.gnyftygnomes.registration.RegistryObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;
import java.util.function.Supplier;

public class BlockInit {
    public static final RegistrationProvider<Block> BLOCKS = RegistrationProvider.get(Registries.BLOCK, Constants.MODID);
    public static final RegistrationProvider<BlockEntityType<?>> BLOCK_ENTITIES = RegistrationProvider.get(Registries.BLOCK_ENTITY_TYPE, Constants.MODID);

    public static final RegistryObject<Block, GnomeBlock> ARCHER_GNOME = registerBlock("archer_gnome", () -> new GnomeBlock(GnomeType.ARCHER, gnomeProperties()));
    public static final RegistryObject<Block, GnomeBlock> HEALING_GNOME = registerBlock("healing_gnome", () -> new GnomeBlock(GnomeType.HEALING, gnomeProperties()));
    public static final RegistryObject<Block, GnomeBlock> WARRIOR_GNOME = registerBlock("warrior_gnome", () -> new GnomeBlock(GnomeType.WARRIOR, gnomeProperties()));

    public static final RegistryObject<BlockEntityType<?>, BlockEntityType<GnomeBlockEntity>> GNOME_BLOCK_ENTITY = BLOCK_ENTITIES.register("gnome",
            () -> BlockEntityType.Builder.of(GnomeBlockEntity::new, ARCHER_GNOME.get(), HEALING_GNOME.get(), WARRIOR_GNOME.get()).build(null));

    private static BlockBehaviour.Properties gnomeProperties() {
        return BlockBehaviour.Properties.of()
                .mapColor(MapColor.DIRT)
                .strength(1.5F)
                .sound(SoundType.WOOD)
                .noOcclusion();
    }

    public static Block getBlock(GnomeType type) {
        return switch (type) {
            case ARCHER -> ARCHER_GNOME.get();
            case HEALING -> HEALING_GNOME.get();
            case WARRIOR -> WARRIOR_GNOME.get();
        };
    }

    public static <T extends Block> RegistryObject<Block, T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, b -> () -> new BlockItem(b.get(), ItemInit.getItemProperties()));
    }

    protected static <T extends Block> RegistryObject<Block, T> registerBlock(String name, Supplier<T> block, Function<RegistryObject<Block, T>, Supplier<? extends BlockItem>> item) {
        RegistryObject<Block, T> reg = BLOCKS.register(name, block);
        ItemInit.ITEMS.register(name, () -> item.apply(reg).get());
        return reg;
    }

    public static <T extends Block> RegistryObject<Block, T> registerBlockWithoutItem(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static void loadClass() {
    }
}
