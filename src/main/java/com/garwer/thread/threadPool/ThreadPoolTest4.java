package com.garwer.thread.threadPool;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.security.RunAs;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午11:07
 * @Version 1.0
 * newScheduledThreadPool 返回ScheduledExecutorService接口类型
 * 多了调度这个概念
 */

@Slf4j
public class ThreadPoolTest4 {
    public static void main(String[] args) {
        //核心线程数5
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
//        executorService.schedule(new Runnable() {
//            @Override
//            public void run() {
//                log.info("schedule run");
//            }
//        }, 3, TimeUnit.SECONDS); //延迟3s后执行 单个

        //  executorService.shutdown();
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                log.info("schedule run");
            }
        }, 1, 3, TimeUnit.SECONDS); //延迟1s 然后每隔3s执行Runnable任务 这种定时的情况就不适合shutdown关闭线程池


        //类似于定时器
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                log.info("timer");
            }
        }, new Date(), 5000);
    }
}
