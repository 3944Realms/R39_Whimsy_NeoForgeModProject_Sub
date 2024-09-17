package com.r3944realms.leashedplayer.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.r3944realms.leashedplayer.client.processBar.IProcessBar;
import com.r3944realms.leashedplayer.client.processBar.TestProcessBar;
import net.minecraft.client.gui.GuiGraphics;

public interface IFadingProcessBarRenderer<T extends IProcessBar> extends IProcessBarRenderer<T> {
    /**
     * @return 当前透明度，0.0（完全透明）到 1.0（完全不透明）
     */
    float getFadeAlpha();
    void setFadeAlpha(float alpha);
    /**
     * @return 是否淡入
     */
    boolean isFadingIn();
    void setFadingIn(boolean fadingIn);
    /**
     * @return 是否淡出
     */
    boolean isFadingOut();
    void setFadingOut(boolean fadingOut);
    /**
     * @return 淡化速度
     */
    float getFadeRate();
    void setFadeRate(float fadeRate);
    /**
     * @return 完全淡入/淡出所需时间（秒）
     */
    float getFadeDuration();
    void setFadeDuration(float fadeDuration);
    /**
     * 更新淡化效果
     */
    default void updateFadeEffect() {
        float fadeSpeed = getFadeRate() / getFadeDuration();
        if (isFadingIn()) {
            fadeInTick(fadeSpeed);

        } else if (isFadingOut()) {
            fadeOutTick(fadeSpeed);
            if(getFadeAlpha() <= 0.0F){
                if (getProcessBar().getCurrentProcess() != 0 || getProcessBar().getCurrentProcess() != getProcessBar().getMaxProcess()) {
                    getProcessBar().retain();
                }
                stopRender();
            }
        }
    }

    /**
     * Do Not Override, instead of using <code>renderProcessBar0()</code><br/>
     * 请勿重载，请重载renderProcessBar0()替代
     * @deprecated
     */
    @Deprecated
    @Override
    default void renderProcessBar(PoseStack poseStack, GuiGraphics guiGraphics) {
        updateFadeEffect();
        renderProcessBar0(poseStack, guiGraphics);
    }
    void renderProcessBar0(PoseStack poseStack, GuiGraphics guiGraphics);

    default void fadeInTick(float fadeSpeed) {
        if (getFadeAlpha() >= 1.0f) {
            setFadingIn(false);
            return;
        }
        setFadeAlpha(Math.min(getFadeAlpha() + fadeSpeed, 1.0f));

    }
    default void fadeOutTick(float fadeSpeed) {
        if (getFadeAlpha() >= 1.0f) {
            setFadingOut(false);
            return;
        }
        setFadeAlpha(Math.max(getFadeAlpha() - fadeSpeed, 0.0f));
    }

    void setProcessBar(TestProcessBar processBar);
}
