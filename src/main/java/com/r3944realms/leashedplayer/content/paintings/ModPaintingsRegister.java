package com.r3944realms.leashedplayer.content.paintings;

import com.r3944realms.leashedplayer.LeashedPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ModPaintingsRegister {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANT =
            DeferredRegister.create(Registries.PAINTING_VARIANT, LeashedPlayer.MOD_ID);
    public static final Supplier<PaintingVariant> GROUP_PHOTO = PAINTING_VARIANT.register("group_photo", () -> new PaintingVariant(1920, 1080, getAssetId("group_photo")));

    private static @NotNull ResourceLocation getAssetId( String paint_name) {
        return ResourceLocation.fromNamespaceAndPath(LeashedPlayer.MOD_ID, "textures/painting/"+paint_name+".png");
    }

    public static void register(IEventBus eventBus) {
        PAINTING_VARIANT.register(eventBus);
    }
}
