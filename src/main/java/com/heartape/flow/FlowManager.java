package com.heartape.flow;

/**
 * 流管理器推拉结合:
 * <li>长连接推送
 * <li>持久化
 */
public interface FlowManager {

    /**
     * 创建推流线程
     */
    void start();

    /**
     * 保留一定数量的线程，其余停止
     * @param i 保留的线程数量
     */
    void stop(int i);

    /**
     * 停止全部
     */
    void stop();
}
