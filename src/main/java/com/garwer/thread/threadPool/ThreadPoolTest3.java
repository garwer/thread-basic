package com.garwer.thread.threadPool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午11:06
 * @Version 1.0
 * newSingleThreadExecutor: 控制单个线程执行任务
 */

@Slf4j
public class ThreadPoolTest3 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                log.info("name->{}",Thread.currentThread().getName());
            });
        }
        executorService.shutdown(); //关闭停止程序
    }
    //23:07:01.573 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest3 - name->pool-1-thread-1
    //23:07:01.580 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest3 - name->pool-1-thread-1
    //23:07:01.580 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest3 - name->pool-1-thread-1
    //23:07:01.580 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest3 - name->pool-1-thread-1
    //23:07:01.580 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest3 - name->pool-1-thread-1
    //23:07:01.580 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest3 - name->pool-1-thread-1
    //23:07:01.580 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest3 - name->pool-1-thread-1
    //23:07:01.580 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest3 - name->pool-1-thread-1
    //23:07:01.580 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest3 - name->pool-1-thread-1
    //23:07:01.580 [pool-1-thread-1] INFO com.garwer.thread.threadPool.ThreadPoolTest3 - name->pool-1-thread-1
}
