package com.garwer.thread.cache.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * @Author: Garwer
 * @Date: 19/2/25 下午10:35
 * @Version 1.0
 */

@Configuration
public class RedisConf {

    @Bean(name = "redisPool")
    public JedisPool jedisPool(@Value("${jedis.host}") String host,
                               @Value("${jedis.port}") int port
                               ) {
        return new JedisPool(host,port);
    };
}
