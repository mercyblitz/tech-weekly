package com.imooc.interview.questions.java.concurrency.thread;

import com.sun.management.ThreadMXBean;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;

public class AllThreadInfoQuestion {

    public static void main(String[] args) {
        ThreadMXBean threadMXBean = (ThreadMXBean) ManagementFactory.getThreadMXBean();
        long[] threadIds = threadMXBean.getAllThreadIds();

        for (long threadId : threadIds) {
//            ThreadInfo threadInfo = threadMXBean.getThreadInfo(threadId);
//            System.out.println(threadInfo.toString());
            long bytes = threadMXBean.getThreadAllocatedBytes(threadId);
            long kBytes = bytes / 1024;
            System.out.printf("线程[ID:%d] 分配内存： %s KB\n", threadId, kBytes);
        }

    }
}
