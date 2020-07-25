/*
 * @(#)AbstractSession.java   1.0  2018年6月11日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.session;

import java.util.Date;
import java.util.List;

import com.swift.core.model.data.AbstractBeanDataModel;

/**
 * 登录用户身份信息 
 * @author zhengjiajin
 * @version 1.0 2018年6月11日
 */
public class AbstractSession extends AbstractBeanDataModel{
    public final static String SESSION_NAME = "SESSION_USER";
    /**
     * 登录类型,以什么方式登录等
     */
    private String loginType;
    /**
     * 某用方式登录产生的权限验证码,每种登录方式不同则不同,可能存在时效性,可用它获取用户信息
     */
    private String tokenId;
    /**
     * 用户ID-登录方式USER_ID,同一用户不同的登录方式可能产生不一样的USER_ID
     */
    private Object userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 关联USER_ID，同一用户下所有登录方式产生的USER_ID列表，帐号融合，常用于业务
     */
    private List<Object> userIdList;
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

    public Object getUserId() {
        return userId;
    }

    public void setUserId(Object userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userIdList
     */
    public List<Object> getUserIdList() {
        return userIdList;
    }

    /**
     * @param userIdList the userIdList to set
     */
    public void setUserIdList(List<Object> userIdList) {
        this.userIdList = userIdList;
    }

    public Date getSessionCreateTime() {
        return sessionCreateTime;
    }

    public void setSessionCreateTime(Date sessionCreateTime) {
        this.sessionCreateTime = sessionCreateTime;
    }
    
}
