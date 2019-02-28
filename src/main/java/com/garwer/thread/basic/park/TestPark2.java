package com.garwer.thread.basic.park;

import java.util.concurrent.locks.LockSupport;

/**
 * @Author: Garwer
 * @Date: 19/2/28 下午11:00
 * @Version 1.0
 * 主线程多次唤醒也只有一次生效 t一直都是waiting状态
 *
 */
public class TestPark2 {
    private static volatile boolean flag = true;
    private static volatile boolean flag1 = true;
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            while(flag) {
            }
            LockSupport.park();
            System.out.println("第一次park");
            LockSupport.park();
            flag1 = false;
            System.out.println("第二次park");

        });
        t.start();
        //t.interrupt(); //取消阻塞
        LockSupport.unpark(t);
        LockSupport.unpark(t);
        LockSupport.unpark(t);
        LockSupport.unpark(t);
        LockSupport.unpark(t);
        flag = false;
        while(flag1) {
            System.out.println(t.getState());
        }
    }
}
//第一次park
//...
//WAITING(死循环输出)
//程序有线程挂起不退出
