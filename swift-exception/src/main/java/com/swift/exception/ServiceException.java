/*
 * @(#)ServiceException.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.exception;

/**
 * 转RuntimeException,但需要定义系统异常错误码
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class ServiceException extends RuntimeException {
    
    private static final long serialVersionUID = -6602097234016394961L;
    
    private int statusCode;

    public ServiceException(int statusCode) {
        super("");
        this.statusCode = statusCode;
    }
    
    public ServiceException(String msg) {
        super(msg);
        this.statusCode = ResultCode.UNKNOWN;
    }
    
    public ServiceException(int statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
    }

    public ServiceException(int statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
}
