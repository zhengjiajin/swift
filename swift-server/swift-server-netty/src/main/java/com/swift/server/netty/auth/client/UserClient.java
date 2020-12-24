/*
 * @(#)UserClient.java   1.0  2020年12月21日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.auth.client;

import com.swift.core.session.AbstractSession;
import com.swift.server.netty.auth.AuthClient;

/**
 * 用户认证对象
 * @author zhengjiajin
 * @version 1.0 2020年12月21日
 */
public class UserClient extends AuthClient {

    /**
     * 用户信息
     */
    private AbstractSession abstractSession;

    /**
     * @return the abstractSession
     */
    public AbstractSession getAbstractSession() {
        return abstractSession;
    }

    /**
     * @param abstractSession the abstractSession to set
     */
    public void setAbstractSession(AbstractSession abstractSession) {
        this.abstractSession = abstractSession;
    }
    
    
}
