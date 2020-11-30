/*
 * @(#)RedisClientFactory.java   1.0  2020年11月27日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.redis.core;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

import com.swift.exception.extend.SystemException;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年11月27日
 */
public class RedisClientFactory extends JedisConnectionFactory {
    public static final Logger LOG = LoggerFactory.getLogger(RedisClientFactory.class);
    
    private static ThreadLocal<Jedis> threadJedis = new ThreadLocal<Jedis>();  
 
    public RedisClientFactory() {
        super();
    }


    public RedisClientFactory(JedisPoolConfig poolConfig) {
        super(poolConfig);
    }


    public RedisClientFactory(RedisSentinelConfiguration sentinelConfig) {
        super(sentinelConfig);
    }


    public RedisClientFactory(RedisSentinelConfiguration sentinelConfig, JedisPoolConfig poolConfig) {
        super(sentinelConfig,poolConfig);
    }


    public RedisClientFactory(RedisClusterConfiguration clusterConfig) {
        super(clusterConfig);
    }


    public RedisClientFactory(RedisClusterConfiguration clusterConfig, JedisPoolConfig poolConfig) {
        super(clusterConfig,poolConfig);
    }

    public RedisClientFactory(RedisStandaloneConfiguration standaloneConfig) {
        super(standaloneConfig);
    }

    public RedisClientFactory(RedisStandaloneConfiguration standaloneConfig, JedisClientConfiguration clientConfig) {
        super(standaloneConfig,clientConfig);
    }

    public RedisClientFactory(RedisSentinelConfiguration sentinelConfig, JedisClientConfiguration clientConfig) {
        super(sentinelConfig,clientConfig);
    }

    public RedisClientFactory(RedisClusterConfiguration clusterConfig, JedisClientConfiguration clientConfig) {
        super(clusterConfig,clientConfig);
    }
    
    public Jedis getJedis(){
        Jedis jedis = null;
        try{
            if(threadJedis.get()!=null) {
                jedis = threadJedis.get();
            }else {
                jedis = super.fetchJedisConnector();
                threadJedis.set(jedis);
            }
            if(!jedis.isConnected()){
                jedis.connect();
            }
        }catch(JedisConnectionException e){
            if(jedis!=null) release(jedis);
            throw new SystemException(e);
        }
        return jedis;
    }
    
    public void release(Jedis jedis){
        if(jedis!=null){
            jedis.close();
            threadJedis.remove();
        }
    }
    
    public void release(){
        Jedis jedis = threadJedis.get();
        if(jedis!=null){
            threadJedis.remove();
            jedis.close();
        }
    }

    public void close() throws IOException {
        super.destroy();
    }
}
