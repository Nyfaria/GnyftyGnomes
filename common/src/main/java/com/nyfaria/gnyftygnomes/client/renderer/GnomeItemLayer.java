package com.nyfaria.gnyftygnomes.client.renderer;

import com.nyfaria.gnyftygnomes.entity.AbstractGnomeEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class GnomeItemLayer extends BlockAndItemGeoLayer<AbstractGnomeEntity> {
    private static final ItemStack BOW = new ItemStack(Items.BOW);

    public GnomeItemLayer(GeoRenderer<AbstractGnomeEntity> renderer) {
        super(renderer);
    }

    @Nullable
    @Override
    protected ItemStack getStackForBone(GeoBone bone, AbstractGnomeEntity animatable) {
        return bone.getName().equals("bow") ? BOW : null;
    }

    @Override
    protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, AbstractGnomeEntity animatable) {
        return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
    }
}
