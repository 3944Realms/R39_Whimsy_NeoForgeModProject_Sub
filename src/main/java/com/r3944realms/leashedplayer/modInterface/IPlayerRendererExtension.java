package com.r3944realms.leashedplayer.modInterface;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;

public interface IPlayerRendererExtension {
    default  <E extends net. minecraft. world. entity. Entity> void renderLeashForCamera(
            Camera pCamera,
            float pPartialTick,
            com.mojang.blaze3d.vertex.PoseStack pPoseStack,
            net.minecraft.client.renderer.MultiBufferSource pBufferSource,
            E pLeashHolder
    ) {
         renderLeashForCamera(pCamera, pPartialTick, pPoseStack, pBufferSource, pLeashHolder, Vec3.ZERO);
    }
     <E extends net. minecraft. world. entity. Entity> void renderLeashForCamera(
            Camera pCamera,
            float pPartialTick,
            com.mojang.blaze3d.vertex.PoseStack pPoseStack,
            net.minecraft.client.renderer.MultiBufferSource pBufferSource,
            E pLeashHolder,
            Vec3 holderOffset
    );
}
