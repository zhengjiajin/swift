/*
 * @(#)ParSendControl.java   1.0  2014-5-16
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.core.service.CallBackService;
import com.swift.core.service.ReqInterfaceFactory;
import com.swift.exception.ServiceException;

/**
 * 发送消息线程
 * 
 * @author jiajin
 * @version 1.0 2014-5-16
 */
public class ServerSendControl implements ThreadPoolInterface {

    private final static Logger log = LoggerFactory.getLogger(ServerSendControl.class);

    private ServiceRequest req;

    private CallBackService callback;

    /**
     * 优先级权值ms秒
     */
    // private final int levelWeight = 50;

    public ServerSendControl(CallBackService callback, ServiceRequest req) {
        if (req.getRequestTime() <= 0) req.setRequestTime(System.currentTimeMillis());
        this.req = req;
        this.callback = callback;
    }

    /**
     * @return the req
     */
    public ServiceRequest getReq() {
        return req;
    }

    /**
     * @see java.util.concurrent.Callable#call()
     */
    @Override
    public void run() {
        log.info("准备处理HTTP请求："+req.getUuid()+";"+req.toString());
        Thread.currentThread().setName(req.getMethod()); 
        ServiceResponse res = new ServiceResponse();
        res.setRequest(req);
        try {
            DataModel obj = ReqInterfaceFactory.getInterface(req.getMethod(), req.getInterfaceVersion()).doService(req);
            if (obj == null) obj = new MapDataModel();
            res.setData(obj);
            res.setResultCode(0);
            res.setReason("");
        } catch (ServiceException ex1) {
            log.error("业务异常", ex1);
            res.setResultCode(ex1.getStatusCode());
            res.setReason(ex1.getMessage());
        } catch (Throwable  ex) {
            log.error("系统异常", ex);
            res.setResultCode(-1);
            res.setReason("系统好像暂时不给力，请稍后再试。");
        }
        res.setResponseTime(System.currentTimeMillis());
        try {
            long stTime = res.getResponseTime()-req.getRequestTime();
            String msg = req.getUuid()+";"+req.getMethod()+";占用时间:"+stTime;
            log.info("处理HTTP请求完毕："+msg);
            if(stTime>5000) {
                log.warn("处理请求时间过长:"+msg);
            }
            this.callback.callback(res);
        } catch (Exception ex) {
            log.error("发送响应失败", ex);
        }
    }

    /**
     * @see com.zxh.paradise.server.excecutor.ThreadPoolInterface#getCompareTo()
     */
    @Override
    public long getCompareTo() {
        return 0;
        // 没做权重优先级先
        // return ReqInterfaceFactory.getLevel(req.getMethod(), req.getInterfaceVersion())+req.getTime()/levelWeight;
    }

}
