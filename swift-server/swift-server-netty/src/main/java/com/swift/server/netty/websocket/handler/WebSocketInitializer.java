/*
 * @(#)WebSocketInitializer.java   1.0  2018年8月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.websocket.handler;

import org.springframework.stereotype.Component;

import com.swift.server.netty.NettyChannelInitializer;
import com.swift.server.netty.message.handler.DefaultServerHandler;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月28日
 */
@Component
public class WebSocketInitializer extends NettyChannelInitializer {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new IdleStateHandler(0, 0, serverConfig.getMaxIdleTime()));
        p.addLast(new HttpServerCodec());
        p.addLast(new ChunkedWriteHandler());
        p.addLast(new HttpObjectAggregator(serverConfig.getWebsocketMaxContentLength()));
        p.addLast(new WebSocketServerProtocolHandler(serverConfig.getWebsocketPath()));
        p.addLast(new WebSocketDecoder());
        p.addLast(new WebSocketEncoder());
        DefaultServerHandler handler = new DefaultServerHandler();
        autowireBeanFactory.autowire(handler);
        p.addLast(handler);
    }
}
