/*
 * @(#)DisLock.java   1.0  2018年6月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.distributed.lock;

import java.util.Collections;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.swift.dao.cache.redis.RedisClientFactory;
import com.swift.exception.SwiftRuntimeException;
import com.swift.util.exec.ThreadUtil;
import com.swift.util.type.TypeUtil;

import redis.clients.jedis.Jedis;

/**
 * 分布式锁 
 * @author zhengjiajin
 * @version 1.0 2018年6月4日
 */
@Component("disLock")
public class DisLock {
    private static final Logger log = LoggerFactory.getLogger(DisLock.class);
    private static final String REDIS_KEY = "DISLOCK_LOCK_KEY:";
    //锁最长使用时间S
    private static final int LOCK_SEC=15;
    //过期时间
    private static final int TIME_OUT=30*1000;
    
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";//EX seconds, PX milliseconds
    private static final Long RELEASE_SUCCESS = 1L;
    
    @Resource
    private RedisClientFactory redisClientFactory;
    /**
     * 获得锁
     * @param obj
     * @return 获得锁的时间
     */
    public long tryLock(Object obj){
        synchronized (obj) {
            Long sysTime = System.currentTimeMillis();
            String key = key(obj);
            Jedis jedis = redisClientFactory.getJedis();
            //30秒内不断尝试获得锁
            while (System.currentTimeMillis() < sysTime + TIME_OUT) {
                long lockTime = System.currentTimeMillis();
                 String result = jedis.set(key, TypeUtil.toString(lockTime), SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, LOCK_SEC);
                 if (LOCK_SUCCESS.equals(result)) {
                    return lockTime;
                 }else{
                    ThreadUtil.sleep(100);
                 }
            }
            redisClientFactory.release(jedis);
            log.warn("多次尝试获得锁失败，最终放弃，:" + obj );
            throw new SwiftRuntimeException("服务器繁忙，请稍后再试。");
        }
    }
   
    public void unLock(Object obj,long lockTime){
        String key = key(obj);
        Jedis jedis = redisClientFactory.getJedis();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(key), Collections.singletonList(TypeUtil.toString(lockTime)));
        if (RELEASE_SUCCESS.equals(result)) {
            return;
        }else {
            log.info("释放锁失败或者锁已经过期:" + obj + "; lockTime:" + lockTime);
        }
        redisClientFactory.release(jedis);
    }
    
    private String key(Object obj){
        return REDIS_KEY+String.valueOf(obj);
    }
}