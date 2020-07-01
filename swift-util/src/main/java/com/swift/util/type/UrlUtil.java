/*
 * @(#)UrlUtil.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.util.type;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.swift.exception.SwiftRuntimeException;

/**
 * 
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class UrlUtil {
    

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new SwiftRuntimeException("urlEncode错误:" + str);
        }
    }

    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new SwiftRuntimeException("URLDecoder错误:" + str);
        }
    }
	
    public static final String QUESTION_MARK = "?";
    public static final String EQUALS = "=";
    public static final String AMPERSAND = "&";
    public static final String EMPTY_STRING = "";
    
    /**
     * 将Map中的数据转换成URL的查询字符串
     * 
     * @param params
     *            要转换的Map
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String toQueryString(Map<String, String> params) {
        return toQueryString(params, null, true, true);
    }

    /**
     * 将Map中的数据转换成URL的查询字符串
     * 
     * @param params
     *            要转换的Map
     * @param excludes
     *            排除的key
     * @return
     */
    public static String toQueryString(Map<String, String> params, String[] excludes) {
        return toQueryString(params, excludes, true, true);
    }

    /**
     * 将Map中的数据转换成URL的查询字符串
     * 
     * @param params
     *            要转换的Map
     * @param excludes
     *            排除的key
     * @param excludeEmptyValue
     *            是否排除空值
     * @return
     */
    public static String toQueryString(Map<String, String> params, String[] excludes, boolean excludeEmptyValue) {
        return toQueryString(params, excludes, excludeEmptyValue, true);
    }

    /**
     * 将Map中的数据转换成URL的查询字符串
     * 
     * @param params
     *            要转换的Map
     * @param excludes
     *            排除的key
     * @param excludeEmptyValue
     *            是否排除空值
     * @param sortByKey
     *            是否按key排序
     * @return
     */
    public static String toQueryString(Map<String, String> params, String[] excludes, boolean excludeEmptyValue,
            boolean sortByKey) {
        return toQueryString(params, excludes, true, excludeEmptyValue, true);
    }
    
    /**
     * 将Map中的数据转换成URL的查询字符串
     * 
     * @param params
     *            要转换的Map
     * @param excludes
     *            排除的key
     * @param excludeNull
     *            是否排除Null值
     * @param excludeEmptyValue
     *            是否排除空字符串值
     * @param sortByKey
     *            是否按key排序
     * @return
     */
    public static String toQueryString(Map<String, String> params, String[] excludes, boolean excludeNull, boolean excludeEmptyValue,
            boolean sortByKey) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Set<String> excludeKeys = new HashSet<String>();
        if (excludes != null) {
            excludeKeys.addAll(Arrays.asList(excludes));
        }
        if (sortByKey) {
            Collections.sort(keys);
        }
        StringBuilder builder = new StringBuilder();
        for (String key : keys) {
            if (excludeKeys.contains(key)) {
                continue;
            }
            String value = params.get(key);
            if (excludeNull) {
                if (value == null) {
                    continue;
                }
            }
            if (excludeEmptyValue) {
                if (value != null && value.isEmpty()) {
                    continue;
                }
            }
            if (builder.length() > 0) {
                builder.append(UrlUtil.AMPERSAND);
            }
            builder.append(key);
            builder.append(UrlUtil.EQUALS);
            builder.append(value == null ? UrlUtil.EMPTY_STRING : value);
        }
        return builder.toString();
    }
}
