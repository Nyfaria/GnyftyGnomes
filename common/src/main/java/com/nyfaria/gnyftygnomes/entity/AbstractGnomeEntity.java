package com.nyfaria.gnyftygnomes.entity;

import com.nyfaria.gnyftygnomes.init.BlockInit;
import com.nyfaria.gnyftygnomes.block.GnomeBlockEntity;
import com.nyfaria.gnyftygnomes.config.GnomeConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.FollowOwner;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGnomeEntity extends TamableAnimal implements GeoEntity, SmartBrainOwner<AbstractGnomeEntity> {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    protected AbstractGnomeEntity(EntityType<? extends TamableAnimal> type, Level level) {
        super(type, level);
    }

    public abstract GnomeType getGnomeType();

    public static AttributeSupplier.Builder createBaseAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, GnomeConfig.dbl(GnomeConfig.FOLLOW_RANGE));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    @Override
    public float getVoicePitch() {
        return super.getVoicePitch() * 2.0F;
    }

    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
        setAggressive(getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET));
    }

    public void aiStep() {
        if (this.level().isClientSide) {
            for(int i = 0; i < 1; ++i) {
                this.level().addParticle(ParticleTypes.ENCHANT, this.getRandomX((double)0.5F), this.getRandomY() - (double)0.25F, this.getRandomZ((double)0.5F), (this.random.nextDouble() - (double)0.5F) * (double)2.0F, -this.random.nextDouble(), (this.random.nextDouble() - (double)0.5F) * (double)2.0F);
            }
        }

        if (tickCount % GnomeConfig.integer(GnomeConfig.PET_HEAL_INTERVAL) == 0) {
            healNearbyPets();
        }

        super.aiStep();
    }

    private void healNearbyPets() {
        float amount = (float) GnomeConfig.dbl(GnomeConfig.PET_HEAL_AMOUNT);
        if (amount <= 0.0F) {
            return;
        }
        AABB area = getBoundingBox().inflate(GnomeConfig.dbl(GnomeConfig.PET_HEAL_RADIUS));
        for (TamableAnimal pet : level().getEntitiesOfClass(TamableAnimal.class, area,
                other -> other.isTame() && other.isAlive() && other.getHealth() < other.getMaxHealth())) {
            pet.heal(amount);

            // Healing Particles
            if (this.level().isClientSide) {
                for(int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.HEART, pet.getRandomX((double)0.5F), pet.getRandomY() - (double)0.25F, pet.getRandomZ((double)0.5F), (this.random.nextDouble() - (double)0.5F) * (double)2.0F, -this.random.nextDouble(), (this.random.nextDouble() - (double)0.5F) * (double)2.0F);
                }
            }
        }
    }

    @Override
    public List<? extends ExtendedSensor<? extends AbstractGnomeEntity>> getSensors() {
        return List.of(
                new NearbyPlayersSensor<>(),
                new NearbyLivingEntitySensor<AbstractGnomeEntity>().setRadius(16),
                new HurtBySensor<>());
    }

    @Override
    public BrainActivityGroup<? extends AbstractGnomeEntity> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>());
    }

    @Override
    public BrainActivityGroup<? extends AbstractGnomeEntity> getIdleTasks() {
        List<ExtendedBehaviour<? super AbstractGnomeEntity>> targeting = new ArrayList<>();
        if (isCombatant()) {
            targeting.add(new SetAttackTarget<AbstractGnomeEntity>(false).attackPredicate(gnome -> true).targetFinder(AbstractGnomeEntity::findHostileOwnerTarget));
            targeting.add(new TargetOrRetaliate<>().attackablePredicate(target -> target instanceof Enemy && target.isAlive()));
        }
        targeting.add(new SetPlayerLookTarget<>());
        targeting.add(new SetRandomLookTarget<>());
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<AbstractGnomeEntity>(targeting.toArray(new ExtendedBehaviour[0])),
                new FollowOwner<AbstractGnomeEntity>().stopFollowingWithin(GnomeConfig.dbl(GnomeConfig.FOLLOW_STOP_DISTANCE)).teleportToTargetAfter(GnomeConfig.dbl(GnomeConfig.FOLLOW_TELEPORT_DISTANCE)),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>(),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60))));
    }

    protected boolean isCombatant() {
        return false;
    }

    @Nullable
    private LivingEntity findHostileOwnerTarget() {
        LivingEntity owner = getOwner();
        if (owner == null) {
            return null;
        }
        LivingEntity target = owner.getLastHurtMob();
        if (target != null && target.isAlive() && target != this && target != owner && target instanceof Enemy) {
            return target;
        }
        return null;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!level().isClientSide() && player.isShiftKeyDown() && (getOwnerUUID() == null || isOwnedBy(player))) {
            return convertToBlock() ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return super.mobInteract(player, hand);
    }

    private boolean convertToBlock() {
        BlockPos pos = blockPosition();
        if (!level().getBlockState(pos).canBeReplaced()) {
            return false;
        }
        Block block = BlockInit.getBlock(getGnomeType());
        BlockState state = block.defaultBlockState();
        if (state.hasProperty(HorizontalDirectionalBlock.FACING)) {
            state = state.setValue(HorizontalDirectionalBlock.FACING, getDirection());
        }
        level().setBlockAndUpdate(pos, state);
        if (level().getBlockEntity(pos) instanceof GnomeBlockEntity gnome) {
            CompoundTag tag = new CompoundTag();
            saveWithoutId(tag);
            gnome.setStoredData(tag);
        }
        discard();
        return true;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", 5, state -> state.setAndContinue(IDLE)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
