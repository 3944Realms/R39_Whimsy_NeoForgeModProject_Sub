package com.r3944realms.leashedplayer.content.gamerules.Server;

import com.r3944realms.leashedplayer.LeashedPlayer;
import com.r3944realms.leashedplayer.content.gamerules.Gamerules;
import com.r3944realms.leashedplayer.utils.Util;
import net.minecraft.world.level.GameRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.r3944realms.leashedplayer.content.gamerules.Gamerules.GAMERULE_REGISTRY;

@EventBusSubscriber(modid = LeashedPlayer.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DisablePlayerMoveCheck {
    public static final boolean DEFAULT_VALUE = true;
    public static final String ID = Util.getGameruleName(DisablePlayerMoveCheck.class);
    public static final String DESCRIPTION_KEY = Gamerules.getDescriptionKey(DisablePlayerMoveCheck.class);
    public static final String NAME_KEY = Gamerules.getNameKey(DisablePlayerMoveCheck.class);
    public static final GameRules.Category CATEGORY = GameRules.Category.PLAYER;

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        GAMERULE_REGISTRY.registerGamerule(ID, CATEGORY, DEFAULT_VALUE);
    }
}