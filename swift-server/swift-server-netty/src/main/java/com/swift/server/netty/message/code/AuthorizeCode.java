/*
 * @(#)AuthorizeCode.java   1.0  2020年12月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.message.code;

import com.swift.api.protocol.message.CLR;
import com.swift.api.protocol.message.handler.MessageHandlerContext;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年12月29日
 */
public class AuthorizeCode {
    
     private MessageHandlerContext messageHandlerContext;
     
     private CLR clr;
     
     private long timestamp;

    /**
     * @return the messageHandlerContext
     */
    public MessageHandlerContext getMessageHandlerContext() {
        return messageHandlerContext;
    }

    /**
     * @param messageHandlerContext the messageHandlerContext to set
     */
    public void setMessageHandlerContext(MessageHandlerContext messageHandlerContext) {
        this.messageHandlerContext = messageHandlerContext;
    }

    /**
     * @return the clr
     */
    public CLR getClr() {
        return clr;
    }

    /**
     * @param clr the clr to set
     */
    public void setClr(CLR clr) {
        this.clr = clr;
    }

    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
     
     
}
