/*
 * @(#)ChannelManagerListener.java   1.0  2020年12月22日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.protocol.listener.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.protocol.auth.client.SysClient;
import com.swift.api.protocol.auth.client.UserClient;
import com.swift.api.protocol.core.ChannelManagerFactory;
import com.swift.api.protocol.listener.ChannelEventListener;
import com.swift.api.protocol.message.handler.MessageHandlerContext;
import com.swift.api.protocol.util.NettyChannel;

/**
 * 断连时清空ChannelManager的连接
 * @author zhengjiajin
 * @version 1.0 2020年12月22日
 */
@Component
public class ChannelCloseListener implements ChannelEventListener {

    @Autowired
    private ChannelManagerFactory channelManagerFactory;
    /** 
     * @see com.swift.server.netty.listener.ChannelEventListener#channelEvent(com.swift.server.netty.listener.ChannelEventListener.Event, com.swift.server.netty.message.handler.MessageHandlerContext)
     */
    @Override
    public void channelEvent(Event event, MessageHandlerContext context) {
        if(!event.equals(Event.CLOSE)) return;
        if(context.getAuthClient()==null) return;
        if(context.getAuthClient() instanceof UserClient) {
            UserClient sysClient = (UserClient)context.getAuthClient();
            channelManagerFactory.deregisterChannel(sysClient.getAbstractSession().getUserId(),sysClient.getClientInfo());
        }
        
        if(context.getAuthClient() instanceof SysClient) {
            SysClient sysClient = (SysClient)context.getAuthClient();
            String ip = NettyChannel.remoteAddress(context.getChannelHandlerContext().channel());
            channelManagerFactory.deregisterChannel(sysClient.getSysId(),ip);
        }
    }

}
