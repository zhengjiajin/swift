/**
 * @(#)DefaultMessageDeliver.java 0.0.1-SNAPSHOT Dec 22, 2015 2:25:54 PM 
 *  
 * Copyright (c) 2015-2015 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.server.jetty.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.swift.core.service.processor.AbstractMessageDeliver;
import com.swift.core.service.processor.send.MessageSender;

/**
 * WEB类消息处理器，只处理接收请求与发送响应
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date Dec 22, 2015 2:25:54 PM
 */
@Component
public class WebMessageDeliver extends AbstractMessageDeliver {

	@Autowired
	@Qualifier("defaultWebHandler")
	private MessageSender messageSender;

	@Override
	public MessageSender getMessageSender() {
		return messageSender;
	}

}
