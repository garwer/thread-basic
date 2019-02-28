package com.garwer.thread.basic.park;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author: Garwer
 * @Date: 19/2/28 下午11:07
 * @Version 1.0
 * Thread.interrupted()会返回是否有中断的标志，并重置中断标志位false【即取消中断】，因此线程将挂起
 *
 * 总结
 * 1.park在线程有中断标志的时候才会生效，park的时候会去检查中断的标志位？
 *
 * 2.就算先unpark，再park，也可以取消挂起状态
 *
 * 3.Thread.interrupted()是否会重置标志位，即取消中断的标识
 *
 * 4.先interrupt再sleep的时候，会抛异常，且移除原有的标志位
 * Thread.interrupted()除了返回阻塞态还有个很重要的作用:清零(重置)标志位
 *
 * 结合aqs源码
 * private final boolean parkAndCheckInterrupt() {
 *     LockSupport.park(this);
 *     return Thread.interrupted();
 * }
 * //如果获取锁失败 先挂起该线程 等待唤醒【前面占用锁的线程释放锁后会唤醒后继节点】 并返回是否中断的标识
 * //LockSupport.park(this)是不会更改标志位的值的
 * //如果线程在等待过程中被中断过，它是不响应的。只是获取资源后才再进行自我中断selfInterrupt()，将中断补上。
 *
 * 这边Thread.interrupted()如果没有清零的作用，起着至关重要的作用，如果没有的话会有以下情况。
 * for循环再到parkAndCheckInterrupt的时候，挂起将不生效，而且interrupted()只会响应中断操作，
 * 如果到后面unpark方法唤醒该线程继续执行return Thread.interrupted()，interrupted只会返回false，
 * 不会执行到selfInterrupt(),这样的话就算唤醒了也不会响应中断。
 */

@Slf4j
public class TestPark3 {
    private static volatile boolean flag = true;
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            while(flag) {
            }
            //等中断interrupt执行后 对其挂起park
            LockSupport.park();
            log.info("第一次park");
            log.info(String.valueOf(Thread.interrupted()));
            log.info(String.valueOf(Thread.interrupted()));
            LockSupport.park();
            log.info("第二次park");

        });
        t.start();
        t.interrupt();
        flag = false;
    }

    //23:09:07.080 [Thread-0] INFO com.garwer.thread.basic.park.TestPark3 - 第一次park
    //23:09:07.086 [Thread-0] INFO com.garwer.thread.basic.park.TestPark3 - true
    //23:09:07.086 [Thread-0] INFO com.garwer.thread.basic.park.TestPark3 - false
    //23:09:07.086 [Thread-0] INFO com.garwer.thread.basic.park.TestPark3 - 第二次park

    //jvm层次
    //
}
