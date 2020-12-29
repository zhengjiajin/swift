/*
 * @(#)SecSocketAcceptLogin.java   1.0  2020年12月29日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.netty.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.swift.api.protocol.auth.client.UserClient;
import com.swift.api.protocol.core.ChannelManagerFactory;
import com.swift.api.protocol.listener.ChannelEventListener;
import com.swift.api.protocol.listener.ChannelEventListener.Event;
import com.swift.api.protocol.message.CLA;
import com.swift.api.protocol.message.CLR;
import com.swift.api.protocol.message.handler.MessageHandlerContext;
import com.swift.api.protocol.util.NettyChannel;
import com.swift.core.auth.Login;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.data.DataModel;
import com.swift.core.service.SynInterface;
import com.swift.core.validator.DataValidator;
import com.swift.core.validator.annotation.ParamNotEmpty;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;
import com.swift.exception.extend.SystemException;
import com.swift.server.netty.message.code.AuthorizeCode;
import com.swift.server.netty.message.code.AuthorizeCodeFactory;

/**
 * 授权接入,此流程需要使用HTTP服务
 * @author zhengjiajin
 * @version 1.0 2020年12月29日
 */
@Login
@Service("secSocketAcceptLogin")
public class SecSocketAcceptLogin implements SynInterface {

    private static final Logger logger = LoggerFactory.getLogger(SecSocketAcceptLogin.class);

    @Autowired
    private AuthorizeCodeFactory authorizeCodeFactory;
    @Autowired
    private ChannelManagerFactory channelManagerFactory;
    @Autowired(required = false)
    private List<ChannelEventListener> channelEventListeners;
    
    @Override
    @DataValidator(NotEmpty = @ParamNotEmpty(param = "secSocketAccept", message = "secSocketAccept不能为空"))
    public DataModel doService(ServiceRequest request) {
        logger.info("Received SecSocketAcceptLogin request with data: {}", request.getData());
        String secSocketAccept = request.getData().getString("secSocketAccept");
        // 根据授权码获取路由信息。
        AuthorizeCode authorizeCode = authorizeCodeFactory.get(secSocketAccept);
        if (authorizeCode == null) {
            throw new ServiceException(500001, "授权码已失效");
        }
        MessageHandlerContext context = authorizeCode.getMessageHandlerContext();
        if(!NettyChannel.isActivity(context)) {
            throw new ServiceException(500002, "连接已断开");
        }
        
        if(context.getAuthClient() instanceof UserClient) {
            UserClient userClient = (UserClient)context.getAuthClient();
            userClient.setAbstractSession(request.getSessionUser());
            userClient.setAuthenticated(true);
            channelManagerFactory.registerChannel(userClient.getAbstractSession().getUserId(),userClient.getClientInfo(), context);
        } else {
            throw new SystemException("只接授用户的登录授权");
        }
        //清除断连定时器
        NettyChannel.cancelScheduledFuture(context);
        NettyChannel.fireChannelEvent(channelEventListeners, Event.AUTH, context);
        
        CLA cla = createCLAMessage(authorizeCode.getClr());
        context.getChannelHandlerContext().channel().write(cla);
        return null;
    }

    private CLA createCLAMessage(CLR clr) {
        CLA cla = new CLA(clr);
        cla.setReason("授权登录成功");
        cla.setResponseTime(System.currentTimeMillis());
        cla.setResultCode(ResultCode.SUCCESS);
        return cla;
    }
    
    
}
