/*
 * @(#)PendingRequest.java   1.0  2020年12月23日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.core.pending;

import com.swift.core.api.rpc.ClientEngine.CallBackEngine;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.server.netty.message.SCR;
import com.swift.server.netty.message.handler.MessageHandlerContext;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年12月23日
 */
public class PendingRequest {
    private long timestamp;
    private SCR scr;
    private ServiceRequest request;
    private MessageHandlerContext messageHandlerContext;
    private CallBackEngine<ServiceResponse> callBack;

    //对外发送的请求
    public PendingRequest(ServiceRequest request,CallBackEngine<ServiceResponse> callBack) {
        this.timestamp=System.currentTimeMillis();
        this.request=request;
        this.callBack=callBack;
    }
    //对内发送的请求
    public PendingRequest(SCR scr,MessageHandlerContext messageHandlerContext) {
        this.timestamp=System.currentTimeMillis();
        this.scr=scr;
        this.messageHandlerContext=messageHandlerContext;
    }
    
    /**
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }


    /**
     * @return the scr
     */
    public SCR getScr() {
        return scr;
    }


    /**
     * @return the messageHandlerContext
     */
    public MessageHandlerContext getMessageHandlerContext() {
        return messageHandlerContext;
    }

    /**
     * @return the callBack
     */
    public CallBackEngine<ServiceResponse> getCallBack() {
        return callBack;
    }

    /**
     * @return the request
     */
    public ServiceRequest getRequest() {
        return request;
    }


}
