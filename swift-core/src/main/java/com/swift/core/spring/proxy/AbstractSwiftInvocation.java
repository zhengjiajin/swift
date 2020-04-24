/*
 * @(#)AbstractSwiftInvocation.java   1.0   2020年4月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 动态代理处理类必须继承此类 
 * @author zhengjiajin
 * @version 1.0  2020年4月15日
 */
public abstract class AbstractSwiftInvocation implements InvocationHandler {

    /** 
     * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this,args);
        }
        return invokeInterface(proxy, method, args);
    }
    
    public abstract Object invokeInterface(Object proxy, Method method, Object[] args) throws Throwable;
}
