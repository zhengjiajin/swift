/*
 * @(#)UrlUtil.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.util.layout;

import java.net.URLEncoder;

import com.swift.util.type.TypeUtil;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class UrlUtil {
    public static String encode(Object value){
        try {
            return URLEncoder.encode(TypeUtil.toString(value,""),"utf-8");
        } catch (Exception ex) {
            return "";
        }
    }
}
