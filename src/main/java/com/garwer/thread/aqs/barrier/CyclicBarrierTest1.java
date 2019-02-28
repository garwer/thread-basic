package com.garwer.thread.aqs.barrier;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by LJW on 2018/11/27
 * 场景:3个玩家做A任务[不一定同时开始],如果同时完成A任务，才可以继续做B任务
 * 主要功能：设置一个公共屏障点
 */

@Slf4j
public class CyclicBarrierTest1 {
    static class Player extends Thread {
        private CyclicBarrier cyclicBarrier;

        public Player(CyclicBarrier cyclicBarrier) {
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            log.info(Thread.currentThread().getName() + "正在执行任务...");
            try {
                //睡眠2s模拟执行任务所需时间
                TimeUnit.SECONDS.sleep(2);
                //阻塞,直到所有线程都到达barrier状态 继续执行后续任务
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
            if (i == 2) {
                TimeUnit.SECONDS.sleep(5);
            }
            new Player(cyclicBarrier).start();
        }
    }
}
