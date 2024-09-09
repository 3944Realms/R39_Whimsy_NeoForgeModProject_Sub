package com.r3944realms.leashedplayer.datagen.provider;

import com.r3944realms.leashedplayer.LeashedPlayer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDataPackBuiltInEntriesProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            ;
    public ModDataPackBuiltInEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(LeashedPlayer.MOD_ID));
        CompletableFuture<HolderLookup.Provider> registryProvider = getRegistryProvider();
    }
}
