package com.r3944realms.leashedplayer.client.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.r3944realms.leashedplayer.client.processBar.IProcessBar;
import net.minecraft.client.gui.GuiGraphics;

public interface IProcessBarRenderer<T extends IProcessBar> {
    T getProcessBar();
    void setProcessBar(T processBar);
    void renderProcessBar(PoseStack poseStack, GuiGraphics guiGraphics);
    default void stopRender() {
        T processBar = getProcessBar();
        processBar.decreaseAliveCount();
        processBar.setRenderStatus(false);
    }
}
