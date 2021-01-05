package com.swift.api.http.jetty;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.swift.core.api.rpc.ClientEngine;
import com.swift.core.model.ServiceRequest;
import com.swift.core.model.ServiceResponse;

public abstract class WebClientCodeOutput<T, R> extends WebClientCode implements ClientEngine<T, R> {
    // private final static Logger log = LoggerFactory.getLogger(WebClientCodeOutput.class);
    protected static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=UTF-8";
    protected static final String POST = "POST";
    protected static final String GET = "GET";
    protected static final String DELETE = "DELETE";
    @Autowired
    protected WebClient webClient;

    protected WebClient getWebClient() {
        return webClient;
    }

    @PostConstruct
    public void init() {
        webClient.registerWebClientCode(getClass().getName(), this);
    }

    /**
     * 创建接口请求
     *
     * @param message
     *            消息
     * @return 微信接口请求
     */
    protected abstract ServiceRequest createServerRequest(T message);

    /**
     * 创建接口响应
     *
     * @param response
     * @return
     */
    protected abstract R createResponse(ServiceResponse response);

    @Override
    public void sendRequestNoReturn(T req) {
        sendRequest(req, null);
    }

    @Override
    public R sendRequest(T req) {
        ServiceRequest sr = createServerRequest(req);
        sr.setMethod(getClass().getName());
        ServiceResponse res = webClient.sendRequest(sr);
        return createResponse(res);
    }

    @Override
    public void sendRequest(T req, CallBackEngine<R> callBack) {
        ServiceRequest sr = createServerRequest(req);
        sr.setMethod(getClass().getName());
        webClient.sendRequest(sr, callBack);
    }

    public Future<R> sendAsynRequest(T req) {
        Future<R> futurn = Executors.newSingleThreadExecutor().submit(new Callable<R>() {
            @Override
            public R call() throws Exception {
                return sendRequest(req);
            }
        });
        return futurn;
    }
}
