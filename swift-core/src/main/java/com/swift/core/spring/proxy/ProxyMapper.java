/*
 * @(#)ProxyMapper.java   1.0   2020年4月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.spring.proxy;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 动态代理实现注解 
 * @author zhengjiajin
 * @version 1.0  2020年4月15日
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ProxyMapper {

    /**
     * 动态代理类，用于返回接口处理的动态代理类
     * @return
     */
    public Class<? extends AbstractSwiftInvocation> value();
}
