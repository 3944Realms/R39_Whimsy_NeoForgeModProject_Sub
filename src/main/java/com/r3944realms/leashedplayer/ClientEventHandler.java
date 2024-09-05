package com.r3944realms.leashedplayer;

import com.r3944realms.leashedplayer.client.renders.LeashRopeArrowRenderer;
import com.r3944realms.leashedplayer.content.entities.ModEntityRegister;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = LeashedPlayer.MOD_ID)
public class ClientEventHandler {
    @SubscribeEvent
    public static void RegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityRegister.LEASH_ROPE_ARROW.get(), LeashRopeArrowRenderer::new);
    }
}
