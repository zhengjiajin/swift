/*
 * @(#)AbstractWebHandlerCode.java   1.0  2016年1月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.jetty.protocol;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.core.server.handle.AbstractHandlerCode;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2016年1月15日
 */
public abstract class AbstractWebHandlerCode extends AbstractHandlerCode implements WebHandlerCode{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractWebHandlerCode.class);
    public String getIp(HttpServletRequest httpRequest){
        String ip = httpRequest.getHeader("X-Forwarded-For");
        LOGGER.info("WEB取得的IP："+ip);
        if(TypeUtil.isNull(ip)){
            ip=httpRequest.getRemoteAddr();
            LOGGER.info("过来的取得的IP："+ip);
        }else{
            if(ip.indexOf(",")>0){
                ip=ip.substring(0, ip.indexOf(","));
                LOGGER.info("处理后IP："+ip);
            }
        }
        return ip;
    }
}
