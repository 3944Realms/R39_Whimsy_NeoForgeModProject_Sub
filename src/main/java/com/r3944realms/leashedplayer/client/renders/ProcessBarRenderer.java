package com.r3944realms.leashedplayer.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.r3944realms.leashedplayer.LeashedPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = LeashedPlayer.MOD_ID, value = Dist.CLIENT)
public class ProcessBarRenderer {

    private static boolean isRendering = false;
    private static final int progressDuration = 100;
    private static int currentProgress = 0;
    public static void startRenderingProgressBar(Player player) {
        isRendering = true;
        currentProgress = 0;
    }
    public static void stopRenderingProgressBar() {
        isRendering = false;
    }
    @SubscribeEvent
    public static  void onClientTick(ClientTickEvent.Pre event) {
        if (isRendering) {
            currentProgress++;
            if (currentProgress >= progressDuration) {
                stopRenderingProgressBar();
            }
        }
    }
    @SubscribeEvent
    public static void onRendererLevel(RenderGuiEvent.Pre event) {
        PoseStack matrixStack = event.getGuiGraphics().pose();
        GuiGraphics guiGraphics = event.getGuiGraphics();
        if(isRendering) {
            renderProgressBar(matrixStack, guiGraphics);
        }
    }
    public static float getProgress() {
        return (float) currentProgress / progressDuration;
    }
    private static void renderProgressBar(PoseStack matrixStack, GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        // 设置进度条的位置和尺寸
        int barWidth = screenWidth / 4;
        int barHeight = 12;
        int x = (screenWidth - barWidth) / 2;
        int y = screenHeight - 47;

        // 绘制背景（灰色）
        guiGraphics.fill(x, y, x + barWidth, y + barHeight, 0xFF555555);

        // 计算当前进度并绘制前景（渐变颜色）
        float progress = getProgress();
        int progressWidth = (int) (progress * barWidth);

        // 渐变颜色效果：从红色到绿色
        int color = lerpColor(progress);
        guiGraphics.fill(x, y, x + progressWidth, y + barHeight, color);

        // 绘制边框：分别绘制四条边
        int borderColor = 0xFFFFFFFF;  // 白色
        guiGraphics.fill(x - 1, y - 1, x + barWidth + 1, y, borderColor);          // 顶边
        guiGraphics.fill(x - 1, y + barHeight, x + barWidth + 1, y + barHeight + 1, borderColor);  // 底边
        guiGraphics.fill(x - 1, y, x, y + barHeight, borderColor);                 // 左边
        guiGraphics.fill(x + barWidth, y, x + barWidth + 1, y + barHeight, borderColor);  // 右边
    }

    // 渐变颜色计算
    private static int lerpColor(float progress) {
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
