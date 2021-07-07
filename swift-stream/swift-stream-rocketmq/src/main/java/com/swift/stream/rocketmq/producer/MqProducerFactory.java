/*
 * @(#)MqProducerFactory.java   1.0  2021年6月30日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PreDestroy;

import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.transaction.TransactionStatus;
import com.swift.exception.extend.SystemException;
import com.swift.stream.rocketmq.MqConfig;
import com.swift.stream.rocketmq.RocketMqConfig;
import com.swift.stream.rocketmq.TopicType;
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 创建MQ生产者连接工厂
 * @author zhengjiajin
 * @version 1.0 2021年6月30日
 */
@Component
public class MqProducerFactory {

    //private static final Logger log = LoggerFactory.getLogger(RocketMqProducerImpl.class);
    
    @Autowired
    private RocketMqConfig rocketMqConfig;
    
    private Map<String,Map<TopicType,Object>> producerFactory = new HashMap<>();
    
    
    private Properties configAliyun(MqConfig config) {
        Properties properties = new Properties();
        // AccessKeyId 阿里云身份验证，在阿里云用户信息管理控制台获取。
        properties.put(PropertyKeyConst.AccessKey,config.getAccessKey());
        // AccessKeySecret 阿里云身份验证，在阿里云用户信息管理控制台获取。
        properties.put(PropertyKeyConst.SecretKey, config.getSecretKey());
        // 设置TCP接入域名，进入消息队列RocketMQ版控制台实例详情页面的接入点区域查看。
        properties.put(PropertyKeyConst.NAMESRV_ADDR, config.getONSAddr());
        // 您在消息队列RocketMQ版控制台创建的Group ID。
        properties.put(PropertyKeyConst.GROUP_ID,config.getGroupId());
        return properties;
    }
    
    private com.aliyun.openservices.ons.api.Producer createAliyunDefault(MqConfig config) {
        com.aliyun.openservices.ons.api.Producer producer = ONSFactory.createProducer(configAliyun(config));
        producer.start();
        return producer;
    }

    private com.aliyun.openservices.ons.api.order.OrderProducer createAliyunOrder(MqConfig config) {
        com.aliyun.openservices.ons.api.order.OrderProducer producer = ONSFactory.createOrderProducer(configAliyun(config));
        producer.start();
        return producer;
    }
    
    private com.aliyun.openservices.ons.api.transaction.TransactionProducer createAliyunTransaction(MqConfig config) {
        com.aliyun.openservices.ons.api.transaction.TransactionProducer producer = ONSFactory.createTransactionProducer(configAliyun(config),  new com.aliyun.openservices.ons.api.transaction.LocalTransactionChecker() {

            @Override
            public TransactionStatus check(com.aliyun.openservices.ons.api.Message msg) {
                return TransactionStatus.CommitTransaction;//TODO 现场景基本都是在MQ名执行事务已成功
            }
            
        });
        producer.start();
        return producer;
    }
    
    private org.apache.rocketmq.client.producer.DefaultMQProducer createApacheDefault(MqConfig config) {
        org.apache.rocketmq.client.producer.DefaultMQProducer producer = new org.apache.rocketmq.client.producer.DefaultMQProducer(config.getGroupId(), config.createRpcHook(), true, null);
        producer.setAccessChannel(AccessChannel.CLOUD);
        producer.setNamesrvAddr(config.getONSAddr());
        try {
            producer.start();
        } catch (MQClientException e) {
            throw new SystemException("rocket "+JsonUtil.toJson(config)+"启动异常!!!",e);
        }
        return producer;
    }

    private org.apache.rocketmq.client.producer.DefaultMQProducer createApacheOrder(MqConfig config) {
        return createApacheDefault(config);
    }
    
