/**
 * @(#)HspInitializer.java  1.0 2020年12月25日
 *  
 * Copyright (c) 2015-2020 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.api.protocol.hsp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.protocol.Constants;
import com.swift.api.protocol.message.handler.DefaultServerHandler;
import com.swift.core.spring.SpringAutowireBeanFactory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 添加说明
 * @author zhengjiajin
 * @version 1.0 2020年12月25日
 */
@Component
public class HspInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    protected SpringAutowireBeanFactory autowireBeanFactory;
    
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new IdleStateHandler(Constants.MAX_IDLE_TIME, 0, 0));
        p.addLast(new HspDecoder());
        p.addLast(new HspEncoder());
        DefaultServerHandler handler = new DefaultServerHandler();
        autowireBeanFactory.autowire(handler);
        p.addLast(handler);
    }
}
