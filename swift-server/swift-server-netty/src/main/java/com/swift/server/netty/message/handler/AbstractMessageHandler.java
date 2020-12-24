/**
 * AbstractMessageHandler.java 1.0.0 2018-10-17 21:52:53
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.server.netty.listener.ChannelEventListener;
import com.swift.server.netty.listener.ChannelEventListener.Event;
import com.swift.server.netty.message.Message;
import com.swift.server.netty.message.MessageType;

/**
 * Abstract message handler
 * @version 1.0.0
 * @date 2018-10-17 21:52:53
 */
public abstract class AbstractMessageHandler<T extends Message> implements MessageHandler<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMessageHandler.class);
    
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
        if (channelEventListeners == null || channelEventListeners.isEmpty()) {
            return;
        }

        for (ChannelEventListener listener : channelEventListeners) {
            try {
                listener.channelEvent(event, context);
            } catch (Throwable ex) {
                logger.error("Unexpected exception while fire channel event: {} on listener: {}", event,
                        listener.getClass().getName(), ex);
            }
        }
    }
}
