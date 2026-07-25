package com.nyfaria.gnyftygnomes.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.nyfaria.gnyftygnomes.entity.AbstractGnomeEntity;
import com.nyfaria.gnyftygnomes.entity.GnomeType;
import com.nyfaria.gnyftygnomes.init.BlockInit;
import com.nyfaria.gnyftygnomes.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GnomeBlock extends BaseEntityBlock {
    public static final MapCodec<GnomeBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            GnomeType.CODEC.fieldOf("gnome_type").forGetter(GnomeBlock::getGnomeType),
            propertiesCodec()
    ).apply(instance, GnomeBlock::new));

    private static final VoxelShape SHAPE = Block.box(3.0D, 0.0D, 3.0D, 13.0D, 14.0D, 13.0D);

    private final GnomeType type;

    public GnomeBlock(GnomeType type, Properties properties) {
        super(properties);
        this.type = type;
        registerDefaultState(stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    public GnomeType getGnomeType() {
        return type;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GnomeBlockEntity(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        }
        return createTickerHelper(blockEntityType, BlockInit.GNOME_BLOCK_ENTITY.get(), (lvl, pos, st, be) -> be.serverTick(lvl, pos, st));
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide()) {
            EntityType<? extends AbstractGnomeEntity> entityType = EntityInit.getEntityType(type);
            AbstractGnomeEntity gnome = entityType.create(level);
            if (gnome != null) {
                CompoundTag stored = level.getBlockEntity(pos) instanceof GnomeBlockEntity be ? be.getStoredData() : new CompoundTag();
                if (!stored.isEmpty()) {
                    gnome.load(stored);
                }
                if (gnome.getOwnerUUID() == null) {
                    gnome.tame(player);
                }
                float yaw = state.getValue(HorizontalDirectionalBlock.FACING).toYRot();
                gnome.moveTo(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, yaw, 0.0F);
                gnome.setYBodyRot(yaw);
                gnome.setYHeadRot(yaw);
                level.removeBlock(pos, false);
                level.addFreshEntity(gnome);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 1, 0.0D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
