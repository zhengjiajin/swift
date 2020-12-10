/*
 * @(#)WebHandler.java 2014-5-16
 * 
 * Copyright (c)    2014-2015. All Rights Reserved. GuangDong Eshore Technology Company LTD.
 */
package com.swift.server.jetty.handler.impl.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.core.model.HttpServiceRequest;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.model.data.FileDefinition;
import com.swift.core.service.processor.send.MessageSender;
import com.swift.exception.NoWarnException;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;
import com.swift.exception.extend.SystemException;
import com.swift.server.jetty.handler.WebHandler;
import com.swift.server.jetty.handler.impl.rest.protocol.JettyWebProtocol;
import com.swift.server.jetty.handler.impl.rest.protocol.JettyWebProtocol.ResModel;
import com.swift.server.jetty.processor.WebMessageDeliver;
import com.swift.util.math.RandomUtil;
import com.swift.util.type.JsonUtil;
import com.swift.util.type.TypeUtil;
import com.swift.util.type.UrlUtil;

/**
 * http接收器，接收http请求，用于http,soap等 协议
 * 
 * @author jiajin
 * @version 1.0 2014-5-15
 */
public abstract class AbstractWebHandler extends AbstractHandler implements WebHandler, MessageSender {

    private final static Logger log = LoggerFactory.getLogger(AbstractWebHandler.class);

    // private static final long DEFAULT_TIMEOUT = 300 * 1000;

    @Autowired
    private JettyWebProtocol[] jettyWebProtocol = new JettyWebProtocol[0];

    @Autowired
    private WebMessageDeliver webMessageDeliver;

    /**
     * 处理接收到的请求
     * 
     * @see org.eclipse.jetty.server.Handler#handle(java.lang.String, org.eclipse.jetty.server.Request,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void handle(String target, Request rawHttpRequest, HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        log.info("收到请求:" + target + ";parameter:" + JsonUtil.toJson(request.getParameterMap()));
        serAccessHeader(request, response);
        if (isDomainCheck(rawHttpRequest)) {
            log.info("判断URL为浏览器跨域检测:" + target);
            sendNullHttp(rawHttpRequest);
            return;
        }
        if (isFavicon(target)) {
            sendNullHttp(rawHttpRequest);
            return;
        }

        if (!isThisHandler(target, request)) return;
        try {
            AsyncContext asyn = rawHttpRequest.startAsync();
            asyn.addListener(new AsyncListener() {
                @Override
                public void onTimeout(AsyncEvent event) throws IOException {
                    sendHttpError(HttpServletResponse.SC_GATEWAY_TIMEOUT, "504 Gateway Timeout", rawHttpRequest, target);
                }

                @Override
                public void onStartAsync(AsyncEvent event) throws IOException {
                }

                @Override
                public void onError(AsyncEvent event) throws IOException {
                    String errorMsg = event.getThrowable()==null?"系统异常":event.getThrowable().getMessage();
                    sendHttpError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMsg, rawHttpRequest, target);
                }

                @Override
                public void onComplete(AsyncEvent event) throws IOException {
                }
            }, request, response);
            
            JettyWebProtocol handlerCode = selectHandler(target);
            if (handlerCode == null) {
                throw new ServiceException(ResultCode.PROTOCOL_ERROR, "找不到相应协议");
            }
            HttpServiceRequest req = handlerCode.decode(target, rawHttpRequest, request, response);
            if (req == null) {
                sendHttpError(200, "200", rawHttpRequest, target);
            }
            req.setRequestTime(System.currentTimeMillis());
            req.setRequest(request);
            req.setResponse(response);
            if (TypeUtil.isNull(req.getDomain())) {
                req.setDomain(rawHttpRequest.getRemoteAddr());
            }
            rawHttpRequest.setHandled(true);
            webMessageDeliver.requestReceived(req);

        } catch (NoWarnException ex) {
            log.warn("Neglectable Exception", ex);
            sendHttpError(ex.getStatusCode(), ex.getMessage(), rawHttpRequest, target);
            return;
        } catch (ServiceException ex) {
            log.error("Service Exception", ex);
            sendHttpError(ex.getStatusCode(), ex.getMessage(), rawHttpRequest, target);
            return;
        } catch (Throwable ex) {
            log.error("Http请求处理失败", ex);
            sendHttpError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage(), rawHttpRequest, target);
            return;
        }
    }

    /**
     * @see com.swift.core.service.processor.send.MessageSender#sendResponse(com.swift.core.model.ServiceResponse)
     */
    @Override
    public void sendResponse(ServiceResponse response) {
        ServiceRequest serviceRequest = response.getRequest();
        if (serviceRequest == null) {
            log.error("对应请求为空:" + JsonUtil.toJson(response));
            return;
        }
        if (!(serviceRequest instanceof HttpServiceRequest)) {
            log.error("此请求非HTTP请求:" + JsonUtil.toJson(serviceRequest));
            return;
        }

        HttpServletRequest servletRequest = ((HttpServiceRequest)serviceRequest).getRequest();
        HttpServletResponse servletResponse = ((HttpServiceRequest)serviceRequest).getResponse();
        if (response.getData() != null && response.getData() instanceof FileDefinition) {
            sendFileResponse(response, servletRequest, servletResponse);
        } else {
            sendResponse(response, servletRequest, servletResponse);
        }
        servletRequest.getAsyncContext().complete();
    }

