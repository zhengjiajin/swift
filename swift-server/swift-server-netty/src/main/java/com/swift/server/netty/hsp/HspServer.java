/*
 * @(#)NettyServer.java   1.0  2018年8月22日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.hsp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.protocol.hsp.HspInitializer;
import com.swift.server.netty.NettyServer;
import com.swift.server.netty.ServerConfig;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月22日
 */
@Component
public class HspServer extends NettyServer {

    @Autowired
    private ServerConfig serverConfig;
  
    @Autowired
    private HspInitializer hspInitializer;

    private boolean isStarted = false;
    /**
     * @see com.swift.hhmk.gateway.protocol.keepalive.NettyServer#channelInitializer()
     */
    @Override
    protected ChannelInitializer<SocketChannel> channelInitializer() {
        return hspInitializer;
    }

    @Override
    public void start(int port) {
        setBacklog(serverConfig.getHspServerBacklog());
        setKeepalive(serverConfig.isHspServerKeepalive());
        setReuseAddress(serverConfig.isHspServerReuseAddress());
        setTcpNoDelay(serverConfig.isHspServerTcpNoDelay());
        super.start(serverConfig.getHspServerPort());
        isStarted=true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.swift.hhmk.gateway.protocol.keepalive.NettyServer#stop()
     */
    @Override
    public void stop() {
        super.stop();
        isStarted=false;
        
    }

    /** 
     * @see com.swift.core.server.LifeCycle#isStarted()
     */
    @Override
    public boolean isStarted() {
        return isStarted;
    }
}
