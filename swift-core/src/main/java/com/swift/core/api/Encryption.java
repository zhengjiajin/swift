/*
 * @(#)Encryption.java   1.0  2020年4月16日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.swift.core.auth.AuthKeyInterface;
import com.swift.core.auth.impl.DefaultAuthKeyInterface;

/**
 * 加密标签
 * ${}格式优先级->.properties->-Arguments->环境变量 
 * @author zhengjiajin
 * @version 1.0 2020年4月16日
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Encryption {
    /*
     * 签名密钥
     * 支持${}格式,
     */
    String value();
    
    /*
     * 获取KEY值的方法
     * implements AuthKeyInterface
     */
    Class<? extends AuthKeyInterface> getKey() default DefaultAuthKeyInterface.class;
}
