/**
 * DWAMessageHandler.java 1.0.0 2018-10-17 21:58:39
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler.impl;

import org.springframework.stereotype.Component;

import com.swift.server.netty.message.DWA;
import com.swift.server.netty.message.MessageType;
import com.swift.server.netty.message.handler.AbstractMessageHandler;
import com.swift.server.netty.message.handler.MessageHandlerContext;

/**
 * DWA消息处理器。
 * 
 * @version 1.0.0
 * @date 2018-10-17 21:58:39
 */
@Component
public class DWAMessageHandler extends AbstractMessageHandler<DWA> {

    /**
     * Construct a new <code>DWAMessageHandler</code> instance.
     */
    public DWAMessageHandler() {
        super(MessageType.DWA);
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public void handle(DWA message, MessageHandlerContext context) {
        
    }
}
