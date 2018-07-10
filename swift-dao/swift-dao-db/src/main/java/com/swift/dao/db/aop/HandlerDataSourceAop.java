/*
 * @(#)HandlerDataSourceAop.java   1.0  2018年5月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.swift.dao.db.annontation.AutoDb;
import com.swift.dao.db.datasource.HandlerDataSource;
import com.swift.util.bean.AnnotationUtil;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年5月5日
 */
@Aspect
@Component
@Order(1)
public class HandlerDataSourceAop {
    private static final Logger log = LoggerFactory.getLogger(HandlerDataSourceAop.class);

    public static final String FILTER_STR = "@within(com.swift.dao.db.annontation.AutoDb) "
        + " || @annotation(com.swift.dao.db.annontation.AutoDb) "
        // + " || @target(com.hhmk.hospital.common.dao.annontation.AutoDb) " //父类方法
        // + " || execution(* com.hhmk.hospital.common.dao.impl.BaseDaoImpl.*(..)) "
        + " || target(com.swift.dao.db.annontation.AutoDbInterface) " + "";

    public HandlerDataSourceAop() {
    }

    @Pointcut(HandlerDataSourceAop.FILTER_STR)
    public void pointcut() {
    }

    @Before("pointcut()")
    public void doBefore(JoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        Class<?> cla = joinPoint.getTarget().getClass();
        AutoDb autoDb = AnnotationUtil.getAnnotation(cla, method, AutoDb.class);
        if (autoDb != null) {
            String dataSource = autoDb.dbName();
            if(TypeUtil.isNull(dataSource)) {
                dataSource=autoDb.value().getDbName();
            }
            if (dataSource != null) HandlerDataSource.putDataSource(dataSource);
            log.info("添加动态切换数据源，className" + cla.getName() + "methodName" + method.getName() + ";dataSourceKey:"
                + dataSource);
        }
    }

    @After("pointcut()")
    public void after(JoinPoint point) {
        if (HandlerDataSource.getDataSource() == null) return;
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        // 清理掉当前设置的数据源，让默认的数据源不受影响
        log.info("清除动态切换数据源，className" + point.getTarget().getClass().getName() + "methodName" + method.getName()
            + ";dataSourceKey:" + HandlerDataSource.getDataSource());
        HandlerDataSource.clear();
    }
    
}
