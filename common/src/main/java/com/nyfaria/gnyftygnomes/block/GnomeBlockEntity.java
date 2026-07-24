package com.nyfaria.gnyftygnomes.block;

import com.nyfaria.gnyftygnomes.config.GnomeConfig;
import com.nyfaria.gnyftygnomes.init.BlockInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;

public class GnomeBlockEntity extends BlockEntity {
    private CompoundTag storedData = new CompoundTag();

    public GnomeBlockEntity(BlockPos pos, BlockState state) {
        super(BlockInit.GNOME_BLOCK_ENTITY.get(), pos, state);
    }

    public void serverTick(Level level, BlockPos pos, BlockState state) {
        if (!(level instanceof ServerLevel serverLevel) || !(state.getBlock() instanceof GnomeBlock gnomeBlock)) {
            return;
        }
        long time = level.getGameTime();
        switch (gnomeBlock.getGnomeType()) {
            case HEALING -> {
                if (time % GnomeConfig.integer(GnomeConfig.HEALER_BLOCK_INTERVAL) == 0L) {
                    growPulse(serverLevel, pos);
                }
            }
            case WARRIOR -> {
                if (time % GnomeConfig.integer(GnomeConfig.WARRIOR_BLOCK_INTERVAL) == 0L) {
                    strengthAura(serverLevel, pos);
                }
            }
            case ARCHER -> {
                if (time % GnomeConfig.integer(GnomeConfig.ARCHER_BLOCK_INTERVAL) == 0L) {
                    shootNearbyEnemy(serverLevel, pos);
                }
            }
        }
    }

    private void growPulse(ServerLevel level, BlockPos pos) {
        RandomSource random = level.getRandom();
        int radius = GnomeConfig.integer(GnomeConfig.HEALER_BLOCK_RADIUS);
        for (BlockPos target : BlockPos.betweenClosed(pos.offset(-radius, -2, -radius), pos.offset(radius, 2, radius))) {
            BlockState state = level.getBlockState(target);
            if (state.getBlock() instanceof BonemealableBlock bonemealable && bonemealable.isValidBonemealTarget(level, target, state)) {
                BlockPos immutable = target.immutable();
                bonemealable.performBonemeal(level, random, immutable, level.getBlockState(immutable));
                level.levelEvent(1505, immutable, 15);
            }
        }
    }

    private void strengthAura(ServerLevel level, BlockPos pos) {
        AABB area = new AABB(pos).inflate(GnomeConfig.dbl(GnomeConfig.WARRIOR_BLOCK_RADIUS));
        int duration = GnomeConfig.integer(GnomeConfig.WARRIOR_BLOCK_STRENGTH_DURATION);
        int amplifier = GnomeConfig.integer(GnomeConfig.WARRIOR_BLOCK_STRENGTH_AMPLIFIER);
        for (Player player : level.getEntitiesOfClass(Player.class, area, EntitySelector.NO_SPECTATORS)) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, duration, amplifier, true, false, true));
        }
    }

    private void shootNearbyEnemy(ServerLevel level, BlockPos pos) {
        Vec3 center = Vec3.atCenterOf(pos);
        AABB area = new AABB(pos).inflate(GnomeConfig.dbl(GnomeConfig.ARCHER_BLOCK_RADIUS));
        Monster target = level.getEntitiesOfClass(Monster.class, area, Monster::isAlive).stream()
                .min(Comparator.comparingDouble(monster -> monster.distanceToSqr(center)))
                .orElse(null);
        if (target == null) {
            return;
        }
        Arrow arrow = new Arrow(level, center.x, center.y + 0.5D, center.z, new ItemStack(Items.ARROW), null);
        double dx = target.getX() - arrow.getX();
        double dy = target.getY(0.5D) - arrow.getY();
        double dz = target.getZ() - arrow.getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        arrow.shoot(dx, dy + horizontal * 0.2D, dz, 1.6F, 6.0F);
        level.playSound(null, pos, SoundEvents.SKELETON_SHOOT, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.addFreshEntity(arrow);
    }

    public void setStoredData(CompoundTag tag) {
        this.storedData = tag;
        setChanged();
    }

    public CompoundTag getStoredData() {
        return storedData;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("StoredData", storedData);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("StoredData")) {
            storedData = tag.getCompound("StoredData");
        }
    }
}
