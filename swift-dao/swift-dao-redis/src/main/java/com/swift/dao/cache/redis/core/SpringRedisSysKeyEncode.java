/*
 * @(#)SpringRedisSysKeyEncode.java   1.0  2019年9月19日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.cache.redis.core;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import com.alibaba.fastjson.JSON;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2019年9月19日
 */
public class SpringRedisSysKeyEncode implements RedisSerializer<String> {

    private final static String SYS_SPILT="_.";
    
    private String sysId="";
    
    public SpringRedisSysKeyEncode(String sysId) {
        if(sysId!=null) this.sysId=sysId;
    }
    /** 
     * @see org.springframework.data.redis.serializer.RedisSerializer#serialize(java.lang.Object)
     */
    @Override
    public byte[] serialize(String t) throws SerializationException {
        String resStr = null;
        if (t == null) {
            return null;
        }
        if (t instanceof String) {
            resStr= sysId+SYS_SPILT+t;
        }else {
            resStr= sysId+SYS_SPILT+JSON.toJSONString(t);
        }
        return resStr.getBytes();
    }

    /** 
     * @see org.springframework.data.redis.serializer.RedisSerializer#deserialize(byte[])
     */
    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        if(bytes==null) return null;
        String t = new String(bytes);
        return sysId+SYS_SPILT+t;
    }

}