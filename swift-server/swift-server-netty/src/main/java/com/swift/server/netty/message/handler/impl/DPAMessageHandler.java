/**
 * DPAMessageHandler.java 1.0.0 2018-10-17 22:01:08
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler.impl;

import org.springframework.stereotype.Component;

import com.swift.server.netty.message.DPA;
import com.swift.server.netty.message.MessageType;
import com.swift.server.netty.message.handler.AbstractMessageHandler;
import com.swift.server.netty.message.handler.MessageHandlerContext;

/**
 * DPA消息处理器。
 * 
 * @version 1.0.0
 * @date 2018-10-17 22:01:08
 */
@Component
public class DPAMessageHandler extends AbstractMessageHandler<DPA> {

    /**
     * Construct a new <code>DPAMessageHandler</code> instance.
     */
    public DPAMessageHandler() {
        super(MessageType.DPA);
    }

    
    @Override
    public void handle(DPA message, MessageHandlerContext context)  {
    }
}
