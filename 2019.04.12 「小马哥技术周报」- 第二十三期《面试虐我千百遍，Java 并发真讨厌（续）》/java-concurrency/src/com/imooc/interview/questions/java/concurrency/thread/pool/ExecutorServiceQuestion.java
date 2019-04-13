package com.imooc.interview.questions.java.concurrency.thread.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceQuestion {

    public static void main(String[] args) {
        /**
         * 1.5
         *  ThreadPoolExecutor
         *  ScheduledThreadPoolExecutor :: ThreadPoolExecutor
         * 1.7
         *  ForkJoinPool
         */
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService = Executors.newScheduledThreadPool(2);

        // executorService 不再被引用，它会被 GC -> finalize() -> shutdown()
        ExecutorService executorService2 = Executors.newSingleThreadExecutor();
    }
}
