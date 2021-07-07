/*
 * @(#)RocketMqProducer.java   1.0  2020年11月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.producer;

import java.util.Date;

import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aliyun.openservices.ons.api.OnExceptionContext;
import com.swift.exception.ServiceException;
import com.swift.stream.rocketmq.RocketMqConfig;
import com.swift.stream.rocketmq.pojo.MqRequest;
import com.swift.util.type.JsonUtil;

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
    
    @Autowired
    private MqProducerFactory mqProducerFactory;
    //重发次数
    private int RE_NUM=3;
  
    private com.aliyun.openservices.ons.api.Message createAliyunMsg(String topic, MqRequest request,DelayLevel levle,Date cdate) {
        com.aliyun.openservices.ons.api.Message msg = new com.aliyun.openservices.ons.api.Message(rocketMqConfig.getDefaultConfig().remoteTopic(topic), request.getTag(), JsonUtil.toJson(request).getBytes());
        msg.setKey(request.getMsgId());
        if(levle!=DelayLevel.LEVEL_NOT) {
            long delayTime = System.currentTimeMillis() + levle.getLazyTime();
            msg.setStartDeliverTime(delayTime);
        }
        if(cdate!=null && cdate.getTime()>System.currentTimeMillis()) {
            msg.setStartDeliverTime(cdate.getTime());
        }
        return msg;
    }
    
    private org.apache.rocketmq.common.message.Message createApacheMsg(String topic, MqRequest request,DelayLevel levle,Date cdate) {
        org.apache.rocketmq.common.message.Message msg = new org.apache.rocketmq.common.message.Message(rocketMqConfig.getDefaultConfig().remoteTopic(topic), request.getTag(), JsonUtil.toJson(request).getBytes());
        msg.setKeys(request.getMsgId());
        if(levle!=DelayLevel.LEVEL_NOT) {
            msg.setDelayTimeLevel(levle.getLevel());
        }
        if(cdate!=null && cdate.getTime()>System.currentTimeMillis()) {
            throw new ServiceException("开源版MQ不支持设定固定的发送时间!!");
        }
        return msg;
    }
    
    private <T> T runReNum(Object producer,RunMqSend<T> run) {
        for(int i=0;i<RE_NUM;i++) {
            try {
                if(producer instanceof com.aliyun.openservices.ons.api.Admin) {
                    return run.sendAliyun();
                }
                if(producer instanceof org.apache.rocketmq.client.producer.MQProducer) {
                    try {
                        return run.sendApache();
                    }catch(Exception ex) {
                        throw new ServiceException("发送MQ消息结果异常",ex);
                    }
                }
                throw new ServiceException("发送MQ消息编码异常");
            } catch (Exception e) {
                log.warn("发送MQ消息异常:",e);
            }
        }
        throw new ServiceException("发送MQ异常,连续"+RE_NUM+"次发送失败!!!");
    }
    
    private interface RunMqSend<T> {
        T sendAliyun();
        
        T sendApache() throws Exception ;
    }
    
    private void checkAliyun(com.aliyun.openservices.ons.api.SendResult aliyunSendResult) {
        if(aliyunSendResult==null) throw new ServiceException("发送MQ消息结果异常:"+aliyunSendResult);
    }
    
    private void checkApache(org.apache.rocketmq.client.producer.SendResult apacheendResult) {
        if(apacheendResult==null) throw new ServiceException("发送MQ消息结果异常:"+apacheendResult);
        if(apacheendResult.getSendStatus()!=SendStatus.SEND_OK)  throw new ServiceException("发送MQ消息结果异常:"+apacheendResult.getSendStatus());
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public String send(String topic, MqRequest request) {
        return send(RocketMqConfig.default_rocket_key, topic, request);
    }



    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#sendOneway(java.lang.String, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public void sendOneway(String topic, MqRequest request) {
        sendOneway(RocketMqConfig.default_rocket_key, topic, request);
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, com.swift.stream.rocketmq.pojo.MqRequest, org.apache.rocketmq.client.producer.SendCallback)
     */
    @Override
    public void send(String topic, MqRequest request, SendCallback callback) {
        send(RocketMqConfig.default_rocket_key,topic, request, callback);
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, java.lang.String, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public String send(String rocketKey, String topic, MqRequest request) {
        return send(rocketKey, topic, DelayLevel.LEVEL_NOT, request);
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#sendOneway(java.lang.String, java.lang.String, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public void sendOneway(String rocketKey, String topic, MqRequest request) {
        sendOneway(rocketKey, topic, DelayLevel.LEVEL_NOT, request);
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, java.lang.String, com.swift.stream.rocketmq.pojo.MqRequest, com.swift.stream.rocketmq.producer.RocketMqProducer.SendCallback)
     */
    @Override
    public void send(String rocketKey, String topic, MqRequest request, SendCallback callback) {
        send(rocketKey, topic, DelayLevel.LEVEL_NOT, request, callback);
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, int, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public String sendOreder(String topic, String shardingKey, MqRequest request) {
        return sendOreder(RocketMqConfig.default_rocket_key,topic, shardingKey, request);
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, java.lang.String, int, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public String sendOreder(String rocketKey, String topic, String shardingKey, MqRequest request) {
        Object producer = mqProducerFactory.getProducer(rocketKey,topic);
        return runReNum(producer,new RunMqSend<String>() {
            @Override
            public String sendAliyun() {
                com.aliyun.openservices.ons.api.SendResult aliyunSendResult = ((com.aliyun.openservices.ons.api.order.OrderProducer)producer).send(createAliyunMsg(topic, request,DelayLevel.LEVEL_NOT,null), shardingKey);
                checkAliyun(aliyunSendResult);
                return aliyunSendResult.getMessageId();
            }

            @Override
            public String sendApache() throws Exception {
                org.apache.rocketmq.client.producer.SendResult apacheendResult = ((org.apache.rocketmq.client.producer.MQProducer)producer).send(createApacheMsg(topic, request,DelayLevel.LEVEL_NOT,null),
                    new SelectMessageQueueByHash(),shardingKey);
                checkApache(apacheendResult);
                return apacheendResult.getMsgId();
            }
            
        });
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, com.swift.stream.rocketmq.producer.DelayLevel, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public String send(String topic, DelayLevel levle, MqRequest request) {
        return send(RocketMqConfig.default_rocket_key, topic, levle, request) ;
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, java.lang.String, com.swift.stream.rocketmq.producer.DelayLevel, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public String send(String rocketKey, String topic, DelayLevel levle, MqRequest request) {
        Object producer = mqProducerFactory.getProducer(rocketKey,topic);
        return runReNum(producer,new RunMqSend<String>() {
            @Override
            public String sendAliyun() {
                com.aliyun.openservices.ons.api.SendResult aliyunSendResult = ((com.aliyun.openservices.ons.api.Producer)producer).send(createAliyunMsg(topic, request,levle,null));
                checkAliyun(aliyunSendResult);
                return aliyunSendResult.getMessageId();
            }

            @Override
            public String sendApache() throws Exception {
                org.apache.rocketmq.client.producer.SendResult apacheendResult = ((org.apache.rocketmq.client.producer.MQProducer)producer).send(createApacheMsg(topic, request,levle,null));
                checkApache(apacheendResult);
                return apacheendResult.getMsgId();
            }
            
        });
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#sendOneway(java.lang.String, com.swift.stream.rocketmq.producer.DelayLevel, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public void sendOneway(String topic, DelayLevel levle, MqRequest request) {
        sendOneway(RocketMqConfig.default_rocket_key, topic, levle, request);
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#sendOneway(java.lang.String, java.lang.String, com.swift.stream.rocketmq.producer.DelayLevel, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public void sendOneway(String rocketKey, String topic, DelayLevel levle, MqRequest request) {
        Object producer = mqProducerFactory.getProducer(rocketKey,topic);
        runReNum(producer,new RunMqSend<Void>() {
            @Override
            public Void sendAliyun() {
                ((com.aliyun.openservices.ons.api.Producer)producer).sendOneway(createAliyunMsg(topic, request,levle,null));
                return null;
            }

            @Override
            public Void sendApache() throws Exception {
                ((org.apache.rocketmq.client.producer.MQProducer)producer).sendOneway(createApacheMsg(topic, request,levle,null));
                return null;
            }
            
        });
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, com.swift.stream.rocketmq.producer.DelayLevel, com.swift.stream.rocketmq.pojo.MqRequest, com.swift.stream.rocketmq.producer.RocketMqProducer.SendCallback)
     */
    @Override
    public void send(String topic, DelayLevel levle, MqRequest request, SendCallback callback) {
        send(RocketMqConfig.default_rocket_key, topic, levle, request, callback);
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, java.lang.String, com.swift.stream.rocketmq.producer.DelayLevel, com.swift.stream.rocketmq.pojo.MqRequest, com.swift.stream.rocketmq.producer.RocketMqProducer.SendCallback)
     */
    @Override
    public void send(String rocketKey, String topic, DelayLevel levle, MqRequest request, SendCallback callback) {
        Object producer = mqProducerFactory.getProducer(rocketKey,topic);
        runReNum(producer,new RunMqSend<Void>() {
            @Override
            public Void sendAliyun() {
                ((com.aliyun.openservices.ons.api.Producer)producer).sendAsync(createAliyunMsg(topic, request,levle,null), new com.aliyun.openservices.ons.api.SendCallback(){

                    @Override
                    public void onSuccess(com.aliyun.openservices.ons.api.SendResult sendResult) {
                        callback.onSuccess(sendResult.getMessageId());
                    }

                    @Override
                    public void onException(OnExceptionContext context) {
                        callback.onException(context.getException());
                    }
                    
                });
                return null;
            }

            @Override
            public Void sendApache() throws Exception {
                ((org.apache.rocketmq.client.producer.MQProducer)producer).send(createApacheMsg(topic, request,levle,null), new org.apache.rocketmq.client.producer.SendCallback() {

                    @Override
                    public void onSuccess(org.apache.rocketmq.client.producer.SendResult sendResult) {
                        callback.onSuccess(sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        callback.onException(e);
                    }
                    
                });
                return null;
            }
            
        });
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, java.util.Date, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public String send(String topic, Date cDate, MqRequest request) {
        return send(RocketMqConfig.default_rocket_key, topic, cDate, request);
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, java.lang.String, java.util.Date, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public String send(String rocketKey, String topic, Date cDate, MqRequest request) {
        Object producer = mqProducerFactory.getProducer(rocketKey,topic);
        return runReNum(producer,new RunMqSend<String>() {
            @Override
            public String sendAliyun() {
                com.aliyun.openservices.ons.api.SendResult aliyunSendResult = ((com.aliyun.openservices.ons.api.Producer)producer).send(createAliyunMsg(topic, request,DelayLevel.LEVEL_NOT,cDate));
                checkAliyun(aliyunSendResult);
                return aliyunSendResult.getMessageId();
            }

            @Override
            public String sendApache() throws Exception {
                org.apache.rocketmq.client.producer.SendResult apacheendResult = ((org.apache.rocketmq.client.producer.MQProducer)producer).send(createApacheMsg(topic, request,DelayLevel.LEVEL_NOT,cDate));
                checkApache(apacheendResult);
                return apacheendResult.getMsgId();
            }
            
        });
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#sendOneway(java.lang.String, java.util.Date, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public void sendOneway(String topic, Date cDate, MqRequest request) {
        sendOneway(RocketMqConfig.default_rocket_key, topic, cDate, request);
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#sendOneway(java.lang.String, java.lang.String, java.util.Date, com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public void sendOneway(String rocketKey, String topic, Date cDate, MqRequest request) {
        Object producer = mqProducerFactory.getProducer(rocketKey,topic);
        runReNum(producer,new RunMqSend<Void>() {
            @Override
            public Void sendAliyun() {
                ((com.aliyun.openservices.ons.api.Producer)producer).sendOneway(createAliyunMsg(topic, request,DelayLevel.LEVEL_NOT,cDate));
                return null;
            }

            @Override
            public Void sendApache() throws Exception {
                ((org.apache.rocketmq.client.producer.MQProducer)producer).sendOneway(createApacheMsg(topic, request,DelayLevel.LEVEL_NOT,cDate));
                return null;
            }
            
        });
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, java.util.Date, com.swift.stream.rocketmq.pojo.MqRequest, com.swift.stream.rocketmq.producer.RocketMqProducer.SendCallback)
     */
    @Override
    public void send(String topic, Date cDate, MqRequest request, SendCallback callback) {
        send(RocketMqConfig.default_rocket_key, topic, cDate, request, callback);
    }

    /** 
     * @see com.swift.stream.rocketmq.producer.RocketMqProducer#send(java.lang.String, java.lang.String, java.util.Date, com.swift.stream.rocketmq.pojo.MqRequest, com.swift.stream.rocketmq.producer.RocketMqProducer.SendCallback)
     */
    @Override
    public void send(String rocketKey, String topic, Date cDate, MqRequest request, SendCallback callback) {
        Object producer = mqProducerFactory.getProducer(rocketKey,topic);
        runReNum(producer,new RunMqSend<Void>() {
            @Override
            public Void sendAliyun() {
                ((com.aliyun.openservices.ons.api.Producer)producer).sendAsync(createAliyunMsg(topic, request,DelayLevel.LEVEL_NOT,cDate), new com.aliyun.openservices.ons.api.SendCallback(){

                    @Override
                    public void onSuccess(com.aliyun.openservices.ons.api.SendResult sendResult) {
                        callback.onSuccess(sendResult.getMessageId());
                    }

                    @Override
                    public void onException(OnExceptionContext context) {
                        callback.onException(context.getException());
                    }
                    
                });
                return null;
            }

            @Override
            public Void sendApache() throws Exception {
                ((org.apache.rocketmq.client.producer.MQProducer)producer).send(createApacheMsg(topic, request,DelayLevel.LEVEL_NOT,cDate), new org.apache.rocketmq.client.producer.SendCallback() {

                    @Override
                    public void onSuccess(org.apache.rocketmq.client.producer.SendResult sendResult) {
                        callback.onSuccess(sendResult.getMsgId());
                    }

                    @Override
                    public void onException(Throwable e) {
                        callback.onException(e);
                    }
                    
                });
                return null;
            }
            
        });
    }

}
