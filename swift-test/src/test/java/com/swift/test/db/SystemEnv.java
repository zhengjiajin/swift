/*
 * @(#)SystemEnv.java   1.0  2018年6月5日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.test.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.swift.core.model.data.IBaseModel;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2018年6月5日
 */
@Entity
@Table(name="System_Env")
public class SystemEnv extends IBaseModel{
    
    @Id
    @Column(name="key_Id")
    private String keyId;
    
    @Column(name="value_Content")
    private String valueContent;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getValueContent() {
        return valueContent;
    }

    public void setValueContent(String valueContent) {
        this.valueContent = valueContent;
    }
    
}
