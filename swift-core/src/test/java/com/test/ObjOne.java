/*
 * @(#)Ac.java   1.0  2019年4月2日
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
public class ObjOne extends AbstractBeanDataModel{
    
    private ObjTwo objTwo;
    
    private List<ObjTwo> objTwoList;
    
    private String objOneStr;

    public ObjTwo getObjTwo() {
        return objTwo;
    }

    public void setObjTwo(ObjTwo objTwo) {
        this.objTwo = objTwo;
    }

    public List<ObjTwo> getObjTwoList() {
        return objTwoList;
    }

    public void setObjTwoList(List<ObjTwo> objTwoList) {
        this.objTwoList = objTwoList;
    }

    public String getObjOneStr() {
        return objOneStr;
    }

    public void setObjOneStr(String objOneStr) {
        this.objOneStr = objOneStr;
    }
    
}
