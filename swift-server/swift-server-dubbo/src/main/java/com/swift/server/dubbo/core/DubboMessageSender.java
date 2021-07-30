/*
 * @(#)DubboMessageSender.java   1.0  2021年7月14日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.dubbo.core;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.service.processor.send.MessageSender;
import com.swift.exception.NoWarnException;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;
import com.swift.util.exec.ThreadUtil;
import com.swift.util.math.RandomUtil;
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2021年7月14日
 */
@Component
public class DubboMessageSender implements MessageSender, GenericService {

    private static Logger log = LoggerFactory.getLogger(DubboMessageSender.class);

    @Autowired
    private DubboMessageDeliver dubboMessageDeliver;

    private static final int time_out = 15 * 1000;

    private Map<String, TimeFuture> requestMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        new CleanTimeOutFuture().start();
    }

    /**
     * @see com.swift.core.service.processor.send.MessageSender#sendResponse(com.swift.core.model.ServiceResponse)
     */
    @Override
    public void sendResponse(ServiceResponse response) {
        TimeFuture timeFuture = requestMap.remove(response.getRequest().getReqId());
        log.info("返回DUBBO响应:"+timeFuture+";"+JsonUtil.toJson(response));
        if (timeFuture == null) return;
        timeFuture.complete(response);
    }

    /**
     * @see org.apache.dubbo.rpc.service.GenericService#$invoke(java.lang.String, java.lang.String[],
     *      java.lang.Object[])
     */
    @Override
    public Object $invoke(String method, String[] parameterTypes, Object[] args) throws GenericException {
        if (TypeUtil.isNull(parameterTypes)) return null;
        if (TypeUtil.isNull(args)) return null;
        log.info("收到DUBBO服务请求:"+args[0].toString());
        ServiceRequest req = formatServiceRequest(args[0]);
        if (TypeUtil.isNull(req.getReqId())) req.setReqId(RandomUtil.createReqId());
        try {
            TimeFuture future = new TimeFuture(System.currentTimeMillis(), req);
            requestMap.put(req.getReqId(), future);
            dubboMessageDeliver.requestReceived(req);
            return future;
        } catch (NoWarnException ex) {
            log.warn("Neglectable Exception", ex);
            return createExceptionResponse(req, ex);
        } catch (ServiceException ex) {
            log.error("Service Exception", ex);
            return createExceptionResponse(req, ex);
        } catch (Throwable ex) {
            log.error("Http请求处理失败", ex);
            return createExceptionResponse(req, ex);
        }
    }

    private ServiceResponse createExceptionResponse(ServiceRequest req, Throwable ex) {
        ServiceResponse response = new ServiceResponse();
        response.setRequest(req);
        if (ex instanceof ServiceException) {
            response.setReason(ex.getMessage());
            response.setResultCode(((ServiceException) ex).getStatusCode());
        } else {
            response.setReason(ex.getMessage());
            response.setResultCode(ResultCode.SYS_ERROR);
        }
        response.setResponseTime(System.currentTimeMillis());
        return response;
    }

    private ServiceRequest formatServiceRequest(Object arg) {
        if (arg instanceof ServiceRequest) return (ServiceRequest) arg;
        if (arg instanceof String) return JsonUtil.toObj(String.valueOf(arg), ServiceRequest.class);
        if (arg instanceof JSON) {
            JSON json = (JSON) arg;
            return JsonUtil.toObj(json.toJSONString(), ServiceRequest.class);
        }
        return null;
    }

    private class CleanTimeOutFuture extends Thread {

        public void run() {
            while (true) {
                try {
                    for (String reqId : requestMap.keySet()) {
                        TimeFuture future = requestMap.get(reqId);
                        if (future == null) continue;
                        if (System.currentTimeMillis() - future.requestTime >= time_out) {
                            future = requestMap.remove(reqId);
                            ServiceResponse response = new ServiceResponse();
                            response.setRequest(future.request);
                            response.setReason("TIMEOUT");
                            response.setResultCode(ResultCode.TIMEOUT);
                            response.setResponseTime(System.currentTimeMillis());
                            future.complete(response);
                        }
                    }
                    ThreadUtil.sleep(300);
                } catch (Throwable ex) {
                    log.error("系统异常:", ex);
                }
            }
        }
    }

    public class TimeFuture extends CompletableFuture<ServiceResponse> {

        private long requestTime;

        private ServiceRequest request;

        private TimeFuture(long requestTime, ServiceRequest request) {
            this.requestTime = requestTime;
            this.request = request;
        }

        /**
         * @return the requestTime
         */
        public long getRequestTime() {
            return requestTime;
        }

        /**
         * @param requestTime the requestTime to set
         */
        public void setRequestTime(long requestTime) {
            this.requestTime = requestTime;
        }

        /**
         * @return the request
         */
        public ServiceRequest getRequest() {
            return request;
        }

        /**
         * @param request the request to set
         */
        public void setRequest(ServiceRequest request) {
            this.request = request;
        }
        
    }

}
