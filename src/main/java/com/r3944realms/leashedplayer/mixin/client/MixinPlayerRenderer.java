package com.r3944realms.leashedplayer.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import com.r3944realms.leashedplayer.LeashedPlayer;
import com.r3944realms.leashedplayer.content.entities.LeashRopeArrow;
import com.r3944realms.leashedplayer.modInterface.ILivingEntityExtension;
import com.r3944realms.leashedplayer.modInterface.IPlayerRendererExtension;
import com.r3944realms.leashedplayer.modInterface.PlayerLeashable;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> implements IPlayerRendererExtension {
    public MixinPlayerRenderer(EntityRendererProvider.Context pContext, PlayerModel<AbstractClientPlayer> pModel, float pShadowRadius) {
        super(pContext, pModel, pShadowRadius);
    }

    @Inject(
            at = @At("HEAD"),
            method =  "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
    )
    private void renderMixin(AbstractClientPlayer pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, CallbackInfo ci) {
        Leashable.LeashData leashDataFromEntityData = ((PlayerLeashable) pEntity).getLeashDataFromEntityData();
        if(leashDataFromEntityData == null) return;
        Either<UUID, BlockPos> delayedLeashInfo = leashDataFromEntityData.delayedLeashInfo;
        if(delayedLeashInfo != null) {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;
            if (delayedLeashInfo.right().isPresent() && delayedLeashInfo.left().isEmpty()) {
                assert level != null;
                renderLeash(pEntity, pPartialTicks, pPoseStack, pBuffer, LeashFenceKnotEntity.getOrCreateKnot(level, delayedLeashInfo.right().get()));
            } else if (delayedLeashInfo.right().isEmpty() && delayedLeashInfo.left().isPresent()) {
                assert level != null;
                Player playerByUUID = level.getPlayerByUUID(delayedLeashInfo.left().get());
                if (playerByUUID != null) {
                    renderLeash(pEntity, pPartialTicks, pPoseStack, pBuffer, playerByUUID);
                } else {
                    double breakDistanceTime = (leashDataFromEntityData.leashHolder instanceof LeashRopeArrow) ? LeashedPlayer.M1() * LeashedPlayer.M2() : LeashedPlayer.M1();
                    double MaxLeashLength = ((ILivingEntityExtension) pEntity).getLeashLength() * breakDistanceTime;
                    List<Entity> entities = level.getEntities(
                    null,
                            new AABB(
                                    pEntity.getX() - MaxLeashLength,
                                    pEntity.getY() - MaxLeashLength,
                                    pEntity.getZ() - MaxLeashLength,
                                    pEntity.getX() + MaxLeashLength,
                                    pEntity.getY() + MaxLeashLength,
                                    pEntity.getZ() + MaxLeashLength
                            )
                    );
                    Entity holder = null;
                    for (Entity entity_ : entities) {
                        if(entity_.getUUID().equals(delayedLeashInfo.left().get())) {
                            holder = entity_;
                            break;
                        }
                    }
                    if (holder != null) {
                        if(holder instanceof LeashRopeArrow) {
                            renderLeash(pEntity, pPartialTicks, pPoseStack, pBuffer, holder, new Vec3(0,0, 0));//TODO: 待擴展Vec3
                        }
                        else renderLeash(pEntity, pPartialTicks, pPoseStack, pBuffer, holder);
                    }
                }
            }
        }
    }
    /**
     * <h1>1. 角度与弧度转换</h1>
     * {@snippet lang=java :
     * double d0 = (double)(pEntity.getPreciseBodyRotation(pPartialTick) * (float)(Math.PI / 180.0)) + (Math.PI / 2);
     * }
     * <ul>
     *      <li><code>pEntity.getPreciseBodyRotation(pPartialTick)</code> 返回实体的旋转角度（通常是以度为单位）。/li>
     *      <li> <code>(Math.PI / 180.0)</code> 是将度数转换为弧度的乘数，因为大多数三角函数（如 <code>cos</code> 和 <code>sin</code>）都需要弧度值。</li>
     *      <li><code>+ (Math.PI / 2)</code> 用于将结果平移90度（四分之一圆），可能是为了校正方向或设置起始方向。 </li>
     * </ul>
     *
     * <p>
     * <h1> 2. 三角函数计算位移</h1>
     * {@snippet lang=java :
     * double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
     * double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
     *  }
     *  <ul>
     *      <li><code>d1</code> 和 <code>d2</code> 是利用三角函数 <code>cos</code> 和 <code>sin</code> 计算出来的位移量，用于确定实体相对于其旋转的实际位置。</li>
     *      <li><code>Math.cos(d0) * vec31.z</code> 和 <code>Math.sin(d0) * vec31.x</code> 分别计算沿 X 和 Z 轴的位移分量，这种计算通常用于旋转一个点或向量。</li>
     *      <li>两个公式结合起来用于旋转平面内的一个点 <code>(vec31.x, vec31.z)</code>，从而得到旋转后的新坐标。</li>
     *  </ul>
     * <p>
     * <h1> 3. 线性插值 (Lerp) </h1>
     * {@snippet lang=java :
     * double d3 = Mth.lerp(pPartialTick, pEntity.xo, pEntity.getX()) + d1;
     * double d4 = Mth.lerp(pPartialTick, pEntity.yo, pEntity.getY()) + vec31.y;
     * double d5 = Mth.lerp(pPartialTick, pEntity.zo, pEntity.getZ()) + d2;
     * }
     * <ul>
     *     <li><code>Mth.lerp</code> 是线性插值函数，通常用于在两个值之间平滑过渡。</li>
     *     <li><code>pEntity.xo</code>, <code>pEntity.yo</code>, <code>pEntity.zo</code> 是实体在上一个刻度（tick）中的位置，而 <code>pEntity.getX()</code>, <code>pEntity.getY()</code>, <code>pEntity.getZ()</code> 是当前刻度的位置。</li>
     *     <li><code>pPartialTick</code> 介于 <code>0</code> 和 <code>1</code> 之间，用来平滑过渡，使得动画更加流畅。</li>
     * </ul>
     * <p>
     * <h1> 4. 向量差值 </h1>
     * {@snippet lang=java :
     * float f = (float)(vec3.x - d3);
     * float f1 = (float)(vec3.y - d4);
     * float f2 = (float)(vec3.z - d5);
     * }
     * <ul>
     *     <li>计算两个点（<code>vec3</code> 和 <code>(d3, d4, d5)</code>）之间的差值，得到的 <code>f</code>，<code>f1</code>，<code>f2</code> 是向量差，用于后续的渲染计算。</li>
     * </ul>
     * <p>
     * <h1> 5. 逆平方根与比例因子 </h1>
     * {@snippet lang=java :
     * float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
     * }
     * <ul>
     *     <li><code>Mth.invSqrt</code> 计算的是逆平方根（通常用于归一化向量或调整比例）。</li>
     *     <li><code>f * f + f2 * f2</code> 是计算向量 <code>(f, f2)</code> 的平方和，用于得到其长度的平方。</li>
     *     <li>乘以 <code>0.025F / 2.0F</code> 用于缩放结果，使得线条在渲染时具有合适的比例。</li>
     * </ul>
     * <p>
     * <h1> 6. 循环绘制 </h1>
     * {@snippet lang=java :
     * for (int i1 = 0; i1 <= 24; i1++) {
     *     addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
     * }
     * }
     * <ul>
     *  <li>循环从 <code>0</code> 到 <code>24</code>，用于创建24个顶点对，形成一个链状结构（或绳索）的外观。</li>
     *  <li>每个循环迭代都会更新顶点的位置、颜色、光照等属性，使得链状结构被绘制出来。</li>
     * </ul>
     * <p>
     * <h1> 总结 </h1>
     * 这些数学运算主要用于计算实体在三维空间中的位置和方向，以确保在渲染链状结构（如拴住的绳索）时，链条能够跟随实体的移动和旋转并正确显示。在图形编程中，这些计算非常常见，尤其是在处理旋转、插值和光照效果时。
     */

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    public <E extends net.minecraft.world.entity.Entity> void renderLeashForCamera(
            Camera camera,
            float partialTick,
            com.mojang.blaze3d.vertex.PoseStack poseStack,
            net.minecraft.client.renderer.MultiBufferSource bufferSource,
            E leashHolder,
            Vec3 holderOffset
    ) {

        poseStack.pushPose();

        // 获得绳索持有者的位置
        Vec3 leashHolderPosition = leashHolder.getRopeHoldPosition(partialTick).add(holderOffset);

        // 获取当前观察的实体
        Entity cameraEntity = camera.getEntity();

        // 计算实体的朝向角度（弧度）
        double entityRotationAngleRadians = (double)(cameraEntity.getPreciseBodyRotation(partialTick) * (float) (Math.PI / 180.0)) + (Math.PI / 2);

        // 计算实体的绳索偏移，此处add偏移让渲染拴绳显示在玩家头部下（大约在脖子处
        Vec3 cameraEntityLeashOffset = cameraEntity.getLeashOffset(partialTick).add(0, -0.2, -0.5);
        double leashOffsetX = Math.cos(entityRotationAngleRadians) * cameraEntityLeashOffset.z + Math.sin(entityRotationAngleRadians) * cameraEntityLeashOffset.x;
        double leashOffsetZ = Math.sin(entityRotationAngleRadians) * cameraEntityLeashOffset.z - Math.cos(entityRotationAngleRadians) * cameraEntityLeashOffset.x;

        // 计算实体当前的实际位置
        double entityPosX = Mth.lerp(partialTick, cameraEntity.xo, cameraEntity.getX()) + leashOffsetX;
        double entityPosY = Mth.lerp(partialTick, cameraEntity.yo, cameraEntity.getY()) + cameraEntityLeashOffset.y;
        double entityPosZ = Mth.lerp(partialTick, cameraEntity.zo, cameraEntity.getZ()) + leashOffsetZ;

        // 在当前变换矩阵上应用偏移
        poseStack.translate(leashOffsetX, cameraEntityLeashOffset.y , leashOffsetZ);

        // 计算绳索的相对位置差
        float deltaX = (float)(leashHolderPosition.x - entityPosX);
        float deltaY = (float)(leashHolderPosition.y - entityPosY);
        float deltaZ = (float)(leashHolderPosition.z - entityPosZ);

        // 获取顶点消费者，用于绘制绳索
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.leash());
        Matrix4f matrix = poseStack.last().pose();

        // 计算比例因子，用于调节绳索的粗细
        float leashLengthRatio = Mth.invSqrt(deltaX * deltaX + deltaZ * deltaZ) * 0.025F / 2.0F;
        float leashXZScaleX = deltaZ * leashLengthRatio;
        float leashXZScaleZ = deltaX * leashLengthRatio;

        // 获取光照信息
        BlockPos cameraEntityBlockPos = BlockPos.containing(cameraEntity.getEyePosition(partialTick));
        BlockPos leashHolderBlockPos = BlockPos.containing(leashHolder.getEyePosition(partialTick));
        int cameraEntityBlockLightLevel = this.getBlockLightLevel((AbstractClientPlayer) cameraEntity, cameraEntityBlockPos);
        int leashHolderBlockLightLevel = 0; //getBlockLightLevel(leashHolder, leashHolderBlockPos);
        int cameraEntitySkyLightLevel = cameraEntity.level().getBrightness(LightLayer.SKY, cameraEntityBlockPos);
        int leashHolderSkyLightLevel = cameraEntity.level().getBrightness(LightLayer.SKY, leashHolderBlockPos);

        // 绘制绳索的上半部分
        for (int segment = 0; segment <= 24; segment++) {
            addVertexPair(vertexConsumer, matrix, deltaX, deltaY, deltaZ, cameraEntityBlockLightLevel, leashHolderBlockLightLevel, cameraEntitySkyLightLevel, leashHolderSkyLightLevel, 0.025F, 0.025F, leashXZScaleX, leashXZScaleZ, segment, false);
        }

        // 绘制绳索的下半部分
        for (int segment = 24; segment >= 0; segment--) {
            addVertexPair(vertexConsumer, matrix, deltaX, deltaY, deltaZ, cameraEntityBlockLightLevel, leashHolderBlockLightLevel, cameraEntitySkyLightLevel, leashHolderSkyLightLevel, 0.025F, 0.0F, leashXZScaleX, leashXZScaleZ, segment, true);
        }

        poseStack.popPose();
    }
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    protected <E extends Entity> void renderLeash(AbstractClientPlayer pEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, E pLeashHolder, Vec3 holderOffset) {
        pPoseStack.pushPose();
        Vec3 vec3 = pLeashHolder.getRopeHoldPosition(pPartialTick).add(holderOffset);
        double d0 = (double)(pEntity.getPreciseBodyRotation(pPartialTick) * (float) (Math.PI / 180.0)) + (Math.PI / 2);
        Vec3 vec31 = pEntity.getLeashOffset(pPartialTick).add(0, -0.2, -0.2);//TODO:待擴展Vec3);
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp(pPartialTick, pEntity.xo, pEntity.getX()) + d1;
        double d4 = Mth.lerp(pPartialTick, pEntity.yo, pEntity.getY()) + vec31.y;
        double d5 = Mth.lerp(pPartialTick, pEntity.zo, pEntity.getZ()) + d2;
        pPoseStack.translate(d1, vec31.y, d2);
        float f = (float)(vec3.x - d3);
        float f1 = (float)(vec3.y - d4);
        float f2 = (float)(vec3.z - d5);
        float f3 = 0.025F;
        VertexConsumer vertexconsumer = pBufferSource.getBuffer(RenderType.leash());
        Matrix4f matrix4f = pPoseStack.last().pose();
        float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = BlockPos.containing(pEntity.getEyePosition(pPartialTick));
        BlockPos blockpos1 = BlockPos.containing(pLeashHolder.getEyePosition(pPartialTick));
        int i = this.getBlockLightLevel(pEntity, blockpos);
        int j = 0;
        int k = pEntity.level().getBrightness(LightLayer.SKY, blockpos);
        int l = pEntity.level().getBrightness(LightLayer.SKY, blockpos1);

        for (int i1 = 0; i1 <= 24; i1++) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
        }

        for (int j1 = 24; j1 >= 0; j1--) {
            addVertexPair(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true);
        }

        pPoseStack.popPose();
    }

}