package com.r3944realms.leashedplayer.utils;

import com.r3944realms.leashedplayer.LeashedPlayer;
import com.r3944realms.leashedplayer.content.gamerules.Gamerules;
import net.neoforged.fml.loading.FMLPaths;

import java.io.File;

import static com.r3944realms.leashedplayer.utils.Logger.logger;

public class Util {
    public static String getGameruleName(Class<?> clazz) {
        return Gamerules.GAMERULE_PREFIX + clazz.getSimpleName();
    }
    public static String getGameruleName(String gamerulesName) {
        return Gamerules.GAMERULE_PREFIX + gamerulesName;
    }

    public static void configFileCreate(String[] children) {//初始化配置文件目录
        File configFile = new File(FMLPaths.CONFIGDIR.get().toFile(), LeashedPlayer.MOD_ID);
        if (!configFile.exists()) {
            boolean mkdirSuccess = configFile.mkdirs();
            if (!mkdirSuccess) {
                logger.error("failed to create config directory for whimsicality");
                throw new RuntimeException("failed to create config directory for " + LeashedPlayer.MOD_ID);
            } else {
                for (String child : children) {
                    File file = new File(configFile, child);
                    if (!file.exists()) {
                        boolean mkdirChildrenSuccess = file.mkdirs();
                        if (!mkdirChildrenSuccess) {
                            logger.error("failed to create " + child + " directory for +" + LeashedPlayer.MOD_ID);
                            throw new RuntimeException("failed to create " + child + " directory for" +LeashedPlayer.MOD_ID);
                        }
                    }
                }
            }
        }
    }
}
