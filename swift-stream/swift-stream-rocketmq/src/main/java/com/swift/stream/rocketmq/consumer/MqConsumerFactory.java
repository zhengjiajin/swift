/*
 * @(#)MqConsumerFactory.java   1.0  2021年6月30日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.consumer;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.exception.extend.SystemException;
import com.swift.stream.rocketmq.MqConfig;
import com.swift.stream.rocketmq.RocketMqConfig;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.bean.AopTargetUtils;
import com.swift.util.type.TypeUtil;

/**
 * 创建MQ观察者连接工厂 
 * @author zhengjiajin
 * @version 1.0 2021年6月30日
 */
@Component
public class MqConsumerFactory {

    @Autowired
    private RocketMqConfig rocketMqConfig;
    //所有
    @Autowired(required = false)
    private List<RocketMqMessageListener> listenerList;
    
    //rocketKey:MessageModel:consumer
    private List<RocketMqConsumer> consumerList = new LinkedList<>();
    @PostConstruct
    private void init() {
        if(TypeUtil.isNull(listenerList)) return;
        for(RocketMqMessageListener listener:listenerList) {
            RocketMqConsumer handler = findConsumer(listener);
            handler.addListener(listener);
        }
        for(RocketMqConsumer consumer:consumerList) {
            consumer.start();
        }
        
    }
    
    private RocketMqConsumer findConsumer(RocketMqMessageListener listener) {
        Topic topic = AnnotationUtil.getAnnotation(AopTargetUtils.getTarget(listener).getClass(), Topic.class);
        if(topic==null) throw new SystemException(listener.getClass()+"没用添加@Topic注解");
        MqConfig mqConfig = rocketMqConfig.getConfig(topic.rocketKey());
        if(mqConfig==null) throw new SystemException(topic.rocketKey()+"找不到对应连接的配置");
        for(RocketMqConsumer consumer:consumerList) {
            if(consumer.getMqconfig()==mqConfig && consumer.getMessageModel().equalsIgnoreCase(topic.messageModel())) {
                return consumer;
            }
        }
        RocketMqConsumer consumer = new RocketMqConsumer();
        consumer.setMqconfig(mqConfig);
        consumer.setMessageModel(topic.messageModel());
        consumerList.add(consumer);
        return consumer;
    }
    
    @PreDestroy
    private void close() {
        for(RocketMqConsumer consumer:consumerList) {
            consumer.close();
        }
    }

    /**
     * @return the listenerList
     */
    public List<RocketMqMessageListener> getListenerList() {
        return listenerList;
    }
    
}
