/**
 * MessageHandlerFactory.java 1.0.0 2018-10-20 08:42:38
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty.message.handler;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.swift.server.netty.message.Message;
import com.swift.server.netty.message.MessageType;

/**
 * Message handler factory.
 * 
 * @version 1.0.0
 * @date 2018-10-20 08:42:38
 */
@Component
public class MessageHandlerFactory implements ApplicationContextAware {

    private final Map<MessageType, MessageHandler<Message>> messageHandlers = new HashMap<MessageType, MessageHandler<Message>>();

    private ApplicationContext applicationContext;

    public MessageHandlerFactory() {
        // Does nothing
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @PostConstruct
    public void init() {
        Map<String, MessageHandler> handlers = applicationContext.getBeansOfType(MessageHandler.class);
        if (handlers != null && !handlers.isEmpty()) {
            for (MessageHandler<Message> handler : handlers.values()) {
                messageHandlers.put(handler.getMessageType(), handler);
            }
        }
    }

    public MessageHandler<Message> getMessageHandler(MessageType messageType) {
        return messageHandlers.get(messageType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.context.ApplicationContextAware#setApplicationContext(org
     * .springframework.context.ApplicationContext)
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
