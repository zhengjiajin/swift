/*
 * @(#)AbstractWebSwiftInvocation.java   1.0  2020年6月18日
 * 
 * Copyright (c)    2014-2020. All Rights Reserved. GuangZhou hhmk Technology Company LTD.
 */
package com.swift.api.http.jetty.proxy;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.http.HttpFields;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.api.http.jetty.WebClient;
import com.swift.core.api.ApiEngine.CallBackApiEngine;
import com.swift.core.api.ApiEngine.ResponseEngine;
import com.swift.core.spring.Spring;
import com.swift.core.spring.proxy.AbstractSwiftInvocation;
import com.swift.exception.ServiceException;
import com.swift.util.type.ByteUtil;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2020年6月18日
 */
public abstract class AbstractWebSwiftInvocation extends AbstractSwiftInvocation {

    private static final Logger log = LoggerFactory.getLogger(AbstractWebSwiftInvocation.class);
    
    private HttpClient httpClient;
    
    
    protected synchronized HttpClient getHttpClient() {
        if(httpClient==null) {
            WebClient webClient=Spring.getBean(WebClient.class);
            httpClient=webClient.getHttpClient();
        }
        return httpClient;
    }
    
    protected ServiceException ioException(Request request,Exception e) {
        if(request==null)  return new ServiceException(-1,"发送http IO失败:",e);
        return new ServiceException(-1,"发送http IO失败:"+request.getURI(),e);
    }
    
    protected ServiceException httpException(Response response) {
        if(response.getStatus()!=200) {
            return new ServiceException("发送http请求返回失败:"+response.getRequest().getURI()+";返回码:"+response.getStatus());
        }
        return null;
    }
    
    protected ServiceException serviceException(Request request,ResponseEngine res) {
        if(res==null) return new ServiceException(request.getURI()+"返回值为空");
        if(res.getResultCode()!=0) return new ServiceException(res.getResultCode(), res.getReason());
        return null;
    }
    

    protected Object sendRequest(Request request,Type resType,String resPath) {
        try {
            ContentResponse res = request.send();
            ServiceException httpException = httpException(res);
            if(httpException!=null) throw httpException;
            ResponseEngine responseEngine = contentToRes(request,res.getHeaders(),res.getContent(), resType,resPath);
            ServiceException serviceException = serviceException(request, responseEngine);
            if(serviceException!=null) throw serviceException;
            return responseEngine.getValue();
        } catch (Exception e) {
            throw ioException(request, e);
        }
    }
    
    protected void sendRequestNoReturn(Request request,Type resType,String resPath) {
        request.onResponseContent(new Response.ContentListener() {
            @Override
            public void onContent(Response response, ByteBuffer content) {
                String json = ByteUtil.byteBufferToString(content);
                log.info("调用"+request.getURI()+"结果为："+response.getStatus()+":"+json);
            }
        }).send(new Response.CompleteListener() {
            
            @Override
            public void onComplete(Result result) {
                ServiceException httpException = httpException(result.getResponse());
                if(httpException!=null) {
                    log.error("http异常:",httpException);
                }
            }
        });
    }
    
    protected void sendRequest(Request request,Type resType,String resPath,CallBackApiEngine callBack) {
        request.onResponseContent(new Response.ContentListener() {
            @Override
            public void onContent(Response response, ByteBuffer content) {
               
                ResponseEngine responseEngine = contentToRes(request,response.getHeaders(),content.array(), resType,resPath);
                if(responseEngine==null) return;
                callBack.receiveResponse(responseEngine);
            }
        }).send(new Response.CompleteListener() {
            @Override
            public void onComplete(Result result) {
                ServiceException httpException = httpException(result.getResponse());
                if(httpException!=null) {
                    log.error("http异常:",httpException);
                    ResponseEngine responseEngine = new ResponseEngine();
                    responseEngine.setRequest(request);
                    responseEngine.setResultCode(httpException.getStatusCode());
                    responseEngine.setReason(httpException.getMessage());
                    callBack.receiveResponse(responseEngine);
                }
            }
        });
    }

    protected abstract ResponseEngine contentToRes(Request request,HttpFields httpFields ,byte[] content,Type resType,String resPath);
}
