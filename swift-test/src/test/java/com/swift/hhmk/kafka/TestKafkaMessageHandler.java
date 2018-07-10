/*
 * @(#)TestKafkaMessageHandler.java   1.0  2018年6月7日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.hhmk.kafka;

import org.springframework.stereotype.Service;

import com.swift.dao.kafka.KafkaRequest;
import com.swift.dao.kafka.KafkaService.KafkaMessageHandler;
import com.swift.dao.kafka.KafkaService.Topic;
import com.swift.util.text.JsonUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月7日
 */
@Service
@Topic("service")
public class TestKafkaMessageHandler implements KafkaMessageHandler {

    /** 
     * @see com.swift.dao.mq.kafka.KafkaService.KafkaMessageHandler#handle(com.swift.dao.mq.kafka.KafkaRequest)
     */
    @Override
    public void handle(KafkaRequest request) {
       System.out.println("********:"+JsonUtil.toJson(request));
    }

}
