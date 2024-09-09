package com.r3944realms.leashedplayer.datagen;

import com.r3944realms.leashedplayer.LeashedPlayer;
import com.r3944realms.leashedplayer.datagen.provider.*;
import com.r3944realms.leashedplayer.utils.Enum.LanguageEnum;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = LeashedPlayer.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataGeneratorHandler {
    @SubscribeEvent
    public static void genData(GatherDataEvent event) {
        CompletableFuture<HolderLookup.Provider> holderFolder = event.getLookupProvider();

        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        /*Language Provider ENGLISH CHINESE(SIM/TRA)*/
        addLanguage(event, LanguageEnum.English);
        addLanguage(event, LanguageEnum.SimpleChinese);
        addLanguage(event, LanguageEnum.TraditionalChinese);
        addLanguage(event, LanguageEnum.LiteraryChinese);
        ModDataPackBuiltInEntriesProvider(event, holderFolder);
        ItemModelGenerator(event, existingFileHelper);
        RecipeGenerator(event, holderFolder);
        ModTagsProvider(event, event.getLookupProvider(), existingFileHelper);
        ModAdvancementProvider(event, holderFolder, existingFileHelper);
    }
    private static void addLanguage(GatherDataEvent event, LanguageEnum language){
        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModLanguageProvider>) pOutput -> new ModLanguageProvider(pOutput, LeashedPlayer.MOD_ID, language)
        );
    }
    private static void ItemModelGenerator(GatherDataEvent event, ExistingFileHelper helper) {
        event.getGenerator().addProvider(
                event.includeClient(),
                (DataProvider.Factory<ModItemModelProvider>) pOutput -> new ModItemModelProvider(pOutput, LeashedPlayer.MOD_ID, helper)
        );
    }
    private static void RecipeGenerator(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> future) {
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModRecipeProvider>) pOutput -> new ModRecipeProvider(pOutput, future)
        );
    }
    private static void ModTagsProvider(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> completableFuture, ExistingFileHelper helper) {
        ModBlockTagProvider modBlockTagProvider = event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModBlockTagProvider>) pOutput ->
                        new ModBlockTagProvider(pOutput, completableFuture, LeashedPlayer.MOD_ID, helper)
        );
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModItemTagProvider>) pOutput ->
                        new ModItemTagProvider(pOutput, completableFuture, modBlockTagProvider.contentsGetter(), helper)
        );
    }

    private static void ModDataPackBuiltInEntriesProvider(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> future) {
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModDataPackBuiltInEntriesProvider>) pOutput -> new ModDataPackBuiltInEntriesProvider(pOutput, future)
        );
    }private static void ModAdvancementProvider(GatherDataEvent event, CompletableFuture<HolderLookup.Provider> pLookUpProvider, ExistingFileHelper helper) {
        event.getGenerator().addProvider(
                event.includeServer(),
                (DataProvider.Factory<ModAdvancementProvider>) pOutput -> new ModAdvancementProvider(pOutput, pLookUpProvider, helper));
    }

}
