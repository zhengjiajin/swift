/*
 * @(#)ServiceConsumer.java   1.0  2020年6月8日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.consumer.abs;

import org.apache.rocketmq.common.message.MessageExt;

import com.swift.core.model.parser.DataJsonParser;
import com.swift.stream.rocketmq.consumer.RocketMqMessageListener;
import com.swift.stream.rocketmq.pojo.MqRequest;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年6月8日
 */
public abstract class ServiceConsumer implements RocketMqMessageListener {

    public boolean requestAll() {
        return false;
    }
    
    
    public MqRequest changeRequest(MessageExt message) {
        MqRequest req = new MqRequest();
        req.setMsgId(message.getKeys());
        req.setTag(message.getTags());
        req.setData(DataJsonParser.jsonToObject(new String(message.getBody())));
        return req;
    }
    
}
