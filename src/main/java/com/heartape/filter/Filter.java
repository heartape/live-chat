package com.heartape.filter;

/**
 * 弹幕过滤器
 */
public interface Filter<T> {
    boolean permit(T t);

}
