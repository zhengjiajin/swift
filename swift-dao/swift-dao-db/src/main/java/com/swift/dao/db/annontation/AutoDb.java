/*
 * @(#)AutoDb.java   1.0  2018年5月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.annontation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.swift.dao.db.datasource.DataSource;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年5月5日
 */
@Target(value= {ElementType.TYPE,ElementType.METHOD})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoDb {
    /**
     * 系统定义方式
     * @return
     */
    public DataSource value() default DataSource.master;
    /**
     * 自定义方式,此方法优先
     * @return
     */
    public String dbName() default "";
    
}
