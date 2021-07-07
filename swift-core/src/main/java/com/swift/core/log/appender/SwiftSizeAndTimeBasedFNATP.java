/*
 * @(#)SwiftSizeAndTimeBasedFNATP.java   1.0  2021年6月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.log.appender;

import java.io.File;

import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年6月29日
 */
public class SwiftSizeAndTimeBasedFNATP<E> extends SizeAndTimeBasedFNATP<E>{
    @Override
    public boolean isTriggeringEvent(File activeFile, final E event) {

        return super.isTriggeringEvent(activeFile, event);
    }
}
