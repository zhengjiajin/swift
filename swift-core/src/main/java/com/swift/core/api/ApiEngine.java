/*
 * @(#)ClientEngine.java   1.0  2020年4月16日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.api;

import java.util.concurrent.Future;

import com.swift.exception.ServiceException;

/**
 * 客户端请求访问类,统一系统调用API规则 要是输出R非ServiceResponse 异常将以ServiceException输出
 * 
 * @author zhengjiajin
 * @version 1.0 2020年4月16日
 */
public interface ApiEngine<T, R> {
    /**
     * 发送请求。本类将根据请求的method寻路并发送出去。
     * 
     * @param cla
     *            本接口类,得到上下文
     * @param req
     *            要发送的请求，其它属性不需要填写。
     * @throws 异常则丢弃,不抛出异常
     */
    public void sendRequestNoReturn(Class<? extends ApiEngine<T, R>> cla, T req);

    /**
     * 发送请求。本类将根据请求的method寻路并发送出去。
     * 
     * @param cla 本接口类,得到上下文
     * @param req 要发送的请求，其它属性不需要填写。
     */
    public void sendRequest(Class<? extends ApiEngine<T, R>> cla, T req, CallBackApiEngine callBack);

    /**
     * 发送同步请求。本类将根据请求的method寻路并发送出去。
     * 
     * @param cla
     *            本接口类,得到上下文
     * @param req
     *            要发送的请求，其它属性不需要填写。
     * @throws ServiceException
     */
    public R sendRequest(Class<? extends ApiEngine<T, R>> cla, T req);

    /**
     * 通过Future异步返回结果集
     * @param cla 本接口类,得到上下文
     * @param req
     * @return
     */
    public Future<R> sendAsynRequest(Class<? extends ApiEngine<T, R>> cla,T req);
    
    /**
     * 异步返回的回调接口
     * 
     * @author zhengjiajin
     * @version 1.0 2015年8月6日
     */
    public interface CallBackApiEngine {
        /**
         * 接收响应，通讯协议层收到响应后直接把消息移交给引擎做业务处理。
         */
        public void receiveResponse(ResponseEngine rsp);
    }

    public class ResponseEngine {
        /**
         * 返回码
         */
        private int resultCode;
        /**
         * 响应结果详细描述。
         */
        private String reason;
        /**
         * 本响应所对应的请求。
         */
        private Object request;
        /**
         * 返回的业务字段
         */
        private Object value;

        public int getResultCode() {
            return resultCode;
        }

        public void setResultCode(int resultCode) {
            this.resultCode = resultCode;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public Object getRequest() {
            return request;
        }

        public void setRequest(Object request) {
            this.request = request;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }

    }

}
