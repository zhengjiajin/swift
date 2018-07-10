/*
 * @(#)MybatisMapping.java   1.0  2018年6月4日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.db.mybatis;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月4日
 */
@Target(value= {ElementType.TYPE})
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface MybatisMapper {

}
