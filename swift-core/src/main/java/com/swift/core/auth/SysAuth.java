/*
 * @(#)SysAuth.java   1.0  2020年12月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.auth;

/**
 * 通过帐号密码验证
 * @author zhengjiajin
 * @version 1.0 2020年12月29日
 */
public interface SysAuth {
    
    /**
     * 返回认证的SYSID
     * @param sysId
     * @param secret
     * @return
     */
    public String decrypt(String sysId,String secret);
}
