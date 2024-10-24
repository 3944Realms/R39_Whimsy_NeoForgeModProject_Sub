package com.r3944realms.leashedplayer.content.items;

import com.r3944realms.leashedplayer.LeashedPlayer;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LeashedPlayer.MOD_ID);
    public static final String LEASHED_PLAYER_TAB_STRING = "creativetab." + LeashedPlayer.MOD_ID;
    public static final String LEASHED_PLAYER_ITEM = "leashedplayer_tab";
    public static final Supplier<CreativeModeTab> TEST_TAB = CREATIVE_MODE_TABS.register(LEASHED_PLAYER_ITEM,() -> CreativeModeTab.builder()
            .title(Component.translatable(getCreativeMod(LEASHED_PLAYER_ITEM)))
            .icon(() -> ModItemRegister.LEASH_ROPE_ARROW.get().getDefaultInstance())
            .displayItems(((pParameters, pOutput) -> {
                pOutput.accept(Items.LEAD);
                pOutput.accept(Items.BOW);
                pOutput.accept(Items.CROSSBOW);
                pOutput.accept(ModItemRegister.LEASH_ROPE_ARROW.get());
                pOutput.accept(ModItemRegister.SPECTRAL_LEASH_ROPE_ARROW.get());
                HolderLookup.RegistryLookup<Potion> potionRegistryLookup = CommonHooks.resolveLookup(Registries.POTION);
                if(potionRegistryLookup != null) {
                    potionRegistryLookup.listElements()
                            .filter(p_337926_ -> Objects.requireNonNull(p_337926_.getKey()).location().getNamespace().equals(LeashedPlayer.MOD_ID))
                            .map(p_330083_ -> PotionContents.createItemStack(Items.POTION, p_330083_))
                            .forEach(pOutput::accept);
                }
            })).build());
    public static String getCreativeMod(@NotNull String tabs) {
        return LEASHED_PLAYER_TAB_STRING + "." + tabs;
    }
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
