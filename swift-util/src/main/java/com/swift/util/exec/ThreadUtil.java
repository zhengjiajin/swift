/*
 * @(#)ThreadUtil.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.util.exec;

import com.swift.exception.extend.SystemException;

/**
 * 线程处理类库
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class ThreadUtil {
    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ex) {
            new SystemException("系统忙",ex);
        }
    }
}
