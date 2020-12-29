/*
 * @(#)SysAuthInterface.java   1.0  2020年12月22日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.auth.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.protocol.auth.AuthClient;
import com.swift.api.protocol.auth.ClientInfo;
import com.swift.api.protocol.auth.client.SysClient;
import com.swift.api.protocol.message.CLR;
import com.swift.api.protocol.message.handler.MessageHandlerContext;
import com.swift.core.auth.SysAuth;
import com.swift.core.auth.SysAuth2;
import com.swift.server.netty.auth.AuthInterface;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年12月22日
 */
@Component
public class SysAuthInterface implements AuthInterface {

    @Autowired(required=false)
    private SysAuth2 sysAuth2;
    
    @Autowired(required=false)
    private SysAuth sysAuth;
    /** 
     * @see com.swift.server.netty.auth.AuthInterface#authChannel(com.swift.server.netty.message.CLR, com.swift.server.netty.message.handler.MessageHandlerContext)
     */
    @Override
    public AuthClient authChannel(CLR clr, MessageHandlerContext context) {
        if(sysAuth==null && sysAuth2==null) return null;
        if(clr.getClientInfo()==null) return null;
        if(!clr.getClientInfo().equals(ClientInfo.SERVER)) return null;
        String sysId = null;
        if(sysAuth!=null && sysId==null) {
            sysId = sysAuth.decrypt(clr.getClientObject(), clr.getSecSocketAccept());
        }
        if(sysAuth2!=null && sysId==null) {
            sysId = sysAuth2.decrypt(clr.getSecSocketAccept());
        }
        if(TypeUtil.isNull(sysId)) return null;
        SysClient client = new SysClient();
        client.setClientInfo(ClientInfo.SERVER);
        client.setAuthenticated(true);
        client.setSysId(sysId);
        return client;
    }

}
