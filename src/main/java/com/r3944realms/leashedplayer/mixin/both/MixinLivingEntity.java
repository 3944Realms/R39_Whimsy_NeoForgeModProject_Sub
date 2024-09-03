package com.r3944realms.leashedplayer.mixin.both;

import com.r3944realms.leashedplayer.modInterface.ILivingEntityExtension;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements ILivingEntityExtension {
    public MixinLivingEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    @Unique
    @SuppressWarnings("WrongEntityDataParameterClass")
    private static final EntityDataAccessor<Float> DATA_ENTITY_LEASH_LENGTH = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.FLOAT);

    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public float getLeashLength() {
        return this.entityData.get(DATA_ENTITY_LEASH_LENGTH);
    }
    @SuppressWarnings("AddedMixinMembersNamePattern")
    @Override
    public void setLeashLength(float length) {
        this.entityData.set(DATA_ENTITY_LEASH_LENGTH, length);
    }

    @Inject(method = {"defineSynchedData"}, at = {@At("TAIL")})
    //定义Client/Server实体同步数据
    private void defineSyncData(SynchedEntityData.Builder pBuilder, CallbackInfo ci) {
        pBuilder.define(DATA_ENTITY_LEASH_LENGTH, 5.0F);
    }
    @Inject(method = {"readAdditionalSaveData"}, at = {@At("RETURN")})
    private void readSaveData(CompoundTag compoundTag, CallbackInfo callbackInfo) {
        if(compoundTag.contains("LeashLength")) {
            this.setLeashLength(compoundTag.getFloat("LeashLength"));
        }
    }
    @Inject(method = {"addAdditionalSaveData"}, at = {@At("RETURN")})
    private void addSaveData(CompoundTag compoundTag, CallbackInfo callbackInfo) {
        compoundTag.putFloat("LeashLength", getLeashLength());
    }
}
