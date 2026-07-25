package com.nyfaria.gnyftygnomes.entity;

import com.nyfaria.gnyftygnomes.config.GnomeConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableRangedAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.StrafeTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;

public class ArcherGnomeEntity extends AbstractGnomeEntity implements RangedAttackMob {

    public ArcherGnomeEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseAttributes()
                .add(Attributes.MAX_HEALTH, GnomeConfig.dbl(GnomeConfig.ARCHER_HEALTH))
                .add(Attributes.MOVEMENT_SPEED, GnomeConfig.dbl(GnomeConfig.ARCHER_SPEED));
    }

    @Override
    public GnomeType getGnomeType() {
        return GnomeType.ARCHER;
    }

    @Override
    protected boolean isCombatant() {
        return true;
    }

    @Override
    public BrainActivityGroup<? extends AbstractGnomeEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new StrafeTarget<ArcherGnomeEntity>().speedMod(1.0F).strafeDistance(GnomeConfig.flt(GnomeConfig.ARCHER_STRAFE_DISTANCE)),
                new LookAtAttackTarget<>(),
                new AnimatableRangedAttack<ArcherGnomeEntity>(GnomeConfig.integer(GnomeConfig.ARCHER_ATTACK_INTERVAL)).attackRadius(GnomeConfig.flt(GnomeConfig.ARCHER_ATTACK_RADIUS)));
    }

    @Override
    public void performRangedAttack(LivingEntity target, float velocity) {
        Arrow arrow = new Arrow(level(), this, new ItemStack(Items.ARROW), null);
        double toX = target.getX() - getX();
        double toZ = target.getZ() - getZ();
        double flatDist = Math.sqrt(toX * toX + toZ * toZ);
        double dirX = flatDist > 1.0E-4D ? toX / flatDist : getLookAngle().x;
        double dirZ = flatDist > 1.0E-4D ? toZ / flatDist : getLookAngle().z;
        double margin = getBbWidth() * 0.5D + 0.4D;
        double startX = getX() + dirX * margin;
        double startZ = getZ() + dirZ * margin;
        double startY = getEyeY() - 0.1D;
        arrow.setPos(startX, startY, startZ);
        double dx = target.getX() - startX;
        double dy = target.getY(0.3333D) - startY;
        double dz = target.getZ() - startZ;
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        arrow.shoot(dx, dy + horizontal * 0.2D, dz, 1.6F, 6.0F);
        playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (getRandom().nextFloat() * 0.4F + 0.8F));
        level().addFreshEntity(arrow);
    }
}
