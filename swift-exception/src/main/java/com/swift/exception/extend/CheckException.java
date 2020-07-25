/*
 * @(#)CheckException.java   1.0  2020年7月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.exception.extend;

import com.swift.exception.NoWarnException;
import com.swift.exception.ResultCode;

/**
 * 检测校验异常
 * 如验证码，加解密等 
 * @author zhengjiajin
 * @version 1.0 2020年7月2日
 */
public class CheckException extends NoWarnException {
    private static final long serialVersionUID = -2062441079517503856L;

    public CheckException(String message) {
        super(ResultCode.CHECK_ERROR,message);
    }
}
