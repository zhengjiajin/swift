/*
 * @(#)ChannelManagerFactory.java   1.0  2020年12月23日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.core;

import java.util.List;

import com.swift.server.netty.auth.ClientInfo;
import com.swift.server.netty.message.handler.MessageHandlerContext;

/**
 * 通道管理与处理 
 * @author zhengjiajin
 * @version 1.0 2020年12月23日
 */
public interface ChannelManagerFactory {
    
    public void registerChannel(String sysId, MessageHandlerContext context);
    
    public void registerChannel(Object userId,ClientInfo clientInfo, MessageHandlerContext context);
    
    public void deregisterChannel(String sysId);
    
    public void deregisterChannel(Object userId,ClientInfo clientInfo);
    
    public MessageHandlerContext getChannel(String sysId);
    
    public MessageHandlerContext getChannel(Object userId,ClientInfo clientInfo);
    
    public List<MessageHandlerContext> getChannel(Object userId);
}
