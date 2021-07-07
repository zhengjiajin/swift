/*
 * @(#)BinlogConsumer.java   1.0  2020年6月8日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.consumer.abs;

import com.swift.core.model.data.DataModel;
import com.swift.core.model.parser.DataJsonParser;
import com.swift.stream.rocketmq.consumer.RocketMqMessageListener;
import com.swift.stream.rocketmq.pojo.MqMessage;
import com.swift.stream.rocketmq.pojo.MqRequest;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年6月8日
 */
public abstract class BinlogConsumer implements RocketMqMessageListener {

    public boolean requestAll() {
        return true;
    }
    
    public MqRequest changeRequest(MqMessage message) {
        MqRequest req = new MqRequest();
        DataModel data = DataJsonParser.jsonToObject(new String(message.getBody()));
        req.setTag(data.getString("database")+"."+data.getString("table"));
        req.setData(data);
        req.setMsgId(message.getMsgId());
        return req;
    }
    
}
