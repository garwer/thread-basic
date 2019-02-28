package com.garwer.thread.aqs.barrier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午6:36
 * @Version 1.0
 * 每3次 一起continue 直到10个全部完成
 *
 */
@Slf4j
public class CyclicBarrierTest4 {
    private static CyclicBarrier barrier = new CyclicBarrier(3);
    private final static int threadCount = 9;
    public static void main(String[] args) throws InterruptedException {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) { //threadCount这边threadCount如果不是CyclicBarrier的整数倍 程序将无法正常退出
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
        log.info("{} ready->" + num);
        barrier.await();
        log.info("{}continue", num);
    }
}
