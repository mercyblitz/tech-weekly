package com.imooc.interview.questions.java.concurrency.barrier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LegacyCountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {

        // 倒数计数 5
        MyCountDownLatch latch = new MyCountDownLatch(5);

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
                action();
                latch.countDown(); // -1
            });
        }

        // 等待完成
        // 当计数 > 0，会被阻塞
        latch.await();

        System.out.println("Done");

        // 关闭线程池
        executorService.shutdown();
    }

    private static void action() {
        System.out.printf("线程[%s] 正在执行...\n", Thread.currentThread().getName());  // 2
    }

    /**
     * Java 1.5+ Lock 实现
     */
    private static class MyCountDownLatch {

        private int count;

        private final Lock lock = new ReentrantLock();

        private final Condition condition = lock.newCondition();

        private MyCountDownLatch(int count) {
            this.count = count;
        }

        public void await() throws InterruptedException {
            // 当 count > 0 等待
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            lock.lock();
            try {
                while (count > 0) {
                    condition.await(); // 阻塞当前线程
                }
            } finally {
                lock.unlock();
            }
        }

        public void countDown() {

            lock.lock();
            try {
                if (count < 1) {
                    return;
                }
                count--;
                if (count < 1) { // 当数量减少至0时，唤起被阻塞的线程
                    condition.signalAll();
                }
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * Java < 1.5 实现
     */
    private static class LegacyCountDownLatch {

        private int count;

        private LegacyCountDownLatch(int count) {
            this.count = count;
        }

        public void await() throws InterruptedException {
            // 当 count > 0 等待
            if (Thread.interrupted()) {
                throw new InterruptedException();
            }

            synchronized (this) {
                while (count > 0) {
                    wait(); // 阻塞当前线程
                }
            }
        }

        public void countDown() {
            synchronized (this) {
                if (count < 1) {
                    return;
                }
                count--;
                if (count < 1) { // 当数量减少至0时，唤起被阻塞的线程
                    notifyAll();
                }
            }
        }
    }
}
