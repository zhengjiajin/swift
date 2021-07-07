/*
 * @(#)LogIpConfig.java   1.0  2021年6月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.log.converter;

import com.swift.util.type.IpUtil;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年6月28日
 */
public class LogIp  extends ClassicConverter {

    private static String IP = IpUtil.getHostAddress(); 
    /** 
     * @see ch.qos.logback.core.pattern.Converter#convert(java.lang.Object)
     */
    @Override
    public String convert(ILoggingEvent event) {
        return IP;
    }

}
