/**
 * Constants.java 1.0.0 2018-10-21 22:56:47
 *
 * Copyright (c) 2015-2018 GuangZhou HHMK Co. Ltd. All rights reserved.
 */
package com.swift.server.netty;

/**
 * 常量。
 * 
 * @version 1.0.0
 * @date 2018-10-21 22:56:47
 */
public interface Constants {

    /**
     * 未认证（未登录）。
     */
    static final int UNAUTHENTICATED = 304;
    /**
     * 无效参数。
     */
    static final int INVALID_PARAMETER = 305;
    /**
     * 已连接（只允许一个连接）。
     */
    static final int ALREADY_CONNECTED = 306;
    /**
     * 服务器内部错误（未知错误）。
     */
    static final int INTERNAL_SERVER_ERROR = 400;
    /**
     * 无效请求（协议解释）。
     */
    static final int BAD_REQUEST = 401;
    /**
     * 主机未连接或连接已关闭。
     */
    static final int CHANNEL_CLOSED = 501;
    /**
     * 超时。
     */
    static final int TIMEOUT = 502;

    /**
     * 分隔符（冒号）
     */
    static final String COLON = ":";
    /**
     * 分隔符（下划线）
     */
    static final String UNDERLINE = "_";
    /**
     * 点号
     */
    static final String DOT = ".";
    /**
     * 斜杠
     */
    static final String SLASH = "/";
    /**
     * 授权码前缀
     */
    static final String AUTHORIZE_CODE_PREFIX = "authorizeCode:";
    /**
     * 主机前缀
     */
    static final String HOST_PREFIX = "host:";
    /**
     * 通道前缀
     */
    static final String CHANNEL_PREFIX = "channel:";
    /**
     * 离线消息前缀
     */
    static final String OFFLINE_PREFIX = "offline:";

    /**
     * 网关集群节点
     */
    static final String CLUSTERS = "clusters";

    /**
     * HTTP
     */
    static final String HTTP_SCHEMA = "http://";
    /**
     * HTTPS
     */
    static final String HTTPS_SCHEMA = "https://";
}
