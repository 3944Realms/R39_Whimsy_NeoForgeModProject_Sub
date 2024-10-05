package com.r3944realms.leashedplayer.content.items;

import com.r3944realms.leashedplayer.LeashedPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

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
                pOutput.accept(ModItemRegister.LEASH_ROPE_ARROW.get());
                pOutput.accept(ModItemRegister.SPECTRAL_LEASH_ROPE_ARROW.get());
            })).build());
    public static String getCreativeMod(@NotNull String tabs) {
        return LEASHED_PLAYER_TAB_STRING + "." + tabs;
    }
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
