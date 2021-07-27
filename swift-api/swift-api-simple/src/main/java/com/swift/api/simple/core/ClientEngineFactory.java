/*
 * @(#)ClientEngineFactory.java   1.0  2021年7月20日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.simple.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.dubbo.DubboClient;
import com.swift.api.http.ApplicationJsonClient;
import com.swift.core.api.rpc.ClientEngine;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.model.parser.HttpDomainUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年7月20日
 */
@Component
public class ClientEngineFactory {

    @Autowired
    private ApplicationJsonClient applicationJsonClient;
    
    @Autowired
    private DubboClient dubboClient;
    //<sysId:method>,ClientEngine
    private Map<String,ClientEngine<ServiceRequest, ServiceResponse>> clientEngineMap = new ConcurrentHashMap<>();
    
    public ClientEngine<ServiceRequest, ServiceResponse> getClientEngine(ServiceRequest req){
        String interfaceKey = interfaceKey(req);
        if(clientEngineMap.get(interfaceKey)!=null) return clientEngineMap.get(interfaceKey);
        //检测是否有注册DUBBO
        boolean checkDubbo = dubboClient.checkDubboClient(req);
        if(checkDubbo) {
            clientEngineMap.put(interfaceKey, dubboClient);
            return dubboClient;
        }
        clientEngineMap.put(interfaceKey, applicationJsonClient);
        return applicationJsonClient;
    }
    
    //<sysId:method>
    public static String interfaceKey(ServiceRequest req) {
        String url = HttpDomainUtil.toStringDamain(req);
        return HttpDomainUtil.toSysId(url)+":"+HttpDomainUtil.toMethod(url);
    }
}
