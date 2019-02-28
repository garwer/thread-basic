package com.garwer.thread.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午11:04
 * @Version 1.0
 * newFixedThreadPool 简单使用
 * 控制并发数 线程数目
 */

@Slf4j
public class ThreadPoolTest2 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                log.info("name->{}",Thread.currentThread().getName());
            });
        }
        executorService.shutdown(); //关闭停止程序
    }

    //23:05:18.715 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest2 - name->pool-1-thread-1
    //23:05:18.715 [pool-1-thread-2] INFO com.garwer.thread.threadPool.ThreadPoolTest2 - name->pool-1-thread-2
    //23:05:18.724 [pool-1-thread-2] INFO com.garwer.thread.threadPool.ThreadPoolTest2 - name->pool-1-thread-2
    //23:05:18.724 [pool-1-thread-2] INFO com.garwer.thread.threadPool.ThreadPoolTest2 - name->pool-1-thread-2
    //23:05:18.715 [pool-1-thread-3] INFO com.garwer.thread.threadPool.ThreadPoolTest2 - name->pool-1-thread-3
    //23:05:18.724 [pool-1-thread-2] INFO com.garwer.thread.threadPool.ThreadPoolTest2 - name->pool-1-thread-2
    //23:05:18.724 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest2 - name->pool-1-thread-1
    //23:05:18.724 [pool-1-thread-3] INFO com.garwer.thread.threadPool.ThreadPoolTest2 - name->pool-1-thread-3
    //23:05:18.724 [pool-1-thread-2] INFO com.garwer.thread.threadPool.ThreadPoolTest2 - name->pool-1-thread-2
    //23:05:18.724 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest2 - name->pool-1-thread-1
}
