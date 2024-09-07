package com.r3944realms.leashedplayer.mixin.both;

import com.r3944realms.leashedplayer.modInterface.PlayerLeashable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public abstract class MixinEntity {
    @Shadow public abstract void igniteForSeconds(float pSeconds);

    /**
     * 这里重定向，当实体类实现了{@link PlayerLeashable}接口时，<br/>
     * 阻止原版的{@link Leashable}中 的tickLeash方法调用，将其<br/>
     * 我们需自己实现相关的逻辑
     * @param entity 实体
     * @param <E> 实体类型
     */
    @Redirect(
            method = "baseTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Leashable;tickLeash(Lnet/minecraft/world/entity/Entity;)V")
    )
    <E extends Entity & Leashable> void checkAndCancelIfTure(E entity) {
        if(!(entity instanceof PlayerLeashable)) {
            Leashable.tickLeash(entity);
        }
    }

}
