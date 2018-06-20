/*
 * @(#)ValidatorBuilderString.java   1.0  2018年6月19日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.core.validator.core;

import java.util.List;

import com.swift.util.type.TypeUtil;

/**
 * 添加说明
 * 
 * @author zhengjiajin
 * @version 1.0 2018年6月19日
 */
public class ValidatorBuilderString {
    
    public static String builderString(Object key, String mes) {
        return mes+"("+key+")";
    }

    public static String builderString(List<String> list) {
        if(TypeUtil.isNull(list)) return null;
        StringBuffer sb = new StringBuffer();
        for(String str:list) {
            sb.append(str+";");
        }
        if(sb.length()>1) return sb.substring(0, sb.length()-1);
        return sb.toString();
    }
}
