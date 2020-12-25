/**
 * CLAMessageHandler.java 1.0.0  2020年12月25日
 *
 * Copyright (c) 2015-2020 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.api.ssp.message.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.protocol.auth.ClientInfo;
import com.swift.api.protocol.auth.client.SysClient;
import com.swift.api.protocol.core.ChannelManagerFactory;
import com.swift.api.protocol.message.CLA;
import com.swift.api.protocol.message.MessageType;
import com.swift.api.protocol.message.handler.AbstractMessageHandler;
import com.swift.api.protocol.message.handler.MessageHandlerContext;
import com.swift.api.ssp.SspproxyConfig;
import com.swift.exception.ResultCode;
import com.swift.util.type.JsonUtil;

/**
 * CLA消息处理器
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年12月25日
 */
@Component
public class CLAMessageHandler extends AbstractMessageHandler<CLA> {
    
    private static final Logger log = LoggerFactory.getLogger(CLAMessageHandler.class);
    
    @Autowired
    private ChannelManagerFactory channelManagerFactory;
    
    @Autowired
    private SspproxyConfig sspproxyConfig;
    /**
     * Construct a new <code>CLAMessageHandler</code> instance.
     */
    public CLAMessageHandler() {
        super(MessageType.CLA);
    }

  
    @Override
    public void handle(CLA message, MessageHandlerContext context) {
        if(message==null) return;
        if(message.getResultCode()!=ResultCode.SUCCESS) {
            log.error("连接监权异常:"+JsonUtil.toJson(message));
            context.getChannelHandlerContext().close();
            return;
        }else {
            log.info("连接监权成功:"+JsonUtil.toJson(message));
            SysClient sys = new SysClient();
            sys.setSysId(sspproxyConfig.getSysId());
            sys.setClientInfo(ClientInfo.SERVER);
            sys.setAuthenticated(false);
            context.setAuthClient(sys);
            channelManagerFactory.registerChannel(sys.getSysId(), context);
        }
    }
}
