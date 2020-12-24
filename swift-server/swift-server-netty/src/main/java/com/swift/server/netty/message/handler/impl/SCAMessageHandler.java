/**
 * SCAMessageHandler.java 1.0.0 2018-10-17 22:02:38
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.server.netty.core.NettyKeepaliveMessageDeliver;
import com.swift.server.netty.message.MessageType;
import com.swift.server.netty.message.SCA;
import com.swift.server.netty.message.handler.AbstractMessageHandler;
import com.swift.server.netty.message.handler.MessageHandlerContext;

/**
 * SCA消息处理器。
 * 
 * @version 1.0.0
 * @date 2018-10-17 22:02:38
 */
@Component
public class SCAMessageHandler extends AbstractMessageHandler<SCA> {

    @Autowired
    private NettyKeepaliveMessageDeliver nettyKeepaliveMessageDeliver;

    /**
     * Construct a new <code>SCAMessageHandler</code> instance.
     */
    public SCAMessageHandler() {
        super(MessageType.SCA);
    }

    @Override
    public void handle(SCA message, MessageHandlerContext context) {
        nettyKeepaliveMessageDeliver.responseReceived(message, context);
    }
}
