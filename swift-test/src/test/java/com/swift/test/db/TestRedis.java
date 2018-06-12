/*
 * @(#)TestRedis.java   1.0  2018年6月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.db;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.dao.cache.redis.RedisClientFactory;
import com.swift.test.BaseJunit4Test;

import redis.clients.jedis.Jedis;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月4日
 */
public class TestRedis extends BaseJunit4Test{

    @Autowired
    private RedisClientFactory redisClientFactory;
    
    @Test
    public void testNx() {
        Jedis jedis = redisClientFactory.getJedis();
        String str = jedis.set("TestRedis.testNx", String.valueOf(100), "NX", "EX", 60);
        System.out.println(str);
        str = jedis.set("TestRedis.testNx", String.valueOf(100), "NX", "EX", 60);
        System.out.println(str);
    }
    
}
