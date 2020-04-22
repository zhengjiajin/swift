/*
 * @(#)AuthKeyInterface.java   1.0  2020年4月16日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.auth;

/**
 * 获取密钥的方法 
 * @author zhengjiajin
 * @version 1.0 2020年4月16日
 */
public interface AuthKeyInterface {
    /**
     * 返回密钥KEY
     * @param toSysId 对端系统ID
     * @param keyRem key取数规则与描述
     * @return
     */
    public byte[] getKey(String toSysId, Object keyRem);
}
