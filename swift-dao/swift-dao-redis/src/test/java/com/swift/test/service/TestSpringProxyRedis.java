/*
 * @(#)TestSpringProxyRedis.java   1.0  2020年11月27日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alicp.jetcache.anno.Cached;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年11月27日
 */
@Service
public class TestSpringProxyRedis {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    
    @Cached
    public String setRedis(String abc) {
        redisTemplate.opsForValue().set("abc", abc);
        System.out.println(redisTemplate.opsForValue().get("abc"));
        return "tesaa:"+abc;
    }
}
