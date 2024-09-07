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

    /**
     * 设置超出断裂长度后等待时间，如果时间到仍超出则会执行断裂操作
     * @apiNote 该为服务器方法，只能在服务器端调用，切勿在客户端线程调用
     * @param keepTick 等待tick（int）
     */
    void setKeepLeashTick(int keepTick);
    /**
     * 获取超出断裂长度后等待时间，如果时间到仍超出则会执行断裂操作
     * @apiNote 该为服务器方法，只能在服务器端调用，切勿在客户端线程调用
     * @return  keepTick 等待tick（int）
     */
    int getKeepLeashTick();
}
