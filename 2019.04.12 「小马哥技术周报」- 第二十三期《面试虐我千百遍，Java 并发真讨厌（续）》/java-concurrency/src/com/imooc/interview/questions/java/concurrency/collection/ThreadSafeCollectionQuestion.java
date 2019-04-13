package com.imooc.interview.questions.java.concurrency.collection;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class ThreadSafeCollectionQuestion {

    public static void main(String[] args) {

        // Java 9 的实现
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        // Java 9 + of 工厂方法，返回 Immutable 对象

        list = List.of(1, 2, 3, 4, 5);

        Set<Integer> set = Set.of(1, 2, 3, 4, 5);

        Map<Integer, String> map = Map.of(1, "A");

        // 以上实现都是不变对象，不过第一个除外

        // 通过 Collections#sychronized* 方法返回

        // Wrapper 设计模式（所有的方法都被 synchronized 同步或互斥）
        list = Collections.synchronizedList(list);

        set = Collections.synchronizedSet(set);

        map = Collections.synchronizedMap(map);

        //
        list = new CopyOnWriteArrayList<>(list);
        set = new CopyOnWriteArraySet<>(set);
        map = new ConcurrentHashMap<>(map);

    }
}
