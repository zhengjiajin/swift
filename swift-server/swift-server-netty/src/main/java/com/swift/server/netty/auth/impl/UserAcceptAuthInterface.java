/*
 * @(#)SessionCoreAuth.java   1.0  2020年12月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.auth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.protocol.auth.AuthClient;
import com.swift.api.protocol.auth.ClientInfo;
import com.swift.api.protocol.auth.client.UserClient;
import com.swift.api.protocol.message.CLR;
import com.swift.api.protocol.message.handler.MessageHandlerContext;
import com.swift.server.netty.auth.AuthInterface;
import com.swift.server.netty.message.code.AuthorizeCode;
import com.swift.server.netty.message.code.AuthorizeCodeFactory;
import com.swift.util.type.TypeUtil;

/**
 * 共享授权码登录
 * @author zhengjiajin
 * @version 1.0 2020年12月29日
 */
@Component
public class UserAcceptAuthInterface implements AuthInterface {
    
    @Autowired
    private AuthorizeCodeFactory authorizeCodeFactory;
    /** 
     * @see com.swift.server.netty.auth.AuthInterface#authChannel(com.swift.api.protocol.message.CLR, com.swift.api.protocol.message.handler.MessageHandlerContext)
     */
    @Override
    public AuthClient authChannel(CLR clr, MessageHandlerContext context) {
        if(clr.getClientInfo()==null) return null;
        if(clr.getClientInfo().equals(ClientInfo.SERVER)) return null;
        if(TypeUtil.isNull(clr.getSecSocketAccept())) return null;
        AuthorizeCode code = new AuthorizeCode();
        code.setClr(clr);
        code.setMessageHandlerContext(context);
        code.setTimestamp(System.currentTimeMillis());
        authorizeCodeFactory.set(clr.getSecSocketAccept(), code);
        
        UserClient userClient = new UserClient();
        userClient.setAuthenticated(false);
        userClient.setClientInfo(clr.getClientInfo());
        return userClient;
    };
}
