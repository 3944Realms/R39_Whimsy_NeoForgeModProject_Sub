package com.r3944realms.leashedplayer.content.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.r3944realms.leashedplayer.config.LeashPlayerCommonConfig;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Command {
    public static final String PREFIX = LeashPlayerCommonConfig.LeashedPlayerModCommandPrefix.get();

    static LiteralArgumentBuilder<CommandSourceStack> getLiterArgumentBuilderOfCSS(String name, boolean shouldAddToList, @Nullable List<LiteralArgumentBuilder<CommandSourceStack>> list) {
        LiteralArgumentBuilder<CommandSourceStack> literal = Commands.literal(name);
        if (shouldAddToList) {
            assert list != null;
            list.add(literal);
        }
        return literal;
    }
}
