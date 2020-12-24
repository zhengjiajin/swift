/*
 * @(#)HspInitializer.java   1.0  2018年8月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.hsp.handler;

import org.springframework.stereotype.Component;

import com.swift.server.netty.NettyChannelInitializer;
import com.swift.server.netty.message.handler.DefaultServerHandler;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月28日
 */
@Component
public class HspInitializer extends NettyChannelInitializer {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new IdleStateHandler(serverConfig.getMaxIdleTime(), 0, 0));
        p.addLast(new HspDecoder());
        p.addLast(new HspEncoder());
        DefaultServerHandler handler = new DefaultServerHandler();
        autowireBeanFactory.autowire(handler);
        p.addLast(handler);
    }
}
