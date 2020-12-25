/*
 * @(#)DPR.java   1.0  2020年12月24日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.protocol.message;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年12月24日
 */
public class DPR extends MessageRequest {

    private Integer disconnectCode;
    
    private String disconnectRem;
    /** 
     * @see com.swift.hhmk.gateway.protocol.keepalive.message.Message#getType()
     */
    @Override
    public MessageType getType() {
        return MessageType.DPR;
    }
    public Integer getDisconnectCode() {
        return disconnectCode;
    }
    public void setDisconnectCode(Integer disconnectCode) {
        this.disconnectCode = disconnectCode;
    }
    public String getDisconnectRem() {
        return disconnectRem;
    }
    public void setDisconnectRem(String disconnectRem) {
        this.disconnectRem = disconnectRem;
    }

}
