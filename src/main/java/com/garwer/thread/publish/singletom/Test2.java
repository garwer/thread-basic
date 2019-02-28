package com.garwer.thread.publish.singletom;

/**
 * @Author: Garwer
 * @Date: 19/2/22 上午12:11
 * @Version 1.0
 * 饿汉模式 虽然线程安全 但是如果没有用到而实例化 又有很多初始化操作的话性能会差
 */
public class Test2 {
    private Test2() {
    }

    private static Test2 instance = new Test2();

    public static Test2 getInstance() {
        return instance;
    }
}
