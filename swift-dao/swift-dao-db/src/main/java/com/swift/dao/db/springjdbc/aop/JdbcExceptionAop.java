/*
 * @(#)JdbcExceptionAop.java   1.0  2019年1月7日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.springjdbc.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2019年1月7日
 */
@Aspect
@Component
@Order
public class JdbcExceptionAop {
    
    public static final String FILTER_STR = "target(com.swift.dao.db.springjdbc.Jdbc)";
    
    @Around(JdbcExceptionAop.FILTER_STR)
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        try {
            return pjp.proceed(pjp.getArgs());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
