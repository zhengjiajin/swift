/*
 * @(#)KeepaliveMessageDeliver.java   1.0  2020年12月21日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.protocol.core;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.swift.api.protocol.auth.AuthClient;
import com.swift.api.protocol.auth.ClientInfo;
import com.swift.api.protocol.auth.client.SysClient;
import com.swift.api.protocol.auth.client.UserClient;
import com.swift.api.protocol.core.pending.PendingRequest;
import com.swift.api.protocol.core.pending.PendingRequestManager;
import com.swift.api.protocol.message.SCA;
import com.swift.api.protocol.message.SCR;
import com.swift.api.protocol.message.handler.MessageHandlerContext;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.service.processor.AbstractMessageDeliver;
import com.swift.core.service.processor.send.MessageSender;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;
import com.swift.exception.extend.SystemException;
import com.swift.util.bean.BeanUtil;
import com.swift.util.math.RandomUtil;
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * 长连接消息管理器
 * @author zhengjiajin
 * @version 1.0 2020年12月21日
 */
@Component
public class KeepaliveMessageDeliverImpl extends AbstractMessageDeliver implements NettyKeepaliveMessageDeliver {
    private static final Logger logger = LoggerFactory.getLogger(KeepaliveMessageDeliverImpl.class);
    
    @Autowired
    private PendingRequestManager pendingRequestManager;
    
    @Autowired
    private ChannelManagerFactory channelManagerFactory;
    
