/*
 * @(#)TestProducer.java   1.0  2018年7月10日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.dao.alimq.AliMqRequest;
import com.swift.dao.alimq.AliMqService;
import com.swift.test.BaseJunit4Test;
import com.swift.util.exec.ThreadUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年7月10日
 */
public class TestProducer extends BaseJunit4Test{
    @Autowired
    private AliMqService aliMqService;
    
    @Test
    public void testMsg() {
        AliMqRequest req = new AliMqRequest();
        req.setMethod("sayshowno");
        req.getData().addObject("vvkkaa", 12);
        req.getData().addObject("ljls", "xxaaax");
        req.getData().addObject("oijj", 7836);
        aliMqService.sendRequest("test-service", req);
        ThreadUtil.sleep(30000);
    }
    @Test
    public void testCon() {
        ThreadUtil.sleep(300000);
    }
}
