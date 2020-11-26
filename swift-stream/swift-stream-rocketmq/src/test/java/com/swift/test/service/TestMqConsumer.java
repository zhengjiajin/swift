/*
 * @(#)TestMqConsumer.java   1.0  2020年11月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.service;

import org.springframework.stereotype.Service;

import com.swift.stream.rocketmq.consumer.Topic;
import com.swift.stream.rocketmq.consumer.abs.ServiceConsumer;
import com.swift.stream.rocketmq.pojo.MqRequest;
import com.swift.util.type.JsonUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年11月26日
 */
@Service
@Topic(value="service",tag="testServiceJiajin*")
public class TestMqConsumer extends ServiceConsumer {

    /** 
     * @see com.swift.stream.rocketmq.consumer.RocketMqMessageListener#handle(com.swift.stream.rocketmq.pojo.MqRequest)
     */
    @Override
    public void handle(MqRequest request) {
        System.out.println(JsonUtil.toJson(request));

    }

}
