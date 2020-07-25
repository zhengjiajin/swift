/**
 * @(#)AbstractMessageDeliver.java 0.0.1-SNAPSHOT Mar 14, 2016 8:45:21 AM 
 *  
 * Copyright (c) 2015-2016 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.core.service.processor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.core.filter.RequestFilter;
import com.swift.core.filter.ResponseFilter;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.service.processor.send.MessageSender;
import com.swift.core.spring.Spring;
import com.swift.core.thread.ServerSendControl;
import com.swift.core.thread.ThreadPoolFactory;

/**
 * 抽象的消息处理器。提供消息分发过程中的公共方法。
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date Mar 14, 2016 8:45:21 AM
 */
public abstract class AbstractMessageDeliver implements MessageDeliver {

	private static final Logger log = LoggerFactory.getLogger(AbstractMessageDeliver.class);

    @Autowired(required=false)
    private List<RequestFilter> requestFilterList;
    @Autowired(required=false)
    private List<ResponseFilter> responseFilterList;

    @Autowired
    protected ThreadPoolFactory threadPool;

	public abstract MessageSender getMessageSender();

	/**
	 * @see com.hhmk.hospital.server.core.processor.MessageDeliver#requestReceived(com.hhmk.hospital.server.common.model.server.ServerRequest)
	 */
	@Override
	public void requestReceived(ServiceRequest request) {
	    forRequestFilter(request);
        CallBackService callback = new CallBackService() {
            @Override
            public void callback(ServiceResponse res) {
                try {
                    res.setRequest(request);
                    sendResponse(res);
                } catch (Throwable ex) {
                    log.error("Error send response: {}", res, ex);
                }
            }
        };
        ServerSendControl serverSendControl = Spring.getApplicationContext().getAutowireCapableBeanFactory().createBean(ServerSendControl.class);
        serverSendControl.setCallback(callback);
        serverSendControl.setReq(request);
        threadPool.execute(serverSendControl);
	}

	/**
	 * @see com.hhmk.hospital.server.core.processor.MessageDeliver#sendResponse(com.hhmk.hospital.server.common.model.server.ServerResponse)
	 */
	@Override
	public void sendResponse(ServiceResponse response) {
	    forResponseFilter(response);
	    getMessageSender().sendResponse(response);
	}


    private void forRequestFilter(ServiceRequest req) {
        if(requestFilterList==null) return;
        for(RequestFilter filter:requestFilterList) {
            filter.doFilter(req);
        }
    }

    private void forResponseFilter(ServiceResponse res ) {
        if(responseFilterList==null) return;
        for(ResponseFilter filter:responseFilterList) {
            try {
                filter.doFilter(res);
            }catch(Throwable ex) {
                log.error("forResponseFilter:异常",ex);
            }
        }
    }
}
