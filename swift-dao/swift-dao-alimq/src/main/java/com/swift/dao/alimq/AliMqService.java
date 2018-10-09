/*
 * @(#)AliMqService.java   1.0  2018年7月10日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.alimq;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.aliyun.openservices.ons.api.SendCallback;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年7月10日
 */
public interface AliMqService {
    public void sendRequest(String topic, AliMqRequest request);
    
    public void sendRequest(String topic, AliMqRequest request, SendCallback callback);
    /**
     * 收到消息时通知
     */
    public interface AliMqMessageListener {
        public void handle(AliMqRequest request);
    }
    
    @Target(value= {ElementType.TYPE})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Topic {
        public String[] value();
        
        public String[] method() default "*";
    }
}
