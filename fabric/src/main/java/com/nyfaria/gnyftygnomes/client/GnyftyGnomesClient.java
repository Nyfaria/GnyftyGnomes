package com.nyfaria.gnyftygnomes.client;

import com.nyfaria.gnyftygnomes.client.renderer.GnomeRenderer;
import com.nyfaria.gnyftygnomes.init.BlockInit;
import com.nyfaria.gnyftygnomes.init.EntityInit;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;

public class GnyftyGnomesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(EntityInit.ARCHER_GNOME.get(), GnomeRenderer::new);
        EntityRendererRegistry.register(EntityInit.HEALING_GNOME.get(), GnomeRenderer::new);
        EntityRendererRegistry.register(EntityInit.WARRIOR_GNOME.get(), GnomeRenderer::new);

        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.ARCHER_GNOME.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.HEALING_GNOME.get(), RenderType.translucent());
        BlockRenderLayerMap.INSTANCE.putBlock(BlockInit.WARRIOR_GNOME.get(), RenderType.translucent());
    }
}
