package com.nyfaria.gnyftygnomes.worldgen;

import com.mojang.serialization.Codec;
import com.nyfaria.gnyftygnomes.*;
import com.nyfaria.gnyftygnomes.config.GnomeConfig;
import com.nyfaria.gnyftygnomes.entity.*;
import com.nyfaria.gnyftygnomes.init.BlockInit;
import com.nyfaria.gnyftygnomes.init.StructureProcessorInit;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class GnomeStructureProcessor extends StructureProcessor {
    public static final GnomeStructureProcessor INSTANCE = new GnomeStructureProcessor();
    public static final Codec<GnomeStructureProcessor> CODEC = Codec.unit(INSTANCE);

    private static final Set<Long> DECIDED_PIECES = ConcurrentHashMap.newKeySet();
    private static boolean errorLogged = false;

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader level, BlockPos offset, BlockPos pos,
                                                             StructureTemplate.StructureBlockInfo localBlock,
                                                             StructureTemplate.StructureBlockInfo worldBlock,
                                                             StructurePlaceSettings settings) {
        try {
            if (!GnomeConfig.bool(GnomeConfig.VILLAGE_GNOME_ENABLED)
                    || !(level instanceof WorldGenLevel worldGenLevel)
                    || !(worldBlock.state().getBlock() instanceof CropBlock)) {
                return worldBlock;
            }
            long seed = worldGenLevel.getSeed();
            if (!DECIDED_PIECES.add(seed * 31L + offset.asLong())) {
                return worldBlock;
            }

            if (CommonClass.RANDOM.nextDouble() >= GnomeConfig.dbl(GnomeConfig.VILLAGE_GNOME_CHANCE)) {
                return worldBlock;
            }
            Block gnome = BlockInit.getBlock(GnomeType.values()[CommonClass.RANDOM.nextInt(GnomeType.values().length)]);
            return new StructureTemplate.StructureBlockInfo(worldBlock.pos(), gnome.defaultBlockState(), worldBlock.nbt());
        } catch (Throwable t) {
            if (!errorLogged) {
                errorLogged = true;
                Constants.LOG.error("Gnome structure processor failed", t);
            }
            return worldBlock;
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorInit.GNOME_FARM.get();
    }
}
