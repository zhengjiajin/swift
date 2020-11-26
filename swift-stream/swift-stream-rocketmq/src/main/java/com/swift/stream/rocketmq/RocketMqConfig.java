/*
 * @(#)RocketMqConfig.java   1.0  2020年11月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.remoting.RPCHook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.swift.util.type.TypeUtil;

/**
 * 启动配置,暂不支持事务消息
 * 
 * @author zhengjiajin
 * @version 1.0 2020年11月20日
 */
@Component
public class RocketMqConfig {
    @Value("${rocket.accessKey:}")
    private String accessKey;
    
    @Value("${rocket.secretKey:}")
    private String secretKey;
    
    @Value("${rocket.ONSAddr:}")
    private String ONSAddr;
    
    @Value("${rocket.topicTop:}")
    private String topicTop;
    
    @Value("${rocket.groupId:}")
    private String groupId;
    
    public RPCHook createRpcHook() {
        return new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
    }
    
    public boolean isStart() {
        if(TypeUtil.isNull(accessKey)) return false;
        if(TypeUtil.isNull(secretKey==null)) return false;
        if(TypeUtil.isNull(ONSAddr==null)) return false;
        return true;
    }

    public String remoteTopic(String topic) {
        if(TypeUtil.isNotNull(topicTop)) {
            if(topic.indexOf(topicTop)==-1) topic=topicTop+topic;
        }
        return topic;
    }
    
    public String localTopic(String topic) {
        if(TypeUtil.isNotNull(topicTop)) {
            topic=topic.replace(topicTop, "");
        }
        return topic;
    }
    
    /**
     * @return the oNSAddr
     */
    public String getONSAddr() {
        return ONSAddr;
    }

    /**
     * @return the topicTop
     */
    public String getTopicTop() {
        return topicTop;
    }

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }
    
}
