package com.heartape.repository;

import java.util.Collection;
import java.util.List;

/**
 * 弹幕存储
 */
public interface BulletChatRepository {

    /**
     * 批量插入数据
     * @param bulletChats 弹幕list
     */
    void insert(Collection<BulletChat> bulletChats);

    /**
     * 获取实时有效数据
     * @return 实时有效数据list
     */
    List<BulletChat> select();

    /**
     * 当前是否有实时数据
     * @return 存在实时有效数据返回true
     */
    boolean isEmpty();

}
