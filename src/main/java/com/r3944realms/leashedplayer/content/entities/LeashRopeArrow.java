package com.r3944realms.leashedplayer.content.entities;

import com.r3944realms.leashedplayer.config.LeashPlayerCommonConfig;
import com.r3944realms.leashedplayer.content.gamerules.GameruleRegistry;
import com.r3944realms.leashedplayer.content.gamerules.Server.KeepLeashNotDropTime;
import com.r3944realms.leashedplayer.content.items.LeashRopeArrowItem;
import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import com.r3944realms.leashedplayer.modInterface.ILivingEntityExtension;
import com.r3944realms.leashedplayer.modInterface.PlayerLeashable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
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
    private static final int maxLifeTime = LeashPlayerCommonConfig.TheLeashArrowMaxLifeTime.get();
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
    public static boolean isLeashRopeArrow(ItemStack bowStack, LivingEntity entity) {
        if (entity instanceof Player player) {

            // 获取将要发射的弹药
            ItemStack projectileStack = player.getProjectile(bowStack);

            // 判断该弹药是否为改箭
            return projectileStack.getItem() instanceof LeashRopeArrowItem;
        }
        return false;
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
        //时间1.40 禁止
        //时间2.240
    //          如果(非仅创造拾取)
            //      如果 (按Shift )
            //          如果(拥有者) -> 拾取到完整箭，取消绑定(super给父类处理)
            //          否则:时间仍为原需时间 ->不能获取完整的箭，重绑定（当前拥有者的Holder是否为本箭，“是”才重绑定）
            //      否则: 禁止
        //      否则:
            //      如果 (按Shift )
            //          如果(拥有者) -> 且拾取到完整箭，取消绑定
            //          否则:时间仍为原需时间 ->不能获取完整的箭，重绑定
            //      否则: 禁止
        //时间3
    //          如果(拥有者) -> 拾取到完整箭，取消绑定
    //          否则:不能获取完整的箭，重绑定

        if(life <= 40 ) {
           return false;
        }
        else {
            PlayerLeashable playerLeashable = (PlayerLeashable) pPlayer;
            if(life <= 240) {
                if(pPlayer.isShiftKeyDown()) {
                    Entity leashDataEntity = PlayerLeashable.getLeashDataEntity((ServerPlayer) this.getOwner(), (ServerLevel) level());
                    if(this.ownedBy(pPlayer)) {
                        this.pickup = Pickup.ALLOWED;
                        if(this.equals(leashDataEntity)) playerLeashable.dropLeash(true, false);
                    } else {
                        if(life >= 120) {
                            Entity owner = getOwner();
                            if( owner != null ) {
                                if(this.equals(leashDataEntity)) {
                                    ((PlayerLeashable) owner).setLeashedTo(pPlayer, true);
                                    ItemEntity itemEntity = new ItemEntity(level(), getX(), getY(), getZ(), Items.ARROW.getDefaultInstance());
                                    level().addFreshEntity(itemEntity);
                                    discard();
                                }
                            } else return true;
                        } else return false;
                    }
                } else return false;

            }
            else {
                Entity leashDataEntity = PlayerLeashable.getLeashDataEntity((ServerPlayer) this.getOwner(), (ServerLevel) level());
                if(this.ownedBy(pPlayer)) {
                    this.pickup = Pickup.ALLOWED;
                    if(this.equals(leashDataEntity)) playerLeashable.dropLeash(true, false);
                } else {
                    if(this.equals(leashDataEntity)) {
                        Entity owner = getOwner();
                        ((PlayerLeashable)owner).setLeashedTo(pPlayer, true);
                        ItemEntity itemEntity = new ItemEntity(level(), getX(), getY(), getZ(), Items.ARROW.getDefaultInstance());
                        level().addFreshEntity(itemEntity);
                        discard();
                    }

                }
            }
        }

        return super.tryPickup(pPlayer);
    }


    @Override
    protected void tickDespawn() {
        this.life++;
        if (this.life >= maxLifeTime) {
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
                    if(leashDataEntity != null) pL.dropLeash(true, !(leashDataEntity instanceof LeashRopeArrow));
                    Entity leashKnotFence = PlayerLeashable.createLeashKnotFence((ServerLevel) this.level(), pResult.getBlockPos());
                    ILivingEntityExtension pLL = (ILivingEntityExtension) pL;
                    pLL.setKeepLeashTick(GameruleRegistry.getGameruleIntValue(level(), KeepLeashNotDropTime.ID));
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
            Entity entity = pResult.getEntity();
            if(entity instanceof LivingEntity){
                if(entity.equals(this.getOwner())) return;
                if (getOwner() instanceof PlayerLeashable pL) {
                    Entity leashDataEntity = PlayerLeashable.getLeashDataEntity((ServerPlayer) getOwner(), (ServerLevel) level());
                    if(leashDataEntity != null) pL.dropLeash(true, !(leashDataEntity instanceof LeashRopeArrow));
                    ItemEntity arrow = new ItemEntity(this.level(), this.position().x, this.position().y, this.position().z, Items.ARROW.getDefaultInstance());
                    ILivingEntityExtension pLL = (ILivingEntityExtension) pL;
                    pLL.setKeepLeashTick(GameruleRegistry.getGameruleIntValue(level(), KeepLeashNotDropTime.ID));
                    pL.setLeashedTo(pResult.getEntity(), true);
                    this.level().addFreshEntity(arrow);
                    discard();
                }
            } else if (entity instanceof LeashFenceKnotEntity leashKnotFence) {
                if (getOwner() instanceof PlayerLeashable pL) {
                    Entity leashDataEntity = PlayerLeashable.getLeashDataEntity((ServerPlayer) getOwner(), (ServerLevel) level());
                    if(leashDataEntity != null) pL.dropLeash(true, true);
                    ItemEntity arrow = new ItemEntity(this.level(), this.position().x, this.position().y, this.position().z, Items.ARROW.getDefaultInstance());
                    ILivingEntityExtension pLL = (ILivingEntityExtension) pL;
                    pLL.setKeepLeashTick(GameruleRegistry.getGameruleIntValue(level(), KeepLeashNotDropTime.ID));
                    pL.setLeashedTo(leashKnotFence, true);
                    this.level().addFreshEntity(arrow);
                    discard();
                    return;
                }
            }
        }
        super.onHitEntity(pResult);
    }
}
