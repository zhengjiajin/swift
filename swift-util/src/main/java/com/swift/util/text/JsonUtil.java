/*
 * @(#)JsonUtil.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.util.text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.swift.exception.SwiftRuntimeException;

/**
 * Json格式转换类库
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    public static String toJson(Object obj) {
        try{
            return ObjectMapperFactory.getObjectMapper().writeValueAsString(obj);
        }catch(Exception ex){
            log.error("转JSON异常",ex);
            throw new SwiftRuntimeException(ex);
        }
    }

    public static <T> T toObj(String json, Class<T> cla) {
        try {
            return ObjectMapperFactory.getObjectMapper().readValue(json, cla);
        }catch(Exception ex){
            log.error("转JSON异常",ex);
            throw new SwiftRuntimeException(ex);
        }
    }

    public static <T> T toObj(byte[] bytes, Class<T> cla) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return ObjectMapperFactory.getObjectMapper().readValue(bytes, cla);
        } catch(Exception ex){
            log.error("转JSON异常",ex);
            throw new SwiftRuntimeException(ex);
        }
    }
}
