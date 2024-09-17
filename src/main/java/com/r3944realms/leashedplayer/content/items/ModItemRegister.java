package com.r3944realms.leashedplayer.content.items;

import com.r3944realms.leashedplayer.LeashedPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModItemRegister {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(BuiltInRegistries.ITEM, LeashedPlayer.MOD_ID);
    public static final List<Supplier<Item>> ITEM_SUPPLIER = new ArrayList<>();
    public static final Supplier<Item> LEASH_ROPE_ARROW = ModItemRegister.register("leash_rope_arrow",
            () -> new LeashRopeArrowItem(new Item.Properties().stacksTo(16))
    );

    public static final Supplier<Item> FABRIC = ModItemRegister.register("fabric",
            () -> new TestItem(new Item.Properties().stacksTo(1))
    );

    public static Supplier<Item> register(String name, Supplier<Item> supplier) {
        return register(name, supplier, true);
    }

    public static Supplier<Item> register(String name, Supplier<Item> supplier, boolean shouldJoinSupplierLists) {
        Supplier<Item> supplierItem = ITEMS.register(name, supplier);
        if(shouldJoinSupplierLists) ITEM_SUPPLIER.add(supplierItem);
        return supplierItem;
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
