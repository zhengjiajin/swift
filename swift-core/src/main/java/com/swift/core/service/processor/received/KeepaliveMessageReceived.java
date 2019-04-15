/*
 * @(#)KeepaliveMessageReceived.java   1.0  2019年4月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.service.processor.received;

import com.swift.core.model.ServiceResponse;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2019年4月12日
 */
public interface KeepaliveMessageReceived extends MessageReceived{
    /**
     * 收到响应
     * 
     * @param response 响应
     */
    void responseReceived(ServiceResponse response);
}
