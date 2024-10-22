package com.r3944realms.leashedplayer.content.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.r3944realms.leashedplayer.config.LeashPlayerCommonConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.TimeArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.ServerTickRateManager;
import net.minecraft.util.TimeUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.r3944realms.leashedplayer.content.commands.Command.getLiterArgumentBuilderOfCSS;

public class TickCommand {
    private static final float MAX_TICKRATE = 10000.0F;
    private static final String DEFAULT_TICKRATE = String.valueOf(20);

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        boolean shouldUsePrefix = LeashPlayerCommonConfig.EnableLeashPlayerCommandPrefix.get();
        @Nullable List<LiteralArgumentBuilder<CommandSourceStack>> nodeList = shouldUsePrefix ? null : new ArrayList<>();
        LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder = Commands.literal(com.r3944realms.leashedplayer.content.commands.Command.PREFIX);
        LiteralArgumentBuilder<CommandSourceStack> $$TickRoot = getLiterArgumentBuilderOfCSS("tick", !shouldUsePrefix, nodeList);

        LiteralArgumentBuilder<CommandSourceStack> Tick = $$TickRoot.requires(p_308941_ -> p_308941_.hasPermission(2))
                .then(Commands.literal("query").executes(p_308950_ -> tickQuery(p_308950_.getSource())))
                .then(
                        Commands.literal("rate")
                                .then(
                                        Commands.argument("rate", FloatArgumentType.floatArg(1.0F, 10000.0F))
                                                .suggests((p_308897_, p_308880_) -> SharedSuggestionProvider.suggest(new String[]{DEFAULT_TICKRATE}, p_308880_))
                                                .executes(p_309119_ -> setTickingRate(p_309119_.getSource(), FloatArgumentType.getFloat(p_309119_, "rate")))
                                )
                )
                .then(
                        Commands.literal("step")
                                .executes(p_309496_ -> step(p_309496_.getSource(), 1))
                                .then(Commands.literal("stop").executes(p_309035_ -> stopStepping(p_309035_.getSource())))
                                .then(
                                        Commands.argument("time", TimeArgument.time(1))
                                                .suggests((p_309113_, p_309105_) -> SharedSuggestionProvider.suggest(new String[]{"1t", "1s"}, p_309105_))
                                                .executes(p_308930_ -> step(p_308930_.getSource(), IntegerArgumentType.getInteger(p_308930_, "time")))
                                )
                )
                .then(
                        Commands.literal("sprint")
                                .then(Commands.literal("stop").executes(p_309190_ -> stopSprinting(p_309190_.getSource())))
                                .then(
                                        Commands.argument("time", TimeArgument.time(1))
                                                .suggests((p_308987_, p_309101_) -> SharedSuggestionProvider.suggest(new String[]{"60s", "1d", "3d"}, p_309101_))
                                                .executes(p_308904_ -> sprint(p_308904_.getSource(), IntegerArgumentType.getInteger(p_308904_, "time")))
                                )
                )
                .then(Commands.literal("unfreeze").executes(p_309184_ -> setFreeze(p_309184_.getSource(), false)))
                .then(Commands.literal("freeze").executes(p_309070_ -> setFreeze(p_309070_.getSource(), true)));
        if(shouldUsePrefix){
            literalArgumentBuilder.then(Tick);
            dispatcher.register(literalArgumentBuilder);
        } else {
            nodeList.forEach(dispatcher::register);
        }
    }

    private static String nanosToMilisString(long nanos) {
        return String.format("%.1f", (float)nanos / (float) TimeUtil.NANOSECONDS_PER_MILLISECOND);
    }

    private static int setTickingRate(CommandSourceStack source, float tickRate) {
        ServerTickRateManager servertickratemanager = source.getServer().tickRateManager();
        servertickratemanager.setTickRate(tickRate);
        String s = String.format("%.1f", tickRate);
        source.sendSuccess(() -> Component.translatable("commands.tick.rate.success", s), true);
        return (int)tickRate;
    }

    private static int tickQuery(CommandSourceStack source) {
        ServerTickRateManager servertickratemanager = source.getServer().tickRateManager();
        String s = nanosToMilisString(source.getServer().getAverageTickTimeNanos());
        float f = servertickratemanager.tickrate();
        String s1 = String.format("%.1f", f);
        if (servertickratemanager.isSprinting()) {
            source.sendSuccess(() -> Component.translatable("commands.tick.status.sprinting"), false);
            source.sendSuccess(() -> Component.translatable("commands.tick.query.rate.sprinting", s1, s), false);
        } else {
            if (servertickratemanager.isFrozen()) {
                source.sendSuccess(() -> Component.translatable("commands.tick.status.frozen"), false);
            } else if (servertickratemanager.nanosecondsPerTick() < source.getServer().getAverageTickTimeNanos()) {
                source.sendSuccess(() -> Component.translatable("commands.tick.status.lagging"), false);
            } else {
                source.sendSuccess(() -> Component.translatable("commands.tick.status.running"), false);
            }

            String s2 = nanosToMilisString(servertickratemanager.nanosecondsPerTick());
            source.sendSuccess(() -> Component.translatable("commands.tick.query.rate.running", s1, s, s2), false);
        }

        long[] along = Arrays.copyOf(source.getServer().getTickTimesNanos(), source.getServer().getTickTimesNanos().length);
        Arrays.sort(along);
        String s3 = nanosToMilisString(along[along.length / 2]);
        String s4 = nanosToMilisString(along[(int)((double)along.length * 0.95)]);
        String s5 = nanosToMilisString(along[(int)((double)along.length * 0.99)]);
        source.sendSuccess(() -> Component.translatable("commands.tick.query.percentiles", s3, s4, s5, along.length), false);
        return (int)f;
    }

    private static int sprint(CommandSourceStack source, int sprintTime) {
        boolean flag = source.getServer().tickRateManager().requestGameToSprint(sprintTime);
        if (flag) {
            source.sendSuccess(() -> Component.translatable("commands.tick.sprint.stop.success"), true);
        }

        source.sendSuccess(() -> Component.translatable("commands.tick.status.sprinting"), true);
        return 1;
    }

    private static int setFreeze(CommandSourceStack source, boolean frozen) {
        ServerTickRateManager servertickratemanager = source.getServer().tickRateManager();
        if (frozen) {
            if (servertickratemanager.isSprinting()) {
                servertickratemanager.stopSprinting();
            }

            if (servertickratemanager.isSteppingForward()) {
                servertickratemanager.stopStepping();
            }
        }

        servertickratemanager.setFrozen(frozen);
        if (frozen) {
            source.sendSuccess(() -> Component.translatable("commands.tick.status.frozen"), true);
        } else {
            source.sendSuccess(() -> Component.translatable("commands.tick.status.running"), true);
        }

        return frozen ? 1 : 0;
    }

    private static int step(CommandSourceStack source, int ticks) {
        ServerTickRateManager servertickratemanager = source.getServer().tickRateManager();
        boolean flag = servertickratemanager.stepGameIfPaused(ticks);
        if (flag) {
            source.sendSuccess(() -> Component.translatable("commands.tick.step.success", ticks), true);
        } else {
            source.sendFailure(Component.translatable("commands.tick.step.fail"));
        }

        return 1;
    }

    private static int stopStepping(CommandSourceStack source) {
        ServerTickRateManager servertickratemanager = source.getServer().tickRateManager();
        boolean flag = servertickratemanager.stopStepping();
        if (flag) {
            source.sendSuccess(() -> Component.translatable("commands.tick.step.stop.success"), true);
            return 1;
        } else {
            source.sendFailure(Component.translatable("commands.tick.step.stop.fail"));
            return 0;
        }
    }

    private static int stopSprinting(CommandSourceStack source) {
        ServerTickRateManager servertickratemanager = source.getServer().tickRateManager();
        boolean flag = servertickratemanager.stopSprinting();
        if (flag) {
            source.sendSuccess(() -> Component.translatable("commands.tick.sprint.stop.success"), true);
            return 1;
        } else {
            source.sendFailure(Component.translatable("commands.tick.sprint.stop.fail"));
            return 0;
        }
    }
}