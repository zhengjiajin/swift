/*
 * @(#)AliMqProducer.java   1.0  2018年7月10日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.alimq.producer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendCallback;
import com.aliyun.openservices.ons.api.SendResult;
import com.swift.dao.alimq.AliMqConfigurer;
import com.swift.dao.alimq.AliMqRequest;
import com.swift.dao.alimq.AliMqService;
import com.swift.exception.SwiftRuntimeException;
import com.swift.util.text.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年7月10日
 */
@Component
public class AliMqProducer implements AliMqService {
    
    private  Producer producer;
    
    @PostConstruct
    private void init() {
        if(TypeUtil.isNotNull(AliMqConfigurer.getProperties().get(PropertyKeyConst.ProducerId))) {
            producer = ONSFactory.createProducer(AliMqConfigurer.getProperties());
            // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可
            producer.start();
        }
    }
    
    /** 
     * @see com.swift.dao.alimq.AliMqService#sendRequest(java.lang.String, com.swift.dao.alimq.AliMqRequest)
     */
    @Override
    public void sendRequest(String topic, AliMqRequest request) {
        if(producer==null) throw new SwiftRuntimeException("请配置ProducerId");
        SendResult sendResult = producer.send(createMsg(topic, request));
        if(sendResult==null) throw new SwiftRuntimeException("发送alimq消息异常");
    }
    
    private Message createMsg(String topic, AliMqRequest request) {
        Message msg = new Message(AliMqConfigurer.removeTopic(topic), request.getMethod(),JsonUtil.toJson(request).getBytes());
        msg.setKey(request.getMsgId());
        return msg;
    }

    /** 
     * @see com.swift.dao.alimq.AliMqService#sendRequest(java.lang.String, com.swift.dao.alimq.AliMqRequest, com.aliyun.openservices.ons.api.SendCallback)
     */
    @Override
    public void sendRequest(String topic, AliMqRequest request, SendCallback callback) {
        if(producer==null) throw new SwiftRuntimeException("请配置ProducerId");
        producer.sendAsync(createMsg(topic, request), callback);
    }

    @PreDestroy
    private void end() {
        if(producer!=null && producer.isStarted()) producer.shutdown();
    }
    
}
