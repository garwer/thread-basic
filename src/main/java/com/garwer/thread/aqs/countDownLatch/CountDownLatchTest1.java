package com.garwer.thread.aqs.countDownLatch;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午4:24
 * @Version 1.0
 *
 */

@Slf4j
public class CountDownLatchTest1 {
    private final static int threadCount = 100;

    public static void main(String[] args) throws Exception{
        ExecutorService exec = Executors.newCachedThreadPool();

        final CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int num = i;
            exec.execute(() -> {
                try {
                    test(num);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
       // latch.await(10, TimeUnit.MILLISECONDS); //countDownlatch支持给定时间的等待 可以避免死等待
        log.info("finish");
        exec.shutdown();
    }


    public static void test(int threadNum) {
        log.info("result->{}", threadNum);
    }
}
