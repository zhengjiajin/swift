/*
 * @(#)KeepaliveMessageSender.java   1.0  2019年4月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.service.processor.send;

import com.swift.core.api.rpc.ClientEngine;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;

/**
 * 长连接服务发送请求接口 
 * @author zhengjiajin
 * @version 1.0 2019年4月12日
 */
public interface KeepaliveMessageSender extends MessageSender,ClientEngine<ServiceRequest,ServiceResponse> {

}
