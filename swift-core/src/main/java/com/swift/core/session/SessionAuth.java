/*
 * @(#)SessionAuth.java   1.0  2020年12月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.session;

/**
 * 登录信息的权限验证,如TOKEN/一次性CODE等方式验证
 * @author zhengjiajin
 * @version 1.0 2020年12月29日
 */
public interface SessionAuth {

    /*
     * 通过验证串得到用户身份
     */
    public AbstractSession decrypt(String sessionUser);
   
}
