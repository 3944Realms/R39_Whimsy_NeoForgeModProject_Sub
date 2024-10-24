package com.r3944realms.leashedplayer.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class LeashPlayerCommonConfig {
    public static ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<String> LeashedPlayerModCommandPrefix;
    public static final ModConfigSpec.BooleanValue EnableLeashPlayerCommandPrefix;
    public static ModConfigSpec.IntValue MinimumLeashLengthCanBeSet, MaximumLeashLengthCanBeSet, TheLeashArrowMaxLifeTime;
    public static ModConfigSpec.DoubleValue TheMultipleThatLeashRopeArrowBreakLength, TheLeashBreakLengthTimesBase;
    static {
        BUILDER.comment("Leash Player Config");

        BUILDER.comment("Leash Player Command");
            BUILDER.push("Command");
                EnableLeashPlayerCommandPrefix = BUILDER.comment("The prefix of this mod's commands"," [ Default: true] ").define("EnableLeashPlayerCommand", true);
                LeashedPlayerModCommandPrefix = BUILDER.comment("The prefix of this mod's commands"," [ Default:'lp'] ").define("LeashedPlayerModCommandPrefix", "lp");

        BUILDER.pop();
        BUILDER.comment("Leash Player Arrow");
            BUILDER.push("LeashRopeArrow");
                TheMultipleThatLeashRopeArrowBreakLength = BUILDER.comment("How many times is the length of the arrow rope based on BreakLength TimeBase", "[ Default : 5.0f, Invalid Range:[2.0f, 10.0f] ]").defineInRange("TheMultipleArrowBreak", 5.0f, 2.0f , 10.0f);
                TheLeashArrowMaxLifeTime = BUILDER.comment("If the LeashArrowEntity's life is bigger than this value ,it will be discarded", "[ Default : 2400, Invalid Range:[1200 , 10240]]").defineInRange("TheLeashArrowMaxLifeTime",2400, 1200, 10240);
        BUILDER.pop();
        BUILDER.comment("Leash Player Misc");
            BUILDER.push("Misc");
        BUILDER.comment("Leash Player Length");
            BUILDER.push("LeashLength");
            TheLeashBreakLengthTimesBase = BUILDER.comment("When it exceeds how many times, the leash will drop"," [ Default:2.0f, Invalid Range:[2.0f, 5.0f] ]").defineInRange("BreakLengthTimeBase", 2.0f,  2.0f ,5.0f);
            MinimumLeashLengthCanBeSet = BUILDER.comment("The minimum integer's length of Leash", " [ Default:5, Invalid Range:[2,10] ]").defineInRange("MinLeashLength", 5, 2, 10);
            MaximumLeashLengthCanBeSet = BUILDER.comment("The maximum integer's length of Leash", " [ Default:1024, Invalid Range:[32, 1024] ]").defineInRange("MaxLeashLength", 1024, 32, 1024);
        BUILDER.pop().pop();



        SPEC = BUILDER.build();
    }
}
