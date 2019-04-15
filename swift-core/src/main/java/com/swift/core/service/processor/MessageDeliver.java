/**
 * @(#)MessageDeliver.java 0.0.1-SNAPSHOT Dec 21, 2015 4:00:02 PM 
 *  
 * Copyright (c) 2015-2015 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.core.service.processor;

import com.swift.core.service.processor.received.MessageReceived;
import com.swift.core.service.processor.send.MessageSender;

/**
 * 消息处理器。负责把收到的消息（请求或响应）分发到业务层或相应的客户端。
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date Dec 21, 2015 4:00:02 PM
 */
public interface MessageDeliver extends MessageSender ,MessageReceived {

	
}
