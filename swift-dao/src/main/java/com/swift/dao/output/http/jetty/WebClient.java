/*
 * @(#)WebClient.java   1.0  2014-1-22
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangDong Eshore Technology Company LTD.
 */
package com.swift.dao.output.http.jetty;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;
import com.swift.dao.output.CallBackEngine;
import com.swift.dao.output.http.jetty.WebClientCode.HttpContentExchange;
import com.swift.exception.ResultCode;
import com.swift.exception.ServiceException;
import com.swift.exception.SwiftRuntimeException;

/**
 * web 的短连接客户端,以http发送
 * 
 * @author jiajin
 * @version 1.0 2014-1-22
 */
@Service
public class WebClient {
    private final static Logger log = LoggerFactory.getLogger(WebClient.class);
    
    private Map<String, WebClientCode> clientCodeMapping = new ConcurrentHashMap<String, WebClientCode>();
    
    /**
     * 客户关
     */
    private HttpClient httpClient;
    /**
     * 超时时间
     */
    private int timeout=30000;
    /**
     * 连接超时时间
     */
    private int connectTimeout=5000;

    /**
     * 初始化方法,spring 起动时启动
     * 
     * @throws Exception
     */
    @PostConstruct
    public void init() throws Exception {
        httpClient = new HttpClient(new SslContextFactory());
        httpClient.setExecutor(new QueuedThreadPool(300));
        httpClient.setConnectTimeout(connectTimeout);
        httpClient.start();
    }

    public void stop() {
        try {
            if (httpClient != null && httpClient.isStarted()) httpClient.stop();
        } catch (Exception ex) {
        }
    }

    public HttpClient getHttpClient(){
        return httpClient;
    }
    
    /**
     * 发送请求
     * 
     * @param req
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
	public void sendRequest(ServiceRequest req, CallBackEngine callBack) {
    	HttpContentExchange exchange = defaultContent(req);
        exchange.setCallBack(callBack);
        Request request = setRequest(exchange);
        request.send(exchange);
        log.info("已发送异步请求:" + req);
    }
    
    private HttpContentExchange defaultContent(ServiceRequest req) {
        //WebClientCode code = ServerEnum.valueOf(req.getMethod()).getCode();
    	WebClientCode code = clientCodeMapping.get(req.getMethod());
		if (code == null) {
			throw new ServiceException(ResultCode.NO_METHOD, "No WebClientCode for '" + req.getMethod() + "'");
		}
        HttpContentExchange exchange = code.encode(req);
        exchange.setHttpClient(httpClient);
        exchange.setRequest(req);
        if(exchange.getTimeout()<=0) exchange.setTimeout(timeout);
        if (exchange.getURL() == null) exchange.setURL(req.getMethod());
        return exchange;
    }
    
    private Request setRequest(HttpContentExchange exchange){
        Request request =httpClient.newRequest(exchange.getURI());
        request.method(exchange.getMethod()).timeout(exchange.getTimeout(), TimeUnit.MILLISECONDS);
        if(exchange.getURL().indexOf("https:") != -1){
           request.scheme("https");
        }else{
           request.scheme("http");
        }
        if(exchange.getRequestContent()!=null){
            request.content(exchange.getRequestContent());
        }
        for(Entry<String, String> entry : exchange.getRequestHeader().entrySet()){
        	request.getHeaders().put(entry.getKey(), entry.getValue());
        }
        return request;
    }

    /**
     * 发送请求
     * 
     * @param req
     * @throws Exception
     */
    public ServiceResponse sendRequest(ServiceRequest req) {
        HttpContentExchange exchange = defaultContent(req);
        Request request = setRequest(exchange);
        log.info("已发送同步请求:" + req);
        try {
            ContentResponse result = request.send();
            return exchange.getServerResponse(result,result.getContent());
        } catch (Exception e) {
            throw new SwiftRuntimeException("请求发送错误",e);
        } 
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
    public void registerWebClientCode(String key, WebClientCode value) {
		clientCodeMapping.put(key, value);
	}
    
    @SuppressWarnings("unused")
	private WebClientCode getWebClientCode(String method) {
		WebClientCode code = clientCodeMapping.get(method);
		if (code == null) {
			throw new ServiceException(ResultCode.NO_METHOD, "No WebClientCode for '" + method + "'");
		}
		return code;
	}

}
