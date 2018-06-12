/*
 * @(#)SessionHandler.java   1.0  2016年2月1日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.jetty.handler;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.session.Session;
import org.eclipse.jetty.server.session.SessionData;
import org.eclipse.jetty.server.session.SessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import com.swift.core.session.AbstractSession;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2016年2月1日
 */
@Service
@Order(0)
public class WebSessionHandler extends SessionHandler implements WebHandler {

    private final static Logger log = LoggerFactory.getLogger(WebSessionHandler.class);
    
    private final static long maxInactiveMs = 60*60*1000;
    
    @PostConstruct
    public void init() {
        this.setMaxInactiveInterval(1800);
    }

    @Override
    public void doScope(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        super.doScope(target, baseRequest, request, response);
        //此处用于自身控制SESSION，并解决HTTPS时SESSION出错问题
        if(baseRequest.getSession(false)==null){
            long createTime = System.currentTimeMillis();
            String sessionId = this.getServer().getSessionIdManager().newSessionId(request, createTime);
            SessionData sessiondata= this.getSessionCache().getSessionDataStore().newSessionData(sessionId, createTime, createTime, createTime, maxInactiveMs);
            Session session = new Session(this, request, sessiondata);
            session.setResident(true);
            baseRequest.setSession(session);
        }
        log.info("doScope收到SESSION:" + target + ";" + baseRequest.getSession().getAttribute(AbstractSession.SESSION_NAME));
    }

    /**
     * @see com.WebHandler.hospital.server.core.handler.HandlerFilter#isThisHandler(java.lang.String,
     *      org.eclipse.jetty.server.Request)
     */
    @Override
    public boolean isThisHandler(String target, HttpServletRequest httpRequest) {
        return true;
    }

    /**
     * @see com.WebHandler.hospital.server.core.handler.HandlerFilter#isPutHandler()
     */
    @Override
    public boolean isPutHandler() {
        log.info(getClass().getName()+"启动");
        return true;
    }

    /** 
     * @see com.WebHandler.hospital.server.core.handler.HandlerFilter#port()
     */
    @Override
    public int port() {
        return 0;
    }

    /** 
     * NULL的话所有都要通过
     * @see com.WebHandler.hospital.server.core.handler.HandlerFilter#getContextPath()
     */
    @Override
    public String getContextPath() {
        return null;
    }

}
