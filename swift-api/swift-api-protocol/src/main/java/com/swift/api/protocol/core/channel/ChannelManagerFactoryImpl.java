/**
 * @(#)ChannelManager.java 2020年12月21日
 *  
 * Copyright (c) 2015-2015 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.api.protocol.core.channel;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.swift.api.protocol.auth.ClientInfo;
import com.swift.api.protocol.core.ChannelManagerFactory;
import com.swift.api.protocol.message.handler.MessageHandlerContext;
import com.swift.api.protocol.util.NettyChannel;
import com.swift.util.math.RandomUtil;
import com.swift.util.type.TypeUtil;

/**
 * 管理已认证的连接及上下文本
 * 
 * @author zhengjiajin
 * @version 1.0 2020年12月21日
 */
@Component
public class ChannelManagerFactoryImpl implements ChannelManagerFactory {

   // private static final Logger logger = LoggerFactory.getLogger(ChannelManagerFactoryImpl.class);
    // 用户同一个客户端类型只能一个连接 userId,ClientInfo,context
    private Map<Object, Map<ClientInfo, MessageHandlerContext>> userChannels = new ConcurrentHashMap<>();
    // 系统同一IP只能一个连接 sysId,Ip,context
    private Map<String, Map<String, MessageHandlerContext>> sysChannels = new ConcurrentHashMap<>();

    private final static Map<Object, Object> LOCK_MAP = new ConcurrentHashMap<>();

    
    private boolean isActivity(MessageHandlerContext channel) {
        if (channel == null) return false;
        if (channel.getChannelHandlerContext() == null) return false;
        if (channel.getChannelHandlerContext().channel() == null) return false;
        if (!channel.getChannelHandlerContext().channel().isActive()) return false;
        return true;
    }

    // 此处线程不安全但问题不大
    private Object getLock(Object obj) {
        if (!LOCK_MAP.containsKey(obj)) {
            LOCK_MAP.put(obj, new Object());
        }
        return LOCK_MAP.get(obj);
    }

    /**
     * 
     * @see com.swift.server.netty.core.ChannelManagerFactory#registerChannel(java.lang.String,
     *      com.swift.server.netty.message.handler.MessageHandlerContext)
     */
    @Override
    public void registerChannel(String sysId, MessageHandlerContext context) {
        if (sysId == null || context == null) return;
        String ip = NettyChannel.remoteAddress(context.getChannelHandlerContext().channel());
        synchronized (getLock(sysId)) {
            // 存在旧连接需要把旧连接断连
            Map<String, MessageHandlerContext> sysMap = sysChannels.get(sysId);
            if (sysMap == null) {
                sysMap = new ConcurrentHashMap<String, MessageHandlerContext>();
                sysChannels.put(sysId, sysMap);
            }
            MessageHandlerContext contextOld = sysMap.get(ip);
            if (contextOld != null) {
                try {
                    contextOld.close();
                }catch(Throwable ex) {
                }
            }
            sysMap.put(ip, context);
        }
    }

    /**
     * @see com.swift.server.netty.core.ChannelManagerFactory#registerChannel(java.lang.Object,
     *      com.swift.server.netty.auth.ClientInfo, com.swift.server.netty.message.handler.MessageHandlerContext)
     */
    @Override
    public void registerChannel(Object userId, ClientInfo clientInfo, MessageHandlerContext context) {
        if (userId == null || clientInfo == null || context == null) return;
        synchronized (getLock(userId)) {
            Map<ClientInfo, MessageHandlerContext> userMap = userChannels.get(userId);
            if (userMap == null) {
                userMap = new ConcurrentHashMap<ClientInfo, MessageHandlerContext>();
                userChannels.put(userId, userMap);
            }
            MessageHandlerContext contextOld = userMap.get(clientInfo);
            if (contextOld != null) {
                try {
                    contextOld.close();
                }catch(Throwable ex) {
                }
            }
            userMap.put(clientInfo, context);
        }
    }

