/*
 * @(#)JetCacheConfig.java   1.0  2018年4月27日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.dao.redis;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alicp.jetcache.CacheBuilder;
import com.alicp.jetcache.anno.CacheConsts;
import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.alicp.jetcache.anno.support.GlobalCacheConfig;
import com.alicp.jetcache.anno.support.SpringConfigProvider;
import com.alicp.jetcache.embedded.LinkedHashMapCacheBuilder;
import com.alicp.jetcache.redis.springdata.RedisSpringDataCacheBuilder;
import com.alicp.jetcache.support.FastjsonKeyConvertor;
import com.alicp.jetcache.support.KryoValueDecoder;
import com.alicp.jetcache.support.KryoValueEncoder;
import com.swift.core.env.EnvDecode;
import com.swift.dao.redis.core.RedisClientFactory;
import com.swift.exception.extend.SystemException;
import com.swift.util.type.IpUtil;
import com.swift.util.type.TypeUtil;

import redis.clients.jedis.Client;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年4月27日
 */
@Configuration
@EnableMethodCache(basePackages = {"com.swift","com.test"})
@EnableCreateCacheAnnotation
public class JetCacheConfig {
    
    @Value("${sysId:}")
    private String sysId;
    //通过此字段从0库中找到系统对应的库
    private final static String SYS_SELECT_DB_KEY="SYS_SELECT_DB";
    
    private final static String SYS_SELECT_DB_INCR="SYS_SELECT_DB_INCR";
    //最大DB数
    private final static int MAX_REDIS_DB=255;
    
    private int maxActive=300;
    private int maxIdle=5;
    private long maxWait=30000;
    private boolean test=false;
    //不再支持多连接，使用代码的方式实现集群
    @Value("${redis.hosts:}")
    private String hosts="";
    @Value("${redis.password}")
    private String password="";

    @Bean
    public RedisClientFactory redisClientFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setMinIdle(3);
        poolConfig.setTestOnBorrow(test);
        poolConfig.setTestOnReturn(test);
        poolConfig.setTestWhileIdle(test);
        JedisClientConfiguration clientConfig = JedisClientConfiguration.builder()
                .usePooling().poolConfig(poolConfig).and().readTimeout(Duration.ofMillis(Protocol.DEFAULT_TIMEOUT)).build();
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(getHost());
        redisConfig.setPort(getPort());
        redisConfig.setPassword(getPassword());
        redisConfig.setDatabase(getSysDatabase());
        return new RedisClientFactory(redisConfig,clientConfig);
    }
    
    private int getSysDatabase() {
        if(TypeUtil.isNull(sysId)) return Protocol.DEFAULT_TIMEOUT;
        //通过0库选择系统库
        Jedis jedis = new Jedis(getHost(),getPort());
        Client client = jedis.getClient();
        client.setPassword(getPassword());
        client.setDb(Protocol.DEFAULT_DATABASE);
        jedis.connect(); 
        try {
            String value = jedis.hget(SYS_SELECT_DB_KEY, sysId);
            if(TypeUtil.isNotNull(value)) return TypeUtil.toInt(value);
            
            Long num = jedis.incr(SYS_SELECT_DB_INCR);
            if(TypeUtil.isNull(num)) throw new SystemException("REDIS选择数据库异常");
            if(num>MAX_REDIS_DB) {
                jedis.decr(SYS_SELECT_DB_INCR);
                throw new SystemException("REDIS库已满");
            }
            jedis.hset(SYS_SELECT_DB_KEY, sysId, TypeUtil.toString(num));
            return TypeUtil.toInt(num);
        } finally{
            jedis.close();
        }
    }
    
    private String getPassword() {
        return EnvDecode.decode(password);
    }
    
    private String getHost() {
        if(hosts.indexOf(":")==-1){
            return IpUtil.domainToIp(hosts);
        }else{
            return IpUtil.domainToIp(hosts.split(":")[0]);
        }
    }
    
    private int getPort() {
        if(hosts.indexOf(":")==-1){
            return Protocol.DEFAULT_PORT;
        }else{
            return Integer.valueOf(hosts.split(":")[1]);
        }
    }
    
    
    @Bean
    public RedisTemplate<String,String> redisTemplate(RedisClientFactory redisClientFactory) {
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<String,String>();
        redisTemplate.setConnectionFactory(redisClientFactory);
        //redisTemplate.setKeySerializer(new SpringRedisSysKeyEncode(sysId));
        //redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        //redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }
    
    @Bean
    public SpringConfigProvider springConfigProvider() {
        return new SpringConfigProvider();
    }
    
    /**
     * 缓存设置
     * @param redisClientFactory
     * @return
     */
    @Bean
    public GlobalCacheConfig config(RedisClientFactory redisClientFactory) {
        Map<String, CacheBuilder> localBuilders = new HashMap<String, CacheBuilder>();
        CacheBuilder localBuilder = LinkedHashMapCacheBuilder.createLinkedHashMapCacheBuilder().keyConvertor(FastjsonKeyConvertor.INSTANCE);
        localBuilders.put(CacheConsts.DEFAULT_AREA, localBuilder);
        Map<String, CacheBuilder> remoteBuilders = new HashMap<String, CacheBuilder>();
        CacheBuilder remoteCacheBuilder = RedisSpringDataCacheBuilder.createBuilder()
                .keyConvertor(new FastjsonKeyConvertor())
                .cacheNullValue(false)
                .valueEncoder(KryoValueEncoder.INSTANCE)
                .valueDecoder(KryoValueDecoder.INSTANCE)
                .expireAfterWrite(7, TimeUnit.DAYS)
                .connectionFactory(redisClientFactory);
        remoteBuilders.put(CacheConsts.DEFAULT_AREA, remoteCacheBuilder);
        GlobalCacheConfig globalCacheConfig = new GlobalCacheConfig();
        globalCacheConfig.setLocalCacheBuilders(localBuilders);
        globalCacheConfig.setRemoteCacheBuilders(remoteBuilders);
        globalCacheConfig.setStatIntervalMinutes(15);
        globalCacheConfig.setAreaInCacheName(false);
        return globalCacheConfig;
    }

}
