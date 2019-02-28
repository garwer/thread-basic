package com.garwer.thread.basic.park;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 *
 * @Author: Garwer
 * @Date: 19/2/28 下午10:57
 * @Version 1.0
 * 案例:标志位的更改是否会影响park
 * interrupt后 park不生效
 * 场景1:主线程中断线程1后，再使用挂起是否生效
 */

@Slf4j
public class TestPark1 {
    private static volatile boolean flag = true;
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            while(flag) {
            }
            //等中断interrupt执行后 对其挂起park
            LockSupport.park();
            log.info("第一次park");
            LockSupport.park();
            log.info("第二次park");
        });
        t.start();
        t.interrupt();
        flag = false;
    }
}

//第一次park
//第二次park
//程序正常退出