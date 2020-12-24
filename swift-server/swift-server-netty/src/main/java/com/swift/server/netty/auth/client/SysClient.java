/*
 * @(#)SysClient.java   1.0  2020年12月21日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.auth.client;

import com.swift.server.netty.auth.AuthClient;

/**
 * 系统认证对象
 * @author zhengjiajin
 * @version 1.0 2020年12月21日
 */
public class SysClient extends AuthClient {

    private String sysId;

    /**
     * @return the sysId
     */
    public String getSysId() {
        return sysId;
    }

    /**
     * @param sysId the sysId to set
     */
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
    
}
