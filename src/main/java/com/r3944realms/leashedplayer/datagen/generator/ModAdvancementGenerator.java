package com.r3944realms.leashedplayer.datagen.generator;

import com.r3944realms.leashedplayer.LeashedPlayer;
import com.r3944realms.leashedplayer.content.criteriaTriggers.LeashPlayerTrigger;
import com.r3944realms.leashedplayer.content.entities.ModEntityRegister;
import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import com.r3944realms.leashedplayer.datagen.LanguageAndOtherData.ModAdvancementKey;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;


public class ModAdvancementGenerator implements AdvancementProvider.AdvancementGenerator {
    private final ResourceLocation ADV_BG = ResourceLocation.fromNamespaceAndPath(LeashedPlayer.MOD_ID, "textures/gui/advancements/backgrounds/leashed_player.png");
    @Override
    public void generate(HolderLookup.@NotNull Provider registries, @NotNull Consumer<AdvancementHolder> saver, @NotNull ExistingFileHelper existingFileHelper) {
        AdvancementHolder hasLeashRopeItem = Advancement.Builder.advancement().display(
                Items.LEAD,
                Component.translatable(ModAdvancementKey.LEASH_START.getNameKey()),
                Component.translatable(ModAdvancementKey.LEASH_START.getDescKey()),
                ADV_BG,
                AdvancementType.TASK,
                true,
                false,
                true
        ).addCriterion("has_leash_rope_item", InventoryChangeTrigger.TriggerInstance.hasItems(Items.LEAD))
                .save(saver, ModAdvancementKey.LEASH_START.getNameWithNameSpace());

        AdvancementHolder hasLeashRopeArrow = Advancement.Builder.advancement().display(
                        ModItemRegister.LEASH_ROPE_ARROW.get(),
                        Component.translatable(ModAdvancementKey.LEASH_ARROW.getNameKey()),
                        Component.translatable(ModAdvancementKey.LEASHED_FRIEND.getDescKey()),
                        null,
                        AdvancementType.TASK,
                        true,
                        false,
                        true
                ).addCriterion("has_leash_rope_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItemRegister.LEASH_ROPE_ARROW.get()))
                .parent(hasLeashRopeItem)
                .save(saver, ModAdvancementKey.LEASH_ARROW.getNameWithNameSpace());

        AdvancementHolder hasFlashLeashRopeArrow = Advancement.Builder.advancement().display(
                        ModItemRegister.SPECTRAL_LEASH_ROPE_ARROW.get(),
                        Component.translatable(ModAdvancementKey.ADVANCEMENT_LEASH_ARROW.getNameKey()),
                        Component.translatable(ModAdvancementKey.ADVANCEMENT_LEASH_ARROW.getDescKey()),
                        null,
                        AdvancementType.TASK,
                        true,
                        false,
                        true
                ).addCriterion("has_flash_leash_rope_item", InventoryChangeTrigger.TriggerInstance.hasItems(ModItemRegister.SPECTRAL_LEASH_ROPE_ARROW.get()))
                .parent(hasLeashRopeArrow)
                .save(saver, ModAdvancementKey.ADVANCEMENT_LEASH_ARROW.getNameWithNameSpace());

        AdvancementHolder leashedMySelf = Advancement.Builder.advancement().display(
                Items.PLAYER_HEAD,
                Component.translatable(ModAdvancementKey.LEASHED_SELF.getNameKey()),
                Component.translatable(ModAdvancementKey.LEASHED_SELF.getDescKey()),
                null,
                AdvancementType.TASK,
                true,
                true,
                true
                ).addCriterion("leash_self", LeashPlayerTrigger.TriggerInstance.LeashPlayer(
                        EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.LEASH_KNOT))
                ))
                .parent(hasLeashRopeItem).save(saver, ModAdvancementKey.LEASHED_SELF.getNameWithNameSpace());
        AdvancementHolder followLeashRopeArrow = Advancement.Builder.advancement().display(
                        ModItemRegister.LEASH_ROPE_ARROW.get(),
                        Component.translatable(ModAdvancementKey.FOLLOW_LEASH_ARROW.getNameKey()),
                        Component.translatable(ModAdvancementKey.FOLLOW_LEASH_ARROW.getDescKey()),
                        null,
                        AdvancementType.TASK,
                        true,
                        false,
                        true
                ).addCriterion("leash_arrow", LeashPlayerTrigger.TriggerInstance.LeashPlayer(
                        EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(ModEntityRegister.LEASH_ROPE_ARROW.get()))
                ))
                .parent(hasLeashRopeArrow)
                .save(saver, ModAdvancementKey.FOLLOW_LEASH_ARROW.getNameWithNameSpace());
        AdvancementHolder dogRunPlayer = Advancement.Builder.advancement().display(
                        Items.BONE,
                        Component.translatable(ModAdvancementKey.DOG_RUNNING_PLAYER.getNameKey()),
                        Component.translatable(ModAdvancementKey.DOG_RUNNING_PLAYER.getDescKey()),
                        null,
                        AdvancementType.CHALLENGE,
                        true,
                        true,
                        true
                ).addCriterion("leash_by_wo_do", LeashPlayerTrigger.TriggerInstance.LeashPlayer(
                        EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.WOLF))
                ))
                .parent(hasLeashRopeArrow).save(saver, ModAdvancementKey.DOG_RUNNING_PLAYER.getNameWithNameSpace());

        AdvancementHolder leashedOther = Advancement.Builder.advancement().display(
                        Items.LEAD,
                        Component.translatable(ModAdvancementKey.LEASHED_FRIEND.getNameKey()),
                        Component.translatable(ModAdvancementKey.LEASHED_FRIEND.getDescKey()),
                        null,
                        AdvancementType.TASK,
                        true,
                        true,
                        true
                ).addCriterion("leash_other_player",
                        LeashPlayerTrigger.TriggerInstance.LeashPlayer(
                                EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(EntityType.PLAYER))
                        )
                )
                .parent(hasLeashRopeItem).save(saver, ModAdvancementKey.LEASHED_FRIEND.getNameWithNameSpace());
    }
}
