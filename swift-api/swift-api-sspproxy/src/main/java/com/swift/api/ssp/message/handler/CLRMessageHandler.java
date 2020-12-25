/**
 * CLRMessageHandler.java 1.0.0 2020年12月25日
 *
 * Copyright (c) 2015-2020 GuangZhou HHMK Co. Ltd. All rights reserved.
 */
package com.swift.api.ssp.message.handler;

import org.springframework.stereotype.Component;

import com.swift.api.protocol.message.CLR;
import com.swift.api.protocol.message.MessageType;
import com.swift.api.protocol.message.handler.AbstractMessageHandler;
import com.swift.api.protocol.message.handler.MessageHandlerContext;

/**
 * CLR消息处理器。
 * 
 * @version 1.0.0
 * @date 2020年12月25日
 */
@Component
public class CLRMessageHandler extends AbstractMessageHandler<CLR> {
    
    /**
     * Construct a new <code>CLRMessageHandler</code> instance.
     */
    public CLRMessageHandler() {
        super(MessageType.CLR);
    }

   
    @Override
    public void handle(CLR message, MessageHandlerContext context) {
       //nothing
    }

}
