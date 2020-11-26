/*
 * @(#)RocketMqService.java   1.0  2020年11月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.producer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;

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
    public SendResult send(String topic, MqRequest request);
    /**
     * 异步发送，不需要管返回的消息，有大丢失风险
     * @param topic
     * @param request
     */
    public void sendOneway(String topic, MqRequest request);
    /**
     * 异步发送消息，有丢失风险
     * @param topic
     * @param request
     * @param callback
     */
    public void send(String topic, MqRequest request, SendCallback callback);

    
}
