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
public interface SessionCrypt {
    /*
     * 加密得到用户加密验证串
     */
    public String encrypt(AbstractSession sessionUser);
    /*
     * 通过验证串得到用户身份
     */
    public AbstractSession decrypt(String sessionUser);
    /*
     * 通过授权码得到用户身份
     */
    public AbstractSession code(String code);
}
