package com.r3944realms.leashedplayer.content.gamerules;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.GameRules;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class Gamerules {
    public static final String GAMERULE_PREFIX = "LP.";
    public static final GameruleRegistry GAMERULE_REGISTRY = GameruleRegistry.INSTANCE;
    public static final HashMap<String, Boolean> gamerulesBooleanValuesClient = new HashMap<>();
    public static final HashMap<String, Integer> gameruleIntegerValuesClient = new HashMap<>();
    public static final HashMap<String, Float> gameruleFloatValuesClient = new HashMap<>();
    public static final String RULE_KEY_PERFix_ = "gamerule." + GAMERULE_PREFIX;
    public static String getDescriptionKey(Class<?> gameRuleClass) {
        return RULE_KEY_PERFix_ + gameRuleClass.getSimpleName() + ".description";
    }
    public static String getDescriptionKey(String gameRuleName) {
        return RULE_KEY_PERFix_ + gameRuleName + ".description";
    }

    public static String getNameKey(Class<?> gameRuleClass) {
        return RULE_KEY_PERFix_ + gameRuleClass.getSimpleName();
    }
    //此次定义了一个浮点数类型的游戏规则
    public static class FloatValue extends GameRules.Value<FloatValue> {
        private float value;
        public static GameRules.Type<FloatValue> create(
                float pDefaultValue, BiConsumer<MinecraftServer, FloatValue> pChangeListener
        ) {
            return new GameRules.Type<>
                    (FloatArgumentType::floatArg,
                    pType -> new FloatValue(pType, pDefaultValue),
                    pChangeListener,
                    GameRules.GameRuleTypeVisitor::visit
                    );
        }
        public static GameRules.Type<FloatValue> create(
                float pDefaultValue, float pMin, float pMax , BiConsumer<MinecraftServer, FloatValue> pChangeListener
        ) {
            return new GameRules.Type<>(
                    () -> FloatArgumentType.floatArg(pMin, pMax),
                    pType -> new FloatValue(pType, pDefaultValue),
                    pChangeListener,
                    GameRules.GameRuleTypeVisitor::visit
            );
        }
        public FloatValue(GameRules.Type<FloatValue> pType, float value) {
            super(pType);
            this.value = value;
        }

        @Override
        protected void updateFromArgument(@NotNull CommandContext<CommandSourceStack> pContext, @NotNull String pParamName) {
            this.value = FloatArgumentType.getFloat(pContext, pParamName);
        }
        public float get() {
            return this.value;
        }

        public void set(float pValue, @Nullable MinecraftServer pServer) {
            this.value = pValue;
            this.onChanged(pServer);
        }
        @Override
        protected void deserialize(@NotNull String pValue) {
            this.value = Float.parseFloat(pValue);
        }

        @Override
        public @NotNull String serialize() {
            return Float.toString(this.value);
        }

        @Override
        public int getCommandResult() {
            return 1;
        }

        @Override
        protected @NotNull FloatValue getSelf() {
            return this;
        }

        @Override
        protected @NotNull FloatValue copy() {
            return new FloatValue(this.type, this.value);
        }

        @Override
        public void setFrom(FloatValue pValue, @Nullable MinecraftServer pServer) {
            this.value = pValue.value;
            this.onChanged(pServer);
        }
    }
}
