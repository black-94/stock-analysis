package com.black.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExecutorUtil {
    private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 1, TimeUnit.SECONDS,
            new ArrayBlockingQueue(1000), (r, e) -> waitQueue(e, r));

    private static void waitQueue(ThreadPoolExecutor e, Runnable r) {
        try {
            e.getQueue().put(r);
        } catch (Exception exception) {
            log.error("", exception);
        }
    }

    public static void waitComplete() {
        while (executor.getQueue().size() > 0) {
            try {
                Thread.sleep(1000L);
            } catch (Exception e) {
                log.error("", e);
            }
        }
    }

    public static void submit(Runnable run) {
        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    run.run();
                } catch (Exception e) {
                    log.error("", e);
                }
            }
        });
    }
}
