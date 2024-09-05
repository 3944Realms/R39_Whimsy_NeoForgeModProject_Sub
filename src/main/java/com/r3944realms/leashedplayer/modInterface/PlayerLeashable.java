package com.r3944realms.leashedplayer.modInterface;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public interface PlayerLeashable extends Leashable {

    /**
     * 获取拴绳的持有者实体 可能不存在
     */
    @Nullable
    Entity getLeashHolder();

    /**
     * 获取拴绳的持有者数据 （从EntityData中获取
     */
    Leashable.LeashData getLeashDataFromEntityData();

    /**
     * 是否立即可被拴住
     */
    boolean canBeLeashedInstantly(Player player);

    /**
     * 设置栓绳数据
     * @param pLeashHolder 拴绳持有者
     * @param pBroadcastPacket 是否广播包
     */
    default void setLeashedTo(@NotNull Entity pLeashHolder, boolean pBroadcastPacket) {
        setLeashedTo((Entity & Leashable)this, pLeashHolder, pBroadcastPacket);
    }

    static <E extends Entity & Leashable> void setLeashedTo(E pEntity, Entity pLeashHolder, boolean pBroadcastPacket) {
        Leashable.LeashData leashable$leashdata = pEntity.getLeashData();
        if (leashable$leashdata == null) {
            leashable$leashdata = new Leashable.LeashData(pLeashHolder);
            pEntity.setLeashData(leashable$leashdata);
        } else {
            leashable$leashdata.setLeashHolder(pLeashHolder);
        }

        if (pBroadcastPacket && pEntity.level() instanceof ServerLevel serverlevel) {
            serverlevel.getChunkSource().broadcast(pEntity, new ClientboundSetEntityLinkPacket(pEntity, pLeashHolder));
        }
        //这边覆写去掉了乘坐相关的逻辑，即乘坐状态下也可以正常被栓住，不影响其乘坐状态

    }
    @Nullable
    static Entity getLeashDataEntity(@NotNull ServerPlayer serverPlayer , @NotNull ServerLevel serverLevel) {
        LeashData leashDataFromEntityData = ((PlayerLeashable) serverPlayer).getLeashDataFromEntityData();
        if (leashDataFromEntityData != null) {
            return getLeashDataEntity(leashDataFromEntityData, serverLevel);
        }
        else return null;
    }

    static Entity getLeashDataEntityOrThrown(@NotNull ServerPlayer serverPlayer ,@NotNull ServerLevel serverLevel) throws Exception {
        Entity leashedEntity = getLeashDataEntity(serverPlayer, serverLevel);
        if(leashedEntity == null) throw new Exception("invalid");
        else return leashedEntity;
    }

    @Nullable
    static Entity getLeashDataEntity(@NotNull Leashable.LeashData leashDataFromEntityData, @NotNull ServerLevel level) {
        if(leashDataFromEntityData.delayedLeashInfo != null) {
            Optional<UUID> UUID = leashDataFromEntityData.delayedLeashInfo.left();
            Optional<BlockPos> BlockPos = leashDataFromEntityData.delayedLeashInfo.right();
            if (UUID.isPresent()) {
                return level.getEntity(UUID.get());
            } else return BlockPos.map(pos -> LeashFenceKnotEntity.getOrCreateKnot(level, pos)).orElse(null);
        }
        else if(leashDataFromEntityData.leashHolder != null) {
            return leashDataFromEntityData.leashHolder;
        }
        else if(leashDataFromEntityData.delayedLeashHolderId != 0) {
            return level.getEntity(leashDataFromEntityData.delayedLeashHolderId);
        }
        else return null;
    }

    static Entity getLeashDataEntityOrThrown(@NotNull Leashable.LeashData leashDataFromEntityData, @NotNull ServerLevel level) throws Exception {
        if(leashDataFromEntityData.delayedLeashInfo != null) {
            Optional<UUID> UUID = leashDataFromEntityData.delayedLeashInfo.left();
            Optional<BlockPos> BlockPos = leashDataFromEntityData.delayedLeashInfo.right();
            if (UUID.isPresent()) {
                return level.getEntity(UUID.get());
            } else if(BlockPos.isPresent()) {
                return LeashFenceKnotEntity.getOrCreateKnot(level, BlockPos.get());
            } else {
                throw new Exception("invalid delayedLeashInfo");
            }
        }
        else if(leashDataFromEntityData.leashHolder != null) {
            return leashDataFromEntityData.leashHolder;
        }
        else if(leashDataFromEntityData.delayedLeashHolderId != 0) {
            Entity entity = level.getEntity(leashDataFromEntityData.delayedLeashHolderId);
            if(entity == null) {
                throw new Exception("Not found Entity. maybe the pId is invalid");
            }
            return entity;
        }
        else {
            throw new Exception("invalid leash data");
        }
    }
    static boolean isLeashFenceKnotEntityExisted(ServerLevel pLevel, BlockPos pPos) {
        int i = pPos.getX();
        int j = pPos.getY();
        int k = pPos.getZ();

        for (LeashFenceKnotEntity leashfenceknotentity : pLevel.getEntitiesOfClass(
                LeashFenceKnotEntity.class, new AABB((double)i - 1.0, (double)j - 1.0, (double)k - 1.0, (double)i + 1.0, (double)j + 1.0, (double)k + 1.0)
        )) {
            if (leashfenceknotentity.getPos().equals(pPos)) {
                return true;
            }
        }
        return false;
    }
    @Nullable
    static Entity getLeashFenceKnotEntity(ServerLevel pLevel, BlockPos pPos) {
        int i = pPos.getX();
        int j = pPos.getY();
        int k = pPos.getZ();

        for (LeashFenceKnotEntity leashfenceknotentity : pLevel.getEntitiesOfClass(
                LeashFenceKnotEntity.class, new AABB((double)i - 1.0, (double)j - 1.0, (double)k - 1.0, (double)i + 1.0, (double)j + 1.0, (double)k + 1.0)
        )) {
            if (leashfenceknotentity.getPos().equals(pPos)) {
                return leashfenceknotentity;
            }
        }
        return null;
    }
    static Entity createLeashKnotFence(ServerLevel pLevel, BlockPos pPos) {
        LeashFenceKnotEntity leashfenceknotentity = new LeashFenceKnotEntity(pLevel, pPos);
        pLevel.addFreshEntity(leashfenceknotentity);
        return leashfenceknotentity;
    }
}
