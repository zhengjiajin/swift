/*
 * @(#)AopTargetUtils.java   1.0  2015年9月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.util.bean;

import java.lang.reflect.Field;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import com.swift.exception.SwiftRuntimeException;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2015年9月5日
 */
public class AopTargetUtils {
    /**
     * 获取 目标对象
     * 
     * @param proxy
     *            代理对象
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) throws SwiftRuntimeException {
        try {
            if (!AopUtils.isAopProxy(proxy)) {
                return proxy;// 不是代理对象
            }
            if (AopUtils.isJdkDynamicProxy(proxy)) {
                return getJdkDynamicProxyTargetObject(proxy);
            } else { // cglib
                return getCglibProxyTargetObject(proxy);
            }
        } catch (Exception ex) {
            throw new SwiftRuntimeException(ex);
        }
    }

    private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
        h.setAccessible(true);
        Object dynamicAdvisedInterceptor = h.get(proxy);

        Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

        return target;
    }

    private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
        Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
        h.setAccessible(true);
        AopProxy aopProxy = (AopProxy) h.get(proxy);

        Field advised = aopProxy.getClass().getDeclaredField("advised");
        advised.setAccessible(true);

        Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();

        return target;
    }

}
