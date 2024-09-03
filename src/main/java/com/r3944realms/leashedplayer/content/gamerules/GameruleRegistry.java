package com.r3944realms.leashedplayer.content.gamerules;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public enum GameruleRegistry {
    INSTANCE;
    public static final Map<String, GameRules.Key<?>> gamerules = new HashMap<>();;
    public static final Map<String, RuleDataType> gameruleDataTypes = new HashMap<>();
    public enum RuleDataType {
        BOOLEAN,
        INTEGER,
    }
    @SuppressWarnings("unchecked")

    public static boolean getGameruleBoolValue(Level level, String gameruleName) {
        if (level.isClientSide && Gamerules.gamerulesBooleanValuesClient.containsKey(gameruleName)) {
            return Gamerules.gamerulesBooleanValuesClient.get(gameruleName);
        }
        if (gameruleDataTypes.get(gameruleName) != RuleDataType.BOOLEAN) {
            return false;
        }
        return level.getGameRules().getBoolean((GameRules.Key<GameRules.BooleanValue>) gamerules.get(gameruleName));
    }
    @SuppressWarnings("unchecked")
    public static Integer getGameruleIntValue(Level level, String gameruleName) {
        if (level.isClientSide && Gamerules.gameruleIntegerValuesClient.containsKey(gameruleName)) {
            return Gamerules.gameruleIntegerValuesClient.get(gameruleName);
        }
        if (gameruleDataTypes.get(gameruleName) != RuleDataType.INTEGER) {
            return 0;
        }
        return level.getGameRules().getInt((GameRules.Key<GameRules.IntegerValue>)gamerules.get(gameruleName));
    }

    public void registerGamerule(String gameruleName, GameRules.Category category, boolean pDefault) {
        registerGamerule(gameruleName, category, pDefault, (s,i)->{});//最后一个仅占位无用
    }
    public void registerGamerule(String gameruleName, GameRules.Category category, boolean pDefault, BiConsumer<MinecraftServer, GameRules.BooleanValue> pChangeListener) {
        gamerules.put(gameruleName, GameRules.register(gameruleName, category, GameRules.BooleanValue.create(pDefault, pChangeListener)));
        gameruleDataTypes.put(gameruleName, RuleDataType.BOOLEAN);
    }
    public void registerGamerule(String gameruleName, GameRules.Category category, int pDefault) {
        registerGamerule(gameruleName, category, pDefault, (BiConsumer<MinecraftServer, GameRules.IntegerValue>) (s, i)->{});//最后一个仅占位无用
    }
    public void registerGamerule(String gameruleName, GameRules.Category category, int pDefault, BiConsumer<MinecraftServer, GameRules.IntegerValue> pChangeListener) {
        gamerules.put(gameruleName, GameRules.register(gameruleName, category, GameRules.IntegerValue.create(pDefault, pChangeListener)));
        gameruleDataTypes.put(gameruleName, RuleDataType.INTEGER);
    }
    public void registerGamerule(String gameruleName, GameRules.Category category, int pDefault, int pMin, int pMax, BiConsumer<MinecraftServer, GameRules.IntegerValue> pChangeListener) {
        gamerules.put(gameruleName, GameRules.register(gameruleName, category, GameRules.IntegerValue.create(pDefault, pMin, pMax, pChangeListener)));
        gameruleDataTypes.put(gameruleName, RuleDataType.INTEGER);
    }
    public void registerGamerule(String gameruleName, GameRules.Category category,float value) {
        registerGamerule(gameruleName, category, value, (s,i)->{});
    }
    public void registerGamerule(String gameruleName, GameRules.Category category, float pDefault, BiConsumer<MinecraftServer, Gamerules.FloatValue> pChangeListener) {
        gamerules.put(gameruleName, GameRules.register(gameruleName, category, Gamerules.FloatValue.create(pDefault, pChangeListener)));
        gameruleDataTypes.put(gameruleName, RuleDataType.INTEGER);
    }
    public void registerGamerule(String gameruleName, GameRules.Category category, float pDefault, float pMin, float pMax,BiConsumer<MinecraftServer, Gamerules.FloatValue> pChangeListener) {
        gamerules.put(gameruleName, GameRules.register(gameruleName, category, Gamerules.FloatValue.create(pDefault, pMin, pMax,pChangeListener)));
        gameruleDataTypes.put(gameruleName, RuleDataType.INTEGER);
    }
}
