/*
 * @(#)TimeoutException.java   1.0  2020年12月16日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.exception.extend;

import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年12月16日
 */
public class TimeoutException extends ServiceException{

    private static final long serialVersionUID = 2037972321986108698L;

    public TimeoutException(String message) {
        super(ResultCode.TIMEOUT,message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(ResultCode.TIMEOUT,message,cause);
    }
}
