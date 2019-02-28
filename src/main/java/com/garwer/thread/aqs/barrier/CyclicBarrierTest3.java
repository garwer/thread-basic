package com.garwer.thread.aqs.barrier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by LJW on 2018/11/27
 * CyclicBarrier可重用性测试.
 * 场景同CyclicBarrierTest1，但是有新的三个玩家加入 重用CyclicBarrier
 * 这点和CountDownLatch区别很大,CountDownLatch无法重用
 */

@Slf4j
public class CyclicBarrierTest3 {
    static class Player extends Thread {
        private CyclicBarrier cyclicBarrier;

        public Player(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            log.info(Thread.currentThread().getName() + "正在执行任务...");
            try {
                //模拟执行任务所需时间
                TimeUnit.SECONDS.sleep(2);
                //阻塞,设个时间参数，如果线程等待参数设置的时间仍然未完成，则不等待，继续做其它任务
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

            log.info("玩家(线程)" + Thread.currentThread().getName() + "执行A任务完毕,继续做B任务去了");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int parties = 3;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

        for (int i = 0; i < parties; i++) {
            new Player(cyclicBarrier).start();
        }

        TimeUnit.SECONDS.sleep(5);

        System.out.println("新玩家加入,CyclicBarrier开始重用");

        int newParties = 3;
        for (int i = 0; i < newParties; i++) {
            new Player(cyclicBarrier).start();
        }
    }
}
