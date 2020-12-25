/*
 * @(#)DPA.java   1.0  2020年12月24日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.protocol.message;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2020年12月24日
 */
public class DPA extends MessageResponse {

    /**
     * Construct a new <code>DPA</code> instance.
     */
    public DPA() {
        super();
    }
    
    /**
     * Construct a new <code>DPA</code> instance with request.
     */
    public DPA(final DPR request) {
        super(request);
    }

    /**
     * @see com.swift.hhmk.gateway.protocol.keepalive.message.Message#getType()
     */
    @Override
    public MessageType getType() {
        return MessageType.DPA;
    }

}
