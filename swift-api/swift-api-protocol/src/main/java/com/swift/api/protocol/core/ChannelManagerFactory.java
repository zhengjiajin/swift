/*
 * @(#)ChannelManagerFactory.java   1.0  2020年12月23日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.protocol.core;

import java.util.Collection;

import com.swift.api.protocol.auth.ClientInfo;
import com.swift.api.protocol.message.handler.MessageHandlerContext;

/**
 * 通道管理与处理 
 * @author zhengjiajin
 * @version 1.0 2020年12月23日
 */
public interface ChannelManagerFactory {
    
    public void registerChannel(String sysId, MessageHandlerContext context);
    
    public void registerChannel(Object userId,ClientInfo clientInfo, MessageHandlerContext context);
    
    public void deregisterChannel(String sysId,String ip);
    
    public void deregisterChannel(Object userId,ClientInfo clientInfo);
    //随机算法返回
    public MessageHandlerContext getChannel(String sysId);
    
    public MessageHandlerContext getChannel(Object userId,ClientInfo clientInfo);
    //返回所有
    public Collection<MessageHandlerContext> getChannel(Object userId);
}
