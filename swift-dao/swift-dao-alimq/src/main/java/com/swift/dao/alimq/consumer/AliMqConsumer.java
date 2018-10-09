/*
 * @(#)AliMqConsumer.java   1.0  2018年7月10日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.alimq.consumer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.swift.dao.alimq.AliMqConfigurer;
import com.swift.dao.alimq.AliMqRequest;
import com.swift.dao.alimq.AliMqService.AliMqMessageListener;
import com.swift.dao.alimq.AliMqService.Topic;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.bean.AopTargetUtils;
import com.swift.util.text.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年7月10日
 */
@Component
public class AliMqConsumer {
    private static final Logger log = LoggerFactory.getLogger(AliMqConsumer.class);
    
    private Consumer consumer;

    @Autowired(required = false)
    private List<AliMqMessageListener> handlers;

    @PostConstruct
    private void init() {
        if (TypeUtil.isNull(AliMqConfigurer.getProperties().get(PropertyKeyConst.ConsumerId))) return;
        if (TypeUtil.isNull(handlers)) return;
        consumer = ONSFactory.createConsumer(AliMqConfigurer.getProperties());
        List<AliMqMessageHandler> handlerList = createHandler();
        if(TypeUtil.isNull(handlerList)) return;
        for (AliMqMessageHandler handler : handlerList) {
             log.info("创建MQ监听:"+handler.topic+";expression="+handler.expression);
             consumer.subscribe(AliMqConfigurer.removeTopic(handler.topic), handler.expression, handler);
        }
        consumer.start();
    }
    
    private List<AliMqMessageHandler> createHandler(){
        Map<String,TopicModel> topicMap = new HashMap<>();
        List<AliMqMessageHandler> listHandler = new ArrayList<>();
        for(AliMqMessageListener handler:handlers) {
            Topic topic = AnnotationUtil.getAnnotation(AopTargetUtils.getTarget(handler).getClass(), Topic.class);
            if (topic == null) continue;
            for(String t:topic.value()) {
                if(!topicMap.containsKey(t)) {
                    topicMap.put(t, new TopicModel());
                }
                TopicModel tm = topicMap.get(t);
                for(String method:topic.method()) {
                    if(!tm.expList.contains(method)) {
                        tm.expList.add(method);
                    }
                }
                
                tm.aliMqMessageListener.add(handler);
            }
        }
        
        
        for(String t_str:topicMap.keySet()) {
            TopicModel tm = topicMap.get(t_str);
            AliMqMessageHandler handler = new AliMqMessageHandler();
            handler.topic=t_str;
            handler.expression=subExpression(tm.expList);
            handler.aliMqMessageListener=tm.aliMqMessageListener;
            listHandler.add(handler);
        }
        return listHandler;
    }
    
    private class TopicModel {
        private List<String> expList=new ArrayList<>();
        
        private List<AliMqMessageListener> aliMqMessageListener=new ArrayList<>();
    }
    
    /**
     * 收到消息时通知
     */
     private class AliMqMessageHandler implements MessageListener{
        
        private String topic;
        
        private String expression;
        
        private List<AliMqMessageListener> aliMqMessageListener;
        
        public Action consume(Message message, ConsumeContext context) {
            try {
                log.info("收到MQ消息:"+new String(message.getBody()));
                for(AliMqMessageListener listener:aliMqMessageListener) {
                    Topic topic = AnnotationUtil.getAnnotation(AopTargetUtils.getTarget(listener).getClass(), Topic.class);
                    if (topic == null) continue;
                    AliMqRequest req = JsonUtil.toObj(message.getBody(), AliMqRequest.class);
                    if(checkExpression(topic.method(), req.getMethod())) {
                        listener.handle(req); 
                    }
                }
            }catch(Throwable ex) {
                log.error("MQ处理信息异常:",ex);
            }
            return Action.CommitMessage;
        }
        
    }
     
    private boolean checkExpression(String[] methodList,String method) {
        if(TypeUtil.isNull(methodList)) return true;
        for(String checkMethod:methodList) {
            if("*".equals(checkMethod)) return true;
            if(checkMethod.equals(method)) return true;
        }
        return false;
    }

    private String subExpression(List<String> method) {
        if(method==null || method.size()<=0) return "*";
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
