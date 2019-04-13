package com.imooc.interview.questions.java.concurrency.thread.pool;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ThreadPoolExecutorThreadQuestion {

    public static void main(String[] args) throws InterruptedException {

        // main 线程启动子线程，子线程的创造来自于 Executors.defaultThreadFactory()

        ExecutorService executorService = Executors.newCachedThreadPool();
        // 之前了解 ThreadPoolExecutor beforeExecute 和 afterExecute 能够获取当前线程数量

        Set<Thread> threadsContainer = new HashSet<>();

        setThreadFactory(executorService, threadsContainer);
        for (int i = 0; i < 9; i++) { // 开启 9 个线程
            executorService.submit(() -> {
            });
        }

        // 线程池等待执行 3 ms
        executorService.awaitTermination(3, TimeUnit.MILLISECONDS);

        threadsContainer.stream()
                .filter(Thread::isAlive)
                .forEach(thread -> {
                    System.out.println("线程池创造的线程 : " + thread);
                });

        Thread mainThread = Thread.currentThread();

        ThreadGroup mainThreadGroup = mainThread.getThreadGroup();

        int count = mainThreadGroup.activeCount();
        Thread[] threads = new Thread[count];
        mainThreadGroup.enumerate(threads, true);

        Stream.of(threads)
                .filter(Thread::isAlive)
                .forEach(thread -> {
                    System.out.println("线程 : " + thread);
                });

        // 关闭线程池
        executorService.shutdown();

    }

    private static void setThreadFactory(ExecutorService executorService, Set<Thread> threadsContainer) {

        if (executorService instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
            ThreadFactory oldThreadFactory = threadPoolExecutor.getThreadFactory();
            threadPoolExecutor.setThreadFactory(new DelegatingThreadFactory(oldThreadFactory, threadsContainer));
        }
    }

    private static class DelegatingThreadFactory implements ThreadFactory {

        private final ThreadFactory delegate;

        private final Set<Thread> threadsContainer;

        private DelegatingThreadFactory(ThreadFactory delegate, Set<Thread> threadsContainer) {
            this.delegate = delegate;
            this.threadsContainer = threadsContainer;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = delegate.newThread(r);
            // cache thread
            threadsContainer.add(thread);
            return thread;
        }
    }
}
