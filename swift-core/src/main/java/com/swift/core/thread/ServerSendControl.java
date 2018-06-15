/*
 * @(#)ParSendControl.java   1.0  2014-5-16
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.core.thread;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.core.filter.EndFilter;
import com.swift.core.filter.InitFilter;
import com.swift.core.filter.RequestFilter;
import com.swift.core.filter.ResponseFilter;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.model.data.DataModel;
import com.swift.core.model.data.MapDataModel;
import com.swift.core.service.CallBackService;
import com.swift.core.service.ReqInterfaceFactory;
import com.swift.core.spring.Spring;
import com.swift.exception.ServiceException;
import com.swift.exception.SwiftRuntimeException;

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

    private static List<InitFilter> initFilterList;

    private static List<EndFilter> endFilterList;

    private static List<RequestFilter> requestFilterList;

    private static List<ResponseFilter> responseFilterList;

    /**
     * 优先级权值ms秒
     */
    // private final int levelWeight = 50;

    public ServerSendControl(CallBackService callback, ServiceRequest req) {
        if (req.getRequestTime() <= 0) req.setRequestTime(System.currentTimeMillis());
        this.req = req;
        this.callback = callback;
        initInitFilter();
        initEndFilter();
        initRequestFilter();
        initResponseFilter();
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
        log.info("准备处理HTTP请求：" + req.getUuid() + ";" + req.toString());
        Thread.currentThread().setName(req.getMethod());
        // 线程开始
        forInitFilter();
        forRequestFilter(req);
        ServiceResponse res = new ServiceResponse();
        res.setRequest(req);
        try {
            DataModel obj = ReqInterfaceFactory.getInterface(req.getMethod(), req.getInterfaceVersion()).doService(req);
            if (obj == null) obj = new MapDataModel();
            res.setData(obj);
            res.setResultCode(0);
            res.setReason("");
        } catch (ServiceException ex) {
            log.error("业务异常", ex);
            res.setResultCode(ex.getStatusCode());
            res.setReason(ex.getMessage());
        } catch (SwiftRuntimeException ex) {
            log.error("业务异常", ex);
            res.setResultCode(-1);
            res.setReason(ex.getMessage());
        } catch (Throwable ex) {
            log.error("系统异常", ex);
            res.setResultCode(-1);
            res.setReason("系统好像暂时不给力，请稍后再试。");
        }
        res.setResponseTime(System.currentTimeMillis());
        try {
            long stTime = res.getResponseTime() - req.getRequestTime();
            String msg = req.getUuid() + ";" + req.getMethod() + ";占用时间:" + stTime;
            log.info("处理HTTP请求完毕：" + msg);
            if (stTime > 5000) {
                log.warn("处理请求时间过长:" + msg);
            }
            this.callback.callback(res);
        } catch (Throwable ex) {
            log.error("发送响应失败", ex);
        }
        forResponseFilter(res);
        // 线程结束
        forEndFilter();
    }

    private void forInitFilter() {
        for(InitFilter filter:initFilterList) {
            filter.init();
        }
    }

    private void forEndFilter() {
        for(EndFilter filter:endFilterList) {
            try {
                filter.end();
            }catch(Throwable ex) {
                log.error("forEndFilter:异常",ex);
            }
        }
    }

    private void forRequestFilter(ServiceRequest req) {
        for(RequestFilter filter:requestFilterList) {
            filter.doFilter(req);
        }
    }

    private void forResponseFilter(ServiceResponse res ) {
        for(ResponseFilter filter:responseFilterList) {
            try {
                filter.doFilter(res);
            }catch(Throwable ex) {
                log.error("forResponseFilter:异常",ex);
            }
        }
    }
    

    private void initInitFilter() {
        if (initFilterList == null) {
            synchronized ("initFilterList") {
                if (initFilterList == null) {
                    initFilterList = new LinkedList<InitFilter>();
                    List<InitFilter> list = Spring.getBeans(InitFilter.class);
                    initFilterList.addAll(list);
                }
            }
        }
    }

    private void initEndFilter() {
        if (endFilterList == null) {
            synchronized ("endFilterList") {
                if (endFilterList == null) {
                    endFilterList = new LinkedList<EndFilter>();
                    List<EndFilter> list = Spring.getBeans(EndFilter.class);
                    endFilterList.addAll(list);
                }
            }
        }
    }

    private void initRequestFilter() {
        if (requestFilterList == null) {
            synchronized ("requestFilterList") {
                if (requestFilterList == null) {
                    requestFilterList = new LinkedList<RequestFilter>();
                    List<RequestFilter> list = Spring.getBeans(RequestFilter.class);
                    requestFilterList.addAll(list);
                }
            }
        }
    }

    private void initResponseFilter() {
        if (responseFilterList == null) {
            synchronized ("responseFilterList") {
                if (responseFilterList == null) {
                    responseFilterList = new LinkedList<ResponseFilter>();
                    List<ResponseFilter> list = Spring.getBeans(ResponseFilter.class);
                    responseFilterList.addAll(list);
                }
            }
        }
    }

}
