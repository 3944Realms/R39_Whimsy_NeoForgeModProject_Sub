package com.r3944realms.leashedplayer;

import com.mojang.brigadier.CommandDispatcher;
import com.r3944realms.leashedplayer.content.commands.LeashCommand;
import com.r3944realms.leashedplayer.content.commands.MotionCommand;
import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;


public class CommonEventHandler {
    @EventBusSubscriber(modid = LeashedPlayer.MOD_ID)
    public static class Game extends CommonEventHandler {
        @SubscribeEvent
        public static void onRegisterCommander(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            LeashCommand.register(dispatcher);
            MotionCommand.register(dispatcher);
        }

    }
    @EventBusSubscriber(modid = LeashedPlayer.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class Mod extends CommonEventHandler {
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            DispenserBlock.registerProjectileBehavior(ModItemRegister.LEASH_ROPE_ARROW.get());
            DispenserBlock.registerProjectileBehavior(ModItemRegister.SPECTRAL_LEASH_ROPE_ARROW.get());


        }
    }



}
