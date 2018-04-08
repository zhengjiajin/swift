/**
 * @(#)ObjectMapperFactory.java 0.0.1-SNAPSHOT May 20, 2016 8:14:29 PM 
 *  
 * Copyright (c) 2015-2016 GuangZhou HHMK Techonlogy Co. Ltd.  All rights reserved. 
 */
package com.swift.core.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * ObjectMapper工厂类
 * 
 * @author zhengjiajin
 * @version 0.0.1-SNAPSHOT
 * @date May 20, 2016 8:14:29 PM
 */
public class ObjectMapperFactory {

	private final static ObjectMapper OM = new ObjectMapper();

	static {
		OM.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);  
		OM.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); 
		OM.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	public static ObjectMapper getObjectMapper() {
		return OM;
	}
}
