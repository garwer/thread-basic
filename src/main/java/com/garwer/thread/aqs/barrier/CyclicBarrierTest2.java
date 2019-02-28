package com.garwer.thread.aqs.barrier;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by LJW on 2018/11/27
 * 场景:3个玩家做A任务[不一定同时开始],本来如果同时完成A任务，可以继续做B任务,
 * 但是又加个有个规定不再要求要同时继续去做b任务。说如果超过一定时间，还是可以继续各自去做B任务,但是会扣取一定团队积分
 */

@Slf4j
public class CyclicBarrierTest2 {
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

                //阻塞,设个时间参数，如果线程等待参数设置的时间仍然未完成，则不等待，继续做其它任务
                cyclicBarrier.await(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                log.info("有玩家超时,扣取10积分!");
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
