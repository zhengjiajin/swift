/*
 * @(#)ParSendControl.java   1.0  2014-5-16
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.core.filter.EndFilter;
import com.swift.core.filter.InitFilter;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.core.service.AsynInterface;
import com.swift.core.service.BaseInterface;
import com.swift.core.service.SynInterface;
import com.swift.core.service.processor.CallBackService;
import com.swift.core.service.processor.ReqInterfaceFactory;
import com.swift.exception.NoWarnException;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;
import com.swift.util.type.JsonUtil;

/**
 * 发送消息线程
 * 
 * @author jiajin
 * @version 1.0 2014-5-16
 */
public class ServerSendControl implements Runnable {

    private final static Logger log = LoggerFactory.getLogger(ServerSendControl.class);

    private ServiceRequest req;

    private CallBackService callback;

    @Autowired(required=false)
    private List<InitFilter> initFilterList;
    @Autowired(required=false)
    private List<EndFilter> endFilterList;

    /**
     * 优先级权值ms秒
     */
    // private final int levelWeight = 50;
   
    public CallBackService getCallback() {
        return callback;
    }

    public void setCallback(CallBackService callback) {
        this.callback = callback;
    }

    public void setReq(ServiceRequest req) {
        this.req = req;
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
        log.info("准备处理请求：" + req.getReqId() + ";" + req.toString());
        Thread.currentThread().setName(req.getMethod());
        ServiceResponse res = new ServiceResponse();
        res.setRequest(req);
        BaseInterface baseInterface = ReqInterfaceFactory.getInterface(req.getMethod(), req.getInterfaceVersion());
        try {
            // 线程开始
            forInitFilter();
            if(baseInterface instanceof SynInterface) {
                DataModel obj = ((SynInterface)baseInterface).doService(req);
                if (obj == null) obj = new MapDataModel();
                res.setData(obj);
                res.setResultCode(ResultCode.SUCCESS);
                res.setReason("");
            }else if(baseInterface instanceof AsynInterface) {
                ((AsynInterface) baseInterface).doService(req);
            }
        } catch (NoWarnException nowarnex) {
            log.warn("返回异常业务", nowarnex);
            res.setResultCode(nowarnex.getStatusCode());
            res.setReason(nowarnex.getMessage());
        } catch (ServiceException ex) {
            log.error("业务异常", ex);
            res.setResultCode(ex.getStatusCode());
            res.setReason(ex.getMessage());
        } catch (Throwable ex) {
            log.error("系统异常", ex);
            res.setResultCode(ResultCode.UNKNOWN);
            res.setReason("系统好像暂时不给力，请稍后再试。");
        }
        res.setResponseTime(System.currentTimeMillis());
        try {
            long stTime = res.getResponseTime() - req.getRequestTime();
            String msg = req.getReqId() + ";" + req.getMethod() + ";占用时间:" + stTime;
            log.info("处理请求完毕：" + msg);
            if (stTime > 5000) {
                log.warn("处理请求时间过长:" + msg);
            }
            log.info("返回响应："+baseInterface.getClass().getName()+JsonUtil.toJson(res));
            if(!(baseInterface instanceof AsynInterface) || res.getResultCode()!=0) {
                this.callback.callback(res);
            }
            // 线程结束
            forEndFilter();
        } catch (Throwable ex) {
            log.error("处理异常", ex);
        }
    }

    private void forInitFilter() {
        if(initFilterList==null) return;
        for(InitFilter filter:initFilterList) {
            filter.init();
        }
    }

    private void forEndFilter() {
        if(endFilterList==null) return;
        for(EndFilter filter:endFilterList) {
            try {
                filter.end();
            }catch(Throwable ex) {
                log.error("forEndFilter:异常",ex);
            }
        }
    }

}
