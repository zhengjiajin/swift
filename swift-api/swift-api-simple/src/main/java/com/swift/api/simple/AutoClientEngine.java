/*
 * @(#)AutoClientEngine.java   1.0  2021年7月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.simple;

import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.swift.api.simple.core.ClientEngineFactory;
import com.swift.core.api.rpc.ClientEngine;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年7月20日
 */
@Component("autoClientEngine")
@Primary
public class AutoClientEngine implements ClientEngine<ServiceRequest, ServiceResponse> {

    @Autowired
   private ClientEngineFactory clientEngineFactory;
    
    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequestNoReturn(java.lang.Object)
     */
    @Override
    public void sendRequestNoReturn(ServiceRequest req) {
        clientEngineFactory.getClientEngine(req).sendRequestNoReturn(req);
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequest(java.lang.Object, com.swift.core.api.rpc.ClientEngine.CallBackEngine)
     */
    @Override
    public void sendRequest(ServiceRequest req, CallBackEngine<ServiceResponse> callBack) {
        clientEngineFactory.getClientEngine(req).sendRequest(req, callBack);
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendAsynRequest(java.lang.Object)
     */
    @Override
    public Future<ServiceResponse> sendAsynRequest(ServiceRequest req) {
        return clientEngineFactory.getClientEngine(req).sendAsynRequest(req);
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequest(java.lang.Object)
     */
    @Override
    public ServiceResponse sendRequest(ServiceRequest req) {
        return clientEngineFactory.getClientEngine(req).sendRequest(req);
    }

}
