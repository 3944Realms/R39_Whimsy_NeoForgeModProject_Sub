package com.r3944realms.leashedplayer.modInterface;

public interface ILivingEntityExtension {
    /**
     * 获取拴绳的长度
     * @return length 拴绳的长度（Float）
     */
    float getLeashLength();

    /**
     * 设置拴绳的长度
     * @param length 拴绳的长度（Float）
     */
    void setLeashLength(float length);
}
