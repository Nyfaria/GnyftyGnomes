package com.nyfaria.gnyftygnomes.client.model;

import com.nyfaria.gnyftygnomes.Constants;
import com.nyfaria.gnyftygnomes.entity.AbstractGnomeEntity;
import com.nyfaria.gnyftygnomes.entity.GnomeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GnomeGeoModel extends GeoModel<AbstractGnomeEntity> {
    @Override
    public ResourceLocation getModelResource(AbstractGnomeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MODID, "geo/entity/" + animatable.getGnomeType().getRegistryName() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AbstractGnomeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MODID, "textures/entity/" + animatable.getGnomeType().getRegistryName() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(AbstractGnomeEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MODID, "animations/entity/" + animatable.getGnomeType().getRegistryName() + ".animation.json");
    }

    @Override
    public void setCustomAnimations(AbstractGnomeEntity animatable, long instanceId, AnimationState<AbstractGnomeEntity> state) {
        super.setCustomAnimations(animatable, instanceId, state);

        GeoBone head = getAnimationProcessor().getBone("head");
        if (head != null) {
            EntityModelData entityData = state.getData(DataTickets.ENTITY_MODEL_DATA);
            if (entityData != null) {
                head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
                head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
            }
        }

        float amount = Math.min(state.getLimbSwingAmount(), 1.0F);
        float cycle = Mth.cos(state.getLimbSwing() * 0.6662F) * 1.4F * amount;
        applyRotation("leftleg", cycle);
        applyRotation("rightleg", -cycle);

        float attack = animatable.getAttackAnim(state.getPartialTick());
        GnomeType type = animatable.getGnomeType();
        if (type == GnomeType.ARCHER && animatable.isAggressive()) {
            applyRotation("right_arm", 1.5F + attack * 0.35F);
            applyRotation("left_arm", 1.5F);
        } else if (type == GnomeType.WARRIOR && attack > 0.0F) {
            float swing = Mth.sin(attack * (float) Math.PI);
            applyRotation("right_arm", -swing * 2.2F);
            applyRotation("left_arm", -cycle);
        } else {
            applyRotation("left_arm", -cycle);
            applyRotation("right_arm", cycle);
        }
    }

    private void applyRotation(String boneName, float rotation) {
        GeoBone bone = getAnimationProcessor().getBone(boneName);
        if (bone != null) {
            bone.setRotX(rotation);
        }
    }
}
