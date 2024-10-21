package com.r3944realms.leashedplayer.content.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.r3944realms.leashedplayer.config.LeashPlayerCommonConfig;
import com.r3944realms.leashedplayer.network.client.UpdatePlayerMovement;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static com.r3944realms.leashedplayer.content.commands.Command.getLiterArgumentBuilderOfCSS;

public class MotionCommand {
    private final static String LEASHED_PLAYER_MOTION_MESSAGE_ = "leashedplayer.command.motion.message.";
    public final static String MOTION_SETTER_SUCCESSFUL = LEASHED_PLAYER_MOTION_MESSAGE_ + "setter.successful",
            MOTION_ADDER_SUCCESSFUL = LEASHED_PLAYER_MOTION_MESSAGE_ + "adder.successful",
            MOTION_MULTIPLY_SUCCESSFUL = LEASHED_PLAYER_MOTION_MESSAGE_ + "multiply.successful";
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        boolean shouldUsePrefix = LeashPlayerCommonConfig.EnableLeashPlayerCommandPrefix.get();
        @Nullable List<LiteralArgumentBuilder<CommandSourceStack>> nodeList = shouldUsePrefix ? null : new ArrayList<>();
        LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder = Commands.literal(com.r3944realms.leashedplayer.content.commands.Command.PREFIX);
        LiteralArgumentBuilder<CommandSourceStack> $$motionRoot = getLiterArgumentBuilderOfCSS("motion", !shouldUsePrefix, nodeList);
        com.mojang.brigadier.Command<CommandSourceStack> motionVecAdder = context -> {
            CommandSourceStack source = context.getSource();
            for(Entity entity : EntityArgument.getEntities(context, "targets")){
                Vec3 motionVec = new Vec3(
                        DoubleArgumentType.getDouble(context, "vecX"),
                        DoubleArgumentType.getDouble(context, "vecY"),
                        DoubleArgumentType.getDouble(context, "vecZ")
                );
                if(entity instanceof ServerPlayer player) {
                    PacketDistributor.sendToPlayer(player, new UpdatePlayerMovement(UpdatePlayerMovement.Operation.ADD, motionVec.x, motionVec.y, motionVec.z));
                } else {
                    entity.addDeltaMovement(motionVec);
                }
                double vecX = entity.getDeltaMovement().x, vecY = entity.getDeltaMovement().y, vecZ = entity.getDeltaMovement().z;
                source.sendSuccess(() -> Component.translatable(MOTION_ADDER_SUCCESSFUL, entity.getName().copy().withStyle(), vecX, vecY, vecZ), true);
            }
            return 0;
        };
        Command<CommandSourceStack> motionVecSetter = context -> {
            CommandSourceStack source = context.getSource();
            for(Entity entity : EntityArgument.getEntities(context, "targets")){
                Vec3 motionVec = new Vec3(
                        DoubleArgumentType.getDouble(context, "vecX"),
                        DoubleArgumentType.getDouble(context, "vecY"),
                        DoubleArgumentType.getDouble(context, "vecZ")
                );
                if(entity instanceof ServerPlayer player) {
                    PacketDistributor.sendToPlayer(player, new UpdatePlayerMovement(UpdatePlayerMovement.Operation.SET, motionVec.x, motionVec.y, motionVec.z));
                } else {
                    entity.setDeltaMovement(motionVec);
                }
                double vecX = entity.getDeltaMovement().x, vecY = entity.getDeltaMovement().y, vecZ = entity.getDeltaMovement().z;
                source.sendSuccess(() -> Component.translatable(MOTION_SETTER_SUCCESSFUL, entity.getName().copy(), vecX, vecY, vecZ), true);
            }
            return 0;
        };
        Command<CommandSourceStack> motionVecMultiply = context -> {
            CommandSourceStack source = context.getSource();
            for(Entity entity : EntityArgument.getEntities(context, "targets")){
                Vec3 motionFactorVec = new Vec3(
                        DoubleArgumentType.getDouble(context, "vecXFactor"),
                        DoubleArgumentType.getDouble(context, "vecYFactor"),
                        DoubleArgumentType.getDouble(context, "vecZFactor")
                );
                if(entity instanceof ServerPlayer player) {
                    PacketDistributor.sendToPlayer(player, new UpdatePlayerMovement(UpdatePlayerMovement.Operation.MULTIPLY, motionFactorVec.x, motionFactorVec.y, motionFactorVec.z));
                } else {
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(motionFactorVec));
                }
                double vecX = entity.getDeltaMovement().x, vecY = entity.getDeltaMovement().y, vecZ = entity.getDeltaMovement().z;
                source.sendSuccess(() -> Component.translatable(MOTION_MULTIPLY_SUCCESSFUL, entity.getName().copy(), vecX, vecY, vecZ), true);
            }
            return 0;
        };

        LiteralArgumentBuilder<CommandSourceStack> Motion = $$motionRoot.requires(cs -> cs.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .then(Commands.literal("add")
                                .then(Commands.argument("vecX", DoubleArgumentType.doubleArg())
                                        .then(Commands.argument("vecY", DoubleArgumentType.doubleArg())
                                                .then(Commands.argument("vecZ", DoubleArgumentType.doubleArg())
                                                        .executes(motionVecAdder)
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("set")
                                .then(Commands.argument("vecX", DoubleArgumentType.doubleArg())
                                        .then(Commands.argument("vecY", DoubleArgumentType.doubleArg())
                                                .then(Commands.argument("vecZ", DoubleArgumentType.doubleArg())
                                                        .executes(motionVecSetter)
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("multiply")
                                .then(Commands.argument("vecXFactor", DoubleArgumentType.doubleArg())
                                        .then(Commands.argument("vecYFactor", DoubleArgumentType.doubleArg())
                                                .then(Commands.argument("vecZFactor", DoubleArgumentType.doubleArg())
                                                        .executes(motionVecMultiply)
                                                )
                                        )
                                )
                        )
                );
        if(shouldUsePrefix){
            literalArgumentBuilder.then(Motion);
            dispatcher.register(literalArgumentBuilder);
        } else {
            nodeList.forEach(dispatcher::register);
        }
    }
}