    @Override
    public MessageSender getMessageSender() {
        return this;
    }
    
    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequestNoReturn(java.lang.Object)
     */
    @Override
    public void sendRequestNoReturn(ServiceRequest req) {
        sendRequest(req, new CallBackEngine<ServiceResponse>() {

            @Override
            public void receiveResponse(ServiceResponse rsp) {
                logger.info("收到不处理回复:"+JsonUtil.toJson(rsp));
            }

            @Override
            public void failed(Throwable ex) {
                logger.error("不处理回复消息异常:",ex);
            }
            
        });
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequest(java.lang.Object, com.swift.core.api.rpc.ClientEngine.CallBackEngine)
     */
    @Override
    public void sendRequest(ServiceRequest req, CallBackEngine<ServiceResponse> callBack) {
        pendingRequestManager.register(req, callBack);
        SCR scr = new SCR();
        scr.setData(req.getData());
        scr.setInterfaceVersion(req.getInterfaceVersion());
        scr.setMethod(req.getMethod());
        if(TypeUtil.isNull(req.getReqId())) {
            req.setReqId(RandomUtil.createReqId());
        }
        scr.setReqId(req.getReqId());
        if(req.getSessionUser()!=null && req.getSessionUser().getUserId()!=null) {
            //发向用户,只获取单个端的消息返回
            ClientInfo clientInfo = getClientInfo(req);
            if(clientInfo!=null) {
                MessageHandlerContext context = channelManagerFactory.getChannel(req.getSessionUser().getUserId(),clientInfo);
                checkContext(context);
                context.write(scr);
                return;
            }
            
            Collection<MessageHandlerContext> contextList = channelManagerFactory.getChannel(req.getSessionUser().getUserId());
            if(TypeUtil.isNull(contextList))  throw new SystemException(req.getSysId()+"对端未连接");
            for(MessageHandlerContext context:contextList) {
                try {
                    checkContext(context);
                    context.write(scr);
                }catch(Exception ex) {
                    
                }
            }
        }else if(TypeUtil.isNotNull(req.getSysId())) {
            //发向系统
            MessageHandlerContext context = channelManagerFactory.getChannel(req.getSysId());
            checkContext(context);
            context.write(scr);
        }
    }
    
    private ClientInfo getClientInfo(ServiceRequest req) {
        if(req==null) return null;
        if(req.getSessionUser()==null) return null;
        if(TypeUtil.isNull(req.getSessionUser().getLoginType())) return null;
        try {
            return ClientInfo.valueOf(req.getSessionUser().getLoginType());
        }catch(Exception ex) {
            return null;
        }
    }
    
    private void checkContext(MessageHandlerContext context) {
        if(context==null || context.getChannelHandlerContext()==null) throw new SystemException("对端未连接");
        if(!context.getChannelHandlerContext().channel().isActive()) throw new SystemException("对端连接已失效");
    }
    
    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendAsynRequest(java.lang.Object)
     */
    @Override
    public Future<ServiceResponse> sendAsynRequest(ServiceRequest req) {
        ExecutorService tp = Executors.newSingleThreadExecutor();
        Lock lock = new ReentrantLock();
        ServiceResponse res=new ServiceResponse();
        CallBackEngine<ServiceResponse> lockcallBack = new CallBackEngine<ServiceResponse>() {
            @Override
            public void receiveResponse(ServiceResponse rsp) {
                BeanUtil.copyProperties(res, rsp);
                lock.unlock();
            }
            @Override
            public void failed(Throwable ex) {
                if(ex instanceof ServiceException) {
                    ServiceException se = (ServiceException)ex;
                    res.setResultCode(se.getStatusCode());
                    res.setReason(se.getMessage());
                }else {
                    res.setResultCode(ResultCode.SYS_ERROR);
                    res.setReason("系统异常");
                    logger.error("系统异常:",ex);
                }
                lock.unlock();
            }
        };
        sendRequest(req, lockcallBack);
        Future<ServiceResponse> future = tp.submit(() -> {
            lock.lock();
            return res;
        });
        return future;
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequest(java.lang.Object)
     */
    @Override
    public ServiceResponse sendRequest(ServiceRequest req) {
        Future<ServiceResponse> future = sendAsynRequest(req);
        try {
            return future.get();
        } catch (Exception e) {
            throw new SystemException("获取结果异常");
        }
    }
    
    /** 
     * @see com.swift.server.netty.core.NettyKeepaliveMessageDeliver#responseReceived(com.swift.server.netty.message.SCA, com.swift.server.netty.message.handler.MessageHandlerContext)
     */
    @Override
    public void responseReceived(SCA message, MessageHandlerContext context) {
        PendingRequest pendingRequest = pendingRequestManager.deregister(message.getReqId());
        if(pendingRequest==null) return;
        if(pendingRequest.getRequest()==null) return;
        if(pendingRequest.getCallBack()==null) return;
        ServiceResponse serviceResponse = new ServiceResponse();
        serviceResponse.setData(message.getData());
        serviceResponse.setReason(message.getReason());
        serviceResponse.setResponseTime(message.getResponseTime());
        serviceResponse.setResultCode(message.getResultCode());
        serviceResponse.setRequest(pendingRequest.getRequest());
        pendingRequest.getCallBack().receiveResponse(serviceResponse);
    }


    /** 
     * @see com.swift.core.service.processor.received.KeepaliveMessageReceived#responseReceived(com.swift.core.model.ServiceResponse)
     */
    @Override
    public void responseReceived(ServiceResponse response) {
        //nothing
        
    }
    
    /** 
     * @see com.swift.server.netty.core.NettyKeepaliveMessageDeliver#requestReceived(com.swift.server.netty.message.SCR, com.swift.server.netty.message.handler.MessageHandlerContext, com.swift.core.api.rpc.ClientEngine.CallBackEngine, boolean)
     */
    @Override
    public void requestReceived(SCR scr, MessageHandlerContext messageHandlerContext) {
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setData(scr.getData());
        serviceRequest.setInterfaceVersion(String.valueOf(scr.getInterfaceVersion()));
        serviceRequest.setDomain(ipAddress(messageHandlerContext.remoteAddress()));
        serviceRequest.setReqId(scr.getReqId());
        serviceRequest.setRequestTime(System.currentTimeMillis());
        AuthClient authClient = messageHandlerContext.getAuthClient();
        if(authClient!=null) {
            if(authClient instanceof UserClient) {
                serviceRequest.setSessionUser(((UserClient)authClient).getAbstractSession());
            }
            if(authClient instanceof SysClient) {
                serviceRequest.setSysId(((SysClient)authClient).getSysId());
            }
        }
        pendingRequestManager.register(scr, messageHandlerContext);
        super.requestReceived(serviceRequest);
    }
    
    /** 
     * @see com.swift.core.service.processor.send.MessageSender#sendResponse(com.swift.core.model.ServiceResponse)
     */
    @Override
    public void sendResponse(ServiceResponse response) {
        ServiceRequest request = response.getRequest();
        if(request==null) throw new SystemException("此响应未关联请求");
        PendingRequest pendingRequest = pendingRequestManager.deregister(request.getReqId());
        if(pendingRequest==null) return;
        if(pendingRequest.getMessageRequest()==null) return;
        if(pendingRequest.getMessageHandlerContext()==null) return;
        SCA sca = new SCA((SCR)pendingRequest.getMessageRequest());
        sca.setData(response.getData());
        sca.setReason(response.getReason());
        sca.setResponseTime(System.currentTimeMillis());
        sca.setResultCode(response.getResultCode());
        pendingRequest.getMessageHandlerContext().write(sca);
    }


    private String ipAddress(SocketAddress socketAddress) {
        if (socketAddress != null) {
            return ((InetSocketAddress) socketAddress).getHostName();
        }
        return null;
    }

    
    
}
