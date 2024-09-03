package com.r3944realms.leashedplayer.utils.Enum;

public enum LanguageEnum {
    English("en_us"),
    SimpleChinese("zh_cn"),
    TraditionalChinese("zh_tw"),
    LiteraryChinese("lzh"),
    ;
    public final String local;
    LanguageEnum(String local) {
        this.local = local;
    }
}
