package com.garwer.thread.lock.ReentrantReadWriteLock;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: Garwer
 * @Date: 19/2/24 下午7:34
 * @Version 1.0
 * 可以保证在没有任何读写的时候 才可以写入操作
 * 悲观读取 想获取写入锁的时候 保证没有读锁保存
 * 如果读取很多，写很少 可能会有饥饿 因为有读不能写【悲观】
 */

@Slf4j
public class ReentrantReadWriteLockTest1 {
    class Data {

    }

    private final Map<String, Data> map = new TreeMap<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final Lock readLock = lock.readLock();

    private final Lock writeLock = lock.writeLock();

    public Data get(String key) {
        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }

    public Set<String> getAllKeys() {
        readLock.lock();
        try {
            return map.keySet();
        } finally {
            readLock.unlock();
        }
    }

    public Data put(String key, Data val) {
        writeLock.lock();
        try {
            return map.put(key, val);
        } finally {
            readLock.unlock();
        }
    }

}
