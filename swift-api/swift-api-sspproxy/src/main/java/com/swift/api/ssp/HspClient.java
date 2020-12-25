/*
 * @(#)HspClient.java   1.0  2020年12月25日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.ssp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.protocol.hsp.HspInitializer;
import com.swift.util.type.TypeUtil;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年12月25日
 */
@Component
public class HspClient extends NettyClient {

    @Autowired
    private SspproxyConfig sspproxyConfig;
    
    @Autowired
    private HspInitializer hspInitializer;
    /** 
     * @see com.swift.api.ssp.NettyClient#channelInitializer()
     */
    @Override
    protected ChannelInitializer<SocketChannel> channelInitializer() {
        return hspInitializer;
    }

    /** 
     * @see com.swift.api.ssp.NettyClient#protocolPort()
     */
    @Override
    protected int protocolPort() {
        return TypeUtil.toInt(sspproxyConfig.getGatewayHspPort());
    }

    
}
