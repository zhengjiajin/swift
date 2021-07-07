/*
 * @(#)MqConfig.java   1.0  2021年6月30日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq;

import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.remoting.RPCHook;

import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年6月30日
 */
public class MqConfig {
    //啊里云默认的生产者GROUP
    public static final String producerGroup="__ONS_PRODUCER_DEFAULT_GROUP";
    
    private String accessKey;
    
    private String secretKey;
    //NAMESEV地址
    private String ONSAddr;
    //同一地址的MQ，测试生产环境区分
    private String topicTop;
    
    private String groupId=MqConfig.producerGroup;

    public RPCHook createRpcHook() {
        return new AclClientRPCHook(new SessionCredentials(accessKey, secretKey));
    }
    
    /**
     * 判断是否阿里云的namesev
     * @param url
     * @return
     */
    public boolean isAliyun() {
        if(ONSAddr.indexOf(".aliyuncs.com")!=-1) return true;
        return false;
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
     * @return the accessKey
     */
    public String getAccessKey() {
        return accessKey;
    }

    /**
     * @param accessKey the accessKey to set
     */
    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    /**
     * @return the secretKey
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     * @param secretKey the secretKey to set
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * @return the oNSAddr
     */
    public String getONSAddr() {
        return ONSAddr;
    }

    /**
     * @param oNSAddr the oNSAddr to set
     */
    public void setONSAddr(String oNSAddr) {
        ONSAddr = oNSAddr;
    }

    /**
     * @return the topicTop
     */
    public String getTopicTop() {
        return topicTop;
    }

    /**
     * @param topicTop the topicTop to set
     */
    public void setTopicTop(String topicTop) {
        this.topicTop = topicTop;
    }

    /**
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * @param groupId the groupId to set
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
    
    
}
