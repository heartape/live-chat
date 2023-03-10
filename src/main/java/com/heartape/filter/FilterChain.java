package com.heartape.filter;

public interface FilterChain<T> {

    /**
     * 匹配
     * @return 是否适用于当前过滤器链
     */
    boolean permit(T t);

}
