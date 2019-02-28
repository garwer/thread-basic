package com.garwer.thread.commonUnsafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @Author: Garwer
 * @Date: 19/2/22 上午12:49
 * @Version 1.0
 */
public class UnsafeTest1 {
    private static StringBuilder sb = new StringBuilder();

    //请求总数
    public static int total = 3000;

    //同时并发执行的线程数
    public static int threadTotal = 200;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(total);
        for (int i = 0; i < total; i++) {
            executorService.execute(() -> {
                try {
                  //  System.out.println(Thread.currentThread().getName());
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("==length==" + sb.length());
    }

    //有且只会执行一次
    private static void add() {
        sb.append("1");
    }
}
