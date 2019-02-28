package com.garwer.thread.basic.interrupt;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Garwer
 * @Date: 19/2/28 下午10:53
 * @Version 1.0
 * interrupt:线程中断机制最重要方法,主要用于设置个标志位，是中断的关键，并不是真正中断的方法【立个flag】
 * 【个人觉得就好比开车的时候，路上有个路标[设置好的flag],有的人可能无视掉继续开，这个时候需要司机[开发者]去判断对这个标志的处理】
 *
 * 单纯的立个flag，使用Thread.interrupt()并不会中断一个正在运行的线程，该方法可以在线程受到阻塞的时候【如sleep,wait,join】抛出一个中断信号，使线程提早退出阻塞状态。
   中断线程只是起个标志，引起对该线程的注意，被中断的线程可以决定如何应对中断【一般情况下一个方法声明抛出InterruptedException都说明改方法是可中断的】
 */

@Slf4j
public class Test1 {
    private static volatile boolean flag = true;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            //直到为false
            while(flag) {
            }
        });
        t1.start();
        t1.interrupt(); //设置中断标志
        flag = false;
        log.info(String.valueOf(t1.isInterrupted())); //true 根据isInterrupted判断是否有设置标志位
    }
}
//即使设置了中断标志，线程也会一直处于RUNNABLE状态，
