package com.r3944realms.leashedplayer.content.entities;

import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpectralLeashRopeArrow extends LeashRopeArrow {
    private int duration = 200;

    public SpectralLeashRopeArrow(EntityType<? extends AbstractArrow> entityType, double pX, double pY, double pZ, Level pLevel, ItemStack pPickupItemStack, @Nullable ItemStack pFiredFromWeapon, @Nullable ServerPlayer serverPlayer) {
        super(entityType, pX, pY, pZ, pLevel, pPickupItemStack, pFiredFromWeapon, serverPlayer);
    }

    protected SpectralLeashRopeArrow(EntityType<? extends AbstractArrow> entityType, Level pLevel) {
        super(entityType, pLevel);
    }

    public SpectralLeashRopeArrow(EntityType<? extends AbstractArrow> entityType, LivingEntity pOwner, Level pLevel, ItemStack pPickupItemStack, @Nullable ItemStack pFiredFromWeapon) {
        super(entityType, pOwner, pLevel, pPickupItemStack, pFiredFromWeapon);
    }
    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && !this.inGround) {
            this.level().addParticle(ParticleTypes.INSTANT_EFFECT, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
        }
    }
    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity pLiving) {
        super.doPostHurtEffects(pLiving);
        MobEffectInstance mobeffectinstance = new MobEffectInstance(MobEffects.GLOWING, this.duration, 0);
        pLiving.addEffect(mobeffectinstance, this.getEffectSource());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Duration")) {
            this.duration = pCompound.getInt("Duration");
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Duration", this.duration);
    }

    @Override
    protected ItemStack getOrginalItemStack() {
        return Items.SPECTRAL_ARROW.getDefaultInstance();
    }
    @Override
    protected ItemStack getSelfItemStack() {
        return ModItemRegister.SPECTRAL_LEASH_ROPE_ARROW.get().getDefaultInstance();
    }
}
