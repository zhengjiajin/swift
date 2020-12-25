/**
 * DWRMessageHandler.java 1.0.0 2020年12月21日
 *
 * Copyright (c) 2015-2020 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.api.protocol.message.handler.impl;

import org.springframework.stereotype.Component;

import com.swift.api.protocol.message.DWA;
import com.swift.api.protocol.message.DWR;
import com.swift.api.protocol.message.MessageType;
import com.swift.api.protocol.message.handler.AbstractMessageHandler;
import com.swift.api.protocol.message.handler.MessageHandlerContext;

/**
 * DWR消息处理器。
 * 
 * @version 1.0.0
 * @date 2020年12月21日
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
