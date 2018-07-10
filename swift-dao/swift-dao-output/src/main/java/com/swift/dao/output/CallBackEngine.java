/*
 * @(#)CallBackEngine.java   1.0  2015年8月6日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.dao.output;

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
