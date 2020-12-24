/*
 * @(#)MessageResponse.java   1.0  2018年8月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.message;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年8月28日
 */
public abstract class MessageResponse extends Message {

    private int resultCode = 0;
    private String reason = "ok";
    @JsonIgnore
    private long responseTime = System.currentTimeMillis();
    @JsonIgnore
    private MessageRequest request;

    /**
     * Construct a new <code>MessageResponse</code> instance.
     */
    public MessageResponse() {
    }
    
    /**
     * Construct a new <code>MessageResponse</code> instance with request.
     */
    public MessageResponse(final MessageRequest request) {
        setRequest(request);
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the responseTime
     */
    public long getResponseTime() {
        return responseTime;
    }

    /**
     * @param responseTime
     *            the responseTime to set
     */
    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public MessageRequest getRequest() {
        return request;
    }

    public void setRequest(final MessageRequest request) {
        this.request = request;
        if (request != null) {
            setReqId(request.getReqId());
        }
    }
}
