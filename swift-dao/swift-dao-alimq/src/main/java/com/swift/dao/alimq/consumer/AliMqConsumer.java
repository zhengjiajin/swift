/*
 * @(#)AliMqConsumer.java   1.0  2018年7月10日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.alimq.consumer;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.swift.dao.alimq.AliMqConfigurer;
import com.swift.dao.alimq.AliMqService.AliMqMessageHandler;
import com.swift.dao.alimq.AliMqService.Topic;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.bean.AopTargetUtils;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年7月10日
 */
@Component
public class AliMqConsumer {

    private Consumer consumer;

    @Autowired(required = false)
    private List<AliMqMessageHandler> handlers;

    @PostConstruct
    private void init() {
        if (TypeUtil.isNull(AliMqConfigurer.getProperties().get(PropertyKeyConst.ConsumerId))) return;
        if (TypeUtil.isNull(handlers)) return;
        consumer = ONSFactory.createConsumer(AliMqConfigurer.getProperties());
        for (AliMqMessageHandler handler : handlers) {
            Topic topic = AnnotationUtil.getAnnotation(AopTargetUtils.getTarget(handler).getClass(), Topic.class);
            if (topic == null) continue;
            for(String top:topic.value()) {
                consumer.subscribe(AliMqConfigurer.removeTopic(top), subExpression(topic.method()), handler);
            }
        }
        consumer.start();
    }

    private String subExpression(String[] method) {
        if(method==null || method.length<=0) return "*";
        StringBuffer sb = new StringBuffer();
        //tag1 || tag2 || tag3
        for(String m:method) {
            sb.append(m).append(" || ");
        }
        return sb.substring(0, sb.length()-4);
    }
    
    @PreDestroy
    private void end() {
        if (consumer != null && consumer.isStarted()) consumer.shutdown();
    }
}
