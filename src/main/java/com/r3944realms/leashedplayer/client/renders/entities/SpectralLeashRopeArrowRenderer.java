package com.r3944realms.leashedplayer.client.renders.entities;

import com.r3944realms.leashedplayer.LeashedPlayer;
import com.r3944realms.leashedplayer.content.entities.SpectralLeashRopeArrow;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SpectralLeashRopeArrowRenderer extends ArrowRenderer<SpectralLeashRopeArrow> {
    public static final ResourceLocation SPECTRAL_LEASH_ROPE_ARROW = ResourceLocation.fromNamespaceAndPath(LeashedPlayer.MOD_ID, "textures/entity/projectiles/spectral_leash_rope_arrow.png");

    public SpectralLeashRopeArrowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull SpectralLeashRopeArrow pEntity) {
        return SPECTRAL_LEASH_ROPE_ARROW;
    }
}
