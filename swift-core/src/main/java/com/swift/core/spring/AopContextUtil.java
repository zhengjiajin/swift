/*
 * @(#)AopContextUtil.java   1.0  2018年6月28日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring;

import org.springframework.aop.framework.AopContext;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月28日
 */
public class AopContextUtil {
    
    public Object currentProxy(Object thisObj) {
        try {
            return AopContext.currentProxy();
        }catch (IllegalStateException e) {
            return thisObj;
        }
    }
}
