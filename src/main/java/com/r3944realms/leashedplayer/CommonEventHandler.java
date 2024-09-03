package com.r3944realms.leashedplayer;

import com.mojang.brigadier.CommandDispatcher;
import com.r3944realms.leashedplayer.content.commands.LeashCommand;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = LeashedPlayer.MOD_ID)
public class CommonEventHandler {
    @SubscribeEvent
    public static void onRegisterCommander(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        LeashCommand.register(dispatcher);
    }
}
