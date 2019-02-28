package com.garwer.thread.threadLocal;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author: Garwer
 * @Date: 19/2/26 下午9:42
 * @Version 1.0
 * ThreadLocal测试 不同线程维护自己的变量
 * 线程封闭->空间换时间策略
 * 同一线程可能有多个ThreadLocal，但同一线程的各个ThreadLocal存在同一个ThreadLocalMap【类似HashMap的结构】中。
 * ThreadLocalMap中的key为threadLocal全局属性，value为当前泛型类的值
 */

@Slf4j
public class ThreadLocalTest1 {

    public static void main(String[] args) throws InterruptedException {
         ThreadLocal<Object> t = new ThreadLocal<>();
         new Thread(() -> {
             log.info("线程1" + (String) t.get());
             t.set("hello world1" + Thread.currentThread().getName());
             log.info((String) t.get());
         }).start();

        new Thread(() -> {
            log.info("线程2" + (String) t.get());
            t.set("hello world2" + Thread.currentThread().getName());
            log.info((String) t.get());
        }).start();

        Thread.sleep(1000);

        log.info("主线程" + (String) t.get());
    }
}
