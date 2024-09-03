package com.r3944realms.leashedplayer.modInterface;

import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

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
}
