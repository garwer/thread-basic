package com.garwer.thread.threadPool;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @Author: Garwer
 * @Date: 19/2/27 下午8:52
 * @Version 1.0
 * 这边核心线程和最大线程数为10 队列容量为10 提交21个有一个就会阻塞拒绝执行
 * newWork方法是线程池的核心
 * worker就是一个RUnable 里面也是构造一个Thread对象 然后start方法运行
 *      this.thread = getThreadFactory().newThread(this);
 *      线程的runable是worker本身
 */

@Slf4j
public class ThreadPoolCommonTest {
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

    //核心线程数10 21个任务 队列数目为10 会出现一次队列满拒绝执行的情况
    public static void main(String[] args) {
        ExecutorService executorService = new ThreadPoolExecutor(10,
                10,
                0L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue(10),
                Executors.defaultThreadFactory(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        log.info("队列现在满了,拒绝执行");
                        //接下来可以做一下记录日志操作啥的
                    }
                });
        for (int i = 0; i < 21; i++) {
            executorService.submit(new MyTask("task" + i));
        }
        executorService.shutdown();
    }
}
