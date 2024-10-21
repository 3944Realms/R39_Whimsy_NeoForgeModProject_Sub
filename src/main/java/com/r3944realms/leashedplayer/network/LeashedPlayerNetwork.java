package com.r3944realms.leashedplayer.network;

import com.r3944realms.leashedplayer.LeashedPlayer;
import com.r3944realms.leashedplayer.network.client.UpdatePlayerMovement;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = LeashedPlayer.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class LeashedPlayerNetwork {

    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(LeashedPlayer.MOD_ID);
        registrar.playToClient(
                UpdatePlayerMovement.TYPE,
                UpdatePlayerMovement.STREAM_CODEC,
                UpdatePlayerMovement::handle
        );
    }

}
