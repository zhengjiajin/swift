/*
 * @(#)AuthException.java   1.0  2020年7月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.exception.extend;

import com.swift.exception.NoWarnException;
import com.swift.exception.ResultCode;

/**
 * 权限校验相关异常 
 * @author zhengjiajin
 * @version 1.0 2020年7月2日
 */
public class AuthException extends NoWarnException {
    
    private static final long serialVersionUID = -3648704284596376513L;

    public AuthException(String message) {
        super(ResultCode.AUTH_ERROR,message);
    }
}
