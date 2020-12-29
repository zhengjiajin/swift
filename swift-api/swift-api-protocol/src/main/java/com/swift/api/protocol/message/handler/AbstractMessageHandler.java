/**
 * AbstractMessageHandler.java 1.0.0 2020年12月21日
 *
 * Copyright (c) 2015-2020 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.api.protocol.message.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.swift.api.protocol.listener.ChannelEventListener;
import com.swift.api.protocol.listener.ChannelEventListener.Event;
import com.swift.api.protocol.message.Message;
import com.swift.api.protocol.message.MessageType;
import com.swift.api.protocol.util.NettyChannel;

/**
 * Abstract message handler
 * @version 1.0.0
 * @date 2020年12月21日
 */
public abstract class AbstractMessageHandler<T extends Message> implements MessageHandler<T> {

    private MessageType type;

    @Autowired(required = false)
    private List<ChannelEventListener> channelEventListeners;
    /**
     * Construct a new <code>AbstractMessageHandler</code> instance.
     */
    public AbstractMessageHandler(MessageType type) {
        this.type = type;
    }

    /*
     * (non-Javadoc)
     * 
     */
    @Override
    public MessageType getMessageType() {
        return type;
    }
    
    protected void fireChannelEvent(Event event, MessageHandlerContext context) {
        NettyChannel.fireChannelEvent(channelEventListeners, event, context);
    }
}
