package com.r3944realms.leashedplayer.datagen.LanguageAndOtherData;

import com.r3944realms.leashedplayer.content.commands.LeashCommand;
import com.r3944realms.leashedplayer.content.gamerules.Server.TeleportWithLeashedPlayers;
import com.r3944realms.leashedplayer.utils.Enum.LanguageEnum;
import com.r3944realms.leashedplayer.utils.Enum.ModPartEnum;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum ModLangKeyValue {
    //COMMAND_MESSAGE
    MESSAGE_LEASH_LENGTH_FAIL(LeashCommand.LEASH_LENGTH_FAIL, ModPartEnum.COMMAND, "Failed (Internal Error)", "失败（内部错误）", "失敗（内部錯誤）", false),
    MESSAGE_LEASH_LENGTH_SHOW(LeashCommand.LEASH_LENGTH_SHOW, ModPartEnum.COMMAND, "The Leash Length of %s is %f blocks", "%s的拴绳长度为%f格", "%s的栓繩長度為%f格" , false),
    MESSAGE_LEASH_LENGTH_SET(LeashCommand.LEASH_LENGTH_SET, ModPartEnum.COMMAND, "The Leash length of %s is set to %f blocks", "%s的拴绳长度被设置为%f格", "%s的栓繩長度被設置為%f格" , false),
    //GAME_RULE_NAME
    TELEPORT_WITH_LEASHED_PLAYERS(TeleportWithLeashedPlayers.NAME_KEY, ModPartEnum.NAME, "Teleport with leashed player", "传送被栓玩家", "傳送被栓玩家" ,false),
    //GAME_RULE_DESCRIPTION
    TELEPORT_WITH_LEASHED_DESCRIPTION(TeleportWithLeashedPlayers.DESCRIPTION_KEY, ModPartEnum.DESCRIPTION, "You will teleport with your leashed players ", "传送时将被栓玩家与自己一起传送", "傳送時將被栓玩家與隨自己一起傳送" ,false),
    ;
    private final Supplier<?> supplier;
    private String key;
    private final String US_EN;
    private final String SIM_CN;
    private final String TRA_CN;
    private final String LZH;
    private final Boolean Default;
    private final ModPartEnum MPE;
    ModLangKeyValue(Supplier<?> Supplier, ModPartEnum MPE, String US_EN, String SIM_CN, String TRA_CN, String LZH, Boolean isDefault) {
        this.supplier = Supplier;
        this.MPE = MPE;
        this.US_EN = US_EN;
        this.SIM_CN = SIM_CN;
        this.TRA_CN = TRA_CN;
        this.LZH = LZH;
        this.Default = isDefault;
    }
    ModLangKeyValue(@NotNull String ResourceKey, ModPartEnum MPE, String US_EN, String SIM_CN, String TRA_CN, String LZH, Boolean isDefault) {
        this.supplier = null;
        this.key = ResourceKey;
        this.MPE = MPE;
        this.US_EN = US_EN;
        this.SIM_CN = SIM_CN;
        this.TRA_CN = TRA_CN;
        this.LZH = LZH;
        this.Default = isDefault;
    }
    ModLangKeyValue(Supplier<?> Supplier, ModPartEnum MPE, String US_EN, String SIM_CN, String TRA_CN, Boolean isDefault) {
        this(Supplier, MPE, US_EN, SIM_CN, TRA_CN, null, isDefault);
    }
    ModLangKeyValue(@NotNull String ResourceKey, ModPartEnum MPE, String US_EN, String SIM_CN, String TRA_CN, Boolean isDefault) {
        this(ResourceKey, MPE, US_EN, SIM_CN, TRA_CN, null, isDefault);
    }
    public static String getLan(LanguageEnum lan, ModLangKeyValue key) {
        if (lan == null || lan == LanguageEnum.English) return getEnglish(key);
        else {
            switch (lan) {
                case SimpleChinese -> {
                    return getSimpleChinese(key);
                }
                case TraditionalChinese -> {
                    return getTraditionalChinese(key);
                }
                case LiteraryChinese -> {
                    return getLiteraryChinese(key);
                }
                default -> {
                    return getEnglish(key);
                }
            }
        }
    }
    private static String getEnglish(ModLangKeyValue key) {
        return key.US_EN;
    }
    private static String getSimpleChinese(ModLangKeyValue key) {
        return key.SIM_CN;
    }
    private static String getTraditionalChinese(ModLangKeyValue key) {
        return key.TRA_CN;
    }
    @Nullable
    public static String getLiteraryChinese(ModLangKeyValue key) {
        return key.LZH;
    }
    public String getKey() {
        if(key == null){
            switch (MPE) {//Don't need to use "break;"[Java feature];
                case CREATIVE_TAB, MESSAGE, INFO, DEFAULT, COMMAND, CONFIG -> throw new UnsupportedOperationException("The Key value is NULL! Please use the correct constructor and write the parameters correctly");
                case ITEM -> key = (getItem()).getDescriptionId();
                case BLOCK -> key =(getBlock()).getDescriptionId();

            }
            //需要完善
        }
        return key;
    }
    @SuppressWarnings("null")
    public Item getItem() {
        assert supplier != null;
        return (Item)supplier.get();
    }
    @SuppressWarnings("null")
    public Block getBlock() {
        assert supplier != null;
        return (Block)supplier.get();
    }
    public boolean isDefaultItem(){
        return MPE == ModPartEnum.ITEM && Default;
    }
    public boolean isDefaultBlock() {
        return MPE == ModPartEnum.BLOCK && Default;
    }
}
