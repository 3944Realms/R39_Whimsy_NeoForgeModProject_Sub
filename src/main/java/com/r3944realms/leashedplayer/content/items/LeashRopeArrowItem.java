package com.r3944realms.leashedplayer.content.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class LeashRopeArrowItem extends ArrowItem {
    public static final String descKey = "item.leash_rope_arrow.description";
    public LeashRopeArrowItem(Item.Properties pProperties) {
        super(pProperties);
    }

    public @NotNull AbstractArrow createArrow(@NotNull Level pLevel, @NotNull ItemStack pAmmo, @NotNull LivingEntity pShooter, @Nullable ItemStack pWeapon) {
        return new com.r3944realms.leashedplayer.content.entities.LeashRopeArrow(pShooter, pLevel, pAmmo.copyWithCount(1), pWeapon);
    }

    @Override
    public @NotNull Component getDescription() {
        return Component.translatable(descKey).withStyle(ChatFormatting.GRAY);
    }
}
