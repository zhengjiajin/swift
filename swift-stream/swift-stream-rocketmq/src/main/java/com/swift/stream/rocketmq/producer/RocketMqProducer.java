/*
 * @(#)RocketMqService.java   1.0  2020年11月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.producer;

import java.util.Date;
import com.swift.stream.rocketmq.pojo.MqRequest;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年11月20日
 */
public interface RocketMqProducer {
    /**
     * 同步发送消息
     * @param topic
     * @param request
     * @return
     */
    public String send(String topic, MqRequest request);
    
    public String send(String rocketKey,String topic, MqRequest request);
    /**
     * 异步发送，不需要管返回的消息，有大丢失风险
     * @param topic
     * @param request
     */
    public void sendOneway(String topic, MqRequest request);
    
    public void sendOneway(String rocketKey,String topic, MqRequest request);
    /**
     * 异步发送消息，有丢失风险
     * @param topic
     * @param request
     * @param callback
     */
    public void send(String topic, MqRequest request, SendCallback callback);
    
    public void send(String rocketKey,String topic, MqRequest request, SendCallback callback);
    
    //顺序消息
    public String sendOreder(String topic,String shardingKey, MqRequest request);
    
    public String sendOreder(String rocketKey,String topic,String shardingKey, MqRequest request);
    
    //廷时消息
    public String send(String topic,DelayLevel levle, MqRequest request);
    
    public String send(String rocketKey,String topic,DelayLevel levle, MqRequest request);
    
    public void sendOneway(String topic, DelayLevel levle, MqRequest request);
    
    public void sendOneway(String rocketKey,String topic, DelayLevel levle, MqRequest request);
    
    public void send(String topic,DelayLevel levle, MqRequest request, SendCallback callback);
    
    public void send(String rocketKey,String topic, DelayLevel levle, MqRequest request, SendCallback callback);
    //定时消息
    public String send(String topic,Date cDate, MqRequest request);
    
    public String send(String rocketKey,String topic,Date cDate, MqRequest request);
    
    public void sendOneway(String topic, Date cDate, MqRequest request);
    
    public void sendOneway(String rocketKey,String topic, Date cDate, MqRequest request);
    
    public void send(String topic, Date cDate, MqRequest request, SendCallback callback);
    
    public void send(String rocketKey,String topic, Date cDate, MqRequest request, SendCallback callback);
    //事务消息 TODO
 
    
    public interface SendCallback {
        void onSuccess(final String sendResult);

        void onException(final Throwable e);
    }
    
}
