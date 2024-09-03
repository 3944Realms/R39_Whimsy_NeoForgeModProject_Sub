package com.r3944realms.leashedplayer.mixin.client;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer {

    @Redirect(
            method = {"renderLeash"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;getLeashOffset(F)Lnet/minecraft/world/phys/Vec3;"
            )
    )
    private @NotNull Vec3 ret(Entity instance, float pPartialTick) {
        if(instance instanceof AbstractClientPlayer) {
            //为了使拴绳在在第三视角下位于玩家脖子处
            return  instance.getLeashOffset(pPartialTick).add(0, -0.2, -0.2);
        }
        return instance.getLeashOffset(pPartialTick);//非实现这个接口则不变
    }
}
