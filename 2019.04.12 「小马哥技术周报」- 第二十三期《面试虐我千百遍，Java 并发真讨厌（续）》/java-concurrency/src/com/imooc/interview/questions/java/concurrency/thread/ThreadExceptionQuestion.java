package com.imooc.interview.questions.java.concurrency.thread;

public class ThreadExceptionQuestion {

    public static void main(String[] args) throws InterruptedException {

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            System.out.printf("线程[%s] 遇到了异常，详细信息：%s\n",
                    thread.getName(),
                    throwable.getMessage());
        });

        // main 线程 -> 子线程
        Thread t1 = new Thread(() -> {
            throw new RuntimeException("数据达到阈值");
        }, "t1");

        t1.start();
        // main 线程会中止吗？
        t1.join();

        // Java Thread 是一个包装，它由 GC 做垃圾回收
        // JVM Thread 可能是一个 OS Thread，JVM 管理，
        // 当线程执行完毕（正常或者异常）
        System.out.println(t1.isAlive());
    }
}
