package com.garwer.thread.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午11:00
 * @Version 1.0
 * newCachedThreadPool 简单实用 但并不推荐 由于不指明线程数 可能会造成oom问题
 */

@Slf4j
public class ThreadPoolTest1 {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                log.info("name->{}",Thread.currentThread().getName());
            });
        }
        executorService.shutdown(); //关闭停止程序
    }
}
