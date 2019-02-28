package com.garwer.thread.publish.singletom;

/**
 * @Author: Garwer
 * @Date: 19/2/22 上午12:08
 * @Version 1.0
 * not safe
 */
public class Test1 {
    private Test1() {
        //如果这里有很多操作
    }

    private static Test1 instance = null;

    public static Test1 getInstance() {
        if (instance == null) {
            instance = new Test1();
        }
        return instance;
    }
}
