/*
 * @(#)HandlerFilter.java   1.0  2016年2月1日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.jetty.handler;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Handler;

/**
 * Handler的过滤接口 
 * @author zhengjiajin
 * @version 1.0 2016年2月1日
 */
public interface WebHandler extends Handler{
    /**
     * 判断是否使用本类处理
     * @param target
     * @param rawHttpRequest
     * @return
     */
    public boolean isThisHandler(String target, HttpServletRequest httpRequest); 
    /**
     * 启动时是否加载此控制
     * @return
     */
    public boolean isPutHandler();
    /**
     * 端口
     * @return
     */
    public int port();
    /**
     * 工程名称
     * @return
     */
    public String getContextPath();
}
