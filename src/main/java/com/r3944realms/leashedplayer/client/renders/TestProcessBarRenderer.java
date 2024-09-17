package com.r3944realms.leashedplayer.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.r3944realms.leashedplayer.client.processBar.IProcessBar;
import com.r3944realms.leashedplayer.client.processBar.TestProcessBar;
import com.r3944realms.leashedplayer.utils.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.Nullable;

public class TestProcessBarRenderer implements IFadingProcessBarRenderer<IProcessBar>{
    @Nullable
    private TestProcessBar processBar;
    private float FadeAlpha;
    private boolean fadingIn, fadingOut;
    private float FadeRate;
    private float FadeDuration;

    public TestProcessBarRenderer(boolean fadingIn, boolean fadingOut, float FadeRate, float FadeDuration){
        this.fadingIn = fadingIn;
        this.fadingOut = fadingOut;
        this.FadeRate = FadeRate;
        this.FadeDuration = FadeDuration;
        this.FadeAlpha = 0.0F;
    }

    public TestProcessBarRenderer(boolean fadingIn, boolean fadingOut, float FadeRate, float FadeDuration, float FadeAlpha){
        this.fadingIn = fadingIn;
        this.fadingOut = fadingOut;
        this.FadeRate = FadeRate;
        this.FadeDuration = FadeDuration;
        this.FadeAlpha = FadeAlpha;
    }

    public TestProcessBarRenderer(TestProcessBar bar, boolean fadingIn, boolean fadingOut, float FadeRate, float FadeDuration) {
        this(fadingIn, fadingOut, FadeRate, FadeDuration);
        setProcessBar(bar);
    }

    public TestProcessBarRenderer(TestProcessBar bar, boolean fadingIn, boolean fadingOut, float FadeRate, float FadeDuration, float FadeAlpha) {
        this(fadingIn, fadingOut, FadeRate, FadeDuration, FadeAlpha);
        setProcessBar(bar);
    }

    public TestProcessBarRenderer() {
        this(true, false, 0.02f, 10.0f, 0.0f);
    }

    public TestProcessBarRenderer(TestProcessBar bar) {
        this(true, false, 0.02f, 10.0f);
        setProcessBar(bar);
    }


    @Override
    public float getFadeAlpha() {
        return this.FadeAlpha;
    }

    @Override
    public void setFadeAlpha(float alpha) {
        this.FadeAlpha = alpha;
    }

    @Override
    public boolean isFadingIn() {
        return this.fadingIn;
    }

    @Override
    public void setFadingIn(boolean fadingIn) {
        this.fadingIn = fadingIn;
    }

    @Override
    public boolean isFadingOut() {
        return this.fadingOut;
    }

    @Override
    public void setFadingOut(boolean fadingOut) {
        this.fadingOut = fadingOut;
    }

    @Override
    public float getFadeRate() {
        return this.FadeRate;
    }

    @Override
    public void setFadeRate(float fadeRate) {
        this.FadeRate = fadeRate;
    }

    @Override
    public float getFadeDuration() {
        return this.FadeDuration;
    }

    @Override
    public void setFadeDuration(float fadeDuration) {
        this.FadeDuration = fadeDuration;
    }

    @Override
    public @Nullable TestProcessBar getProcessBar() {
        return this.processBar;
    }

    @Override
    public void setProcessBar(IProcessBar processBar) {
        if(processBar instanceof TestProcessBar pB){
            setProcessBar(pB);
        } else throw new UnsupportedOperationException("Not supported Non-TestProcessBar");
    }

    @Override
    public void setProcessBar(TestProcessBar processBar) {
        this.processBar = processBar;
    }



    @Override
    public void renderProcessBar0(PoseStack poseStack, GuiGraphics guiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int elementWidth = 200;
        int elementHeight = 12;
        int x = (screenWidth - elementWidth) / 2;
        int y = screenHeight - elementHeight - 47; // 控制 GUI 元素的位置

        // 计算透明度
        float progress = 0;
        if (getProcessBar() != null) {
            progress = (float) getProcessBar().getCurrentProcess() / getProcessBar().getMaxProcess();
        }
        int alpha = (int) (FadeAlpha * 255);
        int backgroundColor = (alpha << 24) | 0xFF555555;  // 背景颜色
        int foregroundColor = (alpha << 24) | ColorUtil.lerpColor(progress);;  // 前景颜色

        // 绘制背景
        guiGraphics.fill(x, y, x + elementWidth, y + elementHeight, backgroundColor);

        // 绘制前景（例如进度条）

        int progressWidth = (int) (progress * elementWidth);
        guiGraphics.fill(x, y, x + progressWidth, y + elementHeight, foregroundColor);

        // 绘制边框
        int borderColor = 0xFFFFFFFF;  // 白色边框
        guiGraphics.fill(x - 1, y - 1, x + elementWidth + 1, y, borderColor);          // 顶边
        guiGraphics.fill(x - 1, y + elementHeight, x + elementWidth + 1, y + elementHeight + 1, borderColor);  // 底边
        guiGraphics.fill(x - 1, y, x, y + elementHeight, borderColor);                 // 左边
        guiGraphics.fill(x + elementWidth, y, x + elementWidth + 1, y + elementHeight, borderColor);  // 右边

    }

}
