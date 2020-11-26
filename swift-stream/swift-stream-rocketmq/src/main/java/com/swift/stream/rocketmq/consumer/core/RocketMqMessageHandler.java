/*
 * @(#)RocketMqMessageHandler.java   1.0  2020年11月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.consumer.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.stream.rocketmq.RocketMqConfig;
import com.swift.stream.rocketmq.consumer.ExpressionUtil;
import com.swift.stream.rocketmq.consumer.RocketMqConsumer;
import com.swift.stream.rocketmq.consumer.RocketMqMessageListener;
import com.swift.stream.rocketmq.consumer.Topic;
import com.swift.stream.rocketmq.pojo.MqRequest;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.bean.AopTargetUtils;
import com.swift.util.type.TypeUtil;

/**
 * 收到消息时的处理类
 * @author zhengjiajin
 * @version 1.0 2020年11月26日
 */
@Component
public class RocketMqMessageHandler implements MessageListenerConcurrently {
        
        private static final Logger log = LoggerFactory.getLogger(RocketMqConsumer.class);
        @Autowired
        private RocketMqConfig rocketMqConfig;
        @Autowired(required = false)
        private List<RocketMqMessageListener> listener;
        //TAG
        private final ConcurrentHashMap<String, List<String>> subscribeTag = new ConcurrentHashMap<>();
        @PostConstruct
        private void init() {
             if(TypeUtil.isNull(listener)) return;
             for(RocketMqMessageListener msl:listener) {
                 Topic topic = AnnotationUtil.getAnnotation(AopTargetUtils.getTarget(msl).getClass(), Topic.class);
                 if (topic == null) continue;
                 for(String t:topic.value()) {
                     if(!subscribeTag.containsKey(t)) {
                         subscribeTag.put(t, new ArrayList<>());
                     }
                     
                     for(String tag:topic.tag()) {
                         if(!subscribeTag.get(t).contains(tag)) {
                             subscribeTag.get(t).add(tag);
                         }
                     }
                 }
             }
        }
        /** 
         * @see org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently#consumeMessage(java.util.List, org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext)
         */
        @Override
        public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
            if(TypeUtil.isNull(msgs)) return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            for(MessageExt message:msgs) {
                try {
                    log.info("收到MQ消息:"+message.getTopic()+";"+message.getMsgId()+";"+message.getKeys()+";"+message.getTags()+new String(message.getBody()));
                    for(RocketMqMessageListener listener:listener) {
                        Topic topic = AnnotationUtil.getAnnotation(AopTargetUtils.getTarget(listener).getClass(), Topic.class);
                        if (topic == null) continue;
                        if(!checkInTopic(topic, message.getTopic())) continue;
                        if(!checkTag(topic, message.getTags())) continue;
                        MqRequest req = listener.changeRequest(message);
                        listener.handle(req); 
                    }
                }catch(Throwable ex) {
                    log.error("MQ处理信息异常:",ex);
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        
        private boolean checkInTopic(Topic topic,String msgTopic) {
            for(String t:topic.value()) {
                if(t.equalsIgnoreCase(rocketMqConfig.localTopic(msgTopic))) {
                    return true;
                }
            }
            return false;
        }
        

        private boolean checkTag(Topic topic,String tag) {
            if(ExpressionUtil.checkExpression(topic.tag(), tag)) {
                return true;
            }
            return false;
        }
        
        /**
         * @return the subscribeTag
         */
        public ConcurrentHashMap<String, List<String>> getSubscribeTag() {
            return subscribeTag;
        }
        /**
         * @return the listener
         */
        public List<RocketMqMessageListener> getListener() {
            return listener;
        }
        
        
}
