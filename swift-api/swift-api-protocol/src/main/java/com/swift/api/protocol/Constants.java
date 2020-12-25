/**
 * Constants.java 1.0.0 2020年12月25日
 *
 * Copyright (c) 2015-2020 GuangZhou HHMK Co. Ltd. All rights reserved.
 */
package com.swift.api.protocol;

/**
 * 常量
 * @author zhengjiajin
 * @version 1.0 2020年12月25日
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

    // 配置为保持服务器客户端的统一，固不使用配置
    /**
     * 最长空闲时间（秒）
     */
    static final int MAX_IDLE_TIME = 60;
    /**
     * WEBSOCKET最大文本
     */
    static final int WEBSOCKET_MAX_CONTENT_LENGTH = 65535;
    /**
     * WEBSOCKET目录
     */
    static final String WEBSOCKET_PATH = "/";
    
    /**
     * 连续解码异常的最大次数，超过该次数，则断连。
     */
    static final int MAX_DECODE_FAILURE_TIMES=3;
    
    /**
     * 连续发送DWR的次数，客户端超过该次数没有回DWA，则断连。
     */
    static final int MAX_DWR_TIMES=3;
    
    /**
     * 连接建立后，等待CLR的最长时间（秒），客户端超过该时间没有发送CLR，则断连。
     */
    static final int AUTH_MAXWAIT_TIME=60;
}
