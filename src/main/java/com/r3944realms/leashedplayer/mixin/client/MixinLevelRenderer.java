package com.r3944realms.leashedplayer.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import com.r3944realms.leashedplayer.modInterface.IPlayerRendererExtension;
import com.r3944realms.leashedplayer.modInterface.PlayerLeashable;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(LevelRenderer.class)
public abstract class MixinLevelRenderer {
    @Shadow
    @Nullable
    private ClientLevel level;

    @Shadow protected abstract void renderEntity(Entity pEntity, double pCamX, double pCamY, double pCamZ, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource);

    @Shadow @Final
    private Minecraft minecraft;

    @Shadow @Final private RenderBuffers renderBuffers;

    @Inject(
            method = {"renderLevel"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/RenderBuffers;bufferSource()Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;",
                    shift = At.Shift.AFTER
            )
    )
    private void renderLevel(DeltaTracker pDeltaTracker, boolean pRenderBlockOutline, Camera pCamera, GameRenderer pGameRenderer, LightTexture pLightTexture, Matrix4f pFrustumMatrix, Matrix4f pProjectionMatrix, CallbackInfo ci) {
        assert this.level != null;
        PoseStack poseStack = new PoseStack();
        MultiBufferSource.BufferSource multibuffersource$buffersource = this.renderBuffers.bufferSource();
        for(Entity entity : this.level.entitiesForRendering()) {
            //对于玩家实体拴绳渲染（从第一人称视角）
            if (entity instanceof AbstractClientPlayer abstractClientPlayer) {
                if(!(pCamera.getEntity() instanceof AbstractClientPlayer)) return;
                Minecraft mc = Minecraft.getInstance();
                PlayerRenderer playerRenderer = (PlayerRenderer) mc.getEntityRenderDispatcher().getRenderer(abstractClientPlayer);
                IPlayerRendererExtension playerRendererExtension = (IPlayerRendererExtension) playerRenderer;
                if (mc.options.getCameraType().isFirstPerson()) {
                    Leashable.LeashData leashDataFromEntityData = ((PlayerLeashable) abstractClientPlayer).getLeashDataFromEntityData();
                    if(leashDataFromEntityData == null) return;
                    Either<UUID, BlockPos> delayedLeashInfo = leashDataFromEntityData.delayedLeashInfo;
                    if(delayedLeashInfo != null) {
                        float partialTickTime = pCamera.getPartialTickTime();
                        Vec3 position = pCamera.getPosition();
                        double dX = Mth.lerp(partialTickTime, abstractClientPlayer.xOld, abstractClientPlayer.getX()) - position.x;
                        double dY = Mth.lerp(partialTickTime, abstractClientPlayer.yOld, abstractClientPlayer.getY()) - position.y;
                        double dZ = Mth.lerp(partialTickTime, abstractClientPlayer.zOld, abstractClientPlayer.getZ()) - position.z;
                        Vec3 vec3 = playerRenderer.getRenderOffset(abstractClientPlayer, partialTickTime);
                        double dX_ = dX + vec3.x();
                        double dY_ = dY + vec3.y();
                        double dZ_ = dZ + vec3.z();
                        poseStack.pushPose();
                        poseStack.translate(dX_, dY_, dZ_);
                        ClientLevel level = mc.level;
                        if (delayedLeashInfo.right().isPresent() && delayedLeashInfo.left().isEmpty()) {
                            assert level != null;
                            playerRendererExtension.renderLeashForCamera(pCamera, partialTickTime, poseStack, multibuffersource$buffersource, LeashFenceKnotEntity.getOrCreateKnot(level, delayedLeashInfo.right().get()));
                        } else if (delayedLeashInfo.right().isEmpty() && delayedLeashInfo.left().isPresent()) {
                            assert level != null;
                            Player playerByUUID = level.getPlayerByUUID(delayedLeashInfo.left().get());
                            if (playerByUUID != null) {
                                playerRendererExtension.renderLeashForCamera(pCamera, partialTickTime, poseStack, multibuffersource$buffersource, playerByUUID);
                            }
                        }
                    }
                }
            }
        }
    }
}

