package com.garwer.thread.lock.condition;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午8:05
 * @Version 1.0
 * condition作为维护类 协作通信 使得线程等待某个条件 直到具备条件 线程才会被唤醒
 * signalAll全部 signal单个
 */

@Slf4j
public class ConditionTest1 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            lock.lock();
            log.info("1等待信号");
            try {
                condition.await(); //将从aqs队列中移除 加入condition的等待队列中
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("4得到信号");
            lock.unlock(); //释放锁
        }).start();

        new Thread(() -> {
            lock.lock();
            log.info("2获取锁"); //线程2由于线程1 释放原因会唤醒 加入aqs等待队列中
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            condition.signal(); //这个时候condition中的队列将被放到aqs队列里 这个时候线程1并未被唤醒
            log.info("3发送信号"); //线程2释放锁 aqs中剩线程1 才得到唤醒
            lock.unlock(); //释放锁
        }).start();

    }
}
