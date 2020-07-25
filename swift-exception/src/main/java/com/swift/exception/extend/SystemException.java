/*
 * @(#)SwiftRuntimeException.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.exception.extend;

import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;

/**
 * 系统错误异常，通用用户框架类的系统错误,如工具类，数据转换等
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class SystemException extends ServiceException {

    private static final long serialVersionUID = -501185588461465595L;
    
    public SystemException(String message) {
        super(ResultCode.SYS_ERROR,message);
    }

    public SystemException(Throwable cause) {
        super(ResultCode.SYS_ERROR,cause.getMessage(), cause);
    }

    public SystemException(String msg, Throwable cause) {
        super(ResultCode.SYS_ERROR,msg, cause);
    }
}
