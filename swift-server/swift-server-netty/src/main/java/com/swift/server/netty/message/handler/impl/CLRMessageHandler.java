/**
 * CLRMessageHandler.java 1.0.0 2018-10-17 21:54:18
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd. All rights reserved.
 */
package com.swift.server.netty.message.handler.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.protocol.auth.AuthClient;
import com.swift.api.protocol.auth.client.SysClient;
import com.swift.api.protocol.auth.client.UserClient;
import com.swift.api.protocol.core.ChannelManagerFactory;
import com.swift.api.protocol.listener.ChannelEventListener.Event;
import com.swift.api.protocol.message.CLA;
import com.swift.api.protocol.message.CLR;
import com.swift.api.protocol.message.MessageType;
import com.swift.api.protocol.message.handler.AbstractMessageHandler;
import com.swift.api.protocol.message.handler.MessageHandlerContext;
import com.swift.api.protocol.util.NettyChannel;
import com.swift.exception.ResultCode;
import com.swift.exception.extend.AuthException;
import com.swift.server.netty.auth.AuthInterface;

/**
 * CLR消息处理器。
 * 
 * @version 1.0.0
 * @date 2018-10-17 21:54:18
 */
@Component
public class CLRMessageHandler extends AbstractMessageHandler<CLR> {
    

    @Autowired(required=false)
    private List<AuthInterface> authInterfaces;
    @Autowired
    private ChannelManagerFactory channelManagerFactory;
    
    /**
     * Construct a new <code>CLRMessageHandler</code> instance.
     */
    public CLRMessageHandler() {
        super(MessageType.CLR);
    }

   
    @Override
    public void handle(CLR message, MessageHandlerContext context) {
        if(authInterfaces==null) throw new AuthException("系统未设置认证实现");
        for(AuthInterface authInterface : authInterfaces) {
            AuthClient authClient = authInterface.authChannel(message, context);
            if(authClient!=null) {
                context.setAuthClient(authClient);
                break;
            }
        }
        if(context.getAuthClient()==null) throw new AuthException("连接认证未通过");
        //等待共享授权
        if(context.getAuthClient().isAuthenticated()==false) return;
        
        //清除断连定时器
        NettyChannel.cancelScheduledFuture(context);
        if(context.getAuthClient() instanceof UserClient) {
            UserClient userClient = (UserClient)context.getAuthClient();
            channelManagerFactory.registerChannel(userClient.getAbstractSession().getUserId(),userClient.getClientInfo(), context);
        }
        
        if(context.getAuthClient() instanceof SysClient) {
            SysClient sysClient = (SysClient)context.getAuthClient();
            channelManagerFactory.registerChannel(sysClient.getSysId(), context);
        }
        super.fireChannelEvent(Event.AUTH, context);
        CLA cla = new CLA(message);
        cla.setResponseTime(System.currentTimeMillis());
        cla.setResultCode(ResultCode.SUCCESS);
        cla.setReason("连接认证通过");
        context.write(cla);
    }

}
