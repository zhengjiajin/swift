/*
 * @(#)Message.java   1.0  2018年8月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swift.util.type.JsonUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月28日
 */
public abstract class Message {

    /**
     * 消息头
     */
    @JsonIgnore
    private Header header;
    /**
     * 消息ID
     */
    private String reqId;

    public abstract MessageType getType();

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public String toString() {
        return JsonUtil.toJson(this);
    }
}