    /**
     * @see com.swift.server.netty.core.ChannelManagerFactory#deregisterChannel(java.lang.String)
     */
    @Override
    public void deregisterChannel(String sysId, String ip) {
        if (sysId == null || ip == null) return;
        synchronized (getLock(sysId)) {
            Map<String, MessageHandlerContext> sysMap = sysChannels.get(sysId);
            if (sysMap == null) return;
            sysMap.remove(ip);
            if (sysMap.isEmpty()) {
                sysChannels.remove(sysId);
            }
        }
    }

    /**
     * @see com.swift.server.netty.core.ChannelManagerFactory#deregisterChannel(java.lang.Object,
     *      com.swift.server.netty.auth.ClientInfo)
     */
    @Override
    public void deregisterChannel(Object userId, ClientInfo clientInfo) {
        if (userId == null || clientInfo == null) return;
        synchronized (getLock(userId)) {
            Map<ClientInfo, MessageHandlerContext> maps = userChannels.get(userId);
            if (maps == null) return;
            maps.remove(clientInfo);
            if (maps.isEmpty()) {
                userChannels.remove(userId);
            }
        }
    }

    /**
     * @see com.swift.server.netty.core.ChannelManagerFactory#getChannel(java.lang.String)
     */
    @Override
    public MessageHandlerContext getChannel(String sysId) {
        if (TypeUtil.isNull(sysId)) return null;
        synchronized (getLock(sysId)) {
            if (!sysChannels.containsKey(sysId)) return null;
            Set<String> channelsKey = sysChannels.get(sysId).keySet();
            if (channelsKey.size() == 0) {
                sysChannels.remove(sysId);
                return null;
            }
            int ranId = RandomUtil.createCode(0, channelsKey.size() - 1);
            String ip = (String) channelsKey.toArray()[ranId];
            MessageHandlerContext context = sysChannels.get(sysId).get(ip);
            if (!isActivity(context)) {
                sysChannels.get(sysId).remove(ip);
                if (sysChannels.get(sysId).isEmpty()) {
                    sysChannels.remove(sysId);
                }
                return getChannel(sysId);
            }
            return context;
        }
    }

    /**
     * @see com.swift.server.netty.core.ChannelManagerFactory#getChannel(java.lang.Object,
     *      com.swift.server.netty.auth.ClientInfo)
     */
    @Override
    public MessageHandlerContext getChannel(Object userId, ClientInfo clientInfo) {
        if (TypeUtil.isNull(userId) || clientInfo == null) return null;
        synchronized (getLock(userId)) {
            if (!userChannels.containsKey(userId)) return null;
            Map<ClientInfo, MessageHandlerContext> map = userChannels.get(userId);
            if (map.isEmpty()) {
                userChannels.remove(userId);
                return null;
            }
            MessageHandlerContext channel = map.get(clientInfo);
            if (!isActivity(channel)) {
                map.remove(clientInfo);
                if (map.isEmpty()) {
                    userChannels.remove(userId);
                    return null;
                }
            }
            return channel;
        }
    }

    /**
     * @see com.swift.server.netty.core.ChannelManagerFactory#getChannel(java.lang.Object)
     */
    @Override
    public Collection<MessageHandlerContext> getChannel(Object userId) {
        if (TypeUtil.isNull(userId)) return null;
        synchronized (getLock(userId)) {
            if (!userChannels.containsKey(userId)) return null;

            Map<ClientInfo, MessageHandlerContext> value = userChannels.get(userId);
            for (Entry<ClientInfo, MessageHandlerContext> entry : value.entrySet()) {
                if (!isActivity(entry.getValue())) {
                    value.remove(entry.getKey());
                }
            }
            if (value.isEmpty()) {
                userChannels.remove(userId);
                return null;
            }
            return value.values();
        }
    }
}
