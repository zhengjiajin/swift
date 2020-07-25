/*
 * @(#)NoWarnException.java   1.0  2019年7月10日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.exception;

/**
 * 不打印错误异常,用于输出WARN类型的异常日志,不需要进行告警操作的业务异常
 * 但会返回调用端,不需要输出异常 
 * @author zhengjiajin
 * @version 1.0 2019年7月10日
 */
public class NoWarnException extends ServiceException {

    private static final long serialVersionUID = -3969839577226169195L;

    public NoWarnException(int statusCode) {
        super(statusCode);
    }
    
    public NoWarnException(int statusCode, String msg) {
        super(statusCode, msg);
    }
}
