/*
 * @(#)SysAuthInterface.java   1.0  2020年12月22日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.auth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.core.auth.Auth2Crypt;
import com.swift.server.netty.auth.AuthClient;
import com.swift.server.netty.auth.AuthInterface;
import com.swift.server.netty.auth.ClientInfo;
import com.swift.server.netty.auth.client.SysClient;
import com.swift.server.netty.message.CLR;
import com.swift.server.netty.message.handler.MessageHandlerContext;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年12月22日
 */
@Component
public class SysAuthInterface implements AuthInterface {

    @Autowired(required=false)
    private Auth2Crypt auth2Crypt;
    /** 
     * @see com.swift.server.netty.auth.AuthInterface#authChannel(com.swift.server.netty.message.CLR, com.swift.server.netty.message.handler.MessageHandlerContext)
     */
    @Override
    public AuthClient authChannel(CLR clr, MessageHandlerContext context) {
        if(auth2Crypt==null) return null;
        if(clr.getClientInfo()==null) return null;
        if(!clr.getClientInfo().equals(ClientInfo.SERVER)) return null;
        String sysId = auth2Crypt.decrypt(clr.getSecSocketAccept());
        if(TypeUtil.isNull(sysId)) return null;
        SysClient client = new SysClient();
        client.setClientInfo(ClientInfo.SERVER);
        client.setAuthenticated(true);
        client.setSysId(sysId);
        return client;
    }

}
