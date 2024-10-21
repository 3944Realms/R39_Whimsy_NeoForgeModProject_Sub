package com.r3944realms.leashedplayer.datagen.provider.attributes;

import com.r3944realms.leashedplayer.LeashedPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;

public class ModPaintingVariants {
    public static final ResourceKey<PaintingVariant> GROUP_PHOTO = create("group_photo");

    public static void bootstrap(BootstrapContext<PaintingVariant> pContext) {
        PaintingVariantBootstrap(pContext);
    }
    public static void PaintingVariantBootstrap(BootstrapContext<PaintingVariant> pContext) {
        ModPaintingVariants.register(pContext, ModPaintingVariants.GROUP_PHOTO, 4, 3);
    }
    private static void register(BootstrapContext<PaintingVariant> pContext, ResourceKey<PaintingVariant> pKey, int pWidth, int pHeight) {
        pContext.register(pKey, new PaintingVariant(pWidth, pHeight, pKey.location()));
    }
    private static ResourceKey<PaintingVariant> create(String pName) {
        return ResourceKey.create(Registries.PAINTING_VARIANT, ResourceLocation.fromNamespaceAndPath(LeashedPlayer.MOD_ID, pName));
    }

    public static String getPaintingVariantTitleKey(ResourceKey<PaintingVariant> pKey) {
        return "painting." + pKey.location().getNamespace() + "." + pKey.location().getPath() + ".title";
    }

    public static String getPaintingVariantAuthorKey(ResourceKey<PaintingVariant> pKey) {
        return "painting." + pKey.location().getNamespace() + "." + pKey.location().getPath() + ".author";
    }

    public static String getPaintingVariantTitleKey(String pName) {
        return "painting." + LeashedPlayer.MOD_ID + "." + pName + ".title";
    }

    public static String getPaintingVariantAuthorKey(String pName) {
        return "painting." + LeashedPlayer.MOD_ID + "." + pName + ".author";
    }
}
