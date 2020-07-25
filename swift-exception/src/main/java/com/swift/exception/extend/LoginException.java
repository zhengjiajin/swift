/*
 * @(#)LoginException.java   1.0  2020年7月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.exception.extend;

import com.swift.exception.NoWarnException;
import com.swift.exception.ResultCode;

/**
 * 登录秘未登录相关的异常 
 * @author zhengjiajin
 * @version 1.0 2020年7月2日
 */
public class LoginException extends NoWarnException {
    
    private static final long serialVersionUID = -8235336369932553782L;

    public LoginException(String message) {
        super(ResultCode.NO_LOGIN,message);
    }
}
