/*
 * @(#)AbstractSession.java   1.0  2018年6月11日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.session;

import com.swift.core.model.data.AbstractBeanDataModel;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月11日
 */
public class AbstractSession extends AbstractBeanDataModel{
    public final static String SESSION_NAME = "SESSION_USER";
    
    /**
     * 验证登录字段
     */
    private String tokenId;

    /**
     * 验证登录字段
     */
    private String unionTokenId;

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
    
    
}
