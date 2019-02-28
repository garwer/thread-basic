package com.garwer.thread.forkJoin;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午8:55
 * @Version 1.0
 * 将若干个任务合并 汇总 类似mapreduce
 * fork
 * join
 * 计算1+2+3...+100
 */

@Slf4j
public class ForkJoinTest extends RecursiveTask<Integer> {
    public static final int threshold = 2;
    private int start;
    private int end;

    public ForkJoinTest(int start, int end) {
        this.start = start;
        this.end = end;
    }

    //如果距离大 就拆分 递归相加
    @Override
    protected Integer compute() {
        log.info("start{}", start);
        log.info("end{}", end);
        int sum = 0;
        boolean canCompute = (end - start) <= threshold;
        if (canCompute) {
            for (int i = start; i <= end; i++) { // <=
                    sum += i;
            }
        } else {//如果任务大于阈值 就分为两个子任务计算
            int mid = (start + end) / 2;
            ForkJoinTest leftTask = new ForkJoinTest(start, mid);
            ForkJoinTest rightTask = new ForkJoinTest(mid + 1, end);

            leftTask.fork();//执行任务
            rightTask.fork();

            //sum = leftTask.join() + rightTask.join(); //合并结果
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();
            // 合并子任务
            sum = leftResult + rightResult;
        }
        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        //生成计算任务
        ForkJoinTest task = new ForkJoinTest(1, 100);
        //执行任务
        Future<Integer> res = forkJoinPool.submit(task);

        log.info("res->{}" ,res.get());

    }
}
