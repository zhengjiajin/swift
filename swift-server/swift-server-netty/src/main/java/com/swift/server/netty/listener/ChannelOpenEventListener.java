/*
 * @(#)ChannelOpenEventListener.java   1.0  2020年12月24日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.listener;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.protocol.Constants;
import com.swift.api.protocol.listener.ChannelEventListener;
import com.swift.api.protocol.message.handler.MessageHandlerContext;
import com.swift.api.protocol.util.NettyChannel;

import io.netty.util.concurrent.EventExecutorGroup;

/**
 * 连接打开时需要发送CLR登录验证
 * @author zhengjiajin
 * @version 1.0 2020年12月24日
 */
@Component
public class ChannelOpenEventListener implements ChannelEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ChannelOpenEventListener.class);
    
    @Autowired
    private EventExecutorGroup eventExecutorGroup;
    /** 
     * @see com.swift.api.protocol.listener.ChannelEventListener#channelEvent(com.swift.api.protocol.listener.ChannelEventListener.Event, com.swift.api.protocol.message.handler.MessageHandlerContext)
     */
    @Override
    public void channelEvent(Event event, MessageHandlerContext context) {
        if(!event.equals(Event.OPEN)) return;
        context.setScheduledFuture(eventExecutorGroup.schedule(new Runnable() {

            @Override
            public void run() {
                logger.warn("No authentication in {} seconds, connection close: {}", Constants.AUTH_MAXWAIT_TIME,
                        NettyChannel.remoteAddress(context.getChannelHandlerContext().channel()));
                context.getChannelHandlerContext().close();
            }
        },  Constants.AUTH_MAXWAIT_TIME, TimeUnit.SECONDS));
    }

}
