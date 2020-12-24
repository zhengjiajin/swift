/**
 * NettyChannel.java 1.0.0 2018-10-26 00:40:51
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty;

import io.netty.channel.Channel;

/**
 * Netty Channel工具类。
 * 
 * @author Avery Xiao
 * @email averyxiao@gmail.com
 * @version 1.0.0
 * @date 2018-10-26 00:40:51
 */
public class NettyChannel {

    /**
     * Construct a new <code>NettyChannel</code> instance.
     */
    private NettyChannel() {
    }

    public static String remoteAddress(Channel channel) {
        if (channel == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(channel.remoteAddress());
        builder.append("(").append(channel.id()).append(")");
        return builder.toString();
    }
}
