package com.r3944realms.leashedplayer;

import com.r3944realms.leashedplayer.config.LeashPlayerCommonConfig;
import com.r3944realms.leashedplayer.content.criteriaTriggers.ModCriteriaTriggers;
import com.r3944realms.leashedplayer.content.entities.ModEntityRegister;
import com.r3944realms.leashedplayer.content.items.ModCreativeTab;
import com.r3944realms.leashedplayer.content.items.ModItemRegister;
import com.r3944realms.leashedplayer.content.paintings.paintings.ModPaintingsRegister;
import com.r3944realms.leashedplayer.utils.Util;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import static com.r3944realms.leashedplayer.utils.Logger.logger;
@Mod(LeashedPlayer.MOD_ID)
public class LeashedPlayer {
    public static final String MOD_ID = "leashedplayer";
    private static Double M1;//拴繩掉落距離倍基數
    private static Double M2;//繩箭拴繩掉落距離倍基數
    public LeashedPlayer(IEventBus event) {
        ModItemRegister.register(event);
        ModPaintingsRegister.register(event);
        ModEntityRegister.register(event);
        ModCreativeTab.register(event);
        ModCriteriaTriggers.register(event);
        initiation();
    }
    private void initiation() {
        logger.info("Initializing LeashedPlayer Mod");
        String leashedPlayerCommonConfig = "LeashedPlayerCommonConfig";
        Util.configFileCreate(new String[]{leashedPlayerCommonConfig});
        ModLoadingContext.get().getActiveContainer().registerConfig(ModConfig.Type.COMMON, LeashPlayerCommonConfig.SPEC, MOD_ID + "/" + leashedPlayerCommonConfig + "/LeashPlayer.toml");
    }
    public static Double M1() {
        if(M1 == null) {
            M1 = LeashPlayerCommonConfig.TheLeashBreakLengthTimesBase.get();
        }
        return M1;
    }
    public static Double M2() {
        if(M2 == null) {
            M2 = LeashPlayerCommonConfig.TheMultipleThatLeashRopeArrowBreakLength.get();
        }
        return M2;
    }


}
