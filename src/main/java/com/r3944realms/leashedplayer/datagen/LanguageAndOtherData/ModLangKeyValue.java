package com.r3944realms.leashedplayer.datagen.LanguageAndOtherData;

import com.r3944realms.leashedplayer.content.commands.LeashCommand;
import com.r3944realms.leashedplayer.content.commands.MotionCommand;
import com.r3944realms.leashedplayer.content.effects.ModEffectRegister;
import com.r3944realms.leashedplayer.content.effects.ModPotionRegister;
import com.r3944realms.leashedplayer.content.entities.ModEntityRegister;
import com.r3944realms.leashedplayer.content.gamerules.Server.CreateLeashFenceKnotEntityIfAbsent;
import com.r3944realms.leashedplayer.content.gamerules.Server.DisablePlayerMoveCheck;
import com.r3944realms.leashedplayer.content.gamerules.Server.KeepLeashNotDropTime;
import com.r3944realms.leashedplayer.content.gamerules.Server.TeleportWithLeashedPlayers;
import com.r3944realms.leashedplayer.content.items.ModCreativeTab;
import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import com.r3944realms.leashedplayer.content.items.type.LeashRopeArrowItem;
import com.r3944realms.leashedplayer.datagen.provider.attributes.ModPaintingVariants;
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
    ITEM_SPECTRAL_LEASH_ROPE_ARROW(ModItemRegister.SPECTRAL_LEASH_ROPE_ARROW, ModPartEnum.ITEM, "Spectral Leash Rope Arrow", "拴绳光灵箭", "拴繩光靈箭", true),
    TEST_FABRIC_ITEM(ModItemRegister.FABRIC, ModPartEnum.ITEM, "Fabric", "Fabric", "Fabric", true),
    //ITEM_DESC
    DESC_ITEM_LEASH_ROPE_ARROW(LeashRopeArrowItem.descKey, ModPartEnum.DESCRIPTION, "Arrows with ropes attached?","带有拴绳的箭矢？", "帶有拴繩的箭矢？", false),
    //PAINTING
    GROUP_PHOTO_TITLE(ModPaintingVariants.getPaintingVariantTitleKey(ModPaintingVariants.GROUP_PHOTO),ModPartEnum.TITLE, "§dGroup Photo §7[§6memorable§7]§r", "§d集体照  §7[§6纪念§7]§r", "§d集體照 §7[§6紀念§7]§r", false),
    GROUP_PHOTO_AUTHOR(ModPaintingVariants.getPaintingVariantAuthorKey(ModPaintingVariants.GROUP_PHOTO),ModPartEnum.AUTHOR, "§9Leisure §4Time §eDock§r","§9闲趣§4时§e坞§r","§9閑趣§4時§e塢§r",false),
    //ENTITY
    LEASH_ROPE_ARROW(ModEntityRegister.getEntityNameKey("leash_rope_arrow"), ModPartEnum.ENTITY, "Leash Rope Arrow", "拴绳箭", "拴繩箭", false),
    SPECTRAL_LEASH_ROPE_ARROW(ModEntityRegister.getEntityNameKey("spectral_leash_rope_arrow"), ModPartEnum.ENTITY, "Spectral Leash Rope Arrow", "拴绳光灵箭", "拴繩光靈箭", false),
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
    MESSAGE_MOTION_ADDER_SUCCESSFUL(MotionCommand.MOTION_ADDER_SUCCESSFUL, ModPartEnum.COMMAND, "§bAdd Successfully.§a%s§7:§f[§eVec§7: §a(§f%f§7,§f%f§7,§f%f§a)§f]§r","§b添加成功.§a%s§7:§f[§e加速度§7:§a(§f%f§7,§f%f§7,§f%f§a)§f]§r","§b添加成功.§a%s§7:§f[§e加速度§7:§a(§f%f§7,§f%f§7,§f%f§a)§f]§r", false),
    MESSAGE_MOTION_SETTER_SUCCESSFUL(MotionCommand.MOTION_SETTER_SUCCESSFUL, ModPartEnum.COMMAND, "§bSet Successfully.§a%s§7:§f[§eVec§7: §a(§f%f§7,§f%f§7,§f%f§a)§f]§r","§b设置成功.§a%s§7:§f[§e加速度§7:§a(§f%f§7,§f%f§7,§f%f§a)§f]§r","§b設置成功.§a%s§7:§f[§e加速度§7:§a(§f%f§7,§f%f§7,§f%f§a)§f]§r",false),
    MESSAGE_MOTION_MULTIPLY_SUCCESSFUL(MotionCommand.MOTION_MULTIPLY_SUCCESSFUL,  ModPartEnum.COMMAND, "§bMultiply Successfully.§a%s§7:§f[§eVec§7: §a(§f%f§7,§f%f§7,§f%f§a)§f]§r","§b倍乘成功.§a%s§7:§f[§e加速度§7:§a(§f%f§7,§f%f§7,§f%f§a)§f]§r","§b倍乘成功.§a%s§7:§f[§e加速度§7:§a(§f%f§7,§f%f§7,§f%f§a)§f]§r",false),
    //GAME_RULE_NAME
    TELEPORT_WITH_LEASHED_PLAYERS(TeleportWithLeashedPlayers.NAME_KEY, ModPartEnum.NAME, "Teleport leashed player with player holder", "被拴玩家随玩家持有者传送", "被拴玩家随玩家持有者傳送" ,false),
    CREATE_LEASH_FENCE_KNOT_ENTITY_IF_ABSENT(CreateLeashFenceKnotEntityIfAbsent.NAME_KEY, ModPartEnum.NAME, "Create Leash Fence Knot Entity if absent", "如果缺失则创建拴绳结", "如果缺失則創建拴繩結", false),
    KEEP_LEASH_NOT_DROP_TIME(KeepLeashNotDropTime.NAME_KEY, ModPartEnum.NAME, "Keep leash alive Time", "保持拴绳不掉落的时间", "保持其不掉落的時間", false),
    DISABLE_MOVE_CHECK(DisablePlayerMoveCheck.NAME_KEY, ModPartEnum.NAME, "Disable Player Move Check", "禁止检查玩家移动", "禁止檢查玩家移動", false),
    //GAME_RULE_DESCRIPTION
    TELEPORT_WITH_LEASHED_DESCRIPTION(TeleportWithLeashedPlayers.DESCRIPTION_KEY, ModPartEnum.DESCRIPTION, "Holder will teleport with their leashed players ", "传送时将被拴玩家与持有者一起传送", "將被拴玩家將隨持有者一起傳送" ,false),
    CREATE_LEASH_FENCE_KNOT_ENTITY_IF_ABSENT_DESCRIPTION(CreateLeashFenceKnotEntityIfAbsent.DESCRIPTION_KEY, ModPartEnum.DESCRIPTION, "Create LeashKnot Entity if it's absent on fence", "如果在栅栏处缺失拴绳结，则创建它", "如果在柵欄処缺失拴繩結，則創建它", false),
    KEEP_LEASH_NOT_DROP_TIME_DESCRIPTION(KeepLeashNotDropTime.DESCRIPTION_KEY, ModPartEnum.DESCRIPTION,"The time of Keep new leash which has far distance alive (Tick)", "当距离过远时，保持新建拴绳不掉落的时间 （刻）", "儅距離過遠時，保持其不掉落的時間（刻）", false),
    DISABLE_MOVE_CHECK_DESCRIPTION(DisablePlayerMoveCheck.DESCRIPTION_KEY, ModPartEnum.DESCRIPTION, "Disable the player's movement Check And Correct it.", "禁止检查玩家移动并且纠正它","禁止檢查玩家移動並糾正他它", false),
    //ADV_NAME
    LEASH_START(ModAdvancementKey.LEASH_START.getNameKey(), ModPartEnum.NAME, "The Power of Traction", "牵引之力", "牽引之力", false),
    LEASH_LR_ARROW(ModAdvancementKey.LEASH_ARROW.getNameKey(), ModPartEnum.NAME, "Arrow with a Tether?" , "拴绳之箭？", "拴繩之箭？", false),
    LEASH_SLP_ARROW(ModAdvancementKey.ADVANCEMENT_LEASH_ARROW.getNameKey(), ModPartEnum.NAME, "More advanced flash arrow with a Tether?", "更闪亮的拴绳箭？", "更閃亮的拴繩箭？", false),
    LEASH_SELF(ModAdvancementKey.LEASHED_SELF.getNameKey(), ModPartEnum.NAME,  "Stable Connection", "稳固联结" ,"穩固聯結", false),
    LEASH_PLAYER(ModAdvancementKey.LEASHED_FRIEND.getNameKey(),ModPartEnum.NAME, "Be bound by Rope", "拴绳链接", "拴繩鏈接" , false),
    FOLLOW_ARROW(ModAdvancementKey.FOLLOW_LEASH_ARROW.getNameKey(), ModPartEnum.NAME, "Launch!!!", "启航！！！" , "啓航！！！",false),
    FOLLOW_WOLF(ModAdvancementKey.DOG_RUNNING_PLAYER.getNameKey(), ModPartEnum.NAME, "It's Walking human time.", "遛“人”时间", "遛“人”時間",false),
    NO_LEASH(ModAdvancementKey.NO_LEASH.getNameKey(), ModPartEnum.NAME, "Don't tie me up", "勿拴我", "請恁勿拴唔", false),
    //ADV_DESC
    LEASH_START_DESC(ModAdvancementKey.LEASH_START.getDescKey(), ModPartEnum.DESCRIPTION, "Journey to becoming a Leash Expert", "拴绳大师之路", "拴繩大師之路", false),
    LEASH_LR_ARROW_DESC(ModAdvancementKey.LEASH_ARROW.getDescKey(), ModPartEnum.DESCRIPTION, "Maybe you can using it to shoot some mob?", "也许可以用它来发射生物？", "也許可以用它發射生物?", false),
    LEASH_SLP_ARROW_DESC(ModAdvancementKey.ADVANCEMENT_LEASH_ARROW.getDescKey(), ModPartEnum.DESCRIPTION, "Well, apart from glowing, there doesn't seem to be any other difference", "嗯，除了发光，似乎没有什么其它不同了" ,"嗯，除了發光，其它好像沒什麽不同", false),
    LEASH_SELF_DESC(ModAdvancementKey.LEASHED_SELF.getDescKey(), ModPartEnum.DESCRIPTION,  "“Restrain oneself with a rope", "用拴绳拴住自己" ,"用栓繩拴住自己", false),
    LEASH_PLAYER_DESC(ModAdvancementKey.LEASHED_FRIEND.getDescKey(),ModPartEnum.DESCRIPTION, "Be Bond by player with lead", "被玩家用拴绳链接", "被玩家用拴繩鏈接", false),
    FOLLOW_ARROW_DESC(ModAdvancementKey.FOLLOW_LEASH_ARROW.getDescKey(), ModPartEnum.DESCRIPTION, "Mc, what are you talking about in physics?", "抱歉，我的世界不存在物理学" , "抱歉，麦块不講物理學",false),
    FOLLOW_WOLF_DESC(ModAdvancementKey.DOG_RUNNING_PLAYER.getDescKey(), ModPartEnum.DESCRIPTION, "In the park where dogs are not allowed to be walked, the dog decided to walk the human instead", "公园不能遛狗，于是狗站起来遛人", "公園裏不許遛狗，於是狗站起來遛人",false),
    NO_LEASH_DESC(ModAdvancementKey.NO_LEASH.getDescKey(), ModPartEnum.NAME, "You cannot be leashed by ANY", "你不会被任何东西拴住", "恁不會被任何拴住", false),
    //MOB_EFFECT
    NO_LEASH_EFFECT(ModEffectRegister.getEffectKey(ModEffectRegister.NO_LEASH_EFFECT.get()), ModPartEnum.NAME, "No Leash", "禁拴", "禁拴", false),
    //POTION
    NO_LEASH_POTION(ModPotionRegister.getPotionNameKey("no_leash", (char) 0), ModPartEnum.NAME, "No Leash Potion", "禁拴药水", "禁拴藥水", false),
    NO_LEASH_POTION_SPLASH(ModPotionRegister.getPotionNameKey("no_leash", (char) 2), ModPartEnum.NAME, "Splash No Leash Potion", "喷溅型禁拴药水", "噴濺型禁拴藥水", false),
    NO_LEASH_POTION_LINGERING(ModPotionRegister.getPotionNameKey("no_leash", (char) 1), ModPartEnum.NAME, "Splash No Leash Potion", "滞留型禁拴药水", "滯留型禁拴藥水", false),
    //ARROW
    NO_LEASH_ARROW(ModPotionRegister.getTippedArrowNameKey("no_leash"), ModPartEnum.ITEM, "Arrow of No Leash", "禁拴之箭", "禁拴之箭", false),
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
