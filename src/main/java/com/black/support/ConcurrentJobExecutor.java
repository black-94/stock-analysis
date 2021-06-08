package com.black.support;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Slf4j
public class ConcurrentJobExecutor {
    public static final class NamedThreadFactory implements ThreadFactory {
        private final AtomicLong seq = new AtomicLong(0);
        private final String namePrefix;

        public NamedThreadFactory(String namePrefix) {
            this.namePrefix = namePrefix;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, namePrefix + "-" + seq.incrementAndGet());
        }
    }

    public static final class BlockingRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            try {
                executor.getQueue().put(r);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("interrupt", e);
            }
        }
    }

    private static ArrayBlockingQueue jobList = new ArrayBlockingQueue(1000);
    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(16, 16, 1, TimeUnit.MINUTES,
            jobList, new NamedThreadFactory("job-executor"), new BlockingRejectedExecutionHandler());
    private static AtomicLong cnt = new AtomicLong(0);
    private static Consumer<Exception> exceptionHandler;

    public static final void setExceptionHandler(Consumer<Exception> c) {
        exceptionHandler = c;
    }

    public static final void addJob(Runnable r) {
        cnt.incrementAndGet();
        executor.submit(() -> {
            try {
                r.run();
            } catch (Exception e) {
                log.error("#jobFail#", e);
                if (exceptionHandler != null) {
                    exceptionHandler.accept(e);
                }
            } finally {
                cnt.decrementAndGet();
                cnt.notifyAll();
            }
        });
    }

    public static final void waitComplete() {
        while (cnt.get() > 0) {
            synchronized (cnt) {
                try {
                    cnt.wait(100000L);
                } catch (InterruptedException e) {
                    log.error("interrupt", e);
                    Thread.currentThread().interrupt();
                }
            }
        }
        exceptionHandler = null;
    }
}
