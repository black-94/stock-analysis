package com.black.util;

import lombok.Data;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;

public class FailContext {
    @Data
    public static class FailObject {
        String type;
        Object param;

        public FailObject(String type, Object param) {
            this.type = type;
            this.param = param;
        }

        public FailObject() {
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            FailObject that = (FailObject) o;
            return Objects.equals(type, that.type) &&
                    Objects.equals(param.toString(), that.param.toString());
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, param.toString());
        }
    }

    public static final FailObject gap = new FailObject();

    private static final LinkedBlockingQueue<FailObject> failQueue = new LinkedBlockingQueue<>();

    private static final ThreadLocal<FailObject> threadLocal = new ThreadLocal<>();

    public static void put(String method, Object param) {
        threadLocal.set(new FailObject(method, param));
    }

    public static FailObject get() {
        return threadLocal.get();
    }

    public static void clear() {
        threadLocal.set(null);
    }

    public static boolean hasNext(){
        return !failQueue.isEmpty();
    }

    public static void addFail(FailObject failObject) {
        if(failQueue.contains(failObject)){
            return;
        }
        failQueue.offer(failObject);
    }

    public static FailObject getFail() {
        return failQueue.poll();
    }
}
