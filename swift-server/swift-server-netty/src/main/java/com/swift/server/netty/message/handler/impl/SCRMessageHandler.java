/**
 * SCRMessageHandler.java 1.0.0 2018-10-17 22:01:54
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.exception.extend.AuthException;
import com.swift.server.netty.auth.AuthClient;
import com.swift.server.netty.core.NettyKeepaliveMessageDeliver;
import com.swift.server.netty.message.MessageType;
import com.swift.server.netty.message.SCR;
import com.swift.server.netty.message.handler.AbstractMessageHandler;
import com.swift.server.netty.message.handler.MessageHandlerContext;

/**
 * SCR消息处理器。
 * 
 * @version 1.0.0
 * @date 2018-10-17 22:01:54
 */
@Component
public class SCRMessageHandler extends AbstractMessageHandler<SCR> {

    @Autowired
    private NettyKeepaliveMessageDeliver nettyKeepaliveMessageDeliver;
    
    /**
     * Construct a new <code>SCRMessageHandler</code> instance.
     */
    public SCRMessageHandler() {
        super(MessageType.SCR);
    }

    
    @Override
    public void handle(SCR message, MessageHandlerContext context) {
        //未认证禁止发送请求
        if(!isAuth(context.getAuthClient())) {
            throw new AuthException("未认证的连接,禁止发送请求");
        }
        nettyKeepaliveMessageDeliver.requestReceived(message, context);
    }

    private boolean isAuth(AuthClient authClient) {
        if(authClient==null) return false;
        return authClient.isAuthenticated();
    }
}
