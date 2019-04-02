/*
 * @(#)ObjTwo.java   1.0  2019年4月2日
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
public class ObjTwo extends AbstractBeanDataModel {

    private String objTwostr;
    
    private List<String> objTwostrList;

    public String getObjTwostr() {
        return objTwostr;
    }

    public void setObjTwostr(String objTwostr) {
        this.objTwostr = objTwostr;
    }

    public List<String> getObjTwostrList() {
        return objTwostrList;
    }

    public void setObjTwostrList(List<String> objTwostrList) {
        this.objTwostrList = objTwostrList;
    }
    
}
