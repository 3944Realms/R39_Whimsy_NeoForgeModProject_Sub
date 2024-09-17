package com.r3944realms.leashedplayer.utils;

public class ColorUtil {
    // 渐变颜色计算
    public static int lerpColor(float progress) {
        int a = (int) (getAlpha(-65536) * (1 - progress) + getAlpha(-16711936) * progress);
        int r = (int) (getRed(-65536) * (1 - progress) + getRed(-16711936) * progress);
        int g = (int) (getGreen(-65536) * (1 - progress) + getGreen(-16711936) * progress);
        int b = (int) (getBlue(-65536) * (1 - progress) + getBlue(-16711936) * progress);
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    private static int getAlpha(int color) { return (color >> 24) & 0xFF; }
    private static int getRed(int color) { return (color >> 16) & 0xFF; }
    private static int getGreen(int color) { return (color >> 8) & 0xFF; }
    private static int getBlue(int color) { return color & 0xFF; }
}

