/*
 * @(#)ErrorCode.java   1.0  2018年6月21日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.exception;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月21日
 */
public class ResultCode {
    // 返回成功
    public final static int SUCCESS = 0;
    // 未定义异常
    public final static int UNKNOWN = -1;
    // 服务能力不存在
    public final static int NO_METHOD = 300;
    // 校验码不正确
    public final static int CHECK_ERROR =  301;
    // 未登录异常
    public final static int NO_LOGIN =  304;
    // 非法参数
    public final static int ERROR_PARAMETER =  305;
    // 系统异常
    public final static int SYS_ERROR =  400;
    // 协议解释异常
    public final static int PROTOCOL_ERROR =  401;
    // 没有权限
    public final static int AUTH_ERROR =  402;
    //外部系统异常
    public final static int OTHER_SYS_ERROR =  500;
}
