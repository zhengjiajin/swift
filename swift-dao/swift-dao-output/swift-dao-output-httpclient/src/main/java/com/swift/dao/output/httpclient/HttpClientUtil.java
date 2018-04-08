/*
 * @(#)HttpClientUtil.java   1.0  2018年1月19日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.dao.output.httpclient;

import java.util.Map;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月19日
 */
public class HttpClientUtil {
    
    public static String get(String url){
        return get(url, null);
    }
    
    public static String get(String url, Map<?, ?> params){
        return get(url, params, null);
    }
    
    public static String get(String url, Map<?, ?> params, Map<?, ?> requestHeaders){
        
        return null;
    }
    
    public static String post(String url){
        return post(url, null);
    }
    
    public static String post(String url, Map<?, ?> params){
        return post(url, params, null);
    }
    
    public static String post(String url, Map<?, ?> params, Map<?, ?> requestHeaders){
        return post(url, params, requestHeaders,null);
    }
    
    public static String post(String url, Map<?, ?> params, Map<?, ?> requestHeaders, String charSet){
        return null;
    }
}
