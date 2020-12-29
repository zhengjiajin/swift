/*
 * @(#)UserAuthInterface.java   1.0  2020年12月22日
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
import com.swift.core.session.AbstractSession;
import com.swift.core.session.SessionAuth;
import com.swift.server.netty.auth.AuthInterface;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年12月22日
 */
@Component
public class UserAuthInterface implements AuthInterface {

    @Autowired(required=false)
    private SessionAuth sessionAuth;
    /** 
     * @see com.swift.server.netty.auth.AuthInterface#authChannel(com.swift.server.netty.message.CLR, com.swift.server.netty.message.handler.MessageHandlerContext)
     */
    @Override
    public AuthClient authChannel(CLR clr, MessageHandlerContext context) {
        if(sessionAuth==null) return null;
        if(clr.getClientInfo()==null) return null;
        if(clr.getClientInfo().equals(ClientInfo.SERVER)) return null;
        if(TypeUtil.isNull(clr.getClientObject())) return null;
        AbstractSession session = sessionAuth.decrypt(clr.getClientObject());
        if(session==null)  return null;
        UserClient client = new UserClient();
        client.setAbstractSession(session);
        client.setAuthenticated(true);
        client.setClientInfo(clr.getClientInfo());
        return client;
    }

}
