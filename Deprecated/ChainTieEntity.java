package com.r3944realms.leashedplayer.content.entities;

import com.r3944realms.leashedplayer.content.entities.props.ChainLeashable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.BlockAttachedEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChainTieEntity extends BlockAttachedEntity {

    public ChainTieEntity(EntityType<? extends BlockAttachedEntity> type, Level worldIn, BlockPos hangingPositionIn) {
        super(type, worldIn, hangingPositionIn);
        this.setPos(hangingPositionIn.getX() + 0.5D, hangingPositionIn.getY(), hangingPositionIn.getZ() + 0.5D);
    }

    public ChainTieEntity(EntityType<? extends BlockAttachedEntity> hangingEntityEntityType, Level level) {
        super(hangingEntityEntityType, level);
    }


    public void playPlacementSound() {
        this.playSound(SoundEvents.ARMOR_EQUIP_CHAIN.value(), 1.0F, 1.0F);
    }
    public static ChainTieEntity createTie(Level worldIn, BlockPos fence) {
        ChainTieEntity entityChainTie = new ChainTieEntity(ModEntityRegister.CHAIN_TIE.get(), worldIn, fence);
        worldIn.addFreshEntity(entityChainTie);
        entityChainTie.playPlacementSound();
        return entityChainTie;
    }
    public static ChainTieEntity getOrCreateKnot(Level pLevel, BlockPos pPos) {
        int i = pPos.getX();
        int j = pPos.getY();
        int k = pPos.getZ();

        for (ChainTieEntity chainTieEntity : pLevel.getEntitiesOfClass(
                ChainTieEntity.class, new AABB((double)i - 1.0, (double)j - 1.0, (double)k - 1.0, (double)i + 1.0, (double)j + 1.0, (double)k + 1.0)
        )) {
            if (chainTieEntity.getPos().equals(pPos)) {
                return chainTieEntity;
            }
        }

        ChainTieEntity chainTieEntity = createTie(pLevel, pPos);
        pLevel.addFreshEntity(chainTieEntity);
        return chainTieEntity;
    }

    @Override
    protected void recalculateBoundingBox() {
        this.setPosRaw(
                this.pos.getX() + 0.5D,
                this.pos.getY() + 0.5D,
                this.pos.getZ() + 0.5D
        );
        double xSize = 0.3D;
        double ySize = 0.875D;
        this.setBoundingBox(
                new AABB(
                        this.getX() - xSize,
                        this.getY() - 0.5,
                        this.getZ() - xSize,
                        this.getX() + xSize,
                        this.getY() + ySize - 0.5,
                        this.getZ() + xSize
                )
        );
    }
    @Override
    public @NotNull InteractionResult interact(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        if (this.level().isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            boolean flag = false;
            List<ChainLeashable> list = ChainLeashable.leashableInArea(this.level(), this.getPos(), chainLeashable -> {
                Entity entity = chainLeashable.getLeashHolder();
                return entity == pPlayer || entity == this;
            });

            for (ChainLeashable ChainLeashable : list) {
                if (ChainLeashable.getLeashHolder() == pPlayer) {
                    ChainLeashable.setLeashedTo(this, true);
                    flag = true;
                }
            }

            boolean flag1 = false;
            if (!flag) {
                this.discard();
                if (pPlayer.getAbilities().instabuild) {
                    for (ChainLeashable ChainLeashable : list) {
                        if (ChainLeashable.isLeashed() && ChainLeashable.getLeashHolder() == this) {
                            ChainLeashable.dropLeash(true, false);
                            flag1 = true;
                        }
                    }
                }
            }

            if (flag || flag1) {
                this.gameEvent(GameEvent.BLOCK_ATTACH, pPlayer);
            }

            return InteractionResult.CONSUME;
        }
    }
    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return distance < 1024.0D;
    }

    @Override
    public boolean survives() {
        return this.level().getBlockState(this.pos).getBlock() instanceof WallBlock;
    }

    @Override
    public void dropItem(@Nullable Entity pEntity) {
        this.playSound(SoundEvents.ARMOR_EQUIP_CHAIN.value(), 1.0F, 1.0F);
    }


    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder pBuilder) {

    }


    @Override
    public void setPos(double x, double y, double z) {
        super.setPos(Mth.floor(x) + 0.5D, Mth.floor(y) + 0.5D, Mth.floor(z) + 0.5D);
    }
    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket(@NotNull ServerEntity pEntity) {
        return new ClientboundAddEntityPacket(this, 0, this.getPos());
    }
    @Override
    public @NotNull Vec3 getRopeHoldPosition(float pPartialTicks) {
        return this.getPosition(pPartialTicks).add(0.0, 0.2, 0.0);
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.CHAIN);
    }
}
