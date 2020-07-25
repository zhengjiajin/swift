/*
 * @(#)ParameterException.java   1.0  2020年7月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.exception.extend;

import com.swift.exception.NoWarnException;
import com.swift.exception.ResultCode;

/**
 * 参数相关异常 
 * @author zhengjiajin
 * @version 1.0 2020年7月2日
 */
public class ParameterException extends NoWarnException {
    
    private static final long serialVersionUID = 4093680913895667653L;

    public ParameterException(String message) {
        super(ResultCode.ERROR_PARAMETER,message);
    }
}
