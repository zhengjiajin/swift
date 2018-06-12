/*
 * @(#)WebNetworkTrafficListener.java   1.0  2015年8月17日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.server.jetty.handler;

import java.net.Socket;
import java.nio.ByteBuffer;

import org.eclipse.jetty.io.NetworkTrafficListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 控制WEB的SOCKET 
 * 
 * @author zhengjiajin
 * @version 1.0 2015年8月17日
 */
@Component
public class WebNetworkTrafficListener implements NetworkTrafficListener {
    private final static Logger log = LoggerFactory.getLogger(NetworkTrafficListener.class);
    @Override
    public void opened(Socket socket) {
        log.info("收到连接：" + socket);
    }

    @Override
    public void closed(Socket socket) {
        log.info("断开连接：" + socket);
    }

    /** 
     * @see org.eclipse.jetty.io.NetworkTrafficListener#incoming(java.net.Socket, java.nio.ByteBuffer)
     */
    @Override
    public void incoming(Socket arg0, ByteBuffer arg1) {
        log.info("收到消息：" + arg0);
    }

    /** 
     * @see org.eclipse.jetty.io.NetworkTrafficListener#outgoing(java.net.Socket, java.nio.ByteBuffer)
     */
    @Override
    public void outgoing(Socket arg0, ByteBuffer arg1) {
        log.info("发送消息：" + arg0);
    }

}
