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
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;

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
        MqRequest req = JsonUtil.toObj(message.getBody(), MqRequest.class);
        if(TypeUtil.isNull(req.getMsgId())) req.setMsgId(message.getKeys());
        if(TypeUtil.isNull(req.getTag())) req.setTag(message.getTags());
        if(TypeUtil.isNull(req.getData())) req.setData(DataJsonParser.jsonToObject(new String(message.getBody())));
        
        return req;
    }
    
}
