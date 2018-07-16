/*
 * @(#)WebHandler.java 2014-5-16
 * 
 * Copyright (c)    2014-2015. All Rights Reserved. GuangDong Eshore Technology Company LTD.
 */
package com.swift.server.jetty.handler;

import java.io.IOException;
import java.io.InputStream;
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
import org.springframework.beans.factory.annotation.Value;

import com.swift.core.model.FileDefinition;
import com.swift.core.model.HttpServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.core.service.CallBackService;
import com.swift.core.thread.ServerSendControl;
import com.swift.core.thread.ThreadPoolFactory;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;
import com.swift.exception.SwiftRuntimeException;
import com.swift.server.jetty.protocol.WebHandlerCode;
import com.swift.server.jetty.protocol.WebHandlerCode.ResModel;
import com.swift.util.math.RandomUtil;
import com.swift.util.text.JsonUtil;
import com.swift.util.type.TypeUtil;

/**
 * http接收器，接收http请求，用于http,soap等 协议
 * @author jiajin
 * @version 1.0 2014-5-15
 */
public abstract class AbstractWebHandler extends AbstractHandler implements WebHandler {

	private final static Logger log = LoggerFactory.getLogger(AbstractWebHandler.class);
    
	private static final long DEFAULT_TIMEOUT = 300 * 1000;
	
    @Autowired
    private WebHandlerCode[] webHandlerCode = new WebHandlerCode[0];

    @Autowired
    protected ThreadPoolFactory threadPool;
    
    /**
     * 端口
     */
    @Value("${webServer.http.port:8090}")
    private int port = 8090;
    
    /**
     * 处理接收到的请求
     * 
     * @see org.eclipse.jetty.server.Handler#handle(java.lang.String, org.eclipse.jetty.server.Request,
     *      javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void handle(String target, Request rawHttpRequest, HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        log.info("收到请求:"+target+";parameter:"+JsonUtil.toJson(request.getParameterMap()));
        if(!isThisHandler(target, request)) return;
        try {
            Continuation continuation = ContinuationSupport.getContinuation(rawHttpRequest);
            continuation.setTimeout(getTimeout());
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
            req.setRequest(request);
            req.setResponse(response);
            if(TypeUtil.isNull(req.getIp())) {
                req.setIp(rawHttpRequest.getRemoteAddr());
            }
            rawHttpRequest.setHandled(true);
            threadPool.execute(new ServerSendControl(new CallBackService() {
                @Override
                public void callback(ServiceResponse res) {
                    try {
                        sendResponse(res,rawHttpRequest,target);
                    } catch (Throwable ex) {
                        log.error("Error send response: {}", res, ex);
                        sendHttpError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage(), rawHttpRequest, target);
                    }
                }
            }, req));
        } catch (ServiceException ex) {
            log.error("请求处理失败", ex);
            sendHttpError(ex.getStatusCode(), ex.getMessage(), rawHttpRequest, target);
            return;
        } catch (Exception ex) {
            log.error("Http请求处理失败", ex);
            sendHttpError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage(), rawHttpRequest, target);
            return;
        }
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
	private void sendResponse(ServiceResponse response,Request rawHttpRequest, String target) {
	    if (response.getData()!=null && response.getData() instanceof FileDefinition) {
	        sendFileResponse(response,rawHttpRequest);
        }else {
    		WebHandlerCode handler = selectHandler(target);
    		ResModel resModel = handler.encode(response);
    		Response rawResponse = rawHttpRequest.getResponse();
    		rawResponse.setContentType(resModel.getContentType());
    		rawResponse.setCharacterEncoding("utf-8");
    		rawResponse.setStatus(resModel.getStatus());
    		List<Cookie> cookies = handler.setCookie(response);
    		if (cookies != null) {
    			for (Cookie cookie : cookies) {
    				rawResponse.addCookie(cookie);
    			}
    		}
    		serAccessHeader(rawHttpRequest,rawResponse);
    		try {
                rawResponse.getOutputStream().write(resModel.getBody());
            } catch (IOException e) {
                throw new SwiftRuntimeException("网络异常", e);
            }
        }
		completeContinuation(rawHttpRequest);
	}
	
	private void sendFileResponse(ServiceResponse sr,Request rawHttpRequest) {
        Response rawResponse = rawHttpRequest.getResponse();
        FileDefinition fileDef = (FileDefinition) sr.getData();
        InputStream input = fileDef.getInputStream();
        if (input == null) {
            throw new SwiftRuntimeException("返回的文件流为空");
        }

        String contentType = fileDef.getContentType();
        if (StringUtils.isBlank(contentType)) {
            contentType = "application/octet-stream";
        }
        rawResponse.setContentType(contentType);
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
        serAccessHeader(rawHttpRequest,rawResponse);
        rawResponse.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        rawResponse.setStatus(200);

        long size = fileDef.getSize();
        try {
            if (size == -1) {
                size = input.available();
            }
            byte[] b = new byte[8192];
            int len = 0;
            while ((len = input.read(b)) != -1) {
                rawResponse.getOutputStream().write(b, 0, len);
            }
            rawResponse.setContentLength((int) size);
            rawResponse.getOutputStream().flush();
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


    private void serAccessHeader(HttpServletRequest reques,HttpServletResponse rawResponse) {
        if(TypeUtil.isNull(reques.getHeader("Origin"))) {
            rawResponse.addHeader("Access-Control-Allow-Origin", reques.getHeader("Host"));
        }else {
            rawResponse.addHeader("Access-Control-Allow-Origin", reques.getHeader("Origin"));
        }
        rawResponse.addHeader("Access-Control-Allow-Credentials", "true");
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
        serAccessHeader(rawHttpRequest,rawHttpResponse);
        try {
            WebHandlerCode handler = selectHandler(target);
            if(handler!=null){
                ResModel res = handler.error(status, msg);
                rawHttpResponse.setContentType(res.getContentType());
                rawHttpResponse.setStatus(res.getStatus());
                rawHttpResponse.getOutputStream().write(res.getBody());
            }else{
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

    /**
     * 发送消息完所消除等待
     * 
     * @param rawHttpRequest
     */
    protected void completeContinuation(Request rawHttpRequest) {
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

	

    /** 
     * @see com.WebHandler.hospital.server.core.handler.HandlerFilter#isPutHandler()
     */
    @Override
    public boolean isPutHandler() {
        if(port<0) {
            log.info(getClass().getName()+":port为"+port+"不启动");
            return false;
        }
        log.info(getClass().getName()+":port为"+port+"启动");
        return true;
    }
    
    protected long getTimeout() {
		return DEFAULT_TIMEOUT;
	}
    
    /** 
     * @see com.WebHandler.hospital.server.core.handler.HandlerFilter#port()
     */
    @Override
    public int port() {
        return port;
    }
}
