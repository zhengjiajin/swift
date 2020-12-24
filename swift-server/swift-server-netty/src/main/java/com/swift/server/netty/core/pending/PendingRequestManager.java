/**
 * @(#)PendingRequestManager.java 2020年12月22日
 *  
 * Copyright (c) 2015-2020 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.server.netty.core.pending;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.core.api.rpc.ClientEngine.CallBackEngine;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.exception.ResultCode;
import com.swift.exception.extend.SystemException;
import com.swift.exception.extend.TimeoutException;
import com.swift.server.netty.ServerConfig;
import com.swift.server.netty.message.SCA;
import com.swift.server.netty.message.SCR;
import com.swift.server.netty.message.handler.MessageHandlerContext;
import com.swift.util.exec.ThreadUtil;
import com.swift.util.type.TypeUtil;

/**
 * 
 * 消息超时管理
 * 
 * @author zhengjiajin
 * @version 1.0 2020年12月22日
 */
@Component
public class PendingRequestManager implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(PendingRequestManager.class);

    private Map<String, PendingRequest> pendingMap = new ConcurrentHashMap<>();

    // 超时时间
    @Autowired
    private ServerConfig serverConfig;

    private AtomicBoolean isTerminated = new AtomicBoolean(false);
    private Thread t;

    @PostConstruct
    public void init() {
        t = new Thread(this);
        t.setName(PendingRequestManager.class.getSimpleName());
        t.setDaemon(true);
        t.start();
    }

    @PreDestroy
    public void destroy() {
        isTerminated.set(true);
        if (t != null) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }
    }

    public void register(SCR scr, MessageHandlerContext messageHandlerContext) {
        if (scr == null) return;
        if (TypeUtil.isNull(scr.getReqId())) {
            throw new SystemException("请求ID必须设置");
        }
        PendingRequest pr = new PendingRequest(scr,messageHandlerContext);
        pendingMap.put(scr.getReqId(), pr);
    }
    
    public void register(ServiceRequest request,CallBackEngine<ServiceResponse> callBack) {
        if (request == null) return;
        if (TypeUtil.isNull(request.getReqId())) {
            throw new SystemException("请求ID必须设置");
        }
        PendingRequest pr = new PendingRequest(request,callBack);
        pendingMap.put(request.getReqId(), pr);
    }

    public PendingRequest deregister(String requestId) {
        if (StringUtils.isBlank(requestId)) {
            return null;
        }
        PendingRequest pendingRequest = pendingMap.remove(requestId);
        return pendingRequest;
    }

    @Override
    public void run() {
        while (!isTerminated.get()) {
            try {
                for(Entry<String, PendingRequest> entry: pendingMap.entrySet()) {
                    if (entry.getValue() == null) continue;
                    if (System.currentTimeMillis() - entry.getValue().getTimestamp() < serverConfig.getServiceReadTimeout()*1000) {
                        continue;
                    }
                    PendingRequest pendingRequest = deregister(entry.getKey());
                    if (pendingRequest == null) continue;
                    if(pendingRequest.getRequest()!=null) {
                        if(pendingRequest.getCallBack()==null) continue;
                        pendingRequest.getCallBack().failed(new TimeoutException(entry.getKey()+"请求超时"));
                        continue;
                    }
                    if(pendingRequest.getScr()!=null) {
                        if(pendingRequest.getMessageHandlerContext()==null) continue;
                        SCA sca = new SCA(pendingRequest.getScr());
                        sca.setReqId(entry.getKey());
                        sca.setResultCode(ResultCode.TIMEOUT);
                        sca.setReason(entry.getKey()+"请求超时");
                        sca.setResponseTime(System.currentTimeMillis());
                        pendingRequest.getMessageHandlerContext().write(sca);
                        continue;
                    }
                }
            } catch (Throwable ex) {
                logger.error("Unexpected exception: ", ex);
            } finally {
                ThreadUtil.sleep(1000);
            }
        }
    }

}
