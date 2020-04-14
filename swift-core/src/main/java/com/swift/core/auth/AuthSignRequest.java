/*
 * @(#)AuthSignRequest.java   1.0  2018年12月25日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.auth;

/**
 * 需要签名验证的请求 
 * @author zhengjiajin
 * @version 1.0 2018年12月25日
 */
public class AuthSignRequest extends AuthRequest {
    //本系统ID
    private String sysId;
    
    // 系统KEY
    private String key;

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
}
