/*
 * @(#)DubboClient.java   1.0  2021年7月14日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.dubbo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.function.BiConsumer;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.swift.core.api.OpenApi;
import com.swift.core.api.asyn.ChangeFuture;
import com.swift.core.api.asyn.FutureValueChange;
import com.swift.core.api.rpc.ClientEngine;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.model.parser.HttpDomainUtil;
import com.swift.core.spring.proxy.AnnotationTypeUtil;
import com.swift.exception.extend.SystemException;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.bean.BeanUtil;
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年7月14日
 */
@Service
public class DubboClient implements ClientEngine<ServiceRequest, ServiceResponse> {
    
    private static Logger log = LoggerFactory.getLogger(DubboClient.class);

    @Value("${sysId:}")
    private String sysId;

    @Autowired(required = false)
    private RegistryConfig registryConfig;
    
    private static final int timeOUt = 30*1000;
    
    //interface:GenericService
    private Map<String,GenericService> genericServiceMap = new ConcurrentHashMap<>();
    
    public boolean checkDubboClient(ServiceRequest req) {
        try {
            if(registryConfig==null) return false;
            getGenericService(req);
            return true;
        }catch(Throwable e) {
            return false;
        }
    }
    
    private String getInterfaceName(ServiceRequest req) {
        String method = req.getMethod();
        if(TypeUtil.isNull(method)) {
            method=HttpDomainUtil.toMethod(req.getDomain());
        }
        String toSysId = HttpDomainUtil.toSysId(req.getDomain());
        if(TypeUtil.isNull(method)) throw new SystemException("请输入需要调用的接口");
        if(method.indexOf(".")!=-1) return method;
        List<String> list = AnnotationTypeUtil.getAnnotationClassList(OpenApi.class);
        if(TypeUtil.isNull(list)) return null;
        for(String className : list) {
            Class<?> cla = BeanUtil.classForName(className);
            OpenApi openApi = AnnotationUtil.getAnnotation(cla, OpenApi.class);
            if(openApi==null) continue;
            if(!openApi.method().equalsIgnoreCase(method)) continue;
            if(TypeUtil.isNull(toSysId)) return className;
            if(!openApi.value().equalsIgnoreCase(toSysId)) continue;
            return className;
        }
        return null;
    }
    
    private GenericService getGenericService(ServiceRequest req) {
        if(registryConfig==null)  throw new SystemException("没有配置DUBBO注册中心");
        String interfaceName = getInterfaceName(req);
        if(TypeUtil.isNull(interfaceName)) throw new SystemException("请输入需要调用的接口");
        if(genericServiceMap.containsKey(interfaceName)) return genericServiceMap.get(interfaceName);
        synchronized (interfaceName) {
            ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
            reference.setInterface(interfaceName);
            reference.setTimeout(timeOUt);
            reference.setGeneric(CommonConstants.GENERIC_SERIALIZATION_DEFAULT);
            reference.setRegistry(registryConfig);
            reference.setCheck(true);
            //reference.setListener(listener);
            GenericService genericService = reference.get();
            genericServiceMap.put(interfaceName, genericService);
            return genericService;
        }
    }
    
    private ServiceResponse formatServiceResponse(Object arg) {
        if(arg instanceof ServiceResponse) return (ServiceResponse)arg;
        if(arg instanceof String) return JsonUtil.toObj(String.valueOf(arg), ServiceResponse.class);
        if(arg instanceof JSON) {
            JSON json = (JSON)arg;
            return JsonUtil.toObj(json.toJSONString(), ServiceResponse.class);
        }
        return null;
    }
    
    private static final String[] parameterTypes = new String[]{"com.swift.core.model.ServiceRequest"};
    
    private String[] createParameterTypes() {
        return parameterTypes;
    }
    
    private Object[] createArgs(ServiceRequest req) {
        return new ServiceRequest[] {req};
    }
    
    private void autoReq(ServiceRequest req) {
        if(TypeUtil.isNull(req.getMethod())) req.setMethod(HttpDomainUtil.toMethod(req.getDomain()));
        if(TypeUtil.isNull(req.getRequestTime())) req.setRequestTime(System.currentTimeMillis());
        log.info("发送DUBBO请求:"+JsonUtil.toJson(req));
    }
    
    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequestNoReturn(java.lang.Object)
     */
    @Override
    public void sendRequestNoReturn(ServiceRequest req) {
        autoReq(req);
        getGenericService(req).$invokeAsync(req.getMethod(), createParameterTypes(), createArgs(req));
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequest(java.lang.Object, com.swift.core.api.rpc.ClientEngine.CallBackEngine)
     */
    @Override
    public void sendRequest(ServiceRequest req, CallBackEngine<ServiceResponse> callBack) {
        autoReq(req);
        CompletableFuture<Object> future = getGenericService(req).$invokeAsync(req.getMethod(), createParameterTypes(), createArgs(req));
        future.whenComplete(new BiConsumer<Object, Throwable>() {

            @Override
            public void accept(Object t, Throwable u) {
                if(t!=null) {
                    callBack.receiveResponse(formatServiceResponse(t));
                    return;
                }
                if(u!=null) {
                    callBack.failed(u);
                    return;
                }
            }
        });
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendAsynRequest(java.lang.Object)
     */
    @Override
    public Future<ServiceResponse> sendAsynRequest(ServiceRequest req) {
        autoReq(req);
        CompletableFuture<Object> future = getGenericService(req).$invokeAsync(req.getMethod(), createParameterTypes(), createArgs(req));
        return new ChangeFuture<Object, ServiceResponse>(future, new FutureValueChange<Object, ServiceResponse>() {

            @Override
            public ServiceResponse toChange(Object t) {
                return formatServiceResponse(t);
            }
            
        });
        
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequest(java.lang.Object)
     */
    @Override
    public ServiceResponse sendRequest(ServiceRequest req) {
        autoReq(req);
        CompletableFuture<Object> future = getGenericService(req).$invokeAsync(req.getMethod(), createParameterTypes(), createArgs(req));
        try {
            return formatServiceResponse(future.get());
        } catch (Exception e) {
            throw new SystemException(JsonUtil.toJson(req)+";异常",e);
        }
    }

}
