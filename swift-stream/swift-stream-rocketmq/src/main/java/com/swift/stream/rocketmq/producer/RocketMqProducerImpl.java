/*
 * @(#)RocketMqProducer.java   1.0  2020年11月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.producer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.exception.extend.SystemException;
import com.swift.stream.rocketmq.RocketMqConfig;
import com.swift.stream.rocketmq.pojo.MqRequest;
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 创建MQ生产者应用服务,暂不支持事务消息
 * @author zhengjiajin
 * @version 1.0 2020年11月20日
 */
@Component
public class RocketMqProducerImpl implements RocketMqProducer{
    
    private static final Logger log = LoggerFactory.getLogger(RocketMqProducerImpl.class);
    
    @Autowired
    private RocketMqConfig rocketMqConfig;
    
    private DefaultMQProducer producer;
    /*
     * 使用默认的GROUP提交数据即可
     */
    private String producerGroup="__ONS_PRODUCER_DEFAULT_GROUP";
    //重发次数
    private int RE_NUM=3;
    
    @PostConstruct
    private void init() throws MQClientException {
        if(!rocketMqConfig.isStart()) return;
        if(TypeUtil.isNotNull(rocketMqConfig.getGroupId())) {
            producerGroup=rocketMqConfig.getGroupId();
        }
        producer = new DefaultMQProducer(producerGroup, rocketMqConfig.createRpcHook(), true, null);
        producer.setAccessChannel(AccessChannel.CLOUD);
        producer.setNamesrvAddr(rocketMqConfig.getONSAddr());
        producer.start();
    }
    
    
    
    @PreDestroy
    private void stop() {
        // 在应用退出前，销毁 Producer 对象
        // 注意：如果不销毁也没有问题
        if(producer!=null) producer.shutdown();
    }



    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public SendResult send(String topic, MqRequest request) {
        check();
        SendResult sendResult = null;
        for(int i=0;i<RE_NUM;i++) {
            try {
                //Message msg = new Message(rocketMqConfig.remoteTopic(topic), request.getTag(), JsonUtil.toJson(request.getData()).getBytes(RemotingHelper.DEFAULT_CHARSET));
                
                Message msg = new Message(rocketMqConfig.remoteTopic(topic), request.getTag(), JsonUtil.toJson(request).getBytes(RemotingHelper.DEFAULT_CHARSET));
                msg.setKeys(request.getMsgId());
                sendResult = producer.send(msg);
                if(sendResult!=null && sendResult.getSendStatus()==SendStatus.SEND_OK)
                return sendResult;
            } catch (Exception e) {
                log.warn("发送MQ消息异常:",e);
            }
        }
        return sendResult;
    }



    /**
     * 
     */
    private void check() {
        if(producer==null) throw new SystemException("Rocket参数未配置");
    }



    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#sendOneway(java.lang.String, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public void sendOneway(String topic, MqRequest request) {
        check();
        for(int i=0;i<RE_NUM;i++) {
            try {
              //Message msg = new Message(rocketMqConfig.remoteTopic(topic), request.getTag(), JsonUtil.toJson(request.getData()).getBytes(RemotingHelper.DEFAULT_CHARSET));
               
                Message msg = new Message(rocketMqConfig.remoteTopic(topic), request.getTag(), JsonUtil.toJson(request).getBytes(RemotingHelper.DEFAULT_CHARSET));
                
                msg.setKeys(request.getMsgId());
                producer.sendOneway(msg);
                return;
            } catch (Exception e) {
                log.warn("发送MQ消息异常:",e);
            }
        }
    }



    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, com.swift.stream.rocketmq.pojo.MqRequest, org.apache.rocketmq.client.producer.SendCallback)
     */
    @Override
    public void send(String topic, MqRequest request, SendCallback callback) {
        check();
        for(int i=0;i<RE_NUM;i++) {
            try {
              //Message msg = new Message(rocketMqConfig.remoteTopic(topic), request.getTag(), JsonUtil.toJson(request.getData()).getBytes(RemotingHelper.DEFAULT_CHARSET));
              
                Message msg = new Message(rocketMqConfig.remoteTopic(topic), request.getTag(), JsonUtil.toJson(request).getBytes(RemotingHelper.DEFAULT_CHARSET));
                
                msg.setKeys(request.getMsgId());
                producer.send(msg, callback);
                return;
            } catch (Exception e) {
                log.warn("发送MQ消息异常:",e);
            }
        }
    }
}
