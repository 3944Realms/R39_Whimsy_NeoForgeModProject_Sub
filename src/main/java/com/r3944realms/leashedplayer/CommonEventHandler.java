package com.r3944realms.leashedplayer;

import com.mojang.brigadier.CommandDispatcher;
import com.r3944realms.leashedplayer.content.commands.LeashCommand;
import com.r3944realms.leashedplayer.content.items.ModCreativeTab;
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
        }

    }
    @EventBusSubscriber(modid = LeashedPlayer.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class Mod extends CommonEventHandler {
        @SubscribeEvent
        public static void onCommonSetup(FMLCommonSetupEvent event) {
            DispenserBlock.registerProjectileBehavior(ModItemRegister.LEASH_ROPE_ARROW.get());
            DispenserBlock.registerProjectileBehavior(ModItemRegister.SPECTRAL_LEASH_ROPE_ARROW.get());
//            NeoForge.EVENT_BUS.addListener(Mod::onPlayerTick);
        }
//        public static void onPlayerTick(PlayerTickEvent.Pre event) {
//            if (!event.getEntity().level().isClientSide && !event.getEntity().isUsingItem()) {
//                Player player = event.getEntity();
//
//                // 检测玩家视线中的实体
//                Entity entity = getEntityLookedAt(player);
//
//                // 如果实体是自定义箭实体
//                if (entity instanceof LeashRopeArrow la) {
//
//                    // 处理玩家右键点击箭的交互逻辑
//                    if (player.isShiftKeyDown()) { // 可以结合 Shift 键
//                        if(la.pickup == AbstractArrow.Pickup.ALLOWED || ( player.isCreative() && la.pickup != AbstractArrow.Pickup.DISALLOWED )){
//                            player.take(la, 1);
//                            player.addItem(ModItemRegister.LEASH_ROPE_ARROW.get().getDefaultInstance());
//                        }
//                    }
//                }
//            }
//        }
//
//        // 射线检测方法，获取玩家视线内的实体//TODO：分离 Maybe XD
//        private static Entity getEntityLookedAt(Player player) {
//            double reach = 5.0D; // 设置射线检测距离 //TODO:与Player互动距离同步
//            Vec3 eyePosition = player.getEyePosition(1.0F);
//            Vec3 lookVector = player.getViewVector(1.0F).scale(reach);
//            Vec3 targetPosition = eyePosition.add(lookVector);
//
//            // 创建一个射线并检测结果
//            AABB boundingBox = player.getBoundingBox().expandTowards(lookVector).inflate(1.0D, 1.0D, 1.0D);
//            EntityHitResult hitResult = ProjectileUtil.getEntityHitResult(player, eyePosition, targetPosition, boundingBox, e -> e instanceof LeashRopeArrow, reach);
//
//            return hitResult == null ? null : hitResult.getEntity();
//        }
//    }
    }



}
