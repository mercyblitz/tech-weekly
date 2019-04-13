package com.imooc.interview.questions.java.concurrency.thread;

import java.io.IOException;

public class ProcessCreationQuestion {

    public static void main(String[] args) throws IOException {

        // 获取 Java Runtime
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("cmd /k start http://www.baidu.com");
        process.exitValue();
    }
}
