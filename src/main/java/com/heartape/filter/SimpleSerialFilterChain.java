package com.heartape.filter;

import com.heartape.repository.BulletChat;

import java.util.ArrayList;
import java.util.List;

public class SimpleSerialFilterChain implements FilterChain<BulletChat> {

    private final List<Filter<BulletChat>> filters;

    private SimpleSerialFilterChain(List<Filter<BulletChat>> filters) {
        this.filters = filters;
    }

    @Override
    public boolean permit(BulletChat bulletChat) {
        for (Filter<BulletChat> filter : this.filters) {
            if (!filter.permit(bulletChat)){
                return false;
            }
        }
        return true;
    }

    public static SimpleSerialFilterChainBuilder builder() {
        return new SimpleSerialFilterChainBuilder();
    }

    public static class SimpleSerialFilterChainBuilder {
        private final List<Filter<BulletChat>> filters;

        SimpleSerialFilterChainBuilder() {
            this.filters = new ArrayList<>();
        }

        public SimpleSerialFilterChainBuilder filter(Filter<BulletChat> filter) {
            filters.add(filter);
            return this;
        }

        public SimpleSerialFilterChain build() {
            return new SimpleSerialFilterChain(this.filters);
        }
    }
}
