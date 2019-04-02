/*
 * @(#)Bc.java   1.0  2019年4月2日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.test;

import java.util.List;

import com.swift.core.model.data.AbstractBeanDataModel;

/**
 * 添加说明 
 * @author zhengjiajin
 * @version 1.0 2019年4月2日
 */
public class ObjTop extends AbstractBeanDataModel{
    
    private List<ObjOne> acList;

    private List<String> list;
    
    private ObjOne acObj;
    
    private String str;
    
    
    public List<ObjOne> getAcList() {
        return acList;
    }

    public void setAcList(List<ObjOne> acList) {
        this.acList = acList;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public ObjOne getAcObj() {
        return acObj;
    }

    public void setAcObj(ObjOne acObj) {
        this.acObj = acObj;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
    
    
}
