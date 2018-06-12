/*
 * @(#)TestKafka.java   1.0  2018年6月7日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.kafka;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.dao.mq.kafka.KafkaRequest;
import com.swift.dao.mq.kafka.KafkaService;
import com.swift.test.BaseJunit4Test;
import com.swift.util.exec.ThreadUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月7日
 */
public class TestKafka extends BaseJunit4Test{
    @Autowired
    private KafkaService kafkaService;
    
    @Test
    public void testKafka() {
        KafkaRequest req = new KafkaRequest();
        req.setMethod("testKafka");
        req.getData().putObject("sfasdfa", "bbb");
        kafkaService.sendRequest("service", req);
        ThreadUtil.sleep(5000000);
    }
    
}
