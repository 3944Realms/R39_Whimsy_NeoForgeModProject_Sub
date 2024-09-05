package com.r3944realms.leashedplayer.content.entities;

import com.r3944realms.leashedplayer.LeashedPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntityRegister {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(Registries.ENTITY_TYPE, LeashedPlayer.MOD_ID);
    public static final DeferredHolder<EntityType<?> ,EntityType<LeashRopeArrow>> LEASH_ROPE_ARROW = ENTITY_TYPE.register(
            "leash_rope_arrow",
            () -> EntityType.Builder.<LeashRopeArrow>of(LeashRopeArrow::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("leash_rope_arrow")
    );
    public static String getEntityNameKey(String entityName) {
        return "entity." + LeashedPlayer.MOD_ID + "." + entityName;
    }
    public static void register(IEventBus eventBus) {
        ENTITY_TYPE.register(eventBus);
    }
}
