/*
 * @(#)Auth2Crypt.java   1.0  2020年12月22日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.auth;

/**
 * 对Auth2的监权效验 
 * @author zhengjiajin
 * @version 1.0 2020年12月22日
 */
public interface Auth2Crypt {
    //返回accessToken
    public String accessToken(String sysId,String secret);
    //返回系统ID
    public String decrypt(String accessToken);
    //返回accessToken
    public String refresh(String accessToken);
}
