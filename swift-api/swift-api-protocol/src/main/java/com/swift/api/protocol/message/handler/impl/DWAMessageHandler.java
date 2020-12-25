/**
 * DWAMessageHandler.java 1.0.0 2020年12月21日
 *
 * Copyright (c) 2015-2020 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.api.protocol.message.handler.impl;

import org.springframework.stereotype.Component;

import com.swift.api.protocol.message.DWA;
import com.swift.api.protocol.message.MessageType;
import com.swift.api.protocol.message.handler.AbstractMessageHandler;
import com.swift.api.protocol.message.handler.MessageHandlerContext;

/**
 * DWA消息处理器。
 * 
 * @version 1.0.0
 * @date 2020年12月21日
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
