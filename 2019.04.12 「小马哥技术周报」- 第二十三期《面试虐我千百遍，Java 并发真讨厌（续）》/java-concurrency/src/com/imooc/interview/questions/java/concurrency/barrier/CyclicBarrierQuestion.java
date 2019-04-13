package com.imooc.interview.questions.java.concurrency.barrier;

import java.util.concurrent.*;

public class CyclicBarrierQuestion {

    public static void main(String[] args) throws InterruptedException {

        CyclicBarrier barrier = new CyclicBarrier(5); // 5

        ExecutorService executorService = Executors.newFixedThreadPool(5); // 3

        for (int i = 0; i < 20; i++) {
            executorService.submit(() -> {
                action();
                try {
                    // CyclicBarrier.await() = CountDownLatch.countDown() + await()
                    // 先计数 -1，再判断当计数 > 0 时候，才阻塞
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }

        // 尽可能不要执行完成再 reset
        // 先等待 3 ms
        executorService.awaitTermination(3, TimeUnit.MILLISECONDS);
        // 再执行 CyclicBarrier reset
        // reset 方法是一个废操作
        barrier.reset();

        System.out.println("Done");

        // 关闭线程池
        executorService.shutdown();
    }

    private static void action() {
        System.out.printf("线程[%s] 正在执行...\n", Thread.currentThread().getName());  // 2
    }

}
