/**
 * MessageHandlerContext.java 1.0.0 2018-10-17 21:06:20
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd. All rights reserved.
 */
package com.swift.server.netty.message.handler;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.google.common.collect.Maps;
import com.swift.exception.extend.SystemException;
import com.swift.server.netty.NettyChannel;
import com.swift.server.netty.auth.AuthClient;
import com.swift.server.netty.message.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.ScheduledFuture;

/**
 * 消息处理器上下文。
 * 
 * @version 1.0.0
 * @date 2018-10-17 21:06:20
 */
public class MessageHandlerContext {

    private final ReentrantReadWriteLock READWRITE_LOCK = new ReentrantReadWriteLock();
    protected final ReadLock READ_LOCK = READWRITE_LOCK.readLock();
    protected final WriteLock WRITE_LOCK = READWRITE_LOCK.writeLock();

    /**
     * 属性
     */
    private Map<Object, Object> attributes = Maps.newConcurrentMap();
    
    /**
     * 连续未确认的DWR次数
     */
    private AtomicInteger unconfirmedDwrTimes = new AtomicInteger(0);
    /**
     * 连接认证的Future
     */
    private ScheduledFuture<?> scheduledFuture;
    /**
     * DPR的Future
     */
    private ScheduledFuture<?> dprFuture;
    /**
     * 对端认证信息
     */
    private AuthClient authClient;

    private ChannelHandlerContext channelHandlerContext;

    public void write(Message message) {
        write(message, false);
    }

    public void write(Message message, boolean close) {
        if (message == null) {
            return;
        }
        if (channelHandlerContext.channel() == null || !channelHandlerContext.channel().isActive()) {
            throw new SystemException("连接未建立或者已断开");
        }

        channelHandlerContext.writeAndFlush(message).addListener(new MessageLoggingListener(message, close));
    }

    public void close() {
        channelHandlerContext.close();
    }

    public String id() {
        if (channelHandlerContext.channel() == null || channelHandlerContext.channel().id() == null) {
            return null;
        }
        return channelHandlerContext.channel().id().asLongText();
    }

    public SocketAddress remoteAddress() {
        if (channelHandlerContext.channel() == null) {
            return null;
        }
        return channelHandlerContext.channel().remoteAddress();
    }

    public String remoteAddressString() {
        return NettyChannel.remoteAddress(channelHandlerContext.channel());
    }

    public Object getAttribute(Object attr) {
        return attributes.get(attr);
    }

    public Map<Object, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public Object addAttribute(Object attr, Object value) {
        return attributes.put(attr, value);
    }

    public Object removeAttribute(Object attr) {
        return attributes.remove(attr);
    }

    public int incrementAndGetUnconfirmedDwrTimes() {
        return unconfirmedDwrTimes.incrementAndGet();
    }

    public int getUnconfirmedDwrTimes() {
        return unconfirmedDwrTimes.get();
    }

    public void resetUnconfirmedDwrTimes() {
        unconfirmedDwrTimes.set(0);
    }

    public ScheduledFuture<?> getScheduledFuture() {
        READ_LOCK.lock();
        try {
            return scheduledFuture;
        } finally {
            READ_LOCK.unlock();
        }
    }

    public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        WRITE_LOCK.lock();
        try {
            this.scheduledFuture = scheduledFuture;
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    public ScheduledFuture<?> getDprFuture() {
        READ_LOCK.lock();
        try {
            return dprFuture;
        } finally {
            READ_LOCK.unlock();
        }
    }

    public void setDprFuture(ScheduledFuture<?> dprFuture) {
        WRITE_LOCK.lock();
        try {
            this.dprFuture = dprFuture;
        } finally {
            WRITE_LOCK.unlock();
        }
    }

    /**
     * @return the authClient
     */
    public AuthClient getAuthClient() {
        READ_LOCK.lock();
        try {
            return authClient;
        } finally {
            READ_LOCK.unlock();
        }
    }

    /**
     * @param authClient the authClient to set
     */
    public void setAuthClient(AuthClient authClient) {
        WRITE_LOCK.lock();
        try {
            this.authClient = authClient;
        } finally {
            WRITE_LOCK.unlock();
        }
    }


    public ChannelHandlerContext getChannelHandlerContext() {
        READ_LOCK.lock();
        try {
            return channelHandlerContext;
        } finally {
            READ_LOCK.unlock();
        }
    }

    public void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {
        WRITE_LOCK.lock();
        try {
            this.channelHandlerContext = channelHandlerContext;
        } finally {
            WRITE_LOCK.unlock();
        }
    }
}
