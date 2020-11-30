/*
 * @(#)TestRedis.java   1.0  2020年11月27日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test;

import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.dao.redis.core.RedisClientFactory;
import com.swift.test.service.TestSpringProxyRedis;

import redis.clients.jedis.Jedis;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年11月27日
 */
public class TestRedis extends BaseJunit4Test {

    @Autowired
    private RedisClientFactory redisClientFactory;
    @Autowired
    private TestSpringProxyRedis testSpringProxyRedis;
    
    @Test
    public void testOne() {
        //for(int i=0;i<50;i++) {
            Jedis j = redisClientFactory.getJedis();
            j.set("abc", "1");
            System.out.println(j.get("abc"));
            
            
            Set<String> keys = j.keys("c.s.t.s.*");
            for(String k:keys) {
                System.out.println(k);
                System.out.println(j.get(k));
                j.del(k);
            }
        //}
    }
    
    @Test
    public void testSpringOne() {
        //for(int i=0;i<50;i++) {
            System.out.println("大柜地："+testSpringProxyRedis.setRedis("665"));
        //}
    }
}
