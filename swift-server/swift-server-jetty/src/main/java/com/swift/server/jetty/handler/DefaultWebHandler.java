/**
 * @(#)DefaultWebHandler.java 0.0.1-SNAPSHOT Mar 14, 2016 10:11:22 AM 
 *  
 * Copyright (c) 2015-2016 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.server.jetty.handler;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.swift.server.jetty.util.WebContextPathUtil;

/**
 * TODO Purpose/description of class
 * 
 * @author Avery Xiao
 * @version 0.0.1-SNAPSHOT
 * @date Mar 14, 2016 10:11:22 AM
 */
@Service
public class DefaultWebHandler extends AbstractWebHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultWebHandler.class);
    
	protected static final String CONTEXT_PATH = "/";
	
	/**
	 * 以/结尾的所有非其它工程的请求
     * @see com.WebHandler.hospital.server.core.handler.HandlerFilter#isThisHandler()
     */
    @Override
    public boolean isThisHandler(String target, HttpServletRequest httpRequest) {
        if(target.indexOf(".")!=-1) return false;
        if (WebContextPathUtil.isContextPath(target, getContextPath())) {
            LOGGER.info("DefaultWebHandler收到请求地址：" + target);
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
