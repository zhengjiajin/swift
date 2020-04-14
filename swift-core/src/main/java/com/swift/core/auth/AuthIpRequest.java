/*
 * @(#)AuthIpRequest.java   1.0  2018年12月25日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.auth;

/**
 * 需要IP验证的请求 
 * @author zhengjiajin
 * @version 1.0 2018年12月25日
 */
public class AuthIpRequest extends AuthRequest {

    //本系统ID
    private String sysId;
    
    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
}
