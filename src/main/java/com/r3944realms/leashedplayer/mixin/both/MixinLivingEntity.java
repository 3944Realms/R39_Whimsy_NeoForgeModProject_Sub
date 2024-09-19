package com.r3944realms.leashedplayer.mixin.both;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//OpenJDK21 JIT 激进的优化将TickHeadTurnAssign优化成死循环了

/**
 * 保证 while 循环的条件永远不成立的
 * @author ustc-zzzz
 */
@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
    @Shadow
    public float yBodyRot;
    @Shadow
    public float yBodyRotO;

    @Shadow
    public float yHeadRot;
    @Shadow
    public float yHeadRotO;

    public MixinLivingEntity(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(
            method = "tick()V",
            at = @At(value = "INVOKE_ASSIGN", target = "tickHeadTurn(FF)F")
    )
    public void onTickHeadTurnAssign(CallbackInfo ignored) {
        var yRot = this.getYRot();
        this.yRotO = yRot - Mth.wrapDegrees(yRot - this.yRotO);
        this.yBodyRotO = this.yBodyRot - Mth.wrapDegrees(this.yBodyRot - this.yBodyRotO);
        var xRot = this.getXRot();
        this.xRotO = xRot - Mth.wrapDegrees(xRot - this.xRotO);
        this.yHeadRotO = this.yHeadRot - Mth.wrapDegrees(this.yHeadRot - this.yHeadRotO);
    }
}
