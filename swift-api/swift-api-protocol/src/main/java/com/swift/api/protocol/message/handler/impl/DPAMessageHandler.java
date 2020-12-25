/**
 * DPAMessageHandler.java 1.0.0  2020年12月25日
 *
 * Copyright (c) 2015-2020 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.api.protocol.message.handler.impl;

import org.springframework.stereotype.Component;

import com.swift.api.protocol.message.DPA;
import com.swift.api.protocol.message.MessageType;
import com.swift.api.protocol.message.handler.AbstractMessageHandler;
import com.swift.api.protocol.message.handler.MessageHandlerContext;

/**
 * DPA消息处理器。
 * 
 * @version 1.0.0
 * @date  2020年12月25日
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
