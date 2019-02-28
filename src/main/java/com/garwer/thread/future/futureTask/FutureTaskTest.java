package com.garwer.thread.future.futureTask;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午8:50
 * @Version 1.0
 * 起一个线程计算 并获取结果 可以使用FutureTask
 */

@Slf4j
public class FutureTaskTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("do something in callable");
                Thread.sleep(3000);
                return "done";
            };
        });

        new Thread(futureTask).start();
        log.info("do something in main");
        Thread.sleep(1000);
        String res = futureTask.get(); //通过futureTask获取结果
        log.info("res:{}", res);
    }
}
