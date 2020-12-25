/*
 * @(#)AuthInterface.java   1.0  2020年12月18日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.auth;

import com.swift.api.protocol.auth.AuthClient;
import com.swift.api.protocol.message.CLR;
import com.swift.api.protocol.message.handler.MessageHandlerContext;

/**
 * 连接认证接口服务类
 * @author zhengjiajin
 * @version 1.0 2020年12月18日
 */
public interface AuthInterface {

    public AuthClient authChannel(CLR clr,MessageHandlerContext context);
}