    private org.apache.rocketmq.client.producer.TransactionMQProducer createApacheTransaction(MqConfig config) {
        org.apache.rocketmq.client.producer.TransactionMQProducer transactionMQProducer = new org.apache.rocketmq.client.producer.TransactionMQProducer(config.getGroupId(), config.createRpcHook());
        transactionMQProducer.setNamesrvAddr(config.getONSAddr());
        transactionMQProducer.setTransactionListener(new org.apache.rocketmq.client.producer.TransactionListener() {

            @Override
            public LocalTransactionState executeLocalTransaction(org.apache.rocketmq.common.message.Message msg, Object arg) {
                return LocalTransactionState.COMMIT_MESSAGE;//TODO 现场景基本都是在MQ名执行事务已成功
            }

            @Override
            public LocalTransactionState checkLocalTransaction(org.apache.rocketmq.common.message.MessageExt msg) {
                return LocalTransactionState.COMMIT_MESSAGE;//TODO 现场景基本都是在MQ名执行事务已成功
            }
            
        });
        try {
            transactionMQProducer.start();
        } catch (MQClientException e) {
            throw new SystemException("rocket "+JsonUtil.toJson(config)+"启动异常!!!",e);
        }
        return transactionMQProducer;
    }
    
    private Object createProducer(TopicType topicType,MqConfig config) {
        if(!config.isStart()) return null;
        if(config.isAliyun()) {
            if(topicType==TopicType.Default) return createAliyunDefault(config);
            if(topicType==TopicType.Order) return createAliyunOrder(config);
            if(topicType==TopicType.Transaction) return createAliyunTransaction(config);
        } else {
            if(topicType==TopicType.Default) return createApacheDefault(config);
            if(topicType==TopicType.Order) return createApacheOrder(config);
            if(topicType==TopicType.Transaction) return createApacheTransaction(config);
        }
        throw new SystemException("rocket "+JsonUtil.toJson(config)+"配置参数异常!!!");
    }
    
    public Object getProducer(String rocketKey,String topic) {
        return getProducer(TopicType.Default, rocketKey, topic);
    }
    
    public Object getProducer(TopicType topicType,String topic) {
        return getProducer(topicType, RocketMqConfig.default_rocket_key, topic);
    }
    
    public Object getProducer(String topic) {
        return getProducer(TopicType.Default, RocketMqConfig.default_rocket_key, topic);
    }
    
    /**
     * 取得对应生产者连接,需要时才创建，即第一次发送时
     * @param cla 需要取连接的类型
     * @param rocketKey 连接对应配置文件的KEY
     * @param topic 需要发送的topic
     * @return
     */
    public Object getProducer(TopicType topicType,String rocketKey,String topic) {
        synchronized (rocketKey) {
            if(!producerFactory.containsKey(rocketKey)) {
                producerFactory.put(rocketKey, new HashMap<TopicType,Object>());
            }
        }
        
        synchronized (topicType) {
            if(!producerFactory.get(rocketKey).containsKey(topicType)) {
                MqConfig config = rocketMqConfig.getConfig(rocketKey);
                if(config==null) throw new SystemException("rocket."+rocketKey+"未配置!!!");
                if(!config.isStart()) throw new SystemException("rocket."+rocketKey+"配置参数不完!!!");
                producerFactory.get(rocketKey).put(topicType, createProducer(topicType, config));
            }
        }
        return producerFactory.get(rocketKey).get(topicType);
    }
    
    @PreDestroy
    private void stop() {
        // 在应用退出前，销毁 Producer 对象
        // 注意：如果不销毁也没有问题
        for(Map<TopicType,Object> mapProducer: producerFactory.values()) {
            if(TypeUtil.isNull(mapProducer)) continue;
            for(Object producer:mapProducer.values()) {
                
                if(producer instanceof com.aliyun.openservices.ons.api.Admin) {
                    ((com.aliyun.openservices.ons.api.Admin)producer).shutdown();
                }
                
                if(producer instanceof org.apache.rocketmq.client.producer.MQProducer) {
                    ((org.apache.rocketmq.client.producer.MQProducer)producer).shutdown();
                }
                
            }
        }
    }
}
