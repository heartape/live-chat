package com.heartape.connect;

import java.util.Collection;

/**
 * 连接管理：长连接降级
 */
public interface ConnectionManager<T extends Connection> {
    T register(long uid, T connection);

    T pick(long uid);

    Collection<T> pickAll();

    void upgrade(long uid);

    void relegation(long uid);

    T logout(long uid);
}
