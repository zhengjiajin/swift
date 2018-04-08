/*
 * @(#)UnknownException.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.exception;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class UnknownException extends SwiftRuntimeException {
    
    private static final long serialVersionUID = -3616397576450593769L;

    public UnknownException() {
        super("未知异常.");
    }

    public UnknownException(String message) {
        super(message);
    }
}
