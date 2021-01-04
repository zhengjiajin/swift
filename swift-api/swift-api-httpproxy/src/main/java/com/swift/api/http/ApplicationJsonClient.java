/*
 * @(#)ApplicationJsonClient.java   1.0  2020年12月8日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.http;

import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.swift.api.http.apache.ApacheHttpClient;
import com.swift.api.http.apache.protocol.ApacheApplicationJsonProtocol;
import com.swift.core.api.asyn.ChangeFuture;
import com.swift.core.api.asyn.FutureValueChange;
import com.swift.core.api.rpc.ClientEngine;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.util.type.TypeUtil;

/**
 * 封装通用的SWIFT-HTTP/ApplicationJson协议的客户端
 * 客户端系统apache-http工具
 * @author zhengjiajin
 * @version 1.0 2020年12月8日
 */
@Service
public class ApplicationJsonClient implements ClientEngine<ServiceRequest, ServiceResponse> {

    @Autowired
    private ApacheApplicationJsonProtocol apacheApplicationJsonProtocol;
    
    @Autowired
    private ApacheHttpClient apacheHttpClient;
    
    @Value("${domain:}")
    private String domain;
    
    
    private void autoReq(ServiceRequest req) {
        if(TypeUtil.isNull(req.getDomain())) req.setDomain(domain);
        if(TypeUtil.isNull(req.getRequestTime())) req.setRequestTime(System.currentTimeMillis());
    }
    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequestNoReturn(java.lang.Object)
     */
    @Override
    public void sendRequestNoReturn(ServiceRequest req) {
        autoReq(req);
        apacheHttpClient.sendRequestNoReturn(apacheApplicationJsonProtocol.toRequest(req));
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequest(java.lang.Object, com.swift.core.api.rpc.ClientEngine.CallBackEngine)
     */
    @Override
    public void sendRequest(ServiceRequest req, CallBackEngine<ServiceResponse> callBack) {
        autoReq(req);
        CallBackEngine<HttpResponse> apacheCallback = new CallBackEngine<HttpResponse>() {

            @Override
            public void receiveResponse(HttpResponse rsp) {
                callBack.receiveResponse(apacheApplicationJsonProtocol.toResponse(req,rsp));
            }

            @Override
            public void failed(Throwable ex) {
                callBack.failed(ex);
            }
            
        };
        apacheHttpClient.sendRequest(apacheApplicationJsonProtocol.toRequest(req), apacheCallback);
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendAsynRequest(java.lang.Object)
     */
    @Override
    public Future<ServiceResponse> sendAsynRequest(ServiceRequest req) {
        autoReq(req);
        Future<HttpResponse> future = apacheHttpClient.sendAsynRequest(apacheApplicationJsonProtocol.toRequest(req));
        FutureValueChange<HttpResponse,ServiceResponse> apacheFutureChange = new FutureValueChange<HttpResponse,ServiceResponse>(){

            @Override
            public ServiceResponse toChange(HttpResponse t) {
                return apacheApplicationJsonProtocol.toResponse(req,t);
            }
            
        };
        return new ChangeFuture<HttpResponse,ServiceResponse>(future, apacheFutureChange);
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequest(java.lang.Object)
     */
    @Override
    public ServiceResponse sendRequest(ServiceRequest req) {
        autoReq(req);
        HttpResponse res = apacheHttpClient.sendRequest(apacheApplicationJsonProtocol.toRequest(req));
        return apacheApplicationJsonProtocol.toResponse(req,res);
    }
    
}
