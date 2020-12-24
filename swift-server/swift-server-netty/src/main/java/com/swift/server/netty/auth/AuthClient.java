/*
 * @(#)AuthClient.java   1.0  2020年12月18日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.auth;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年12月18日
 */
public abstract class AuthClient {
    
    private ClientInfo clientInfo;
    /**
     * 是否已认证
     */
    private AtomicBoolean authenticated = new AtomicBoolean(false);
    
    public boolean isAuthenticated() {
        return authenticated.get();
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated.set(authenticated);
    }

    /**
     * @return the clientInfo
     */
    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    /**
     * @param clientInfo the clientInfo to set
     */
    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

}
