/*
 * @(#)KeepaliveMessageDeliver.java   1.0  2019年4月12日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.service.processor;

import com.swift.core.service.processor.received.KeepaliveMessageReceived;
import com.swift.core.service.processor.send.KeepaliveMessageSender;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2019年4月12日
 */
public interface KeepaliveMessageDeliver extends KeepaliveMessageReceived ,KeepaliveMessageSender{

    
}
