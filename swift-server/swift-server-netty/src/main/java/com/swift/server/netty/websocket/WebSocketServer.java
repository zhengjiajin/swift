/*
 * @(#)NettyServer.java   1.0  2018年8月22日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.server.netty.NettyServer;
import com.swift.server.netty.ServerConfig;
import com.swift.server.netty.websocket.handler.WebSocketInitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月22日
 */
@Component
public class WebSocketServer extends NettyServer {

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private WebSocketInitializer webSocketInitializer;

    private boolean isStarted =false;
    /**
     * @see com.swift.hhmk.gateway.protocol.keepalive.NettyServer#channelInitializer()
     */
    @Override
    protected ChannelInitializer<SocketChannel> channelInitializer() {
        return webSocketInitializer;
    }

    @Override
    public void start(int port) {
        setBacklog(serverConfig.getWebsocketServerBacklog());
        setKeepalive(serverConfig.isWebsocketServerKeepalive());
        setReuseAddress(serverConfig.isWebsocketServerReuseAddress());
        setTcpNoDelay(serverConfig.isWebsocketServerTcpNoDelay());
        super.start(serverConfig.getWebsocketServerPort());
        isStarted=true;
    }

    /** 
     * @see com.swift.core.server.LifeCycle#isStarted()
     */
    @Override
    public boolean isStarted() {
        return isStarted;
    }
}