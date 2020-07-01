/*
 * @(#)JsonUtil.java   1.0  2018年1月31日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou YY Technology Company LTD.
 */
package com.swift.util.type;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swift.exception.SwiftRuntimeException;

/**
 * Json格式转换类库
 * @author 郑家锦
 * @version 1.0 2018年1月31日
 */
public class JsonUtil {
    private static final Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private final static ObjectMapper OM = new ObjectMapper();

	static {
		OM.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		OM.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		OM.setSerializationInclusion(Include.NON_NULL);
	}

	public static ObjectMapper getObjectMapper() {
		return OM;
	}
    
    public static String toJson(Object obj) {
        try{
            if(obj==null) return "{}";
            return getObjectMapper().writeValueAsString(obj);
        }catch(Exception ex){
            log.error("转JSON异常",ex);
            throw new SwiftRuntimeException(ex);
        }
    }

    public static <T> T toObj(String json, Class<T> cla) {
        try {
            if(TypeUtil.isNull(json)) return null;
            return getObjectMapper().readValue(json, cla);
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
            return getObjectMapper().readValue(bytes, cla);
        } catch(Exception ex){
            log.error("转JSON异常",ex);
            throw new SwiftRuntimeException(ex);
        }
    }
    
    public static <T> List<T> toListObject(String content, Class<T> valueType) {
        if (TypeUtil.isNull(content)) {
            return null;
        }
        try {
            JavaType javaType = getCollectionType(ArrayList.class, valueType); 
            return getObjectMapper().readValue(content,javaType);
        }catch (Exception e) {
            throw new SwiftRuntimeException(e.getMessage(), e);
        }
    }
    
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {   
        return getObjectMapper().getTypeFactory().constructParametricType(collectionClass, elementClasses);   
    }
    
}
