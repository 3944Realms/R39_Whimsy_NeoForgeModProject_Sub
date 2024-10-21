package com.r3944realms.leashedplayer.mixin.registry;


import com.r3944realms.leashedplayer.datagen.provider.attributes.ModPaintingVariants;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.entity.decoration.PaintingVariants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PaintingVariants.class)
public class MixinPaintingVariants {
    @Inject(method = {"bootstrap"}, at = @At("TAIL"))
    private static void bootstrap(BootstrapContext<PaintingVariant> pContext , CallbackInfo ci) {
        ModPaintingVariants.PaintingVariantBootstrap(pContext);
    }
}
