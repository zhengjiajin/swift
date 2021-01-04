/*
 * @(#)ApacheHttpClientConfig.java   1.0  2020年11月30日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.http.apache;

import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.ConnectingIOReactor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.swift.api.http.util.ContentTypeUtil;
import com.swift.core.api.rpc.ClientEngine;
import com.swift.exception.extend.SystemException;
import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2020年11月30日
 */
@Component
public class ApacheHttpClient implements ClientEngine<HttpUriRequest, HttpResponse> {
    
    private final static Logger log = LoggerFactory.getLogger(ApacheHttpClient.class);
    // 异步客户端
    private CloseableHttpAsyncClient asynClient;
    // 同步客户端
    private CloseableHttpClient synClient;

    @PostConstruct
    private void init() throws Exception {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setSocketTimeout(30000)
            .setConnectionRequestTimeout(50).build();

        // 配置线程
        IOReactorConfig ioReactorConfig = IOReactorConfig.custom()
            .setIoThreadCount(Runtime.getRuntime().availableProcessors()).setSoKeepAlive(true).build();
        // 设置连接池大小
        ConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(ioReactorConfig);

        PoolingNHttpClientConnectionManager connManager = new PoolingNHttpClientConnectionManager(ioReactor);
        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(100);

        asynClient = HttpAsyncClients.custom()
            .setConnectionManager(connManager)
            .setDefaultRequestConfig(requestConfig)
            .build();

        asynClient.start();

        synClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
    }

    public void stop() {
        try {
            if (asynClient != null && asynClient.isRunning()) asynClient.close();
            if (synClient != null) synClient.close();
        } catch (Exception ex) {
        }
    }

    /**
     * @return the asynClient
     */
    public CloseableHttpAsyncClient getAsynClient() {
        return asynClient;
    }

    /**
     * @return the synClient
     */
    public CloseableHttpClient getSynClient() {
        return synClient;
    }

    /** 
     * @see com.swift.core.api.rpc.ClientEngine#sendRequestNoReturn(java.lang.Object)
     */
    @Override
    public void sendRequestNoReturn(HttpUriRequest req) {
        checkAsyn(req);
        getAsynClient().execute(req, new LogHttpResponse(req));
    }

    /**
     * @see com.swift.core.api.rpc.ClientEngine#sendRequest(java.lang.Object,
     *      com.swift.core.api.rpc.ClientEngine.CallBackEngine)
     */
    @Override
    public void sendRequest(HttpUriRequest req, CallBackEngine<HttpResponse> callBack) {
        checkAsyn(req);
        getAsynClient().execute(req, new CallBackEngineFuture(callBack));
    }

    /**
     * @see com.swift.core.api.rpc.ClientEngine#sendAsynRequest(java.lang.Object)
     */
    @Override
    public Future<HttpResponse> sendAsynRequest(HttpUriRequest req) {
        checkAsyn(req);
        return getAsynClient().execute(req, new LogHttpResponse(req));
    }

    /**
     * @see com.swift.core.api.rpc.ClientEngine#sendRequest(java.lang.Object)
     */
    @Override
    public HttpResponse sendRequest(HttpUriRequest req) {
        try {
            return getSynClient().execute(req);
        } catch (Exception e) {
            throw new SystemException("http请求异常", e);
        }
    }
    
    private void checkAsyn(HttpUriRequest req) {
        if(allowAsyn(req)) return;
        throw new SystemException("禁止使用异步上传文件");
    }

    private boolean allowAsyn(HttpUriRequest req) {
        Header contentType = req.getFirstHeader(ContentTypeUtil.CONTENT_TYPE);
        if(TypeUtil.isNull(contentType)) return false;
        if(ContentTypeUtil.isFileData(contentType.getValue())) return false;
        return true;
    }
    
    
    private class LogHttpResponse implements FutureCallback<HttpResponse> {

        private HttpUriRequest req;
        
        private LogHttpResponse(HttpUriRequest req) {
            this.req=req;
        }
        
        @Override
        public void failed(Exception ex) {
            log.error("Http请求异常", ex);
        }

        @Override
        public void completed(HttpResponse result) {
            log.info("收到Http响应：" + req.getURI());
        }

        @Override
        public void cancelled() {
           
        }
    }
    
    private class CallBackEngineFuture implements FutureCallback<HttpResponse> {

        private CallBackEngine<HttpResponse> callBack;
        
        private CallBackEngineFuture(CallBackEngine<HttpResponse> callBack) {
            this.callBack=callBack;
        }

        @Override
        public void completed(HttpResponse result) {
            callBack.receiveResponse(result);
        }

        @Override
        public void failed(Exception ex) {
            callBack.failed(ex);
        }

        @Override
        public void cancelled() {
           
        }
        
    }

}
