package com.imooc.interview.questions.java.concurrency.thread;

public class SynchronizedKeywordQuestion {

    public static void main(String[] args) {

    }

    private static void synchronizedBlock() {
        synchronized (SynchronizedKeywordQuestion.class) {
        }
    }

    private synchronized static void synchronizedMethod() {
    }
}
