/**
 * DPRMessageHandler.java 1.0.0  2020年12月25日
 *
 * Copyright (c) 2015-2020 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.api.protocol.message.handler.impl;

import org.springframework.stereotype.Component;

import com.swift.api.protocol.message.DPA;
import com.swift.api.protocol.message.DPR;
import com.swift.api.protocol.message.MessageType;
import com.swift.api.protocol.message.handler.AbstractMessageHandler;
import com.swift.api.protocol.message.handler.MessageHandlerContext;

/**
 * DPR消息处理器。
 * 
 * @version 1.0.0
 * @date  2020年12月25日
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
