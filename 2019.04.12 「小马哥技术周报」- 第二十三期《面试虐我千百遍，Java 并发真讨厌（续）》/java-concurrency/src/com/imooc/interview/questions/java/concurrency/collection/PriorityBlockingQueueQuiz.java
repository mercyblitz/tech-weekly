package com.imooc.interview.questions.java.concurrency.collection;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class PriorityBlockingQueueQuiz {

    public static void main(String[] args) throws Exception {
        BlockingQueue<Integer> queue = new PriorityBlockingQueue<>(2);
        // 1. PriorityBlockingQueue put(Object) 方法不阻塞
        // 2. PriorityBlockingQueue offer(Object) 方法不限制
        // 3. PriorityBlockingQueue 插入对象会做排序，默认参照元素 Comparable 实现，
        //    或者显示地传递 Comparator
        queue.put(9);
        queue.put(1);
        queue.put(8);
        System.out.println("queue.size() = " + queue.size());
        System.out.println("queue.take() = " + queue.take());
        System.out.println("queue = " + queue);
    }
}
