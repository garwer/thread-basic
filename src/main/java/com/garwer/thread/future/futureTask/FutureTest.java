package com.garwer.thread.future.futureTask;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午8:26
 * @Version 1.0
 */

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * java 1.5以后出现Callable 与Runbale接口对比
 * 不同是有返回值(泛型) 功能更强 有返回值并且可以抛出异常
 * Future接口: future可以得到其它线程的返回值
 * FutureTask类:实现RunnableFuture接口 -> Runable和Future 所以既可以作为Runable被线程执行
 * 也可以作为Future得到Callable的返回值
 * 这样的组合应用场景:假设有一个需要计算很长时间的过程 同时这个值不是马上需要 可以起一个线程
 */

@Slf4j

public class FutureTest {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService exec = Executors.newCachedThreadPool();
        Future<String> future = exec.submit(new MyCallable());
        log.info("do something");
        Thread.sleep(1000);
        String res = future.get();
        log.info("res:{}", res);

        exec.shutdown();
    }
}

@Slf4j
class MyCallable implements Callable<String> {
    @Override
    public String call() throws Exception {
        log.info("do something in callable");
        Thread.sleep(3000);
        return "done";
    }
}