    /**
     * 找出处理解码类
     * 
     * @param target
     * @return
     */
    private JettyWebProtocol selectHandler(String target) {
        JettyWebProtocol handler = null;
        for (JettyWebProtocol code : jettyWebProtocol) {
            if (code.isThisCode(target)) {
                handler = code;
                break;
            }
        }
        return handler;
    }

    /**
     * 处理响应
     * 
     * @param rawRequest
     * @param response
     * @throws IOException
     */
    private void sendResponse(ServiceResponse response, HttpServletRequest servletRequest,HttpServletResponse servletResponse) {
        String target = servletRequest.getRequestURI();
        JettyWebProtocol handler = selectHandler(target);
        ResModel resModel = handler.encode(response);
        servletResponse.setContentType(resModel.getContentType());
        servletResponse.setCharacterEncoding("utf-8");
        servletResponse.setStatus(resModel.getStatus());
        List<Cookie> cookies = handler.setCookie(response);
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                servletResponse.addCookie(cookie);
            }
        }
        try {
            servletResponse.getOutputStream().write(resModel.getBody());
        } catch (IOException e) {
            log.warn("网络异常", e);
        }
    }

    private void sendFileResponse(ServiceResponse sr, HttpServletRequest servletRequest,HttpServletResponse servletResponse) {
        FileDefinition fileDef = (FileDefinition) sr.getData();
        InputStream input = fileDef.getInputStream();
        if (input == null) {
            throw new SystemException("返回的文件流为空");
        }

        String contentType = fileDef.getContentType();
        if (StringUtils.isBlank(contentType)) {
            contentType = "application/octet-stream";
        }
        servletResponse.setContentType(contentType);
        servletResponse.setCharacterEncoding("utf-8");
        String fileName = fileDef.getFileName();
        if (StringUtils.isBlank(fileName)) {
            fileName = RandomUtil.createReqId();
        } else {
            fileName = fileName.replace("\\\\", "/");
            int idx = fileName.lastIndexOf("/");
            if (idx >= 0) {
                fileName = fileName.substring(idx + 1);
            }
        }
        servletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + UrlUtil.urlEncode(fileName) + "\"");
       
        servletResponse.setStatus(200);

        long size = fileDef.getSize();
        try {
            if (size == -1) {
                size = input.available();
            }
            byte[] b = new byte[8192];
            int len = 0;
            while ((len = input.read(b)) != -1) {
                servletResponse.getOutputStream().write(b, 0, len);
            }
            servletResponse.setContentLength((int) size);
            servletResponse.getOutputStream().flush();
            input.close();
            if (fileDef.getOperationCallback() != null) {
                try {
                    fileDef.getOperationCallback().callback(fileDef.getFileName());
                } catch (Throwable ex) {
                    log.error("Failed to invoke callback: ", ex);
                }
            }
        } catch (IOException ex) {
            log.error("Unexpected exception: ", ex);
            throw new ServiceException(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "读取文件失败");
        }
    }

    private void serAccessHeader(HttpServletRequest reques, HttpServletResponse rawResponse) {
        String origin = reques.getHeader("Origin");
        if (TypeUtil.isNull(origin)) {
            origin = reques.getHeader("Referer");
        }
        if (TypeUtil.isNull(origin)) {
            origin = reques.getHeader("Host");
        }
        rawResponse.addHeader("Access-Control-Allow-Origin", origin);
        log.info("origin:" + origin);
        rawResponse.addHeader("Access-Control-Allow-Credentials", "true");
        rawResponse.addHeader("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept");
        rawResponse.addHeader("Access-Control-Allow-Methods", "GET,POST");
    }

    /**
     * 发送http错误消息
     * 
     * @param status
     * @param msg
     * @param rawHttpRequest
     * @throws IOException
     */
    protected void sendHttpError(int status, String msg, Request rawHttpRequest, String target) {
        Response rawHttpResponse = rawHttpRequest.getResponse();
        try {
            JettyWebProtocol handler = selectHandler(target);
            if (handler != null) {
                ResModel res = handler.error(status, msg);
                rawHttpResponse.setContentType(res.getContentType());
                rawHttpResponse.setStatus(res.getStatus());
                rawHttpResponse.getOutputStream().write(res.getBody());
            } else {
                rawHttpResponse.setContentType("text/html;charset=UTF-8");
                rawHttpResponse.setStatus(status);
                rawHttpResponse.getOutputStream().write(msg.getBytes());
            }
            rawHttpRequest.getAsyncContext().complete();
        } catch (Exception ex) {
            log.error("系统异常", ex);
        }
    }

    private boolean isFavicon(String target) {
        if (TypeUtil.isNull(target)) return true;
        if (target.indexOf("favicon.ico") != -1) return true;
        return false;
    }

    private boolean isDomainCheck(Request rawHttpRequest) {
        if (TypeUtil.isNotNull(rawHttpRequest.getHeader("Access-Control-Request-Headers"))) return true;
        if (TypeUtil.isNotNull(rawHttpRequest.getHeader("Access-Control-Request-Method"))) return true;
        return false;
    }

    private void sendNullHttp(Request rawHttpRequest) {
        Response rawHttpResponse = rawHttpRequest.getResponse();
        try {
            rawHttpResponse.setContentType("text/html;charset=UTF-8");
            rawHttpResponse.setStatus(200);
            rawHttpResponse.flushBuffer();
        } catch (Exception ex) {
            log.error("系统异常", ex);
        }
    }

    /**
     * @param webHandlerCode
     *            the webHandlerCode to set
     */
    public void setWebHandlerCode(JettyWebProtocol[] jettyWebProtocol) {
        this.jettyWebProtocol = jettyWebProtocol;
    }

}
