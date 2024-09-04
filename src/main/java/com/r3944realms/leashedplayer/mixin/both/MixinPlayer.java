package com.r3944realms.leashedplayer.mixin.both;

import com.r3944realms.leashedplayer.modInterface.ILivingEntityExtension;
import com.r3944realms.leashedplayer.modInterface.PlayerLeashable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.function.Consumer;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements PlayerLeashable {

    @Unique
    @Nullable
    private LeashData Pl$LeashData;//Data

    @SuppressWarnings("WrongEntityDataParameterClass")
    @Unique//客户端与服务器端的实体同步数据
    private static final EntityDataAccessor<CompoundTag> Pl$LEASH_DATA = SynchedEntityData.defineId(Player.class, EntityDataSerializers.COMPOUND_TAG);

    protected MixinPlayer(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    
    @Inject(method = {"tick"}, at = {@At("HEAD")})
    private void tickForLeash(CallbackInfo ci) {
        if(!this.level().isClientSide) {
            Pl$tickLeash();//服务器端每tick任务
        }
        PlayerLeashable playerLeashable = this;
        Entity leashHolder = playerLeashable.getLeashHolder();
        if(leashHolder != null ) {
            //存在则更新
            Pl$UpdateLeash(leashHolder, (Entity) playerLeashable);
        }
    }
    @Unique
    private static void Pl$UpdateLeash(Entity holderEntity, Entity restrainedEntity) {
        if(holderEntity == null || holderEntity.level() != restrainedEntity.level())
            return;
        float leashLength = 6.0f;
        if(restrainedEntity instanceof ILivingEntityExtension iEntity) {
            //获取长度
            float leashLengthFormValue = iEntity.getLeashLength();
            leashLength = leashLengthFormValue > 6 ? leashLengthFormValue : 6;
        }
        //两者距离
        float distance = holderEntity.distanceTo(restrainedEntity);
        //大于长度情况
        if(distance > leashLength) {
            //作用对象（实体所坐载体还是实体【根据isPassenger来判断】
            Entity applyMovementEntity = restrainedEntity.isPassenger() ? restrainedEntity.getVehicle() : restrainedEntity;
            if(applyMovementEntity != null){
                double dX = (holderEntity.getX() - applyMovementEntity.getX()) / (double) distance;
                double dY = (holderEntity.getY() - applyMovementEntity.getY()) / (double) distance;
                double dZ = (holderEntity.getZ() - applyMovementEntity.getZ()) / (double) distance;
                //给予作用实体其向holderEntity的一个速度动量
                applyMovementEntity.setDeltaMovement(
                        applyMovementEntity.getDeltaMovement().add(
                                Math.copySign(dX * dX * 0.4d, dX),
                                Math.copySign(dY * dY * 0.4d, dY),
                                Math.copySign(dZ * dZ * 0.4d, dZ)
                        )
                );
                //刹车，避免偏激移动
                Whimsy$Brake(applyMovementEntity, 1, 1, 1);
            }
        }

        //降低坠落伤害
        restrainedEntity.checkSlowFallDistance();
    }

    /**
     * 刹车（
     * @param pEntity 刹车的实体
     * @param pMaxX X方向的最大动量
     * @param pMaxY Y方向的最大动量
     * @param pMaxZ Z方向的最大动量
     */
    @Unique
    private static void Whimsy$Brake(Entity pEntity, double pMaxX, double pMaxY, double pMaxZ) {
        Vec3 deltaMovement = pEntity.getDeltaMovement();
        double dX = deltaMovement.x > pMaxX ? 0 : deltaMovement.x;
        double dY = deltaMovement.y > pMaxY ? 0 : deltaMovement.y;
        double dZ = deltaMovement.z > pMaxZ ? 0 : deltaMovement.z;
        pEntity.setDeltaMovement(dX, dY,dZ);
        pEntity.hurtMarked = true;
    }
    /**
     * 刹车（
     * @param pEntity 刹车的实体
     * @param pOpt 自定义规则
     */
    @Unique
    private static void Whimsy$Brake(Entity pEntity, @Nullable Consumer<Entity> pOpt) {
        Consumer<Entity> consumer = pOpt;
        if(pOpt == null) {
            consumer = entity -> {
                Vec3 deltaMovement = entity.getDeltaMovement();
                double dX = deltaMovement.x > 1 ? 0 : deltaMovement.x;
                double dY = deltaMovement.y > 1 ? 0 : deltaMovement.y;
                double dZ = deltaMovement.z > 1 ? 0 : deltaMovement.z;
                entity.setDeltaMovement(dX, dY,dZ);
                entity.hurtMarked = true;
            };
        }
        consumer.accept(pEntity);
    }
    @Unique
    protected void Pl$tickLeash() {

        if(this.Pl$LeashData == null) return;//没有Data直接退出
        //info -> Holder整理
        Pl$RestoreLeashFormSave();
        //默认值设为6.0f距离
        float leashLength = 6.0f;
        Entity entity = this.Pl$LeashData.leashHolder;
        //保存数据
        saveLeashData(Pl$LeashData);
        if(this instanceof ILivingEntityExtension iEntityExtension) {
            //获取设定值
            float leashLengthSelf = iEntityExtension.getLeashLength();
            leashLength = leashLengthSelf > 6 ? leashLengthSelf : 6;
        }
        if (entity != null) {
            if(!isAlive() || !entity.isAlive() || distanceTo(entity) > Math.max(leashLength * 2.0f, 10.0f)){
                //玩家死亡 或 持有者不存在 或 距离大于设定值的2倍（长度2倍若低于10格，则选10格） ，
                // 则取消拴绳关系，并掉落拴绳
                dropLeash(true, true);
            } else if(distanceTo(entity) > leashLength * 1.3f) {
                //大于1.3倍绳长则会让其跳跃（在<1.25格阻拦情况下，跳跃阻拦
                jumpFromGround();
            }
        }
    }
    @Override
    public Entity getLeashHolder() {
        if (Pl$LeashData == null) return null;
        if (Pl$LeashData.leashHolder == null && Pl$LeashData.delayedLeashHolderId != 0 ) {
            Pl$LeashData.leashHolder = this.level().getEntity(Pl$LeashData.delayedLeashHolderId);
        }
        return Pl$LeashData.leashHolder;
    }

    /**
     * 数据整理 -> 如果Pl$LeashData非null，最终Pl$LeashData的leashHolder将不为null
     */
    @Unique
    private void Pl$RestoreLeashFormSave() {
        assert this.Pl$LeashData != null;
        if(!(this.level() instanceof ServerLevel)) {
            //非服务器端退出
            return;
        }

        if(this.Pl$LeashData.delayedLeashInfo == null) {
            //delayedLeashInfo无数据
            if(Pl$LeashData.leashHolder != null) {//且LeashHolder不为null，则直接用它
                setLeashedTo(Pl$LeashData.leashHolder, true);
                return;
            }  return;

        }
        if(this.Pl$LeashData.delayedLeashInfo.left().isPresent()) {
            //如果有实体的UUID（一般是LivingEntity），则在服务器其通过UUID来查找实体
            Entity entity = ((ServerLevel) this.level()).getEntity(this.Pl$LeashData.delayedLeashInfo.left().get());
            if(entity != null) {
                setLeashedTo(entity, true);
            }
        } else if(this.Pl$LeashData.delayedLeashInfo.right().isPresent()) {
            //如果有实体的坐标（一般就是拴绳结），在服务器端获取拴绳结实体（通过给定坐标和维度获取）
            setLeashedTo(LeashFenceKnotEntity.getOrCreateKnot(this.level(), this.Pl$LeashData.delayedLeashInfo.right().get()), true);
        }

    }

    @org.jetbrains.annotations.Nullable
    @Override
    public LeashData getLeashData() {
        return Pl$LeashData;
    }
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public LeashData getLeashDataFromEntityData() {
        CompoundTag compoundTag = this.entityData.get(Pl$LEASH_DATA);
        return readLeashData(compoundTag);
    }
    @Override
    public void setLeashData(@org.jetbrains.annotations.Nullable Leashable.LeashData pLeashData) {
        this.Pl$LeashData = pLeashData;
        saveLeashData(pLeashData);
    }
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Unique
    private void saveLeashData(@org.jetbrains.annotations.Nullable LeashData pLeashData) {
        CompoundTag compoundTag = new CompoundTag();
        this.writeLeashData(compoundTag, pLeashData);

        this.entityData.set(Pl$LEASH_DATA, compoundTag);
    }
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public boolean canBeLeashedInstantly(Player player) {
        return !isLeashed();
    }
    @Inject(
            method = {"defineSynchedData"}, at = {@At("TAIL")}
    )
    //定义Client/Server player 同步数据
    private void defineSyncData (SynchedEntityData.Builder pBuilder, CallbackInfo ci) {
        CompoundTag leashCompoundTag = new CompoundTag();
        this.writeLeashData(leashCompoundTag, null);
        pBuilder.define(Pl$LEASH_DATA, leashCompoundTag);
    }
    @Inject(
            method = {"addAdditionalSaveData"}, at = {@At("RETURN")}
    )//数据保存
    private void addSaveData(CompoundTag pCompound, CallbackInfo ci) {
        CompoundTag pLeashTag = new CompoundTag();
        writeLeashData(pLeashTag, Pl$LeashData);
        pCompound.put("Pl$LeashData", pLeashTag);
        this.entityData.set(Pl$LEASH_DATA, pLeashTag);
    }
    @Inject(
            method = {"readAdditionalSaveData"}, at = {@At("RETURN")}
    )//数据读取
    private void readSaveData(CompoundTag pCompound, CallbackInfo ci) {
        if(pCompound.contains("Pl$LeashData")) {
            CompoundTag pl$LeashData = pCompound.getCompound("Pl$LeashData");
            this.entityData.set(Pl$LEASH_DATA, pl$LeashData);
            Pl$LeashData = readLeashData(pl$LeashData);
        }
    }


}
