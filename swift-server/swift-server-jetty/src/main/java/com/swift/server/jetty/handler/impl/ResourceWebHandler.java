/*
 * @(#)ResourceWebHandler.java   1.0  2018年7月26日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.jetty.handler.impl;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.handler.ResourceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.swift.server.jetty.handler.WebHandler;
import com.swift.server.jetty.handler.impl.rest.ApplicationJsonHandler;
import com.swift.server.jetty.handler.impl.rest.WebContextPathUtil;
import com.swift.util.exec.PathUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年7月26日
 */
@Service
public class ResourceWebHandler extends ResourceHandler implements WebHandler{
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationJsonHandler.class);
    
    protected static final String CONTEXT_PATH = "/";
    
    public ResourceWebHandler() {
        String classpath = ResourceWebHandler.class.getResource("/").getPath();
        LOGGER.info("ResourceWebHandler.classpath__"+classpath);
        ///home/app/hhmk-xxx-test/etc/
        ///E:/workspace470/hhmk-crm/target/test-classes/
        String webpath = PathUtil.findOnClassPath("webapp");
        if(TypeUtil.isNull(webpath)) webpath= defWebapp(classpath);
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
    
    private String defWebapp(String classpath) {
        if(classpath.indexOf("/etc/")!=-1) {
            classpath = classpath.substring(0, classpath.indexOf("/etc/"));
        }
        if(classpath.indexOf("/target/")!=-1) {
            classpath = classpath.substring(0, classpath.indexOf("/target/"));       
        }
        if(classpath.indexOf("/lib/")!=-1) {
            classpath = classpath.substring(0, classpath.indexOf("/lib/"));
        }
        if(classpath.indexOf("/bin/")!=-1) {
            classpath = classpath.substring(0, classpath.indexOf("/bin/"));
        }
        return classpath+"/webapp";
    }
    
}
