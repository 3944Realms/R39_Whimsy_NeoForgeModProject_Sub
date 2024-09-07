package com.r3944realms.leashedplayer;

import com.r3944realms.leashedplayer.client.renders.LeashRopeArrowRenderer;
import com.r3944realms.leashedplayer.content.entities.LeashRopeArrow;
import com.r3944realms.leashedplayer.content.entities.ModEntityRegister;
import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD, modid = LeashedPlayer.MOD_ID)
public class ClientEventHandler {
    @SubscribeEvent
    public static void onRegisterItemProperties(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(Items.CROSSBOW, ResourceLocation.withDefaultNamespace("leash_rope_arrow"),
                    ((pStack, pLevel, pEntity, pSeed) -> {
                        ChargedProjectiles chargedProjectiles = pStack.get(DataComponents.CHARGED_PROJECTILES);
                        return chargedProjectiles != null && chargedProjectiles.contains(ModItemRegister.LEASH_ROPE_ARROW.get()) ? 1.0F : 0.0F;
                    }));
            ItemProperties.register(Items.BOW, ResourceLocation.withDefaultNamespace("leash_rope_arrow_pulling"),
                    ((pStack, pLevel, pEntity, pSeed) ->
                        (pEntity != null && pEntity.isUsingItem() && pEntity.getUseItem() == pStack && LeashRopeArrow.isLeashRopeArrow(pStack, pEntity)) ? 1.0F: 0.0F
            ));
        });

    }
    @SubscribeEvent
    public static void RegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityRegister.LEASH_ROPE_ARROW.get(), LeashRopeArrowRenderer::new);
    }
}
