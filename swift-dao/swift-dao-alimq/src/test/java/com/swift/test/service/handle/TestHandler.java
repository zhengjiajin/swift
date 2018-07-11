/*
 * @(#)TestHandler.java   1.0  2018年7月10日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.service.handle;

import org.springframework.stereotype.Service;

import com.swift.dao.alimq.AliMqRequest;
import com.swift.dao.alimq.AliMqService.AliMqMessageHandler;
import com.swift.dao.alimq.AliMqService.Topic;
import com.swift.util.text.JsonUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年7月10日
 */
@Service
@Topic("test-service")
public class TestHandler extends AliMqMessageHandler {

    /** 
     * @see com.swift.dao.alimq.AliMqService.AliMqMessageHandler#handle(com.swift.dao.alimq.AliMqRequest)
     */
    @Override
    public void handle(AliMqRequest request) {
        System.out.println("handle*****"+JsonUtil.toJson(request));
    }

}
