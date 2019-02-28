package com.garwer.thread.cache.redis.controller;

import com.garwer.thread.cache.redis.client.RedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Garwer
 * @Date: 19/2/25 下午10:53
 * @Version 1.0
 */

@RestController
@RequestMapping("/cache")
public class CacheRedisController {
    @Autowired
    private RedisClient reidsClient;

    @GetMapping("/set")
    public String set(@RequestParam("k") String k, @RequestParam("v") String v) {
        reidsClient.set(k, v);
        return "success";
    }

    @GetMapping("/get")
    public String get(@RequestParam("k") String k) {
        return reidsClient.get(k);
    }
}
