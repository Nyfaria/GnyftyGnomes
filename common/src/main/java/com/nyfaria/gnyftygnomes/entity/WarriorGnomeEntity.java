package com.nyfaria.gnyftygnomes.entity;

import com.nyfaria.gnyftygnomes.config.GnomeConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;

public class WarriorGnomeEntity extends AbstractGnomeEntity {

    public WarriorGnomeEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseAttributes()
                .add(Attributes.MAX_HEALTH, GnomeConfig.dbl(GnomeConfig.WARRIOR_HEALTH))
                .add(Attributes.ATTACK_DAMAGE, GnomeConfig.dbl(GnomeConfig.WARRIOR_ATTACK_DAMAGE))
                .add(Attributes.MOVEMENT_SPEED, GnomeConfig.dbl(GnomeConfig.WARRIOR_SPEED));
    }

    @Override
    public GnomeType getGnomeType() {
        return GnomeType.WARRIOR;
    }

    @Override
    protected boolean isCombatant() {
        return true;
    }

    @Override
    public BrainActivityGroup<? extends AbstractGnomeEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>(),
                new LookAtAttackTarget<>(),
                new AnimatableMeleeAttack<AbstractGnomeEntity>(0).attackInterval(gnome -> GnomeConfig.integer(GnomeConfig.WARRIOR_ATTACK_INTERVAL)));
    }
}
