/*
 * @(#)RocketMqMessageHandler.java   1.0  2020年11月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.consumer.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.swift.exception.extend.SystemException;
import com.swift.stream.rocketmq.MqConfig;
import com.swift.stream.rocketmq.consumer.ExpressionUtil;
import com.swift.stream.rocketmq.consumer.RocketMqConsumer;
import com.swift.stream.rocketmq.consumer.RocketMqMessageListener;
import com.swift.stream.rocketmq.consumer.Topic;
import com.swift.stream.rocketmq.pojo.MqMessage;
import com.swift.stream.rocketmq.pojo.MqRequest;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.bean.AopTargetUtils;
import com.swift.util.type.TypeUtil;

/**
 * 收到消息时的处理类
 * 
 * @author zhengjiajin
 * @version 1.0 2020年11月26日
 */
public class RocketMqMessageHandler implements MessageListenerConcurrently,MessageListener {

    private static final Logger log = LoggerFactory.getLogger(RocketMqConsumer.class);

    private MqConfig mqconfig;
    
    private String messageModel = "CLUSTERING";
    
    private List<RocketMqMessageListener> listenerList=new LinkedList<>();
    // TOPIC:TAG
    private final Map<String, List<String>> subscribeTag = new HashMap<>();

    public void addListener(RocketMqMessageListener listener){
        Topic topic = AnnotationUtil.getAnnotation(AopTargetUtils.getTarget(listener).getClass(), Topic.class);
        if (topic == null) throw new SystemException("设置的Listener没用Topic注解:"+listener.getClass());
        if(!listenerList.contains(listener)) {
            listenerList.add(listener);
            initTag(listener);
        }
    }
    
    public void setListener(List<RocketMqMessageListener> listenerList){
        subscribeTag.clear();
        if(TypeUtil.isNull(listenerList)) return;
        for(RocketMqMessageListener listener:listenerList) {
            addListener(listener);
        }
    }
    
    private void initTag(RocketMqMessageListener listener) {
        Topic topic = AnnotationUtil.getAnnotation(AopTargetUtils.getTarget(listener).getClass(), Topic.class);
        for (String t : topic.value()) {
            if (!subscribeTag.containsKey(t)) {
                subscribeTag.put(t, new ArrayList<>());
            }

            for (String tag : topic.tag()) {
                if (listener.requestAll()) tag = "*";
                if (!subscribeTag.get(t).contains(tag)) {
                    subscribeTag.get(t).add(tag);
                }
            }
        }
    }

    /**
     * @see org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently#consumeMessage(java.util.List,
     *      org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext)
     */
    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        if (TypeUtil.isNull(msgs)) return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        for (MessageExt message : msgs) {
            log.info("收到MQ消息:" + message.getTopic() + ";" + message.getMsgId() + ";" + message.getKeys() + ";"
                + message.getTags() + new String(message.getBody()));
            for (RocketMqMessageListener listener : listenerList) {
                try {
                    Topic topic = AnnotationUtil.getAnnotation(AopTargetUtils.getTarget(listener).getClass(), Topic.class);
                    if (topic == null) continue;
                    if (!checkInTopic(topic, message.getTopic())) continue;
                    MqMessage mqMessage = new MqMessage();
                    mqMessage.setBody(message.getBody());
                    mqMessage.setKey(message.getKeys());
                    mqMessage.setMsgId(message.getMsgId());
                    mqMessage.setTag(message.getTags());
                    mqMessage.setTopic(message.getTopic());
                    MqRequest req = listener.changeRequest(mqMessage);
                    if (!checkTag(topic, req.getTag())) continue; 
                    listener.handle(req);
                } catch (Throwable ex) {
                    log.error("MQ处理信息异常:", ex);
                }
            }
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
    

    /** 
     * @see com.aliyun.openservices.ons.api.MessageListener#consume(com.aliyun.openservices.ons.api.Message, com.aliyun.openservices.ons.api.ConsumeContext)
     */
    @Override
    public Action consume(Message message, ConsumeContext context) {
        if (TypeUtil.isNull(message)) return Action.ReconsumeLater;
        log.info("收到MQ消息:" + message.getTopic() + ";" + message.getMsgID() + ";" + message.getKey() + ";"
            + message.getTag() + new String(message.getBody()));
        for (RocketMqMessageListener listener : listenerList) {
            try {
                Topic topic = AnnotationUtil.getAnnotation(AopTargetUtils.getTarget(listener).getClass(), Topic.class);
                if (topic == null) continue;
                if (!checkInTopic(topic, message.getTopic())) continue;
                MqMessage mqMessage = new MqMessage();
                mqMessage.setBody(message.getBody());
                mqMessage.setKey(message.getKey());
                mqMessage.setMsgId(message.getMsgID());
                mqMessage.setTag(message.getTag());
                mqMessage.setTopic(message.getTopic());
                MqRequest req = listener.changeRequest(mqMessage);
                if (!checkTag(topic, req.getTag())) continue;
                listener.handle(req);
            } catch (Throwable ex) {
                log.error("MQ处理信息异常:", ex);
            }
        }
        return Action.CommitMessage;
    }

    private boolean checkInTopic(Topic topic, String msgTopic) {
        for (String t : topic.value()) {
            if (t.equalsIgnoreCase(mqconfig.localTopic(msgTopic))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkTag(Topic topic, String tag) {
        if (ExpressionUtil.checkExpression(topic.tag(), tag)) {
            return true;
        }
        return false;
    }

    /**
     * @return the subscribeTag
     */
    public Map<String, List<String>> getSubscribeTag() {
        return subscribeTag;
    }

    /**
     * @return the listener
     */
    public List<RocketMqMessageListener> getListener() {
        return listenerList;
    }
   
    /**
     * @return the mqconfig
     */
    public MqConfig getMqconfig() {
        return mqconfig;
    }

    /**
     * @param mqconfig the mqconfig to set
     */
    public void setMqconfig(MqConfig mqconfig) {
        this.mqconfig = mqconfig;
    }

    /**
     * @return the messageModel
     */
    public String getMessageModel() {
        return messageModel;
    }

    /**
     * @param messageModel the messageModel to set
     */
    public void setMessageModel(String messageModel) {
        this.messageModel = messageModel;
    }

}
