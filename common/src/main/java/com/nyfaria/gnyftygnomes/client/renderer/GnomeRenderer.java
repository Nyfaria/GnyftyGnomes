package com.nyfaria.gnyftygnomes.client.renderer;

import com.nyfaria.gnyftygnomes.client.model.GnomeGeoModel;
import com.nyfaria.gnyftygnomes.entity.AbstractGnomeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GnomeRenderer extends GeoEntityRenderer<AbstractGnomeEntity> {
    public GnomeRenderer(EntityRendererProvider.Context context) {
        super(context, new GnomeGeoModel());
        addRenderLayer(new GnomeItemLayer(this));
    }
}
