package com.garwer.thread.basic;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Garwer
 * @Date: 19/2/27 下午11:15
 * @Version 1.0
 * 这边主线程看到了t1.join 必须等t1执行完后才能继续执行主线程 去执行后续t2内容
 * 即等t1完成后
 * 为什么要用join
 *
 */

@Slf4j
public class ThreadJoinTest {
    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(() -> {
            log.info("线程1执行");
            try {
                Thread.sleep(2000);
                log.info("线程1睡眠结束");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(() -> {
            log.info("线程2执行");
        });

        t1.start();
        t1.join();
        t2.start();
    }
}
