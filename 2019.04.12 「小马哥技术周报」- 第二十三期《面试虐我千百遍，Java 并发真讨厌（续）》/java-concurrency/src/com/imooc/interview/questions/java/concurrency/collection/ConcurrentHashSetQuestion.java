package com.imooc.interview.questions.java.concurrency.collection;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashSetQuestion {


    public static void main(String[] args) {

    }

    private static class ConcurrentHashSet<E> implements Set<E> {

        private final Object OBJECT = new Object();

        private final ConcurrentHashMap<E, Object> map = new ConcurrentHashMap<>();

        private Set<E> keySet() {
            return map.keySet();
        }

        @Override
        public int size() {
            return keySet().size();
        }

        @Override
        public boolean isEmpty() {
            return keySet().isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return keySet().contains(o);
        }

        @Override
        public Iterator<E> iterator() {
            return keySet().iterator();
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(E e) {
            return map.put(e, OBJECT) == null;
        }

        @Override
        public boolean remove(Object o) {
            return map.remove(o) != null;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }
    }
}
