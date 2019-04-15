/**
 * @(#)MessageSender.java 0.0.1-SNAPSHOT Dec 24, 2015 5:25:05 PM 
 *  
 * Copyright (c) 2015-2015 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.core.service.processor.send;

import com.swift.core.model.ServiceResponse;

/**
 * 响应消息发送
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date Dec 24, 2015 5:25:05 PM
 */
public interface MessageSender {
    
	/**
	 * 发送响应
	 * 
	 * @param response
	 */
	void sendResponse(ServiceResponse response);
}
