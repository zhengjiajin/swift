/*
 * @(#)ProtocolException.java   1.0  2020年7月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.exception.extend;

import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;

/**
 * 协议解释等相关异常 
 * @author zhengjiajin
 * @version 1.0 2020年7月2日
 */
public class ProtocolException extends ServiceException {

    private static final long serialVersionUID = 2037972321986108698L;

    public ProtocolException(String message) {
        super(ResultCode.PROTOCOL_ERROR,message);
    }

    public ProtocolException(String message, Throwable cause) {
        super(ResultCode.PROTOCOL_ERROR,message,cause);
    }
}
