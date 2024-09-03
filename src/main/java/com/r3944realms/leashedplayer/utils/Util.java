package com.r3944realms.leashedplayer.utils;

import com.r3944realms.leashedplayer.content.gamerules.Gamerules;

public class Util {
    public static String getGameruleName(Class<?> clazz) {
        return Gamerules.GAMERULE_PREFIX + clazz.getSimpleName();
    }
    public static String getGameruleName(String gamerulesName) {
        return Gamerules.GAMERULE_PREFIX + gamerulesName;
    }

}
