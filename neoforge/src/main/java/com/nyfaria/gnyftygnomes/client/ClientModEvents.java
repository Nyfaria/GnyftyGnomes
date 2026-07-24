package com.nyfaria.gnyftygnomes.client;

import com.nyfaria.gnyftygnomes.Constants;
import com.nyfaria.gnyftygnomes.client.renderer.GnomeRenderer;
import com.nyfaria.gnyftygnomes.init.EntityInit;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.ARCHER_GNOME.get(), GnomeRenderer::new);
        event.registerEntityRenderer(EntityInit.HEALING_GNOME.get(), GnomeRenderer::new);
        event.registerEntityRenderer(EntityInit.WARRIOR_GNOME.get(), GnomeRenderer::new);
    }
}
