package com.r3944realms.leashedplayer.content.entities;

import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import com.r3944realms.leashedplayer.modInterface.PlayerLeashable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LeashRopeArrow extends AbstractArrow  {
    protected LeashRopeArrow(EntityType<? extends AbstractArrow> entityType,Level pLevel) {
        super(entityType, pLevel);
    }

    protected LeashRopeArrow(double pX, double pY, double pZ, Level pLevel, ItemStack pPickupItemStack, @Nullable ItemStack pFiredFromWeapon, @Nullable ServerPlayer serverPlayer) {
        super(ModEntityRegister.LEASH_ROPE_ARROW.get(), pX, pY, pZ, pLevel, pPickupItemStack, pFiredFromWeapon);
        if(serverPlayer != null && !level().isClientSide) {
            ((PlayerLeashable)serverPlayer).setLeashedTo(this, true);
        }
    }

    public LeashRopeArrow(LivingEntity pOwner, Level pLevel, ItemStack pPickupItemStack, @Nullable ItemStack pFiredFromWeapon) {
        super(ModEntityRegister.LEASH_ROPE_ARROW.get(), pOwner, pLevel, pPickupItemStack, pFiredFromWeapon);
        if(pOwner instanceof PlayerLeashable lPlayer && !level().isClientSide) {
            lPlayer.setLeashedTo(this, true);
        }
    }

    @Override
    protected @NotNull ItemStack getDefaultPickupItem() {
        return ModItemRegister.LEASH_ROPE_ARROW.get().getDefaultInstance();
    }

    @Override
    public void setOwner(@Nullable Entity pEntity) {
        super.setOwner(pEntity);

        this.pickup = this.pickup == Pickup.CREATIVE_ONLY ? this.pickup : Pickup.DISALLOWED;
    }

    @Override
    protected boolean tryPickup(@NotNull Player pPlayer) {
        if(life <= 240) {
           return false;
        } else {
            if(life >= 480 || this.ownedBy(pPlayer) && (this.pickup != Pickup.CREATIVE_ONLY) ) {
                this.pickup = Pickup.ALLOWED;
                if(this.ownedBy(pPlayer)) {
                    ((PlayerLeashable)pPlayer).dropLeash(true, false);
                } else if (this.getOwner() instanceof PlayerLeashable pL){
                    pL.setLeashedTo(this, false);
                }
            }
            return super.tryPickup(pPlayer);
        }
    }

    @Override
    protected void tickDespawn() {
        this.life++;
        if (this.life >= 2400) {//TODO 加到配置中去，修改
            ItemEntity leash_rope_arrow = new ItemEntity(this.level(), this.position().x, this.position().y, this.position().z, ModItemRegister.LEASH_ROPE_ARROW.get().getDefaultInstance());
            this.level().addFreshEntity(leash_rope_arrow);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult pResult) {
        if(!level().isClientSide) {
            if (getOwner() instanceof PlayerLeashable pL) {
                if (this.level().getBlockState(pResult.getBlockPos()).is(BlockTags.FENCES)) {
                    Entity leashDataEntity = PlayerLeashable.getLeashDataEntity((ServerPlayer) getOwner(), (ServerLevel) level());
                    if(leashDataEntity != null) pL.dropLeash(true, true);
                    Entity leashKnotFence = PlayerLeashable.createLeashKnotFence((ServerLevel) this.level(), pResult.getBlockPos());
                    pL.setLeashedTo(leashKnotFence, true);
                    ItemEntity arrow = new ItemEntity(this.level(), this.position().x, this.position().y, this.position().z, Items.ARROW.getDefaultInstance());
                    this.level().addFreshEntity(arrow);
                    discard();
                }
            }
        }
        super.onHitBlock(pResult);

    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult pResult) {
        if(!level().isClientSide()){
            if(pResult.getEntity() instanceof LivingEntity){
                if (getOwner() instanceof PlayerLeashable pL) {
                    Entity leashDataEntity = PlayerLeashable.getLeashDataEntity((ServerPlayer) getOwner(), (ServerLevel) level());
                    if(leashDataEntity != null) pL.dropLeash(true, true);
                    pL.setLeashedTo(pResult.getEntity(), true);
                    ItemEntity arrow = new ItemEntity(this.level(), this.position().x, this.position().y, this.position().z, Items.ARROW.getDefaultInstance());
                    this.level().addFreshEntity(arrow);
                    discard();
                }
            }
        }
        super.onHitEntity(pResult);
    }
}
