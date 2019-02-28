package com.garwer.thread.aqs.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午6:08
 * @Version 1.0
 */

@Slf4j
public class SemaphoreTest3 {
    private static final int threadCount = 100;

    public static void main(String[] args) {
        final Semaphore semaphore = new Semaphore(6);
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            final int num = i;
            exec.execute(() -> {
                try {
                    //尝试获取一个许可 能拿到许可就做 同一时刻并发数是6 只有6个线程获取到许可 剩下的不执行
                    // 剩下的丢弃 这边会输出12个
                   if (semaphore.tryAcquire(1500,TimeUnit.MILLISECONDS)) { //可填写许可个数 时间参数等 时间内获取许可
                       test(num);
                       semaphore.release();
                   }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        exec.shutdown();
    }

    public static void test(int num) throws InterruptedException {
        log.info("num {} ->" + num);
       // log.info(Thread.currentThread().getName());
        Thread.sleep(1000);
    }
}
