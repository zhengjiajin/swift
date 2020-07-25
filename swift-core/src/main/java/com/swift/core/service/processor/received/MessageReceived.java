/*
 * @(#)MessageReceived.java   1.0  2019年4月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.service.processor.received;

import com.swift.core.model.ServiceRequest;

/**
 * 接收请求接口 
 * @author zhengjiajin
 * @version 1.0 2019年4月12日
 */
public interface MessageReceived {

    /**
     * 收到请求
     * 
     * @param request 请求
     */
    void requestReceived(ServiceRequest request);
}
