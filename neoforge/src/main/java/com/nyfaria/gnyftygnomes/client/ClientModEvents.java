package com.nyfaria.gnyftygnomes.client;

import com.nyfaria.gnyftygnomes.Constants;
import com.nyfaria.gnyftygnomes.client.renderer.GnomeRenderer;
import com.nyfaria.gnyftygnomes.init.EntityInit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.ARCHER_GNOME.get(), GnomeRenderer::new);
        event.registerEntityRenderer(EntityInit.HEALING_GNOME.get(), GnomeRenderer::new);
        event.registerEntityRenderer(EntityInit.WARRIOR_GNOME.get(), GnomeRenderer::new);
    }
}
