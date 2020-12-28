/*
 * @(#)NettyServer.java   1.0  2018年8月22日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.core.server.LifeCycle;
import com.swift.util.type.IpUtil;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月22日
 */
public abstract class NettyServer implements LifeCycle {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private static final int DEFAULT_BACKLOG = 1024;
    private static final boolean DEFAULT_TCP_NO_DELAY = true;
    private static final boolean DEFAULT_REUSEADDR = true;
    private static final boolean DEFAULT_KEEPALIVE = true;

    private int backlog = DEFAULT_BACKLOG;
    private boolean tcpNoDelay = DEFAULT_TCP_NO_DELAY;
    private boolean reuseAddress = DEFAULT_REUSEADDR;
    private boolean keepalive = DEFAULT_KEEPALIVE;

    @Autowired(required = false)
    private ServerLiftCycleListener serverLiftCycleListener;
    
    private Thread launcher;
    
    private ChannelFuture future;
    
    private InetSocketAddress addr;

    protected abstract ChannelInitializer<SocketChannel> channelInitializer();

    @Override
    public void start(int port) {
        launcher = new Thread() {
            public void run() {
                EventLoopGroup bossGroup = new NioEventLoopGroup(1);
                EventLoopGroup workerGroup = new NioEventLoopGroup();
                try {
                    logger.info("启动长连接服务:" + port);
                    ServerBootstrap b = new ServerBootstrap();
                    b.option(ChannelOption.TCP_NODELAY, tcpNoDelay);
                    b.option(ChannelOption.SO_BACKLOG, backlog);
                    b.option(ChannelOption.SO_REUSEADDR, reuseAddress);
                    b.option(ChannelOption.SO_KEEPALIVE, keepalive);
                    b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                            .handler(new LoggingHandler(LogLevel.INFO)).childHandler(channelInitializer());
                    addr = new InetSocketAddress(IpUtil.getHostAddress(), port);
                    future = b.bind(addr).sync().channel().closeFuture().sync();
                    logger.info("启动长连接服务:" + port + "结束");
                    if (serverLiftCycleListener != null) {
                        serverLiftCycleListener.serverStart(IpUtil.getHostAddress(),port);
                    }
                } catch (Throwable ex) {
                    String msg = "An exception occurred: ";
                    logger.error(msg, ex);
                    throw new RuntimeException(msg, ex);
                } finally {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            };
        };
        launcher.setName("launcher");
        launcher.setDaemon(true);
        launcher.start();
    }

    @Override
    public void stop() {
        if (launcher != null && launcher.isAlive()) {
            launcher.interrupt();
            try {
                launcher.join();
            } catch (InterruptedException e) {
            }
        }
        if(future!=null && future.isSuccess() && future.channel()!=null) {
            future.channel().close();
        }
        if (serverLiftCycleListener != null) {
            serverLiftCycleListener.serverStop(addr.getHostName(),addr.getPort());
        }
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public void setKeepalive(boolean keepalive) {
        this.keepalive = keepalive;
    }

    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }
}
