package com.r3944realms.leashedplayer.datagen.provider;

import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
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

    }


}