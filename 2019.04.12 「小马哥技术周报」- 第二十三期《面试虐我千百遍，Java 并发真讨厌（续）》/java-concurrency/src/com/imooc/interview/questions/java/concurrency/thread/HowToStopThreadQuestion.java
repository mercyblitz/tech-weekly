package com.imooc.interview.questions.java.concurrency.thread;

public class HowToStopThreadQuestion {

    public static void main(String[] args) throws InterruptedException {

        Action action = new Action();

        // 子线程
        Thread t1 = new Thread(action, "t1");

        t1.start();

        // 改变 action stopped 状态
        action.setStopped(true);

        t1.join();

        Thread t2 = new Thread(() -> {
            if (!Thread.currentThread().isInterrupted()) {
                action();
            }
        }, "t2");

        t2.start();
        // 中断操作(仅仅设置状态，而并非中止线程）
        t2.interrupt();
        t2.join();
    }


    private static class Action implements Runnable {

        // 线程安全问题，确保可见性（Happens-Before)
        private volatile boolean stopped = false;

        @Override
        public void run() {
            if (!stopped) {
                // 执行动作
                action();
            }
        }

        public void setStopped(boolean stopped) {
            this.stopped = stopped;
        }
    }

    private static void action() {
        System.out.printf("线程[%s] 正在执行...\n", Thread.currentThread().getName());  // 2
    }
}
