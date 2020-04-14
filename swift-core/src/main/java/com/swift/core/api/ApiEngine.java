/*
 * @(#)ClientEngine.java   1.0  2015年8月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.api;

/**
 * 客户端请求访问类,统一系统调用API规则
 * @author zhengjiajin
 * @version 1.0 2015年8月6日
 */
public interface ApiEngine<T,R> {
	/**
     * 发送请求。本类将根据请求的method寻路并发送出去。
     * 
     * @param req 要发送的请求，其它属性不需要填写。
     */
    public void sendRequestNoReturn(T req);
    /**
     * 发送请求。本类将根据请求的method寻路并发送出去。
     * 
     * @param req 要发送的请求，其它属性不需要填写。
     */
    public void sendRequest(T req, CallBackEngine<R> callBack);
    
    /**
     * 发送同步请求。本类将根据请求的method寻路并发送出去。
     * 
     * @param req 要发送的请求，其它属性不需要填写。
     */
    public R sendRequest(T req);
    
    /**
     * 异步返回的回调接口
     * @author zhengjiajin
     * @version 1.0 2015年8月6日
     */
    public interface CallBackEngine<R> {
        /**
         * 接收响应，通讯协议层收到响应后直接把消息移交给引擎做业务处理。
         */
        public void receiveResponse(R rsp);
    }
}
