package com.r3944realms.leashedplayer.content.criteriaTriggers;

import com.r3944realms.leashedplayer.LeashedPlayer;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCriteriaTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, LeashedPlayer.MOD_ID);
    public static final DeferredHolder<CriterionTrigger<?>, LeashPlayerTrigger> LEASH_PLAYER_TRIGGER =
            TRIGGERS.register("leash_player", LeashPlayerTrigger::new);
    public static void register(IEventBus eventBus) {
        TRIGGERS.register(eventBus);
    }
}
