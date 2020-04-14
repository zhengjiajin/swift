/*
 * @(#)AbstractSession.java   1.0  2018年6月11日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.session;

import java.util.Date;

import com.swift.core.model.data.AbstractBeanDataModel;

/**
 * 登录用户身份信息 
 * @author zhengjiajin
 * @version 1.0 2018年6月11日
 */
public class AbstractSession extends AbstractBeanDataModel{
    public final static String SESSION_NAME = "SESSION_USER";
    /**
     * 登录类型
     */
    private String loginType;
    /**
     * 验证登录字段
     */
    private String tokenId;
    /**
     * 验证登录字段
     */
    private String unionTokenId;
    /**
     * 用户ID
     */
    private Integer userId;
    /**
     * 用户名称
     */
    private String userName;
    
    /**
     * session创建时间
     */
    private Date sessionCreateTime;

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getUnionTokenId() {
        return unionTokenId;
    }

    public void setUnionTokenId(String unionTokenId) {
        this.unionTokenId = unionTokenId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Date getSessionCreateTime() {
        return sessionCreateTime;
    }

    public void setSessionCreateTime(Date sessionCreateTime) {
        this.sessionCreateTime = sessionCreateTime;
    }
    
}
