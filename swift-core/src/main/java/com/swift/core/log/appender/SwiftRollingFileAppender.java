/*
 * @(#)SwiftRollingFileAppender.java   1.0  2021年6月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.log.appender;

import ch.qos.logback.core.rolling.RollingFileAppender;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2021年6月29日
 */
public class SwiftRollingFileAppender<E> extends RollingFileAppender<E>{

    @Override
    public void setFile(String file) {
        //处理file,使其支持pattern自定义参数
        super.setFile(file);
    }
    
    @Override
    public String getFile() {
        return super.getFile();
    }
    
    @Override
    protected void subAppend(E event) {
        
        super.subAppend(event);
    }
}
