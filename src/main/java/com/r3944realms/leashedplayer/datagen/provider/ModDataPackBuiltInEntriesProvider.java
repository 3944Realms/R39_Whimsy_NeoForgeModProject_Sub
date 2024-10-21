package com.r3944realms.leashedplayer.datagen.provider;

import com.r3944realms.leashedplayer.LeashedPlayer;
import com.r3944realms.leashedplayer.datagen.provider.attributes.ModPaintingVariants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDataPackBuiltInEntriesProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.PAINTING_VARIANT, ModPaintingVariants::bootstrap);
    public ModDataPackBuiltInEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(LeashedPlayer.MOD_ID));
        CompletableFuture<HolderLookup.Provider> registryProvider = getRegistryProvider();
    }
}
