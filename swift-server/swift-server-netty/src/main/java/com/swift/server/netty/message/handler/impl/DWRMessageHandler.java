/**
 * DWRMessageHandler.java 1.0.0 2018-10-17 21:57:57
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler.impl;

import org.springframework.stereotype.Component;

import com.swift.server.netty.message.DWA;
import com.swift.server.netty.message.DWR;
import com.swift.server.netty.message.MessageType;
import com.swift.server.netty.message.handler.AbstractMessageHandler;
import com.swift.server.netty.message.handler.MessageHandlerContext;

/**
 * DWR消息处理器。
 * 
 * @version 1.0.0
 * @date 2018-10-17 21:57:57
 */
@Component
public class DWRMessageHandler extends AbstractMessageHandler<DWR> {

    /**
     * Construct a new <code>DWRMessageHandler</code> instance.
     */
    public DWRMessageHandler() {
        super(MessageType.DWR);
    }

    
    @Override
    public void handle(DWR message, MessageHandlerContext context) {
        context.write(new DWA(message));
    }
}
