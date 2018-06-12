/*
 * @(#)KafkaService.java   1.0  2018年6月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.mq.kafka;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月6日
 */
public interface KafkaService {

    public void sendRequest(String topic, KafkaRequest request);
    
    public void sendRequest(String topic, KafkaRequest request, ProducerCallBack callback);
    
    /**
     * 异步返回的回调接口
     * @author ryan
     * @version 1.0 2017年3月13日
     */
    public interface ProducerCallBack {
        // 发送成功
        public static Integer SUCCESS = 0;
        // 发送失败
        public static Integer ERROR = 1;
        /**
         * 接收响应，收到响应后直接把消息移交给调用者做业务处理。
         */
        public void onCompletion(RecordMetadata recordMetadata, Integer resultCode);
    }
    
    /**
     * Kafka消息处理器。
     * 收到消息时通知
     * 
     * @author Avery Xiao
     * @version 0.0.1-SNAPSHOT
     * @date Mar 6, 2017 3:15:49 PM
     */
    public interface KafkaMessageHandler {
        void handle(KafkaRequest request);
    }
    
    @Target(value= {ElementType.TYPE})
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Topic {
        public String[] value();
    }
}
