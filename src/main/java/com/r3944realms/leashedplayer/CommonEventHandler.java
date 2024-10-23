package com.r3944realms.leashedplayer;

import com.mojang.brigadier.CommandDispatcher;
import com.r3944realms.leashedplayer.content.commands.LeashCommand;
import com.r3944realms.leashedplayer.content.commands.MotionCommand;
import com.r3944realms.leashedplayer.content.commands.TickCommand;
import com.r3944realms.leashedplayer.content.effects.ModEffectRegister;
import com.r3944realms.leashedplayer.content.effects.ModPotionRegister;
import com.r3944realms.leashedplayer.content.entities.LeashRopeArrow;
import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import com.r3944realms.leashedplayer.modInterface.PlayerLeashable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;


public class CommonEventHandler {
    @EventBusSubscriber(modid = LeashedPlayer.MOD_ID)
    public static class Game extends CommonEventHandler {
        @SubscribeEvent
        public static void onRegisterCommander(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
            LeashCommand.register(dispatcher);
            MotionCommand.register(dispatcher);
            TickCommand.register(dispatcher);
        }
        @SubscribeEvent
        public static void OnRegisterPotionBrewing(RegisterBrewingRecipesEvent event) {
            PotionBrewing.Builder builder = event.getBuilder();
            builder.addMix(Potions.WATER, Items.SLIME_BALL, ModPotionRegister.NO_LEASH);
        }
        @SubscribeEvent
        public static void OnLivingTickEvent(EntityTickEvent.Post event) {
            Entity entity = event.getEntity();
            if (entity.level().isClientSide()) {
                return;
            }
            if (entity instanceof LivingEntity living) {
                MobEffectInstance effect = living.getEffect(ModEffectRegister.NO_LEASH_EFFECT);
                if(effect != null && effect.getDuration() > 0){
                    if (entity instanceof PlayerLeashable player) {
                        if (player.getLeashHolder() != null)
                            player.dropLeash(true, !(player.getLeashHolder() instanceof LeashRopeArrow));
                    } else if (entity instanceof Leashable leashable) {
                        if (leashable.getLeashHolder() != null)
                            leashable.dropLeash(true, !(leashable.getLeashHolder() instanceof LeashRopeArrow));
                    }
                }
            }
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
