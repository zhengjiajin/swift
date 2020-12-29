/*
 * @(#)SessionCrypt.java   1.0  2020年12月9日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.session;

/**
 * 对SESSION用户信息的加解密控制 
 * @author zhengjiajin
 * @version 1.0 2020年12月9日
 */
public interface SessionCrypt extends SessionAuth {
    /*
     * 加密得到用户加密验证串
     */
    public String encrypt(AbstractSession sessionUser);
    
}
