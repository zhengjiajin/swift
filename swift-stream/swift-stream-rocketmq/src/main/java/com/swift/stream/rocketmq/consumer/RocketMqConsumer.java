/*
 * @(#)AliMqConsumer.java   1.0  2018年7月10日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.consumer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.stream.rocketmq.RocketMqConfig;
import com.swift.stream.rocketmq.consumer.core.RocketMqMessageHandler;
import com.swift.util.type.TypeUtil;

/**
 * 现只支持普通队列，排序队列和事务队列暂不支持
 * 
 * @author zhengjiajin
 * @version 1.0 2018年7月10日
 */
@Component
public class RocketMqConsumer {
    
    @Autowired
    private RocketMqConfig rocketMqConfig;
    
    @Autowired(required = false)
    private RocketMqMessageHandler rocketMqMessageHandler;
    
    private DefaultMQPushConsumer consumer;
    
    @PostConstruct
    private void init() throws MQClientException {
        if (!rocketMqConfig.isStart()) return;
        if (TypeUtil.isNull(rocketMqConfig.getGroupId())) return;
        if(TypeUtil.isNull(rocketMqMessageHandler.getSubscribeTag())) return;
        if (TypeUtil.isNull(rocketMqMessageHandler.getListener())) return;
        consumer = new DefaultMQPushConsumer(rocketMqConfig.getGroupId(), rocketMqConfig.createRpcHook(), new AllocateMessageQueueAveragely());
        consumer.setNamesrvAddr(rocketMqConfig.getONSAddr());
        consumer.setAccessChannel(AccessChannel.CLOUD);
        consumer.setConsumeMessageBatchMaxSize(1);
        for(String topic:rocketMqMessageHandler.getSubscribeTag().keySet()) {
            consumer.subscribe(rocketMqConfig.remoteTopic(topic), ExpressionUtil.subExpression(rocketMqMessageHandler.getSubscribeTag().get(topic)));
        }
        consumer.registerMessageListener(rocketMqMessageHandler);
        consumer.start();
    }
    
    @PreDestroy
    private void end() {
        if (consumer != null) consumer.shutdown();
    }
    
}
