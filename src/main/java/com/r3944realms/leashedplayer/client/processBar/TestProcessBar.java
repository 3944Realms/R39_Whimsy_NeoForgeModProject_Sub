package com.r3944realms.leashedplayer.client.processBar;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;

import java.util.concurrent.CompletableFuture;

public class TestProcessBar implements IProcessBar {
    private int aliveCount = 0;
    private boolean isRendering;
    private int maxProgress;
    private int progress;
    public TestProcessBar() {
        aliveCount++;
        isRendering = false;
        maxProgress = 100;
        progress = 0;
    }
    public TestProcessBar(int maxProgress) {
        aliveCount++;
        isRendering = false;
        this.maxProgress = maxProgress;
        progress = 0;
    }
    public TestProcessBar(int maxProgress, int progress) {
        aliveCount++;
        isRendering = false;
        this.maxProgress = maxProgress;
        this.progress = progress;
    }

    @Override
    public int aliveCount() {
        return aliveCount;
    }

    @Override
    public void retain() {
        aliveCount++;
    }

    @Override
    public void decreaseAliveCount() {
       aliveCount = aliveCount > 0 ? aliveCount - 1 : 0;
    }

    @Override
    public void setRenderStatus(boolean status) {
        isRendering = status;
    }

    @Override
    public boolean shouldRender() {
        return isRendering;
    }

    @Override
    public int getCurrentProcess() {
        return progress;
    }

    @Override
    public int getMaxProcess() {
        return maxProgress;
    }

    @Override
    public void setMaxProcess(int maxProcess) {
        this.maxProgress = maxProcess;
    }

    @Override
    public void setProcess(int process) {
        this.progress = process < 0 ? 0 : (Math.min(process, maxProgress));
    }

    @Override
    public void resetProcess() {
        this.progress = 0;
    }

    @Override
    public void completeTask() {
        Runnable runnable = () -> {
            if (Minecraft.getInstance().player != null) {
                Minecraft.getInstance().getChatListener().handleDisguisedChatMessage(Component.literal("Completed"), ChatType.bind(ChatType.MSG_COMMAND_OUTGOING, Minecraft.getInstance().player));
            }
        };
        CompletableFuture.runAsync(runnable);
    }
}
