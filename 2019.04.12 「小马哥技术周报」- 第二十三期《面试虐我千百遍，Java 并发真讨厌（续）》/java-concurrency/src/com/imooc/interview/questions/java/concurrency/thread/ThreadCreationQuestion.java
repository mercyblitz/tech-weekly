package com.imooc.interview.questions.java.concurrency.thread;

public class ThreadCreationQuestion {

    public static void main(String[] args) {
        // main 线程 -> 子线程
        Thread thread = new Thread(() -> {
        }, "子线程-1");

    }

    /**
     * 不鼓励自定义（扩展） Thread
     */
    private static class MyThread extends Thread {

        /**
         * 多态的方式，覆盖父类实现
         */
        @Override
        public void run(){
            super.run();
        }
    }

}
