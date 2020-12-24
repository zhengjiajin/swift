/*
 * @(#)CLA.java   1.0  2018年8月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.message;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月28日
 */
public class CLA extends MessageResponse {
    
    /**
     * Construct a new <code>CLA</code> instance.
     */
    public CLA() {
        super();
    }
    
    /**
     * Construct a new <code>CLA</code> instance with request.
     */
    public CLA(final CLR request) {
        super(request);
    }

    public MessageType getType() {
        return MessageType.CLA;
    }

}
