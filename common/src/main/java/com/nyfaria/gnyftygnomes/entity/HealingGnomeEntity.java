package com.nyfaria.gnyftygnomes.entity;

import com.nyfaria.gnyftygnomes.config.GnomeConfig;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class HealingGnomeEntity extends AbstractGnomeEntity {

    public HealingGnomeEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseAttributes()
                .add(Attributes.MAX_HEALTH, GnomeConfig.dbl(GnomeConfig.HEALER_HEALTH))
                .add(Attributes.MOVEMENT_SPEED, GnomeConfig.dbl(GnomeConfig.HEALER_SPEED));
    }

    @Override
    public GnomeType getGnomeType() {
        return GnomeType.HEALING;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (tickCount % GnomeConfig.integer(GnomeConfig.HEALER_THROW_INTERVAL) == 0) {
            LivingEntity target = findHealTarget();
            if (target != null) {
                throwHealingPotion(target);
            }
        }
    }

    private LivingEntity findHealTarget() {
        AABB area = getBoundingBox().inflate(GnomeConfig.dbl(GnomeConfig.HEALER_HEAL_RADIUS));
        LivingEntity best = null;
        double mostMissing = 0.5D;
        for (Player player : level().getEntitiesOfClass(Player.class, area, p -> p.isAlive() && p.getHealth() < p.getMaxHealth())) {
            double missing = player.getMaxHealth() - player.getHealth();
            if (missing > mostMissing) {
                mostMissing = missing;
                best = player;
            }
        }
        return best;
    }

    private void throwHealingPotion(LivingEntity target) {
        ItemStack potion = PotionContents.createItemStack(Items.SPLASH_POTION, Potions.HEALING);
        ThrownPotion thrown = new ThrownPotion(level(), this);
        thrown.setItem(potion);
        double dx = target.getX() - getX();
        double dy = target.getEyeY() - getEyeY();
        double dz = target.getZ() - getZ();
        double horizontal = Math.sqrt(dx * dx + dz * dz);
        thrown.shoot(dx, dy + horizontal * 0.2D, dz, 0.75F, 8.0F);
        playSound(SoundEvents.WITCH_THROW, 1.0F, 0.8F + getRandom().nextFloat() * 0.4F);
        level().addFreshEntity(thrown);
    }
}
