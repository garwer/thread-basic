package com.garwer.thread.atomic;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author: Garwer
 * @Date: 19/2/27 下午9:20
 * @Version 1.0
 * atomic压力测试 对比锁
 * 单线程下atomicLong表现很好
 * 高并发[10个线程同时做增操作]下LongAdder和atomicInteger 表现很好
 */

@Slf4j
public class AtomicEffTest {
    public static void main(String[] args) {
        //场景1 atomic
        long start1 = System.currentTimeMillis();
        AtomicInteger atomicInteger = new AtomicInteger();
        for (int i = 0; i < 100000000; i++) {
            atomicInteger.incrementAndGet();
        }
        long end1 = System.currentTimeMillis();
        log.info(String.valueOf(end1 - start1)); //876

        //场景2 加锁
        long start2 = System.currentTimeMillis();
        sysInt val = new sysInt(0);
        for (int i = 0; i < 100000000; i++) {
            val.increase();
        }
        long end2 = System.currentTimeMillis();
        log.info(String.valueOf(end2 - start2)); //3004

        //场景3 longAdder
        long start3= System.currentTimeMillis();
        LongAdder longAdder = new LongAdder();
        for (int i = 0; i < 100000000; i++) {
            longAdder.increment();
        }
        long end3 = System.currentTimeMillis();
        log.info(String.valueOf(end3 - start3)); //1217


        //场景4
        long start4 = System.currentTimeMillis();
        AtomicLong atomicLong = new AtomicLong();
        for (int i = 0; i < 100000000; i++) {
            atomicLong.incrementAndGet(); //
        }
        long end4 = System.currentTimeMillis();
        log.info(String.valueOf(end4 - start4)); //7455

        //多线程下比较 这里100000000太大 设10000
        //场景5 并发AtomicLong
        ExecutorService exe = Executors.newFixedThreadPool(10);
        AtomicLong atomicLongThread = new AtomicLong();
        long start5 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            exe.submit(() -> {
                atomicLongThread.incrementAndGet();
            });
        }
        long end5 = System.currentTimeMillis();
        log.info(String.valueOf(end5 - start5)); //378
        exe.shutdown();

        ExecutorService exe1 = Executors.newFixedThreadPool(10);
        LongAdder longAdderThread = new LongAdder();
        long start6 = System.currentTimeMillis();

        for (int i = 0; i < 10000; i++) {
            exe1.submit(() -> {
                longAdderThread.increment();
            });
        }
        long end6 = System.currentTimeMillis();
        log.info(String.valueOf(end6 - start6)); //64
        exe1.shutdown();


        ExecutorService exe2 = Executors.newFixedThreadPool(10);
        AtomicInteger atomicIntegerThread = new AtomicInteger();
        long start7 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            exe2.submit(() -> {
                atomicIntegerThread.incrementAndGet();
            });
        }
        long end7 = System.currentTimeMillis();
        log.info(String.valueOf(end7 - start7)); //30
        exe2.shutdown();

        //
        ExecutorService exe3 = Executors.newFixedThreadPool(10);
        long start8 = System.currentTimeMillis();
        sysInt sysIntThread = new sysInt(0);
        for (int i = 0; i < 10000; i++) {
            exe3.submit(() -> {
                sysIntThread.increase();
            });
        }
        long end8 = System.currentTimeMillis();
        log.info(String.valueOf(end8 - start8)); //44
        exe3.shutdown();
        //等线程池执行完任务再取值 否则可能取到不是最新的
        log.info("val{}",String.valueOf(val.getVal()));
        log.info("atomicLong{}",String.valueOf(atomicLong.get()));
        log.info("atomicInteger{}",String.valueOf(atomicInteger.get()));
        log.info("longAdderThread{}",String.valueOf(longAdderThread.longValue()));
        log.info("atomicLongThread{}",String.valueOf(atomicLong.get()));
        log.info("atomicIntegerThread{}",String.valueOf(atomicIntegerThread.get()));
        log.info("longAdder{}",String.valueOf(longAdder.longValue()));
        log.info("sysIntThread{}",String.valueOf(sysIntThread.getVal()));

    }
}

class sysInt {
     private volatile int val;

     public synchronized int getVal() {
         return val;
     }

     public synchronized void setVal(int val) {
         this.val = val;
     }

     public sysInt(int val) {
         this.val = val;
     }

     public synchronized int increase() {
         return val++;
     }
 }                                                         