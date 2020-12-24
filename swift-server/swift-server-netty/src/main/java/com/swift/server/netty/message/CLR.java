/*
 * @(#)CLR.java   1.0  2018年8月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.message;

import com.swift.server.netty.auth.ClientInfo;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年8月28日
 */
public class CLR extends MessageRequest {

    //客户端信息
    private ClientInfo clientInfo;
    //加密后的用户信息/系统
    private String clientObject;
    //随机授权码/accessToken
    private String secSocketAccept;
    /** 
     * @see com.swift.hhmk.gateway.protocol.keepalive.message.Message#getType()
     */
    @Override
    public MessageType getType() {
        return MessageType.CLR;
    }
    public ClientInfo getClientInfo() {
        return clientInfo;
    }
    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }
    
    /**
     * @return the clientObject
     */
    public String getClientObject() {
        return clientObject;
    }
    /**
     * @param clientObject the clientObject to set
     */
    public void setClientObject(String clientObject) {
        this.clientObject = clientObject;
    }
    public String getSecSocketAccept() {
        return secSocketAccept;
    }
    public void setSecSocketAccept(String secSocketAccept) {
        this.secSocketAccept = secSocketAccept;
    }
    
}
