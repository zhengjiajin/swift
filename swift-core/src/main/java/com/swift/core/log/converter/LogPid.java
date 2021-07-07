/*
 * @(#)LogPid.java   1.0  2021年6月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.log.converter;

import java.lang.management.ManagementFactory;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年6月28日
 */
public class LogPid extends ClassicConverter {

    private static String pidname = ManagementFactory.getRuntimeMXBean().getName();
    
    private static String pid = pidname.substring(0, pidname.indexOf("@"));

    /** 
     * @see ch.qos.logback.core.pattern.Converter#convert(java.lang.Object)
     */
    @Override
    public String convert(ILoggingEvent event) {
        return pid;
    }
}
