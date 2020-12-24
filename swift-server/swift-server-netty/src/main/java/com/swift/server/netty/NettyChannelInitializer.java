/**
 * NettyChannelInitializer.java 1.0.0 2018-10-15 23:16:32
 *
 * Copyright (c) 2006-2018 GuangZhou Leopard Techonlogy Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty;

import org.springframework.beans.factory.annotation.Autowired;

import com.swift.core.spring.SpringAutowireBeanFactory;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Channel initializer of Netty.
 * 
 * @author Avery Xiao
 * @email averyxiao@gmail.com
 * @version 1.0.0
 * @date 2018-10-15 23:16:32
 */
public abstract class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    protected ServerConfig serverConfig;

    @Autowired
    protected SpringAutowireBeanFactory autowireBeanFactory;
}
