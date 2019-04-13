package com.imooc.interview.questions.java.concurrency.future;

import java.util.concurrent.*;

public class CancellableFutureQuestion {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future future = executorService.submit(() -> { // 3秒内执行完成，才算正常
            action(5);
        });

        try {
            future.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // Thread 恢复中断状态
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            // 执行超时，适当地关闭
            Thread.currentThread().interrupt(); // 设置中断状态
            future.cancel(true); // 尝试取消
        }

        executorService.shutdown();
    }

    private static void action(int seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds)); // 5 - 3
            // seconds - timeout = 剩余执行时间
            if (Thread.interrupted()) { // 判断并且清除中断状态
                return;
            }
            action();
        } catch (InterruptedException e) {
        }
    }

    private static void action() {
        System.out.printf("线程[%s] 正在执行...\n", Thread.currentThread().getName());  // 2
    }

}
