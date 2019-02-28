package com.garwer.thread.deadLock;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午11:35
 * @Version 1.0
 * 死锁的例子
 * 先锁定a 睡眠0.5s
 * r1睡眠后要锁定b才能继续执行 而此时b已被r2锁定
 * r2睡眠后要锁定a才能继续执行 而此时a已被r1锁定
 */

@Slf4j
public class DeadLockTest1 {
    public static void main(String[] args) {
        MyRunable r1 = new MyRunable();
        MyRunable r2 = new MyRunable();
        r1.flag = true;
        r2.flag = false;
        new Thread(r1).start();
        new Thread(r2).start();
    }

    static class MyRunable implements Runnable {
        private boolean flag = true; //要造成死锁 此处不能static 否则就变成所有类对象共享了
        //静态对象是类的所有对象共享的
        private static Object a = new Object(), b = new Object();
        @Override
        public void run() {
            log.info("flag");
            if (flag) {
                synchronized (a) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (b) {
                        log.info("get b");
                    }
                }
            }
            if(!flag) {
                synchronized (b) { //先锁a再锁b
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (a) {
                        log.info("get a");
                    }
                }
            }
        }
    }
}
