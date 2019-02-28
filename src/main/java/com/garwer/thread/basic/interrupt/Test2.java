package com.garwer.thread.basic.interrupt;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Garwer
 * @Date: 19/2/28 下午10:55
 * @Version 1.0
 * //一旦发现阻塞(sleep)都不用等2s就抛出InterruptedException异常  也不会输出sleep后的内容
 */

@Slf4j
public class Test2 {
    private static volatile boolean flag = true;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            //直到为false
            while(flag) {
            }
            try {
                Thread.sleep(2000);
                log.info("t1睡眠结束"); //不输出
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        t1.start();
        t1.interrupt(); //设置中断标志【需要注意的是 如果interrupt设在start前 那么将不会设置中断标识】
        flag = false;
        // Thread.sleep(1000);
        log.info(String.valueOf(t1.isInterrupted())); //true 如果上面注释去掉 主线程睡眠1s则输出false 这是因为在t1抛异常后会重置t1的中断状态为false 即退出阻塞态
    }
}
