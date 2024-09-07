package com.r3944realms.leashedplayer.content.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.r3944realms.leashedplayer.config.LeashPlayerCommonConfig;
import com.r3944realms.leashedplayer.content.gamerules.GameruleRegistry;
import com.r3944realms.leashedplayer.content.gamerules.Server.CreateLeashFenceKnotEntityIfAbsent;
import com.r3944realms.leashedplayer.modInterface.ILivingEntityExtension;
import com.r3944realms.leashedplayer.modInterface.PlayerLeashable;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LeashCommand {
    public static final Integer MIN_VALUE = LeashPlayerCommonConfig.MinimumLeashLengthCanBeSet.get();
    public static final Integer MAX_VALUE = LeashPlayerCommonConfig.MaximumLeashLengthCanBeSet.get();
    private final static String LEASHEDPLAYER_LEASH_MESSAGE_ = "leashedplayer.command.leash.message.";

    public final static String LEASH_FAILED = LEASHEDPLAYER_LEASH_MESSAGE_ + "leash.length.failed";

    public final static String  LEASH_LENGTH_SHOW = LEASHEDPLAYER_LEASH_MESSAGE_ + "leash.length.show",
                                LEASH_LENGTH_SET = LEASHEDPLAYER_LEASH_MESSAGE_ + "leash.length.set",
                                LEASH_DATA = LEASHEDPLAYER_LEASH_MESSAGE_ + "leash.data",
                                LEASH_DATA_CLEAR = LEASH_DATA + ".clear"
                                ;

    public final static String  NO_LEASH_DATA = LEASH_DATA + ".null",
                                LEASH_DATA_SHOW = LEASH_DATA + ".show",
                                LEASH_DATA_SET = LEASH_DATA + ".set",
                                LEASH_DATA_SET_FAILED_DIFF_LEVEL = LEASH_DATA_SET + ".failed.diff_level",
                                LEASH_DATA_SET_FAILED_TOO_FAR = LEASH_DATA_SET + ".failed.too_far",
                                LEASH_DATA_SET_FAILED_NO_KNOT_EXISTED_IN_THAT_POS = LEASH_DATA_SET + ".failed.no_knot_exist_in_that_pos",
                                LEASH_DATA_SET_FAILED_FORBID_SAME_ENTITY = LEASH_DATA_SET + ".failed.forbid_same_entity",
                                LEASH_DATA_CLEAR_FAILED_NO_DATA = LEASH_DATA_CLEAR + ".leash.clear.failed.no_data"
                                ;
    private static LiteralArgumentBuilder<CommandSourceStack> getLiterArgumentBuilderOfCSS(String name, boolean shouldAddToList, @Nullable List<LiteralArgumentBuilder<CommandSourceStack>> list) {
        LiteralArgumentBuilder<CommandSourceStack> literal = Commands.literal(name);
        if (shouldAddToList) {
            assert list != null;
            list.add(literal);
        }
        return literal;
    }
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        boolean shouldUsePrefix = LeashPlayerCommonConfig.EnableLeashPlayerCommandPrefix.get();
        @Nullable List<LiteralArgumentBuilder<CommandSourceStack>> nodeList = shouldUsePrefix ? null : new ArrayList<>();
        LiteralArgumentBuilder<CommandSourceStack> literalArgumentBuilder = Commands.literal(com.r3944realms.leashedplayer.content.commands.Command.PREFIX);
        LiteralArgumentBuilder<CommandSourceStack> $$leashRoot = getLiterArgumentBuilderOfCSS("leash", !shouldUsePrefix, nodeList);

        Command<CommandSourceStack> getSelfLeashLength = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = source.getPlayerOrException();
                float leashLength = ((ILivingEntityExtension)player).getLeashLength();
                source.sendSuccess(() -> Component.translatable(LEASH_LENGTH_SHOW, player.getDisplayName(), leashLength), true);
            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> getRefPlayerLeashLength = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = EntityArgument.getPlayer(context, "targetPlayer");
                float leashLength = ((ILivingEntityExtension)player).getLeashLength();
                source.sendSuccess(() -> Component.translatable(LEASH_LENGTH_SHOW, player.getDisplayName(), leashLength), true);
            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> getRefPlayersLeashLength = context -> {
            CommandSourceStack source = context.getSource();
            try {
                Collection<ServerPlayer> playerCol = EntityArgument.getPlayers(context, "targetPlayers");
                playerCol.forEach(player -> {
                    float leashLength = ((ILivingEntityExtension) player).getLeashLength();
                    source.sendSuccess(() -> Component.translatable(LEASH_LENGTH_SHOW, player.getDisplayName(), leashLength), true);
                });
            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> setSelfLengthLeashLength = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = source.getPlayerOrException();
                float leashLength = context.getArgument("leashLength", Float.class);
                ((ILivingEntityExtension)player).setLeashLength(leashLength);
                source.sendSuccess(() -> Component.translatable(LEASH_LENGTH_SET, player.getDisplayName(), leashLength), true);
            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> setRefPlayerLengthLeashLength = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = EntityArgument.getPlayer(context, "targetPlayer");
                float leashLength = context.getArgument("leashLength", Float.class);
                ((ILivingEntityExtension)player).setLeashLength(leashLength);
                source.sendSuccess(() -> Component.translatable(LEASH_LENGTH_SET, player.getDisplayName(), leashLength), true);
            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> setRefPlayersLengthLeashLength = context -> {
            CommandSourceStack source = context.getSource();
            float leashLength = context.getArgument("leashLength", Float.class);
            try {
                Collection<ServerPlayer> playerCol = EntityArgument.getPlayers(context, "targetPlayer");
                playerCol.forEach(player -> {
                    try {
                        ((ILivingEntityExtension)player).setLeashLength(leashLength);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        //获取Data 构造一个MutableComponent显示数据
        Command<CommandSourceStack> geSelfLeashData = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = source.getPlayerOrException();
                Integer x = LeashLengthGetResultInt(player, source);
                if (x != null) return x;
            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };

        Command<CommandSourceStack> getRefPlayerLeashData = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = EntityArgument.getPlayer(context, "targetPlayer");
                Integer x = LeashLengthGetResultInt(player, source);
                if (x != null) return x;
            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> getRefPlayersLeashData = context -> {
            CommandSourceStack source = context.getSource();
            try {
                Collection<ServerPlayer> playerCol = EntityArgument.getPlayers(context, "targetPlayers");
                playerCol.forEach(player -> {
                    try {
                        LeashLengthGetResultInt(player, source);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        //设置前要判断其实体距离（同一维度，且距离不得大于其绳长的1.2倍（待定，也许可以设置在配置文件里）
        Command<CommandSourceStack> setSelfLeashDataEntity = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = source.getPlayerOrException();
                Integer x = LeashDataEntitySetResultInt(context, player, source);
                if (x != null) return x;

            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> setSelfLeashDataByBlockPos = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = source.getPlayerOrException();
                Integer x = LeashDataBlockPosSetResultInt(context, source, player);
                if (x != null) return x;


            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };

        Command<CommandSourceStack> setRefPlayerLeashDataEntity = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = EntityArgument.getPlayer(context, "targetPlayer");
                Integer x = LeashDataEntitySetResultInt(context, player, source);
                if (x != null) return x;

            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> setRefPlayersLeashDataEntity = context -> {
            CommandSourceStack source = context.getSource();
            try {
                Collection<ServerPlayer> playerCol = EntityArgument.getPlayers(context, "targetPlayers");
                playerCol.forEach(player -> {
                    try {
                        LeashDataEntitySetResultInt(context, player, source);
                    } catch (CommandSyntaxException e) {
                        throw new RuntimeException(e);
                    }
                });
            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> setRefPlayersLeashDataByBlockPos = context -> {
            CommandSourceStack source = context.getSource();
            try {
                Collection<ServerPlayer> playerCol = EntityArgument.getPlayers(context, "targetPlayers");
                playerCol.forEach(player -> {
                    LeashDataBlockPosSetResultInt(context, source, player);
                });

            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> setRefPlayerLeashDataByBlockPos = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = EntityArgument.getPlayer(context, "targetPlayer");
                Integer x = LeashDataBlockPosSetResultInt(context, source, player);
                if (x != null) return x;

            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };

        Command<CommandSourceStack> clearSelfLeashData = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = source.getPlayerOrException();
                Integer x = LeashDataClearResultInt(source, PlayerLeashable.getLeashDataEntity(player, source.getLevel()), player);
                if (x != null) return x;

            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> clearRefPlayerLeashData = context -> {
            CommandSourceStack source = context.getSource();
            try {
                ServerPlayer player = EntityArgument.getPlayer(context, "targetPlayer");
                Integer x = LeashDataClearResultInt(source, PlayerLeashable.getLeashDataEntity(player, source.getLevel()),player);

            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };
        Command<CommandSourceStack> clearRefPlayersLeashData = context -> {
            CommandSourceStack source = context.getSource();
            try {
                Collection<ServerPlayer> playerCol = EntityArgument.getPlayers(context, "targetPlayers");
                playerCol.forEach(player -> {
                    try {
                        LeashDataClearResultInt(source, PlayerLeashable.getLeashDataEntity(player, source.getLevel()),player);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

            } catch (Exception e) {
                source.sendFailure(Component.translatable(LEASH_FAILED));
                return -1;
            }
            return 0;
        };


        LiteralArgumentBuilder<CommandSourceStack> SelfLeashLength = $$leashRoot.then(Commands.literal("length").executes(getSelfLeashLength)
                .then(Commands.literal("get").executes(getSelfLeashLength))
                .then(Commands.literal("set").requires(cs -> cs.hasPermission(2))
                        .then(Commands.argument("leashLength", FloatArgumentType.floatArg(MIN_VALUE, MAX_VALUE)).executes(setSelfLengthLeashLength))
                )
        );

        LiteralArgumentBuilder<CommandSourceStack> RefPlayerLeashLength = $$leashRoot.then(
                Commands.literal("length")
                        .then(Commands.argument("targetPlayer", EntityArgument.player()).executes(getRefPlayerLeashLength)
                                .then(Commands.literal("get").executes(getRefPlayerLeashLength))
                                .then(Commands.literal("set").requires(cs -> cs.hasPermission(2))
                                        .then(
                                                Commands.argument("leashLength", FloatArgumentType.floatArg(MIN_VALUE, MAX_VALUE)).executes(setRefPlayerLengthLeashLength)
                                        )
                                )
                        )
                        .then(Commands.argument("targetPlayers", EntityArgument.players()).executes(getRefPlayerLeashLength)
                                .then(Commands.literal("get").executes(getRefPlayerLeashLength))
                                .then(Commands.literal("set").requires(cs -> cs.hasPermission(2))
                                        .then(
                                                Commands.argument("leashLength", FloatArgumentType.floatArg(MIN_VALUE, MAX_VALUE)).executes(setRefPlayerLengthLeashLength)
                                        )
                                )
                        )
        );

        LiteralArgumentBuilder<CommandSourceStack> RefPLayerData = $$leashRoot.then(
                Commands.literal("data")
                        .then(Commands.argument("targetPlayer", EntityArgument.player()).executes(getRefPlayerLeashData)
                                .then(Commands.literal("get")
                                        .executes(getRefPlayerLeashData)
                                )
                                .then(Commands.literal("set").requires(cs -> cs.hasPermission(2))
                                        .then(Commands.argument("holderEntity", EntityArgument.entity())
                                                .executes(setRefPlayerLeashDataEntity)
                                        )
                                        .then(Commands.argument("BlockPos", BlockPosArgument.blockPos())
                                                .executes(setRefPlayerLeashDataByBlockPos)
                                        )
                                )
                                .then(Commands.literal("clear").requires(cs -> cs.hasPermission(2)).executes(clearRefPlayerLeashData))
                        )
                        .then(Commands.argument("targetPlayers", EntityArgument.players()).executes(getRefPlayersLeashData)
                                .then(Commands.literal("get")
                                        .executes(getRefPlayerLeashData)
                                )
                                .then(Commands.literal("set").requires(cs -> cs.hasPermission(2))
                                        .then(Commands.argument("holderEntity", EntityArgument.entity())
                                                .executes(setRefPlayersLeashDataEntity)
                                        )
                                        .then(Commands.argument("BlockPos", BlockPosArgument.blockPos())
                                                .executes(setRefPlayersLeashDataByBlockPos)
                                        )
                                )
                                .then(Commands.literal("clear").requires(cs -> cs.hasPermission(2)).executes(clearRefPlayersLeashData))
        ));

        LiteralArgumentBuilder<CommandSourceStack> SelfData = $$leashRoot.then(
                Commands.literal("data")
                        .then(Commands.literal("get")
                                .executes(geSelfLeashData)
                        )
                        .then(Commands.literal("set").requires(cs -> cs.hasPermission(2))
                                .then(Commands.argument("holderEntity", EntityArgument.entity())
                                        .executes(setSelfLeashDataEntity)
                                )
                                .then(Commands.argument("BlockPos", BlockPosArgument.blockPos())
                                        .executes(setSelfLeashDataByBlockPos)
                                )
                        )
                        .then(Commands.literal("clear").requires(cs -> cs.hasPermission(2))
                                .executes(clearSelfLeashData)
                        )
        );

        if(shouldUsePrefix) {
            literalArgumentBuilder
                    .then(RefPlayerLeashLength)
                    .then(SelfLeashLength)
                    .then(RefPLayerData)
                    .then(SelfData);
            dispatcher.register(literalArgumentBuilder);
        } else {
            nodeList.forEach(dispatcher::register);
        }

    }

    private static @Nullable Integer LeashLengthGetResultInt(ServerPlayer player, CommandSourceStack source) throws Exception {
        Leashable.LeashData leashDataFromEntityData = ((PlayerLeashable) player).getLeashDataFromEntityData();

        if(leashDataFromEntityData == null) {
            source.sendSuccess(() -> Component.translatable(NO_LEASH_DATA, player.getDisplayName()), true);
            return 1;
        } else {
            Entity leashDataEntity = PlayerLeashable.getLeashDataEntityOrThrown(leashDataFromEntityData, source.getLevel());
            source.sendSuccess(() -> Component.translatable(LEASH_DATA_SHOW, player.getDisplayName(), leashDataEntity.getDisplayName()), true);
        }
        return null;
    }

    private static @Nullable Integer LeashDataBlockPosSetResultInt(CommandContext<CommandSourceStack> context, CommandSourceStack source, ServerPlayer player) {
        BlockPos blockPos = BlockPosArgument.getBlockPos(context, "BlockPos");
        Entity leashDataEntity = PlayerLeashable.getLeashFenceKnotEntity(source.getLevel(), blockPos);
        PlayerLeashable leashedPlayer = (PlayerLeashable) player;
        Component targetPlayerDisplayName = player.getDisplayName();
        if(leashDataEntity == null) {
            ServerLevel level = context.getSource().getLevel();
            if(GameruleRegistry.getGameruleBoolValue(level,CreateLeashFenceKnotEntityIfAbsent.ID)) {
                if(level.getBlockState(blockPos).is(BlockTags.FENCES)) {
                    Entity leashKnotFence = PlayerLeashable.createLeashKnotFence(level, blockPos);
                    leashedPlayer.setLeashedTo(leashKnotFence, true);
                    source.sendSuccess(() -> Component.translatable(LEASH_DATA_SET, targetPlayerDisplayName, leashKnotFence.getDisplayName()), true);
                    return null;
                }
            }
            source.sendFailure(Component.translatable(LEASH_DATA_SET_FAILED_NO_KNOT_EXISTED_IN_THAT_POS, blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            return 1;
        }
        Component leashDataEntityDisplayName = leashDataEntity.getDisplayName();

        return LeashDataCommonPartSetResultInt(source, player, leashDataEntity, leashedPlayer, targetPlayerDisplayName, leashDataEntityDisplayName);
    }

    private static @Nullable Integer LeashDataEntitySetResultInt(CommandContext<CommandSourceStack> context, ServerPlayer player, CommandSourceStack source) throws CommandSyntaxException {
        Entity leashDataEntity = EntityArgument.getEntity(context, "holderEntity");
        PlayerLeashable leashedPlayer = (PlayerLeashable) player;
        Component targetPlayerDisplayName = player.getDisplayName();
        Component leashDataEntityDisplayName = leashDataEntity.getDisplayName();
        if(player.equals(leashDataEntity)) {
            source.sendFailure(Component.translatable(LEASH_DATA_SET_FAILED_FORBID_SAME_ENTITY));
            return 1;
        }
        return LeashDataCommonPartSetResultInt(source, player, leashDataEntity, leashedPlayer, targetPlayerDisplayName, leashDataEntityDisplayName);
    }

    private static @Nullable Integer LeashDataCommonPartSetResultInt(CommandSourceStack source, ServerPlayer player, Entity leashDataEntity, PlayerLeashable leashedPlayer, Component targetPlayerDisplayName, Component leashDataEntityDisplayName) {
        if(player.level() != leashDataEntity.level()) {
            source.sendFailure(Component.translatable(LEASH_DATA_SET_FAILED_DIFF_LEVEL, targetPlayerDisplayName, leashDataEntityDisplayName));
            return 2;
        }
        ILivingEntityExtension targetPlayerExtension = (ILivingEntityExtension) player;
        if (player.distanceTo(leashDataEntity) > targetPlayerExtension.getLeashLength() * 1.2f) {
            source.sendFailure(Component.translatable(LEASH_DATA_SET_FAILED_TOO_FAR, targetPlayerDisplayName, leashDataEntityDisplayName, targetPlayerExtension.getLeashLength()));
            return 3;
        }
        leashedPlayer.setLeashedTo(leashDataEntity, true);
        source.sendSuccess(() -> Component.translatable(LEASH_DATA_SET, targetPlayerDisplayName, leashDataEntityDisplayName), true);
        return null;
    }

    private static @Nullable Integer LeashDataClearResultInt(CommandSourceStack source, Entity leashDataEntity, ServerPlayer serverPlayer) {
       if(leashDataEntity == null) {
           source.sendFailure(Component.translatable(LEASH_DATA_CLEAR_FAILED_NO_DATA, serverPlayer.getDisplayName()));
           return 1;
       }
        ((PlayerLeashable)serverPlayer).dropLeash(true, false);
        source.sendSuccess(() -> Component.translatable(LEASH_DATA_CLEAR, serverPlayer.getDisplayName(), leashDataEntity.getDisplayName()), true);
        return null;
    }


}
