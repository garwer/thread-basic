package com.garwer.thread.aqs.barrier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午7:05
 * @Version 1.0
 * 可以在到达屏障的时候优先执行某个操作
 * 加runable 保证到达屏障点的时候优先执行里面的内容
 * 先执行callable is finish再执行continue
 */

@Slf4j
public class CyclicBarrierTest5 {
    private static CyclicBarrier barrier = new CyclicBarrier(3, () -> {
        log.info("callable is finish");
    });
    private final static int threadCount = 10;
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            final int num = i;
            Thread.sleep(1000);
            exec.execute(() -> {
                try {
                    test(num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        exec.shutdown();
    }

    private static void test(int num) throws InterruptedException, BrokenBarrierException {
        Thread.sleep(1000);
        log.info("{} ready->",  num);
        barrier.await();
        log.info("{}continue", num);
    }
}
