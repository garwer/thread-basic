package com.garwer.thread.handler;

/**
 * @Author: Garwer
 * @Date: 19/2/27 下午8:03
 * @Version 1.0
 */

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 模拟阻塞队列满了线程池拒绝策略
 * 限定限定队列容量为5
 * CallerRunsPolicy:由调用线程处理任务 这里为main主线程
 */

@Slf4j
public class HandlerTest4 {
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
                new ThreadPoolExecutor.CallerRunsPolicy());
        for (int i = 0; i < 100; i++) {
            executorService.submit(new MyTask("task" + i));
        }
        executorService.shutdown();
    }

}
