/**
 * MessageHandler.java 1.0.0 2018-10-17 01:06:44
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler;

import com.swift.server.netty.message.Message;
import com.swift.server.netty.message.MessageType;

/**
 * 消息处理器接口。
 * 
 * @version 1.0.0
 * @date 2018-10-17 01:06:44
 */
public interface MessageHandler<T extends Message> {

    /**
     * 获取消息处理器处理的消息类型。
     * 
     * @return 消息类型
     */
    MessageType getMessageType();

    /**
     * 处理消息。
     * 
     * @param message
     *            消息
     * @param context
     *            消息上下文
     * @throws GatewayException
     *             the GatewayException
     */
    void handle(T message, MessageHandlerContext context);
}
