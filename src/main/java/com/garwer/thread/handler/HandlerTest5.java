package com.garwer.thread.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @Author: Garwer
 * @Date: 19/2/27 下午8:40
 * @Version 1.0
 * 自定义拒绝策略 实现RejectedExecutionHandler接口
 */

@Slf4j
public class HandlerTest5 {
    private static Logger logger = LoggerFactory.getLogger(HandlerTest1.class);

    static class MyTask implements Runnable {
        private String name;

        public MyTask(String name) {
            this.name = name;
        }
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info(this.name + "is running" + Thread.currentThread().getName());
        }
    }

    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(1,
                1,
                0L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue(5),
                Executors.defaultThreadFactory(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        logger.info("队列现在满了,拒绝执行");
                        //接下来可以做一下记录日志操作啥的
                    }
                });
        for (int i = 0; i < 100; i++) {
            executorService.submit(new HandlerTest4.MyTask("task" + i));
        }
        executorService.shutdown();
    }
}
