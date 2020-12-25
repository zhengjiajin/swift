/**
 * ChannelEventListener.java 1.0.0 2020年12月25日
 *
 * Copyright (c) 2015-2020 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.api.protocol.listener;

import com.swift.api.protocol.message.handler.MessageHandlerContext;

/**
 * 通道事件监听接口。
 * @author zhengjiajin
 * @version 1.0 2020年12月25日
 */
public interface ChannelEventListener {

    public enum Event {
        /**
         * 通道建立事件
         */
        OPEN,
        /**
         * 通过验证明
         */
        AUTH,
        /**
         * 通道关闭事件
         */
        CLOSE,
        /**
         * 通道标黄
         */
        WARNING,
        /**
         * 告警恢复通信
         */
        GREEN,
        /**
         * 通道收到消息事件,只含SCR,SCA
         */
        RECEIVED_MESSAGE;
    }

    /**
     * 通道事件发生时触发。
     * 
     * @param event
     *            事件
     * @param context
     *            上下文
     */
    void channelEvent(Event event, MessageHandlerContext context);
}
