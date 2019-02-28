package com.garwer.thread.basic.park;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Garwer
 * @Date: 19/2/28 下午11:13
 * @Version 1.0
 */

@Slf4j
public class TestPark4 {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(2000);
                log.info("t1执行完睡眠方法");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        });

        Thread t2 = new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(2000);
                log.info("t2执行完睡眠方法");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        });

        t1.start();
        t2.start();
        Thread.sleep(1000);
        t2.interrupt();

    }
}

//23:13:56.761 [Thread-0] INFO com.garwer.thread.basic.park.TestPark4 - t1执行完睡眠方法
//java.lang.InterruptedException: sleep interrupted
//	at java.lang.Thread.sleep(Native Method)
//	at com.garwer.thread.basic.park.TestPark4.lambda$main$1(TestPark4.java:31)
//	at com.garwer.thread.basic.park.TestPark4$$Lambda$2/942731712.run(Unknown Source)
//	at java.lang.Thread.run(Thread.java:745)
//t1在执行时，t2进入到等待队列，中途t2被interrupt，当t2获取锁开始执行，selfInterrupt()更改了中断标志位，因此到sleep的时候会立即响应这个中断抛出异常。

//线程在等待队列中都是忽略中断的。AQS也支持响应中断的
