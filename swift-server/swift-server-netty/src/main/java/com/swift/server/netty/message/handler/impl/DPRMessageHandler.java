/**
 * DPRMessageHandler.java 1.0.0 2018-10-17 21:59:43
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler.impl;

import org.springframework.stereotype.Component;

import com.swift.server.netty.message.DPA;
import com.swift.server.netty.message.DPR;
import com.swift.server.netty.message.MessageType;
import com.swift.server.netty.message.handler.AbstractMessageHandler;
import com.swift.server.netty.message.handler.MessageHandlerContext;

/**
 * DPR消息处理器。
 * 
 * @version 1.0.0
 * @date 2018-10-17 21:59:43
 */
@Component
public class DPRMessageHandler extends AbstractMessageHandler<DPR> {

    /**
     * Construct a new <code>DPRMessageHandler</code> instance.
     */
    public DPRMessageHandler() {
        super(MessageType.DPR);
    }

    
    @Override
    public void handle(DPR message, MessageHandlerContext context) {
        context.write(new DPA(message));
    }
}
