package com.r3944realms.leashedplayer.datagen.LanguageAndOtherData;

import com.r3944realms.leashedplayer.content.commands.LeashCommand;
import com.r3944realms.leashedplayer.content.entities.LeashRopeArrow;
import com.r3944realms.leashedplayer.content.entities.ModEntityRegister;
import com.r3944realms.leashedplayer.content.gamerules.Server.CreateLeashFenceKnotEntityIfAbsent;
import com.r3944realms.leashedplayer.content.gamerules.Server.TeleportWithLeashedPlayers;
import com.r3944realms.leashedplayer.content.items.LeashRopeArrowItem;
import com.r3944realms.leashedplayer.content.items.ModCreativeTab;
import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import com.r3944realms.leashedplayer.utils.Enum.LanguageEnum;
import com.r3944realms.leashedplayer.utils.Enum.ModPartEnum;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import static com.r3944realms.leashedplayer.content.items.ModCreativeTab.LEASHED_PLAYER_ITEM;

public enum ModLangKeyValue {
    //ITEM
    ITEM_LEASH_ROPE_ARROW(ModItemRegister.LEASH_ROPE_ARROW, ModPartEnum.ITEM, "Leash Rope Arrow", "拴绳箭", "拴繩箭", true),
    //ITEM_DESC
    DESC_ITEM_LEASH_ROPE_ARROW(LeashRopeArrowItem.descKey, ModPartEnum.DESCRIPTION, "Arrows with ropes attached?","带有拴绳的箭矢？", "帶有拴繩的箭矢？", false),
    //ENTITY
    LEASH_ROPE_ARROW(ModEntityRegister.getEntityNameKey("leash_rope_arrow"), ModPartEnum.ENTITY, "Leash Rope Arrow", "拴绳箭", "拴繩箭", false),
    //CREATIVE_TAB
    CREATIVE_TAB_NAME(ModCreativeTab.getCreativeMod(LEASHED_PLAYER_ITEM), ModPartEnum.CREATIVE_TAB, "Leashed Player","可拴玩家", "可拴玩家", false),
    //COMMAND_MESSAGE
    MESSAGE_LEASH_LENGTH_FAILED(LeashCommand.LEASH_FAILED, ModPartEnum.COMMAND, "Failed (Internal Error, maybe your command is incorrect)", "失败（内部错误,可能是你输的指令有误）", "失敗（内部錯誤,,可能你輸入的指令有誤）", false),
    MESSAGE_LEASH_LENGTH_SHOW(LeashCommand.LEASH_LENGTH_SHOW, ModPartEnum.COMMAND, "The Leash Length of %1$s is %2$s blocks", "%1$s的拴绳长度为%2$s格", "%1$s的拴繩長度為%2$s格" , false),
    MESSAGE_LEASH_LENGTH_SET(LeashCommand.LEASH_LENGTH_SET, ModPartEnum.COMMAND, "The Leash length of %1$s is set to %2$s blocks", "%1$s的拴绳长度被设置为%2$s格", "%1$s的拴繩長度被設置為%2$s格" , false),
    MESSAGE_LEASH_GET_LEASH_DATA(LeashCommand.LEASH_DATA_SHOW, ModPartEnum.COMMAND, "%1$s's LeashDataEntity is %2$s", "%1$s的拴绳数据实体为%2$s", "%1$s拴繩數據實體為%2$s",false),
    MESSAGE_LEASH_NO_LEASH_DATA(LeashCommand.NO_LEASH_DATA, ModPartEnum.COMMAND, "%1$s has no LeashDataEntity", "%1$s沒有拴绳数据实体", "%1$s沒有拴繩數據實體", false),
    MESSAGE_LEASH_SET_LEASH_DATA(LeashCommand.LEASH_DATA_SET, ModPartEnum.COMMAND, "%1$s LeashDataEntity now is set as %2$s", "%1$s拴绳数据实体被设置为%2$s", "%1$s拴繩數據實體設置為%2$s", false),
    MESSAGE_LEASH_SET_FAILED_DIFF_LEVEL(LeashCommand.LEASH_DATA_SET_FAILED_DIFF_LEVEL, ModPartEnum.COMMAND,"%1$s and %2$s are not at a same level", "%1$s和%2$s不在同一维度上", "%1$s和%2$s不在同一緯度上", false),
    MESSAGE_LEASH_SET_FAILED_TOO_FAR(LeashCommand.LEASH_DATA_SET_FAILED_TOO_FAR, ModPartEnum.COMMAND,"The distance between %1$s and %2$s is larger than the 1.2 times of LeashLength, LeashLength is %3$s blocks", "%1$s到%2$s的距离超过了1.2倍 拴绳长度，原长:%3$s 格", "%1$s到%2$s的距離超過了1.2倍拴繩長度，原長:%3$s 格", false),
    MESSAGE_LEASH_SET_FAILED_NO_KNOT_EXIST_IN_THAT_POS(LeashCommand.LEASH_DATA_SET_FAILED_NO_KNOT_EXISTED_IN_THAT_POS, ModPartEnum.COMMAND, "No knot found at (X:%f,Y:%f,Z:%f)", "未找到拴绳结在(X:%f, Y:%f, Z:%f)处", "未找到拴繩結在(X:%f, Y:%f, Z:%f)処", false),
    MESSAGE_LEASH_CLEAR(LeashCommand.LEASH_DATA_CLEAR, ModPartEnum.COMMAND, "%1$s's LeashData(LeashHolderEntity: %2$s) now is clear", "%1$s的拴绳数据（拴绳持有者实体：%2$s）现在已清除", "%1$s的拴繩數據（拴繩持有者實體：%2$s）現在已清除", false),
    MESSAGE_LEASH_SET_FAILED_FORBID_SAME_ENTITY(LeashCommand.LEASH_DATA_SET_FAILED_FORBID_SAME_ENTITY, ModPartEnum.COMMAND, "Prohibit setting the same entity","禁止设置同一实体", "禁止設置同一實體", false),
    MESSAGE_LEASH_CLEAR_FAILED(LeashCommand.LEASH_DATA_CLEAR_FAILED_NO_DATA, ModPartEnum.COMMAND, "%1$s has no LeashData can be clear", "%1$s沒有拴绳数据可清除", "%1$s沒有拴繩數據實體可被清除", false),
    //GAME_RULE_NAME
    TELEPORT_WITH_LEASHED_PLAYERS(TeleportWithLeashedPlayers.NAME_KEY, ModPartEnum.NAME, "Teleport leashed player with player holder", "被拴玩家随玩家持有者传送", "被拴玩家随玩家持有者傳送" ,false),
    CREATE_LEASH_FENCE_KNOT_ENTITY_IF_ABSENT(CreateLeashFenceKnotEntityIfAbsent.NAME_KEY, ModPartEnum.NAME, "Create Leash Fence Knot Entity if absent", "如果缺失则创建拴绳结", "如果缺失則創建拴繩結", false),
    //GAME_RULE_DESCRIPTION
    TELEPORT_WITH_LEASHED_DESCRIPTION(TeleportWithLeashedPlayers.DESCRIPTION_KEY, ModPartEnum.DESCRIPTION, "Holder will teleport with their leashed players ", "传送时将被拴玩家与持有者一起传送", "將被拴玩家將隨持有者一起傳送" ,false),
    CREATE_LEASH_FENCE_KNOT_ENTITY_IF_ABSENT_DESCRIPTION(CreateLeashFenceKnotEntityIfAbsent.DESCRIPTION_KEY, ModPartEnum.DESCRIPTION, "Create LeashKnot Entity if it's absent on fence", "如果在栅栏处缺失拴绳结，则创建它", "如果在柵欄処缺失拴繩結，則創建它",false),

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
