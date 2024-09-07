package com.r3944realms.leashedplayer.content.gamerules.Server;

import com.r3944realms.leashedplayer.LeashedPlayer;
import com.r3944realms.leashedplayer.content.gamerules.Gamerules;
import com.r3944realms.leashedplayer.utils.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.function.BiConsumer;

import static com.r3944realms.leashedplayer.content.gamerules.Gamerules.GAMERULE_REGISTRY;

@EventBusSubscriber(modid = LeashedPlayer.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class KeepLeashNotDropTime {
    public static final int DEFAULT_VALUE = 240;
    public static final String ID = Util.getGameruleName(KeepLeashNotDropTime.class);
    public static final String DESCRIPTION_KEY = Gamerules.getDescriptionKey(KeepLeashNotDropTime.class);
    public static final String NAME_KEY = Gamerules.getNameKey(KeepLeashNotDropTime.class);
    public static final GameRules.Category CATEGORY = GameRules.Category.MISC;

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        GAMERULE_REGISTRY.registerGamerule(ID, CATEGORY, DEFAULT_VALUE, 80, 1200, (BiConsumer<MinecraftServer, GameRules.IntegerValue>) (i, j)->{});
    }
}
