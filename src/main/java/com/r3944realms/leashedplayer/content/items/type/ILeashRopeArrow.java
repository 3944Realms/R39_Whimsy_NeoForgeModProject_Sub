package com.r3944realms.leashedplayer.content.items.type;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface ILeashRopeArrow {
    static boolean isLeashRopeArrow(ItemStack bowStack, LivingEntity entity) {
        if (entity instanceof Player player) {

            // 获取将要发射的弹药
            ItemStack projectileStack = player.getProjectile(bowStack);

            // 判断该弹药是否为拴绳箭
            return projectileStack.getItem() instanceof ILeashRopeArrow;
        }
        return false;
    }
}
