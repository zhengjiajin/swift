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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.SendCallback;
import com.swift.util.text.JsonUtil;

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
     * 
     * @author Avery Xiao
     * @version 0.0.1-SNAPSHOT
     * @date Mar 6, 2017 3:15:49 PM
     */
    public abstract class AliMqMessageHandler implements MessageListener{
        private final static Logger log = LoggerFactory.getLogger(AliMqMessageHandler.class);
        
        public Action consume(Message message, ConsumeContext context) {
            try {
                handle(JsonUtil.toObj(message.getBody(), AliMqRequest.class));
            }catch(Throwable ex) {
                log.error("MQ处理信息异常:",ex);
            }
            return Action.CommitMessage;
        }
        
        public abstract void handle(AliMqRequest request);
    }
    
    @Target(value= {ElementType.TYPE})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Topic {
        public String[] value();
        
        public String[] method() default "*";
    }
}
