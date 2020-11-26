/*
 * @(#)TestRocket.java   1.0  2020年11月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test;

import org.apache.rocketmq.client.producer.SendResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.stream.rocketmq.pojo.MqRequest;
import com.swift.stream.rocketmq.producer.RocketMqProducer;
import com.swift.util.exec.ThreadUtil;
import com.swift.util.math.RandomUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年11月26日
 */
public class TestRocket extends BaseJunit4Test {
    
    @Autowired
    private RocketMqProducer rocketMqProducer;
    
    @Test
    public void testProducer() {
        MqRequest request = new MqRequest();
        request.setMsgId(RandomUtil.createReqId());
        request.setTag("testServiceJiajin");
        request.getData().addObject("name", "jiajin");
        request.getData().addObject("id", 1);
        SendResult result = rocketMqProducer.send("service", request);
        System.out.println(result.getSendStatus());
        ThreadUtil.sleep(100000);
    }
    
}
