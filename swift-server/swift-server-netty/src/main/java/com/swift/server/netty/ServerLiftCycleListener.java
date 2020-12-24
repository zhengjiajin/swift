/**
 * ServerLiftCycleListener.java 1.0.0 2019-04-12 10:28:09
 *
 * Copyright (c) 2015-2019 GuangZhou HHMK Co. Ltd.  All rights reserved.
 */
package com.swift.server.netty;

/**
 * 服务生命周期监听接口。
 * @version 1.0.0
 * @date 2019-04-12 10:28:09
 */
public interface ServerLiftCycleListener {

    /**
     * 服务启动成功后触发。
     * 
     * @param serverAddress
     *            服务地址
     * @param port
     *            端口号
     */
    void serverStart(String serverAddress, int port);

    /**
     * 服务结束后触发。
     */
    void serverStop();
}
