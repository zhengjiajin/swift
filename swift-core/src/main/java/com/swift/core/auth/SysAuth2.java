/*
 * @(#)SysAuth.java   1.0  2020年12月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.auth;

/**
 * 通过AUTH2验证
 * @author zhengjiajin
 * @version 1.0 2020年12月29日
 */
public interface SysAuth2 {

    //返回系统ID
    public String decrypt(String accessToken);
}
