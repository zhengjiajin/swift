/*
 * @(#)HttpClient.java   1.0  2018年1月15日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.dao.output.httpclient;

import java.util.Map;

/**
 * 支持http,https
 * @author 郑家锦
 * @version 1.0 2018年1月15日
 */
public interface HttpClient {
    
    public String get(String url);
    
    public String get(String url, Map<?, ?> params);
    
    public String get(String url, Map<?, ?> params, Map<?, ?> requestHeaders);
    
    public String post(String url);
    
    public String post(String url, Map<?, ?> params);
    
    public String post(String url, Map<?, ?> params, Map<?, ?> requestHeaders);
    
    public String post(String url, Map<?, ?> params, Map<?, ?> requestHeaders, String charSet);
    
}
