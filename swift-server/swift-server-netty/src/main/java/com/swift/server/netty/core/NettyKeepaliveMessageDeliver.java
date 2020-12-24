/*
 * @(#)NettyKeepaliveMessageDeliver.java   1.0  2020年12月23日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.core;

import com.swift.core.service.processor.KeepaliveMessageDeliver;
import com.swift.server.netty.message.SCA;
import com.swift.server.netty.message.SCR;
import com.swift.server.netty.message.handler.MessageHandlerContext;

/**
 * Netty处理的连接控制器
 * @author zhengjiajin
 * @version 1.0 2020年12月23日
 */
public interface NettyKeepaliveMessageDeliver extends KeepaliveMessageDeliver {

    public void requestReceived(SCR scr, MessageHandlerContext messageHandlerContext);
    
    public void responseReceived(SCA message, MessageHandlerContext context);
    
}
