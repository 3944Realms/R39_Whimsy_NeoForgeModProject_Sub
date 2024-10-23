package com.r3944realms.leashedplayer.content.effects;

import com.r3944realms.leashedplayer.LeashedPlayer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModPotionRegister {
    public static DeferredRegister<Potion> POTIONS = DeferredRegister.create(Registries.POTION, LeashedPlayer.MOD_ID);
    public static final DeferredHolder<Potion, Potion> NO_LEASH = register("no_leash",
            () -> new Potion("no_leash", new MobEffectInstance(ModEffectRegister.NO_LEASH_EFFECT, 7200, 0))
    );
    public static <T extends Potion>DeferredHolder<Potion, Potion> register(String Name, Supplier<T> supplier) {
        return POTIONS.register(Name, supplier);
    }
    /**
     *
     * @param name the Name of Potion
     * @param type (char)<br/> [ <br/> 0 & 3 ~ 255 : potion <br/>1 : lingering_potion <br/>2 : splash_potion<br/>]
     * @return Language Key
     */
    public static String getPotionNameKey(String name, char type) {
        return "item.minecraft." +
                (type == 1 ? "lingering_potion" :
                        (type == 2 ? "splash_potion" : "potion")
                )
                + ".effect." + name;
    }
    public static String getTippedArrowNameKey(String Name) {
        return "item.minecraft.tipped_arrow.effect." + Name;
    }

    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }
}
