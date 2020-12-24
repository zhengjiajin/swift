/**
 * MessageLoggingListener.java 1.0.0 2018-10-16 23:12:10
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.server.netty.NettyChannel;
import com.swift.server.netty.message.Message;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

/**
 * 消息日志监听器，监听消息发送的结果，并记录相应的日志。
 * 
 * @version 1.0.0
 * @date 2018-10-16 23:12:10
 */
public class MessageLoggingListener implements ChannelFutureListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageLoggingListener.class);

    private final Message message;
    private final boolean close;

    public MessageLoggingListener(final Message message) {
        this(message, false);
    }

    public MessageLoggingListener(final Message message, boolean close) {
        this.message = message;
        this.close = close;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.netty.util.concurrent.GenericFutureListener#operationComplete(io.netty.
     * util.concurrent.Future)
     */
    @Override
    public void operationComplete(ChannelFuture future) throws Exception {
        Logger log = logger;
        if (future.cause() != null) {
            log.error("Failed to send message to {} (TEXT): {}", NettyChannel.remoteAddress(future.channel()), message,
                    future.cause());
        } else if (future.isSuccess()) {
            log.debug("Message sent to {} (TEXT): {}", NettyChannel.remoteAddress(future.channel()), message);
        }
        if (close) {
            if (future.channel() != null && future.channel().isActive()) {
                future.channel().close();
            }
        }
    }
}
