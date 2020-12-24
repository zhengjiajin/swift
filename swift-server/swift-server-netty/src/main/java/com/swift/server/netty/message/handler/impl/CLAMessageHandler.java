/**
 * CLAMessageHandler.java 1.0.0 2018-10-17 21:56:21
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler.impl;

import org.springframework.stereotype.Component;

import com.swift.server.netty.message.CLA;
import com.swift.server.netty.message.MessageType;
import com.swift.server.netty.message.handler.AbstractMessageHandler;
import com.swift.server.netty.message.handler.MessageHandlerContext;

/**
 * CLA消息处理器。
 * 
 * @version 1.0.0
 * @date 2018-10-17 21:56:21
 */
@Component
public class CLAMessageHandler extends AbstractMessageHandler<CLA> {

    /**
     * Construct a new <code>CLAMessageHandler</code> instance.
     */
    public CLAMessageHandler() {
        super(MessageType.CLA);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.swift.hhmk.gateway.core.MessageHandler#handle(com.swift.hhmk.gateway.
     * protocol.keepalive.message.Message,
     * com.swift.hhmk.gateway.core.MessageHandlerContext)
     */
    @Override
    public void handle(CLA message, MessageHandlerContext context) {
        // Does nothing
    }
}
