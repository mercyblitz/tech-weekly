package com.imooc.interview.questions.java.concurrency.thread;

public class ThreadExecutionQuestion {

    public static void main(String[] args) throws InterruptedException {


        threadWait();
    }

    private static void threadWait() throws InterruptedException {

        Thread t1 = new Thread(ThreadExecutionQuestion::action, "t1");
        Thread t2 = new Thread(ThreadExecutionQuestion::action, "t2");
        Thread t3 = new Thread(ThreadExecutionQuestion::action, "t3");

        threadStartAndWait(t1);
        threadStartAndWait(t2);
        threadStartAndWait(t3);
    }

    private static void threadStartAndWait(Thread thread) {

        if (Thread.State.NEW.equals(thread.getState())) {
            thread.start();
        }

        // Java Thread 对象和实际 JVM 执行的 OS Thread 不是相同对象
        // JVM Thread 回调 Java Thread.run() 方法，
        // 同时 Thread 提供一些 native 方法来获取 JVM Thread 状态
        // 当 JVM Thread 执行后，自动就 notify() 了
        while (thread.isAlive()) { // Thread 特殊的 Object
            // 当线程 Thread isAlive() == false 时，thread.wait() 操作会被自动释放
            synchronized (thread) {
                try {
                    thread.wait(); // 到底是谁通知 Thread -> thread.notify();
//                    LockSupport.park(); // 死锁发生
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void threadSleep() throws InterruptedException {

        Thread t1 = new Thread(ThreadExecutionQuestion::action, "t1");
        Thread t2 = new Thread(ThreadExecutionQuestion::action, "t2");
        Thread t3 = new Thread(ThreadExecutionQuestion::action, "t3");

        t1.start();

        while (t1.isAlive()) {
            // sleep
            Thread.sleep(0);
        }

        t2.start();

        while (t2.isAlive()) {
            Thread.sleep(0);
        }

        t3.start();

        while (t3.isAlive()) {
            Thread.sleep(0);
        }

    }

    private static void threadLoop() {

        Thread t1 = new Thread(ThreadExecutionQuestion::action, "t1");
        Thread t2 = new Thread(ThreadExecutionQuestion::action, "t2");
        Thread t3 = new Thread(ThreadExecutionQuestion::action, "t3");

        t1.start();

        while (t1.isAlive()) {
            // 自旋 Spin
        }

        t2.start();

        while (t2.isAlive()) {

        }

        t3.start();

        while (t3.isAlive()) {

        }

    }

    private static void threadJoinOneByOne() throws InterruptedException {
        Thread t1 = new Thread(ThreadExecutionQuestion::action, "t1");
        Thread t2 = new Thread(ThreadExecutionQuestion::action, "t2");
        Thread t3 = new Thread(ThreadExecutionQuestion::action, "t3");

        // start() 仅是通知线程启动
        t1.start();
        // join() 控制线程必须执行完成
        t1.join();

        t2.start();
        t2.join();

        t3.start();
        t3.join();
    }

    private static void action() {
        System.out.printf("线程[%s] 正在执行...\n", Thread.currentThread().getName());  // 2
    }
}
