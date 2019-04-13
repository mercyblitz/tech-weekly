package com.imooc.interview.questions.java.concurrency.collection;

import java.util.Arrays;
import java.util.List;

public class ArraysAsListMethodQuestion {

    public static void main(String[] args) {

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        // 调整第三个元素为 9
        list.set(2, 9);
        // 3 -> 9
        // Arrays.asList 并非线程安全
        list.forEach(System.out::println);
        // Java < 5 , Collections#synchronizedList
        // Java 5+  , CopyOnWriteArrayList
        // Java 9+  , List.of(...) 只读
    }
}
