/*
 * @(#)KeepaliveMessageSender.java   1.0  2019年4月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.service.processor.send;

import com.swift.core.model.ServiceRequest;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2019年4月12日
 */
public interface KeepaliveMessageSender extends MessageSender {

    /**
     * 发送请求
     * 
     * @param request
     */
    void sendRequest(ServiceRequest request);
}
