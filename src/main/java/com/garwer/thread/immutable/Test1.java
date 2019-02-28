package com.garwer.thread.immutable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Garwer
 * @Date: 19/2/22 上午12:33
 * @Version 1.0
 * 通过unmodifiableMap方法的map无法改变
 */
public class Test1 {
    private static Map<Integer, Integer> map = new HashMap<>();

    static {
        map.put(1,2);
        map = Collections.unmodifiableMap(map);
    }

    public static void main(String[] args) {
        map.put(1,3); //会抛出异常
    }
}
