/*
 * @(#)WebHandler.java 2014-5-16
 * 
 * Copyright (c)    2014-2015. All Rights Reserved. GuangDong Eshore Technology Company LTD.
 */
package com.swift.server.jetty.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.swift.core.model.FileDefinition;
import com.swift.core.model.HttpServiceRequest;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.service.processor.send.MessageSender;
import com.swift.exception.NoWarnException;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;
import com.swift.exception.SwiftRuntimeException;
import com.swift.server.jetty.processor.WebMessageDeliver;
import com.swift.server.jetty.protocol.WebHandlerCode;
import com.swift.server.jetty.protocol.WebHandlerCode.ResModel;
import com.swift.util.math.RandomUtil;
import com.swift.util.text.JsonUtil;
import com.swift.util.type.TypeUtil;

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
    private WebHandlerCode[] webHandlerCode = new WebHandlerCode[0];

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
            Continuation continuation = ContinuationSupport.getContinuation(rawHttpRequest);
            // continuation.setTimeout(getTimeout());
            continuation.suspend(rawHttpRequest.getResponse());
            if (continuation.isExpired()) {
                throw new ServiceException(ResultCode.PROTOCOL_ERROR, "504 Gateway Timeout");
            }
            WebHandlerCode handlerCode = selectHandler(target);
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
            if (TypeUtil.isNull(req.getIp())) {
                req.setIp(rawHttpRequest.getRemoteAddr());
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
        completeContinuation(servletRequest);
    }

    /**
     * 找出处理解码类
     * 
     * @param target
     * @return
     */
    private WebHandlerCode selectHandler(String target) {
        WebHandlerCode handler = null;
        for (WebHandlerCode code : webHandlerCode) {
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
        WebHandlerCode handler = selectHandler(target);
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
            throw new SwiftRuntimeException("网络异常", e);
        }
    }

    private void sendFileResponse(ServiceResponse sr, HttpServletRequest servletRequest,HttpServletResponse servletResponse) {
        FileDefinition fileDef = (FileDefinition) sr.getData();
        InputStream input = fileDef.getInputStream();
        if (input == null) {
            throw new SwiftRuntimeException("返回的文件流为空");
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
        try {
            servletResponse.setHeader("Content-Disposition",
                "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
        } catch (UnsupportedEncodingException e) {
            servletResponse.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        }
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
            WebHandlerCode handler = selectHandler(target);
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
            rawHttpResponse.flushBuffer();
        } catch (Exception ex) {
            log.error("系统异常", ex);
        }
        completeContinuation(rawHttpRequest);
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
        completeContinuation(rawHttpRequest);
    }

    /**
     * 发送消息完所消除等待
     * 
     * @param rawHttpRequest
     */
    protected void completeContinuation(HttpServletRequest rawHttpRequest) {
        Continuation continuation = ContinuationSupport.getContinuation(rawHttpRequest);
        if (continuation.isSuspended()) {
            continuation.complete();
        }
    }

    /**
     * @param webHandlerCode
     *            the webHandlerCode to set
     */
    public void setWebHandlerCode(WebHandlerCode[] webHandlerCode) {
        this.webHandlerCode = webHandlerCode;
    }

}
