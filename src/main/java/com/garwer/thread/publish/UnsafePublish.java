package com.garwer.thread.publish;

/**
 * @Author: Garwer
 * @Date: 19/2/22 上午12:00
 * @Version 1.0
 * 这样发布不安全 states域可能被修改 不知道这个域是否被修改
 */
public class UnsafePublish {
    private String[] states = {"a", "b", "c"};

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public static void main(String[] args) {

    }
}
