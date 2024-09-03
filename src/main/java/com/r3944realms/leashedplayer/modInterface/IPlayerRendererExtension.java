package com.r3944realms.leashedplayer.modInterface;

import net.minecraft.client.Camera;

public interface IPlayerRendererExtension {
    <E extends net. minecraft. world. entity. Entity> void renderLeashForCamera(
            Camera pCamera,
            float pPartialTick,
            com.mojang.blaze3d.vertex.PoseStack pPoseStack,
            net.minecraft.client.renderer.MultiBufferSource pBufferSource,
            E pLeashHolder
    );
}
