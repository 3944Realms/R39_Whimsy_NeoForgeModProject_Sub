package com.r3944realms.leashedplayer.client.processBar;

import java.util.concurrent.CompletableFuture;

/**
 * 进度条

 */
public interface IProcessBar {
    /**
     *
     * @return 存活计数 （<=0 则会被清除）
     */
    int aliveCount();
    /**
     * 维持计数
     */
    void retain();
    void decreaseAliveCount();
    void setRenderStatus(boolean status);
    boolean shouldRender();
    int getCurrentProcess();
    int getMaxProcess();
    void setMaxProcess(int maxProcess);
    void setProcess(int process);
    void resetProcess();

    /**
     * 完成进度条执行的任务，由实现者实现
     */
    void completeTask();
    default void decrease() {
        int updateValue = getCurrentProcess() - 1;
        if(updateValue < 0) {
           setRenderStatus(false);
           setProcess(0);
        }
        else {
            setRenderStatus(true);
            setProcess(updateValue);
        }
    }
    default void increase() {
        int updateValue = getCurrentProcess() + 1;
        if(updateValue > getMaxProcess()) {
            setRenderStatus(false);
            setProcess(getMaxProcess());
            completeTask();//执行任务
        }
        else {
            setRenderStatus(true);
            setProcess(updateValue);
        }
    }
}
