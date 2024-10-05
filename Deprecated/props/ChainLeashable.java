package com.r3944realms.leashedplayer.content.entities.props;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface ChainLeashable {
    String LEASH_TAG = "chain_leash";
    @Nullable
    ChainLeashable.ChainData getLeashData();

    void setLeashData(@Nullable ChainLeashable.ChainData pLeashData);

    default boolean isLeashed() {
        return this.getLeashData() != null && this.getLeashData().leashHolder != null;
    }

    default boolean mayBeLeashed() {
        return this.getLeashData() != null;
    }

    default boolean canHaveALeashAttachedToIt() {
        return this.canBeLeashed() && !this.isLeashed();
    }

    default boolean canBeLeashed() {
        return true;
    }
    
    default boolean handleLeashAtDistance(Entity pLeashHolder, float pDistance) {
        return true;
    }

    default void leashTooFarBehaviour() {
        this.dropLeash(true, true);
    }

    default void closeRangeLeashBehaviour(Entity pEntity) {
    }

    default void elasticRangeLeashBehaviour(Entity pLeashHolder, float pDistance) {
        legacyElasticRangeLeashBehaviour((Entity & ChainLeashable)this, pLeashHolder, pDistance);
    }
    
    @Nullable
    default Entity getLeashHolder() {
        return getLeashHolder((Entity & ChainLeashable)this);
    }
    
    private static <E extends Entity & ChainLeashable> void legacyElasticRangeLeashBehaviour(E pEntity, Entity pLeashHolder, float pDistance) {
        double d0 = (pLeashHolder.getX() - pEntity.getX()) / (double)pDistance;
        double d1 = (pLeashHolder.getY() - pEntity.getY()) / (double)pDistance;
        double d2 = (pLeashHolder.getZ() - pEntity.getZ()) / (double)pDistance;
        pEntity.setDeltaMovement(
                pEntity.getDeltaMovement().add(Math.copySign(d0 * d0 * 0.4, d0), Math.copySign(d1 * d1 * 0.4, d1), Math.copySign(d2 * d2 * 0.4, d2))
        );
    }
    @Nullable
    private static <E extends Entity & ChainLeashable> Entity getLeashHolder(E pEntity) {
        ChainLeashable.ChainData chainLeashable$chaindata = pEntity.getLeashData();
        if (chainLeashable$chaindata == null) {
            return null;
        } else {
            if (chainLeashable$chaindata.delayedLeashHolderId != 0 && pEntity.level().isClientSide) {
                Entity entity = pEntity.level().getEntity(chainLeashable$chaindata.delayedLeashHolderId);
                if (entity instanceof Entity) {
                    chainLeashable$chaindata.setLeashHolder(entity);
                }
            }

            return chainLeashable$chaindata.leashHolder;
        }
    }
    default void setLeashedTo(Entity pLeashHolder, boolean pBroadcastPacket) {
        setLeashedTo((Entity & ChainLeashable)this, pLeashHolder, pBroadcastPacket);
    }
    static List<ChainLeashable> leashableInArea(Level pLevel, BlockPos pPos, Predicate<ChainLeashable> pPredicate) {
        double d0 = 7.0;
        int i = pPos.getX();
        int j = pPos.getY();
        int k = pPos.getZ();
        AABB aabb = new AABB((double)i - 7.0, (double)j - 7.0, (double)k - 7.0, (double)i + 7.0, (double)j + 7.0, (double)k + 7.0);
        return pLevel.getEntitiesOfClass(Entity.class, aabb, p_353023_ -> p_353023_ instanceof ChainLeashable chainLeashable && pPredicate.test(chainLeashable)).stream().map(ChainLeashable.class::cast).toList();
    }

    private static <E extends Entity & ChainLeashable> void setLeashedTo(E pEntity, Entity pLeashHolder, boolean pBroadcastPacket) {
        ChainLeashable.ChainData chainLeashable$chaindata = pEntity.getLeashData();
        if (chainLeashable$chaindata == null) {
            chainLeashable$chaindata = new ChainLeashable.ChainData(pLeashHolder);
            pEntity.setLeashData(chainLeashable$chaindata);
        } else {
            chainLeashable$chaindata.setLeashHolder(pLeashHolder);
        }

        if (pBroadcastPacket && pEntity.level() instanceof ServerLevel serverlevel) {
            //TODO：自写Packet ClientboundSetEntityLinkPacket(pEntity, pLeashHolder)
            serverlevel.getChunkSource().broadcast(pEntity, new ClientboundSetEntityLinkPacket(pEntity, pLeashHolder));
        }

        if (pEntity.isPassenger()) {
            pEntity.stopRiding();
        }
    }
    default void dropLeash(boolean pBroadcastPacket, boolean pDropItem) {
        dropLeash((Entity & ChainLeashable)this, pBroadcastPacket, pDropItem);
    }

    private static <E extends Entity & ChainLeashable> void dropLeash(E pEntity, boolean pBroadcastPacket, boolean pDropItem) {
        ChainLeashable.ChainData chainLeashable$chaindata = pEntity.getLeashData();
        if (chainLeashable$chaindata != null && chainLeashable$chaindata.leashHolder != null) {
            pEntity.setLeashData(null);
            if (!pEntity.level().isClientSide && pDropItem) {
                pEntity.spawnAtLocation(Items.CHAIN);
            }

            if (pBroadcastPacket && pEntity.level() instanceof ServerLevel serverlevel) {
                serverlevel.getChunkSource().broadcast(pEntity, new ClientboundSetEntityLinkPacket(pEntity, null));
            }
        }
    }
    static <E extends Entity & ChainLeashable> void tickLeash(E pEntity) {
        ChainLeashable.ChainData chainLeashable$chaindata = pEntity.getLeashData();
        if (chainLeashable$chaindata != null && chainLeashable$chaindata.delayedLeashInfo != null) {
            restoreLeashFromSave(pEntity, chainLeashable$chaindata);
        }

        if (chainLeashable$chaindata != null && chainLeashable$chaindata.leashHolder != null) {
            if (!pEntity.isAlive() || !chainLeashable$chaindata.leashHolder.isAlive()) {
                dropLeash(pEntity, true, true);
            }

            Entity entity = pEntity.getLeashHolder();
            if (entity != null && entity.level() == pEntity.level()) {
                float f = pEntity.distanceTo(entity);
                if (!pEntity.handleLeashAtDistance(entity, f)) {
                    return;
                }

                if ((double)f > 10.0) {
                    pEntity.leashTooFarBehaviour();
                } else if ((double)f > 6.0) {
                    pEntity.elasticRangeLeashBehaviour(entity, f);
                    pEntity.checkSlowFallDistance();
                } else {
                    pEntity.closeRangeLeashBehaviour(entity);
                }
            }
        }
    }

    private static <E extends Entity & ChainLeashable> void restoreLeashFromSave(E pEntity, ChainLeashable.ChainData pLeashData) {
        if (pLeashData.delayedLeashInfo != null && pEntity.level() instanceof ServerLevel serverlevel) {
            Optional<UUID> optional1 = pLeashData.delayedLeashInfo.left();
            Optional<BlockPos> optional = pLeashData.delayedLeashInfo.right();
            if (optional1.isPresent()) {
                Entity entity = serverlevel.getEntity(optional1.get());
                if (entity != null) {
                    setLeashedTo(pEntity, entity, true);
                    return;
                }
            } else if (optional.isPresent()) {
                setLeashedTo(pEntity, LeashFenceKnotEntity.getOrCreateKnot(serverlevel, optional.get()), true);
                return;
            }

            if (pEntity.tickCount > 100) {
                pEntity.spawnAtLocation(Items.LEAD);
                pEntity.setLeashData(null);
            }
        }
    }

    final class ChainData {
        public int delayedLeashHolderId;
        @Nullable
        public Entity leashHolder;
        @Nullable
        public Either<UUID, BlockPos> delayedLeashInfo;

        ChainData(@org.jetbrains.annotations.Nullable Either<UUID, BlockPos> pDelayedLeashInfo) {
            this.delayedLeashInfo = pDelayedLeashInfo;
        }

        public ChainData(@org.jetbrains.annotations.Nullable Entity pLeashHolder) {
            this.leashHolder = pLeashHolder;
        }

        ChainData(int pDelayedLeashInfoId) {
            this.delayedLeashHolderId = pDelayedLeashInfoId;
        }

        public void setLeashHolder(Entity pLeashHolder) {
            this.leashHolder = pLeashHolder;
            this.delayedLeashInfo = null;
            this.delayedLeashHolderId = 0;
        }
    }
}
