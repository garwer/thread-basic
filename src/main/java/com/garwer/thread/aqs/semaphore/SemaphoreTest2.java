package com.garwer.thread.aqs.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午5:31
 * @Version 1.0
 */

@Slf4j
public class SemaphoreTest2 {
    private static final int threadCount = 100;

    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(5);
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            final int num = i;
            exec.execute(() -> {
                try {
                    semaphore.acquire(); //默认获取一个许可
                    test(num);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        exec.shutdown();
    }

    public static void test(int num) throws InterruptedException {
        log.info("num {} ->" + num);
        log.info(Thread.currentThread().getName());
        Thread.sleep(1000);
    }
}
