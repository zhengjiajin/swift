/*
 * @(#)OpenApi.java   1.0  2020年4月16日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 对端接口描述 
 * @author zhengjiajin
 * @version 1.0 2020年4月16日
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OpenApi {
    /*
     * toSysId 简化配置，此值为toSysId,不同时优先使用toSysId
     * 可以是toSysId.method配置方式，如udb.getUserInfo
     */
    String value() default "";
    /*
     * 访问对端系统的能力方法
     * 默认为类名,首字母小写
     */
    String method() default "";
    /*
     * 参入参数字段描述,如userId=1,则填写userId
     * 技持xxx.xxx.xxx
     * 不填写输入参数必须为DataModel类型,否则异常
     */
    String reqParam() default "";
    /*
     * 返回参数取值类型，支持xxx.xxx.xx格式
     * 不传则返回值必须是DataModel类型,否则异常
     */
    String resParam() default "";
    /*
     * 是否需要签名
     */
    Sign[] sign() default {};
    /*
     * 是否需要加密
     */
    Encryption[] encryption() default {};
}
