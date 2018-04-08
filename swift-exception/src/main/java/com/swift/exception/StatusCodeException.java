/*
 * @(#)StatusCodeException.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.exception;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class StatusCodeException extends SwiftRuntimeException {
    
    private static final long serialVersionUID = -6602097234016394961L;
    
    private int statusCode;

    public StatusCodeException(int statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
