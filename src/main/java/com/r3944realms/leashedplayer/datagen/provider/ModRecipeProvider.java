package com.r3944realms.leashedplayer.datagen.provider;

import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> future) {
        super(pOutput, future);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput pRecipeOutput) {

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItemRegister.LEASH_ROPE_ARROW.get(),1)
                .requires(Items.LEAD)
                .requires(Items.ARROW)
                .unlockedBy("has_lead",has(Items.LEAD))
                .save(pRecipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItemRegister.SPECTRAL_LEASH_ROPE_ARROW.get(),1)
                .requires(Items.LEAD)
                .requires(Items.SPECTRAL_ARROW)
                .unlockedBy("has_lead",has(Items.LEAD))
                .unlockedBy("has_spectral_arrow",has(Items.SPECTRAL_ARROW))
                .save(pRecipeOutput, "spectral_leash_rope_arrow_with_leash_rope_arrow");
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItemRegister.SPECTRAL_LEASH_ROPE_ARROW.get(),1)
                .pattern(" $ ")
                .pattern("$#$")
                .pattern(" $ ")
                .define('#', ModItemRegister.LEASH_ROPE_ARROW.get())
                .define('$', Items.GLOWSTONE_DUST)
                .unlockedBy("has_lead",has(Items.LEAD))
                .unlockedBy("has_glowstone_dust",has(Items.GLOWSTONE_DUST))
                .save(pRecipeOutput,"spectral_leash_rope_arrow_with_glowstone_dust");
    }


}