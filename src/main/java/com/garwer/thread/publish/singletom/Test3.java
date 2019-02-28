package com.garwer.thread.publish.singletom;

/**
 * @Author: Garwer
 * @Date: 19/2/22 上午12:14
 * @Version 1.0
 * 双重同步锁单例模式
 */
public class Test3 {
    private Test3() {

    }

    private volatile static  Test3 instance = null; //volatile限制指令重排

    public static Test3 getInstance() {
        if (instance == null) {
            synchronized (Test3.class) { //双重检测机制 如果没有volatile可能不安全
                if (instance == null) {
                    instance = new Test3(); //可能会导致返回不是完整的实例
                    //1 分配对象空间 2 初始化对象 3 设置instance指向刚分配的内存 jvm和cpu优化会发生指令重排 2.3可能会有变化
                }
            }
        }
        return instance;
    }
}
