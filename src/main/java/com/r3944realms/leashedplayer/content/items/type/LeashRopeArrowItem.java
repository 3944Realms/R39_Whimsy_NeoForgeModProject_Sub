package com.r3944realms.leashedplayer.content.items.type;

import com.r3944realms.leashedplayer.content.entities.LeashRopeArrow;
import com.r3944realms.leashedplayer.content.entities.ModEntityRegister;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class LeashRopeArrowItem extends ArrowItem implements ILeashRopeArrow{
    public static final String descKey = "item.leash_rope_arrow.description";
    public LeashRopeArrowItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public @NotNull AbstractArrow createArrow(@NotNull Level pLevel, @NotNull ItemStack pAmmo, @NotNull LivingEntity pShooter, @Nullable ItemStack pWeapon) {
        return new com.r3944realms.leashedplayer.content.entities.LeashRopeArrow(ModEntityRegister.LEASH_ROPE_ARROW.get(),pShooter, pLevel, pAmmo.copyWithCount(1), pWeapon);
    }

    @Override
    public @NotNull Projectile asProjectile(@NotNull Level pLevel, @NotNull Position pPos, @NotNull ItemStack pStack, @NotNull Direction pDirection) {
        LeashRopeArrow arrow = new LeashRopeArrow(ModEntityRegister.LEASH_ROPE_ARROW.get(), pPos.x(), pPos.y(), pPos.z(), pLevel, this.getDefaultInstance(),null, null);
        arrow.pickup = AbstractArrow.Pickup.DISALLOWED;
        return arrow;
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable(descKey).withStyle(ChatFormatting.GRAY);
    }
    public void appendHoverText(@NotNull ItemStack pStack, Item.@NotNull TooltipContext pContext, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        //TODO:也许会做
    }

}
