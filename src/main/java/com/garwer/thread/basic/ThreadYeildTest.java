package com.garwer.thread.basic;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Author: Garwer
 * @Date: 19/2/28 下午10:23
 * @Version 1.0
 * yiled()测试
 * 线程让步、使线程让出cpu执行时间片
 * 这边i为5的话 因为让出时间片会比较晚输出
 */

@Slf4j
public class ThreadYeildTest {
    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(10,
                10,
                0L,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        for (int i = 0; i < 10; i++) {
            final int num = i;
            executorService.submit(() -> {
                if (num == 5) {
                    Thread.yield();
                }
                log.info(String.valueOf(num));
            });
        }
        executorService.shutdown();
    }
}
