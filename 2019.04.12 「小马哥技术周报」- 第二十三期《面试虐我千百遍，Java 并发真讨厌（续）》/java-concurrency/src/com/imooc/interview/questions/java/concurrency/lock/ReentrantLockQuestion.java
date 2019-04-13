package com.imooc.interview.questions.java.concurrency.lock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockQuestion {

    /**
     * T1 , T2 , T3
     *
     * T1(lock) , T2(park), T3(park)
     * Waited Queue -> Head-> T2 next -> T3
     * T1(unlock) -> unpark all
     * Waited Queue -> Head-> T2 next -> T3
     * T2(free), T3(free)
     *
     * -> T2(lock) , T3(park)
     * Waited Queue -> Head-> T3
     * T2(unlock) -> unpark all
     * T3(free)
     */


    /**
     * java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued
     * 如果当前线程已被其他线程调用了 interrupt() 方法时，这时会返回 true
     * acquireQueued 执行完时，interrupt 清空（false）
     * 再通过 selfInterrupt() 方法将状态恢复（interrupt=true）
     */


    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        // thread[main] ->
        // lock     lock           lock
        // main -> action1() -> action2() -> action3()
        synchronizedAction(ReentrantLockQuestion::action1);

        lockVsLockInterruptibly();
    }

    private static void lockVsLockInterruptibly() {

        try {
            lock.lockInterruptibly();
            action1();
        } catch (InterruptedException e) {
            // 显示地恢复中断状态
            Thread.currentThread().interrupt();
            // 当前线程并未消亡，线程池可能还在存活
        } finally {
            lock.unlock();
        }
    }


    private static void action1() {
        synchronizedAction(ReentrantLockQuestion::action2);

    }

    private static void action2() {
        synchronizedAction(ReentrantLockQuestion::action3);
    }

    private static void action3() {
        System.out.println("Hello,World");
    }

    private static void synchronizedAction(Runnable runnable) {
        lock.lock();
        try {
            runnable.run();
        } finally {
            lock.unlock();
        }
    }
}
