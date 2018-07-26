/*
 * @(#)ResourceWebHandler.java   1.0  2018年7月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.server.handler;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.swift.server.jetty.handler.DefaultWebHandler;
import com.swift.server.jetty.handler.WebHandler;
import com.swift.server.jetty.util.WebContextPathUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年7月26日
 */
@Service
public class ResourceWebHandler extends ResourceHandler implements WebHandler{
private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebHandler.class);
    
    protected static final String CONTEXT_PATH = "/";
    
    public ResourceWebHandler() {
        String classpath = ResourceWebHandler.class.getResource("/").getPath();
        String webpath = classpath.substring(0, classpath.indexOf("/target"))+"/webapp";
        super.setDirectoriesListed(true);
        super.setResourceBase(webpath);
        super.setStylesheet("");
    }
    /**
     * 以/结尾的所有非其它工程的请求
     * @see com.WebHandler.hospital.server.core.handler.HandlerFilter#isThisHandler()
     */
    @Override
    public boolean isThisHandler(String target, HttpServletRequest httpRequest) {
        if(target.indexOf(".")==-1) return false;
        if (WebContextPathUtil.isContextPath(target, getContextPath())) {
            LOGGER.info("ResourceWebHandler收到请求地址：" + target);
            return true;
        }
        return false;
    }

    /** 
     * @see com.WebHandler.hospital.server.core.handler.HandlerFilter#getContextPath()
     */
    @Override
    public String getContextPath() {
        return CONTEXT_PATH;
    }
}
